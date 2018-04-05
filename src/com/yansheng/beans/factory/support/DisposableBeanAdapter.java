package com.yansheng.beans.factory.support;

import java.io.Closeable;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import com.yansheng.beans.BeanUtils;
import com.yansheng.beans.factory.DisposableBean;
import com.yansheng.beans.factory.config.BeanPostProcessor;
import com.yansheng.beans.factory.config.DestructionAwareBeanPostProcessor;

public class DisposableBeanAdapter implements DisposableBean, Serializable, Runnable {

    private static final String CLOSE_METHOD_NAME = "close";
    private static final String SHUTDOWN_METHOD_NAME = "shutdown";
    private static final Log logger = LogFactory.getLog(DisposableBeanAdapter.class);
    @SuppressWarnings("rawtypes")
    private static Class closeableInterface;
    static {
        try {
            closeableInterface = DisposableBeanAdapter.class.getClassLoader().loadClass("java.lang.AutoCloseable");
        } catch (ClassNotFoundException e) {
            closeableInterface = Closeable.class;
        }
    }

    private final Object bean;
    private final String beanName;
    private final boolean invokeDisposableBean;
    private final boolean nonPublicAccessAllowed;
    private String destroyMethodName;
    private transient Method destroyMethod;
    private List<DestructionAwareBeanPostProcessor> beanPostProcessors;
    private final AccessControlContext acc;

    public DisposableBeanAdapter(
            Object bean, String beanName, RootBeanDefinition beanDefinition, List<BeanPostProcessor> postProcessors,
            AccessControlContext acc) {
        Assert.notNull(bean, "Disposable bean must not be null");
        this.bean = bean;
        this.beanName = beanName;
        this.invokeDisposableBean = (this.bean instanceof DisposableBean
                && !beanDefinition.isExternallyManagedDestroyMethod("destroy"));
        this.nonPublicAccessAllowed = beanDefinition.isNonPublicAccessAllowed();
        this.acc = acc;
        String destroyMethodName = inferDestroyMethodIfNecessary(bean, beanDefinition);
        if (destroyMethodName != null && !(this.invokeDisposableBean && "destroy".equals(destroyMethodName)) &&
                !beanDefinition.isExternallyManagedDestroyMethod(destroyMethodName)) {
            this.destroyMethodName = destroyMethodName;
            this.destroyMethod = determineDestroyMethod();
            if (this.destroyMethod == null) {
                if (beanDefinition.isEnforceDestroyMethod()) {
                    throw new BeanDefinitionValidationException("Couldn't find a destroy method named '" +
                            destroyMethodName + "' on bean with name '" + beanName + "'");
                }
            } else {
                Class<?>[] paramTypes = this.destroyMethod.getParameterTypes();
                if (paramTypes.length > 1) {
                    throw new BeanDefinitionValidationException("Method '" + destroyMethodName + "' of bean '" +
                            beanName + "' has more than one parameter - not supported as destroy method");
                } else if (paramTypes.length == 1 && !paramTypes[0].equals(boolean.class)) {
                    throw new BeanDefinitionValidationException("Method '" + destroyMethodName + "' of bean '" +
                            beanName + "' has a non-boolean parameter - not supported as destroy method");
                }
            }
        }
        this.beanPostProcessors = filterPostProcessors(postProcessors);
    }

    private DisposableBeanAdapter(Object bean, String beanName, boolean invokeDisposableBean,
            boolean nonPublicAccessAllowed, String destroyMethodName,
            List<DestructionAwareBeanPostProcessor> postProcessors) {
        this.bean = bean;
        this.beanName = beanName;
        this.invokeDisposableBean = invokeDisposableBean;
        this.nonPublicAccessAllowed = nonPublicAccessAllowed;
        this.destroyMethodName = destroyMethodName;
        this.beanPostProcessors = postProcessors;
        this.acc = null;
    }

    private String inferDestroyMethodIfNecessary(Object bean, RootBeanDefinition beanDefinition) {
        if (AbstractBeanDefinition.INFER_METHOD.equals(beanDefinition.getDestroyMethodName()) ||
                (beanDefinition.getDestroyMethodName() == null && closeableInterface.isInstance(bean))) {
            if (!(bean instanceof DisposableBean)) {
                try {
                    return bean.getClass().getMethod(CLOSE_METHOD_NAME).getName();
                } catch (NoSuchMethodException ex) {
                    try {
                        return bean.getClass().getMethod(SHUTDOWN_METHOD_NAME).getName();
                    } catch (NoSuchMethodException ex2) {
                        // no candidate destroy method found
                    }
                }
            }
            return null;
        }
        return beanDefinition.getDestroyMethodName();
    }

    private List<DestructionAwareBeanPostProcessor> filterPostProcessors(List<BeanPostProcessor> postProcessors) {
        List<DestructionAwareBeanPostProcessor> filterPostProcessors = null;
        if (postProcessors != null && !postProcessors.isEmpty()) {
            filterPostProcessors = new ArrayList<DestructionAwareBeanPostProcessor>(postProcessors.size());
            for (BeanPostProcessor postProcessor : postProcessors) {
                if (postProcessor instanceof DestructionAwareBeanPostProcessor) {
                    filterPostProcessors.add((DestructionAwareBeanPostProcessor) postProcessor);
                }
            }
        }
        return filterPostProcessors;
    }

    @Override
    public void run() {
        destroy();
    }

    @Override
    public void destroy() {
        if (this.beanPostProcessors != null && !this.beanPostProcessors.isEmpty()) {
            for (DestructionAwareBeanPostProcessor processor : this.beanPostProcessors) {
                processor.postProcessBeforeDestruction(this.bean, this.beanName);
            }
        }
        if (this.invokeDisposableBean) {
            if (logger.isDebugEnabled()) {
                logger.debug("Invoking destroy() on bean with name '" + this.beanName + "'");
            }
            try {
                if (System.getSecurityManager() != null) {
                    AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
                        public Object run() throws Exception {
                            ((DisposableBean) bean).destroy();
                            return null;
                        }
                    }, acc);
                } else {
                    ((DisposableBean) bean).destroy();
                }
            } catch (Throwable ex) {
                String msg = "Invocation of destroy method failed on bean with name '" + this.beanName + "'";
                if (logger.isDebugEnabled()) {
                    logger.warn(msg, ex);
                } else {
                    logger.warn(msg + ": " + ex);
                }
            }
        }
        if (this.destroyMethod != null) {
            invokeCustomDestroyMethod(this.destroyMethod);
        } else if (this.destroyMethodName != null) {
            Method methodToCall = determineDestroyMethod();
            if (methodToCall != null) {
                invokeCustomDestroyMethod(methodToCall);
            }
        }
    }

    private Method determineDestroyMethod() {
        try {
            if (System.getSecurityManager() != null) {
                return AccessController.doPrivileged(new PrivilegedAction<Method>() {
                    public Method run() {
                        return findDestroyMethod();
                    }
                });
            } else {
                return findDestroyMethod();
            }
        } catch (IllegalArgumentException ex) {
            throw new BeanDefinitionValidationException("Couldn't find a unique destroy method on bean with name '" +
                    this.beanName + ": " + ex.getMessage());
        }
    }

    private Method findDestroyMethod() {
        return (this.nonPublicAccessAllowed
                ? BeanUtils.findMethodWithMinimalParamaters(this.bean.getClass(), this.destroyMethodName)
                : BeanUtils.findMethodWithMinimalParameters(this.bean.getClass().getMethods(), this.destroyMethodName));
    }

    private void invokeCustomDestroyMethod(final Method destroyMethod) {
        Class<?>[] paramTypes = destroyMethod.getParameterTypes();
        final Object[] args = new Object[paramTypes.length];
        if (paramTypes.length == 1) {
            args[0] = Boolean.TRUE;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Invoking destroy method '" + this.destroyMethodName +
                    "' on bean with name '" + this.beanName + "'");
        }
        try {
            if (System.getSecurityManager() != null) {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        ReflectionUtils.makeAccessible(destroyMethod);
                        return null;
                    }
                });
                try {
                    AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
                        public Object run() throws Exception {
                            destroyMethod.invoke(bean, args);
                            return null;
                        }
                    }, acc);
                } catch (PrivilegedActionException pax) {
                    throw (InvocationTargetException) pax.getException();
                }
            } else {
                ReflectionUtils.makeAccessible(destroyMethod);
                destroyMethod.invoke(bean, args);
            }
        } catch (InvocationTargetException ex) {
            String msg = "Invocation of destroy method '" + this.destroyMethodName +
                    "' failed on bean with name '" + this.beanName + "'";
            if (logger.isDebugEnabled()) {
                logger.warn(msg, ex.getTargetException());
            } else {
                logger.warn(msg + ": " + ex.getTargetException());
            }
        } catch (Throwable ex) {
            logger.error("Couldn't invoke destroy method '" + this.destroyMethodName +
                    "' on bean with name '" + this.beanName + "'", ex);
        }
    }

    protected Object writeReplace() {
        List<DestructionAwareBeanPostProcessor> serializablePostProcessors = null;
        if (this.beanPostProcessors != null && !this.beanPostProcessors.isEmpty()) {
            serializablePostProcessors = new ArrayList<DestructionAwareBeanPostProcessor>();
            for (DestructionAwareBeanPostProcessor postProcessor : this.beanPostProcessors) {
                if (postProcessor instanceof Serializable) {
                    serializablePostProcessors.add(postProcessor);
                }
            }
        }
        return new DisposableBeanAdapter(this.bean, this.beanName, this.invokeDisposableBean,
                this.nonPublicAccessAllowed, this.destroyMethodName, serializablePostProcessors);
    }

    public static boolean hasDestroyMethod(Object bean, RootBeanDefinition beanDefinition) {
        if (bean instanceof DisposableBean || closeableInterface.isInstance(bean)) {
            return true;
        }
        String destroyMethodName = beanDefinition.getDestroyMethodName();
        if (AbstractBeanDefinition.INFER_METHOD.equals(destroyMethodName)) {
            return ClassUtils.hasMethod(bean.getClass(), CLOSE_METHOD_NAME);
        }
        return (destroyMethodName != null);
    }
}
