package com.yansheng.beans.factory.support;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.yansheng.beans.BeansException;
import com.yansheng.beans.factory.BeanCreationException;
import com.yansheng.beans.factory.BeanCurrentlyInCreationException;
import com.yansheng.beans.factory.FactoryBean;
import com.yansheng.beans.factory.FactoryBeanNotInitializedException;

public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry {

    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<String, Object>(16);

    @SuppressWarnings("rawtypes")
    protected Class getTypeForFactoryBean(final FactoryBean factoryBean) {
        try {
            if (System.getSecurityManager() != null) {
                return AccessController.doPrivileged(new PrivilegedAction<Class>() {
                    public Class run() {
                        return factoryBean.getObjectType();
                    }
                }, getAccessControlContext());
            } else {
                return factoryBean.getObjectType();
            }
        } catch (Throwable e) {
            logger.warn("FactoryBean threw exception from getObjectType, despite the contract saying " +
                    "that it should return null if the type of its object cannot be determined yet", e);
            return null;
        }
    }

    protected Object getCachedObjectForFactoryBean(String beanName) {
        Object object = this.factoryBeanObjectCache.get(beanName);
        return (object != NULL_OBJECT ? object : null);
    }

    @SuppressWarnings("rawtypes")
    protected Object getObjectFromFactoryBean(FactoryBean factory, String beanName, boolean shouldPostProcess) {
        if (factory.isSingleton() && containsSingleton(beanName)) {
            synchronized (getSingletonMutex()) {
                Object object = this.factoryBeanObjectCache.get(beanName);
                if (object == null) {
                    object = doGetObjectFromFactoryBean(factory, beanName, shouldPostProcess);
                    this.factoryBeanObjectCache.put(beanName, (object != null ? object : NULL_OBJECT));
                }
                return object;
            }
        } else {
            return doGetObjectFromFactoryBean(factory, beanName, shouldPostProcess);
        }
    }

    @SuppressWarnings("rawtypes")
    private Object doGetObjectFromFactoryBean(
            final FactoryBean factoryBean, final String beanName, final boolean shouldPostProcess) {
        Object object;
        try {
            if (System.getSecurityManager() != null) {
                AccessControlContext acc = getAccessControlContext();
                try {
                    object = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
                        public Object run() throws Exception {
                            return factoryBean.getObject();
                        }
                    }, acc);
                } catch (PrivilegedActionException pae) {
                    throw pae.getException();
                }
            } else {
                object = factoryBean.getObject();
            }
        } catch (FactoryBeanNotInitializedException e) {
            throw new BeanCurrentlyInCreationException(beanName, e.toString());
        } catch (Throwable e) {
            throw new BeanCreationException(beanName, "FactoryBean threw exception on object creation", e);
        }

        if (object == null && isSingletonCurrentlyInCreation(beanName)) {
            throw new BeanCurrentlyInCreationException(
                    beanName, "FactoryBean which is currently in creation returned null from getObject");
        }
        if (object != null && shouldPostProcess) {
            try {
                object = postProcessObjectFromFactoryBean(object, beanName);
            } catch (Throwable e) {
                throw new BeanCreationException(beanName, "Post-processing of the FactoryBean's object failed", e);
            }
        }
        return object;
    }

    protected Object postProcessObjectFromFactoryBean(Object object, String beanName) throws BeansException {
        return object;
    }

    @SuppressWarnings("rawtypes")
    protected FactoryBean getFactoryBean(String beanName, Object beanInstance) throws BeansException {
        if (!(beanInstance instanceof FactoryBean)) {
            throw new BeanCreationException(beanName,
                    "Bean instance of type [" + beanInstance.getClass() + "] is not a FactoryBean");
        }
        return (FactoryBean) beanInstance;
    }

    protected void removeSingleton(String beanName) {
        super.removeSingleton(beanName);
        this.factoryBeanObjectCache.remove(beanName);
    }

    protected AccessControlContext getAccessControlContext() {
        return AccessController.getContext();
    }
}
