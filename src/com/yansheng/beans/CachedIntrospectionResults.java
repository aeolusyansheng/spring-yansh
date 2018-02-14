package com.yansheng.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.FatalBeanException;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

public class CachedIntrospectionResults {

	private static final Log logger = LogFactory.getLog(CachedIntrospectionResults.class);
	private static List<BeanInfoFactory> beanInfoFactories = SpringFactoriesLoader.loadFactories(BeanInfoFactory.class,
			CachedIntrospectionResults.class.getClassLoader());

	static final Set<ClassLoader> acceptedClassLoaders = new HashSet<ClassLoader>();

	@SuppressWarnings("rawtypes")
	static final Map<Class, Object> classCache = new WeakHashMap<Class, Object>();

	public static void acceptClassLoader(ClassLoader classLoader) {
		if (classLoader != null) {
			synchronized (acceptedClassLoaders) {
				acceptedClassLoaders.add(classLoader);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static void clearClassLoader(ClassLoader classLoader) {
		synchronized (classCache) {
			for (Iterator<Class> it = classCache.keySet().iterator(); it.hasNext();) {
				Class beanClass = it.next();
				if (isUnderneathClassLoader(beanClass.getClassLoader(), classLoader)) {
					it.remove();
				}
			}
		}
		synchronized (acceptedClassLoaders) {
			for (Iterator<ClassLoader> it = acceptedClassLoaders.iterator(); it.hasNext();) {
				ClassLoader registeredLoader = it.next();
				if (isUnderneathClassLoader(registeredLoader, classLoader)) {
					it.remove();
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	static CachedIntrospectionResults forClass(Class beanClass) throws BeansException {
		CachedIntrospectionResults results;
		Object value;
		synchronized (classCache) {
			value = classCache.get(beanClass);
			if (value instanceof Reference) {
				Reference ref = (Reference) value;
				results = (CachedIntrospectionResults) ref.get();
			} else {
				results = (CachedIntrospectionResults) value;
			}
			if (results == null) {
				if (ClassUtils.isCacheSafe(beanClass, CachedIntrospectionResults.class.getClassLoader())
						|| isClassLoaderAccepted(beanClass.getClassLoader())) {
					results = new CachedIntrospectionResults(beanClass);
					synchronized (classCache) {
						classCache.put(beanClass, results);
					}
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Not strongly caching class [" + beanClass.getName()
								+ "] because it is not cache-safe");
					}
					results = new CachedIntrospectionResults(beanClass);
					synchronized (classCache) {
						classCache.put(beanClass, new WeakReference<CachedIntrospectionResults>(results));
					}
				}
			}
		}
		return results;
	}

	private static boolean isClassLoaderAccepted(ClassLoader classLoader) {
		ClassLoader[] acceptedLoaderArray;
		synchronized (acceptedClassLoaders) {
			acceptedLoaderArray = acceptedClassLoaders.toArray(new ClassLoader[acceptedClassLoaders.size()]);
		}
		for (ClassLoader registeredLoader : acceptedLoaderArray) {
			if (isUnderneathClassLoader(classLoader, registeredLoader)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isUnderneathClassLoader(ClassLoader candidate, ClassLoader parent) {
		if (candidate == parent) {
			return true;
		}
		if (candidate == null) {
			return false;
		}
		ClassLoader classLoaderToCheck = candidate;
		while (classLoaderToCheck != null) {
			classLoaderToCheck = classLoaderToCheck.getParent();
			if (classLoaderToCheck == parent) {
				return true;
			}
		}
		return false;
	}

	private final BeanInfo beanInfo;
	private final Map<String, PropertyDescriptor> propertyDescriptorCache;

	@SuppressWarnings("rawtypes")
	private CachedIntrospectionResults(Class beanClass) throws BeansException {
		try {
			if (logger.isTraceEnabled()) {
				logger.trace("Getting BeanInfo for class [" + beanClass.getName() + "]");
			}
			BeanInfo beanInfo = null;
			for (BeanInfoFactory beanInfoFactory : beanInfoFactories) {
				beanInfo = beanInfoFactory.getBeanInfo(beanClass);
				if (beanInfo != null) {
					break;
				}
			}
			if (beanInfo == null) {
				beanInfo = Introspector.getBeanInfo(beanClass);
			}
			this.beanInfo = beanInfo;

			Class classToFlush = beanClass;
			do {
				Introspector.flushFromCaches(classToFlush);
				classToFlush = classToFlush.getSuperclass();
			} while (classToFlush != null);

			if (logger.isTraceEnabled()) {
				logger.trace("Caching PropertyDescriptors for class [" + beanClass.getName() + "]");
			}
			this.propertyDescriptorCache = new LinkedHashMap<String, PropertyDescriptor>();
			PropertyDescriptor[] pds = this.beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor pd : pds) {
				if (Class.class.equals(beanClass) && "classLoader".equals(pd.getName())) {
					continue;
				}
				if (logger.isTraceEnabled()) {
					logger.trace("Found bean property '" + pd.getName() + "'"
							+ (pd.getPropertyType() != null ? " of type [" + pd.getPropertyType().getName() + "]" : "")
							+ (pd.getPropertyEditorClass() != null
									? "; editor [" + pd.getPropertyEditorClass().getName() + "]"
									: ""));
				}
				pd = buildGenericTypeAwarePropertyDescriptor(beanClass, pd);
				this.propertyDescriptorCache.put(pd.getName(), pd);
			}
		} catch (IntrospectionException e) {
			throw new FatalBeanException("Failed to obtain BeanInfo for class [" + beanClass.getName() + "]", e);
		}
	}

	BeanInfo getBeanInfo() {
		return this.beanInfo;
	}

	@SuppressWarnings("rawtypes")
	Class getBeanClass() {
		return this.beanInfo.getBeanDescriptor().getBeanClass();
	}

	PropertyDescriptor getPropertyDescriptor(String name) {
		PropertyDescriptor pd = this.propertyDescriptorCache.get(name);
		if (pd == null && StringUtils.hasLength(name)) {
			pd = this.propertyDescriptorCache.get(name.substring(0, 1).toLowerCase() + name.substring(1));
			if (pd == null) {
				pd = this.propertyDescriptorCache.get(name.substring(0, 1).toUpperCase() + name.substring(1));
			}
		}
		return (pd == null || pd instanceof GenericTypeAwarePropertyDescriptor ? pd
				: buildGenericTypeAwarePropertyDescriptor(getBeanClass(), pd));
	}

	PropertyDescriptor[] getPropertyDescriptors() {
		PropertyDescriptor[] pds = new PropertyDescriptor[this.propertyDescriptorCache.size()];
		int i = 0;
		for (PropertyDescriptor pd : this.propertyDescriptorCache.values()) {
			pds[i] = (pd instanceof GenericTypeAwarePropertyDescriptor ? pd
					: buildGenericTypeAwarePropertyDescriptor(getBeanClass(), pd));
			i++;
		}
		return pds;
	}

	@SuppressWarnings("rawtypes")
	private PropertyDescriptor buildGenericTypeAwarePropertyDescriptor(Class beanClass, PropertyDescriptor pd) {
		try {
			return new GenericTypeAwarePropertyDescriptor(beanClass, pd.getName(), pd.getReadMethod(),
					pd.getWriteMethod(), pd.getPropertyEditorClass());
		} catch (IntrospectionException e) {
			throw new FatalBeanException("Failed to re-introspect class [" + beanClass.getName() + "]", e);
		}
	}
}
