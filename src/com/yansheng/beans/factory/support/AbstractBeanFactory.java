package com.yansheng.beans.factory.support;

import java.beans.PropertyEditor;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.DecoratingClassLoader;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.StringValueResolver;

import com.yansheng.beans.BeanUtils;
import com.yansheng.beans.BeanWrapper;
import com.yansheng.beans.BeansException;
import com.yansheng.beans.PropertyEditorRegistrar;
import com.yansheng.beans.PropertyEditorRegistry;
import com.yansheng.beans.PropertyEditorRegistrySupport;
import com.yansheng.beans.SimpleTypeConverter;
import com.yansheng.beans.TypeConverter;
import com.yansheng.beans.TypeMismatchException;
import com.yansheng.beans.factory.BeanCreationException;
import com.yansheng.beans.factory.BeanCurrentlyInCreationException;
import com.yansheng.beans.factory.BeanDefinitionStoreException;
import com.yansheng.beans.factory.BeanFactory;
import com.yansheng.beans.factory.BeanFactoryUtils;
import com.yansheng.beans.factory.BeanIsAbstractException;
import com.yansheng.beans.factory.BeanIsNotAFactoryException;
import com.yansheng.beans.factory.BeanNotOfRequiedTypeException;
import com.yansheng.beans.factory.CannotLoadBeanClassException;
import com.yansheng.beans.factory.FactoryBean;
import com.yansheng.beans.factory.NoSuchBeanDefinitionException;
import com.yansheng.beans.factory.ObjectFactory;
import com.yansheng.beans.factory.SmartFactoryBean;
import com.yansheng.beans.factory.config.BeanDefinition;
import com.yansheng.beans.factory.config.BeanDefinitionHolder;
import com.yansheng.beans.factory.config.BeanExpressionContext;
import com.yansheng.beans.factory.config.BeanExpressionResolver;
import com.yansheng.beans.factory.config.BeanPostProcessor;
import com.yansheng.beans.factory.config.ConfigurableBeanFactory;
import com.yansheng.beans.factory.config.DestructionAwareBeanPostProcessor;
import com.yansheng.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.yansheng.beans.factory.config.Scope;

public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

	private BeanFactory parentBeanFactory;
	private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
	private ClassLoader tempClassLoader;
	private boolean cacheBeanMetadata = true;
	private BeanExpressionResolver beanExpressionResolver;
	private ConversionService conversionService;
	private final Set<PropertyEditorRegistrar> propertyEditorRegistrars = new LinkedHashSet<PropertyEditorRegistrar>(4);
	private TypeConverter typeConverter;
	private final Map<Class<?>, Class<? extends PropertyEditor>> customEditors = new HashMap<Class<?>, Class<? extends PropertyEditor>>(
			4);
	private final List<StringValueResolver> embeddedValueResolvers = new LinkedList<StringValueResolver>();
	private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();
	private boolean hasInstantiationAwareBeanPostProcessors;
	private boolean hasDestructionAwareBeanPostProcessors;
	private final Map<String, Scope> scopes = new HashMap<String, Scope>(8);
	private SecurityContextProvider securityContextProvider;
	private final Map<String, RootBeanDefinition> mergedBeanDefinitions = new ConcurrentHashMap<String, RootBeanDefinition>(
			64);
	private final Map<String, Boolean> alreadyCreated = new ConcurrentHashMap<String, Boolean>(64);
	private final ThreadLocal<Object> prototypesCurrentlyInCreation = new NamedThreadLocal<Object>(
			"Prototype beans currently in creation");

	public AbstractBeanFactory() {
	}

	public AbstractBeanFactory(BeanFactory parentBeanFactory) {
		this.parentBeanFactory = parentBeanFactory;
	}

	// ---------------------------------------------------------------------
	// Implementation of BeanFactory interface
	// ---------------------------------------------------------------------

	@Override
	public Object getBean(String name) throws BeansException {
		return doGetBean(name, null, null, false);
	}

	@Override
	public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
		return doGetBean(name, requiredType, null, false);
	}

	public <T> T getBean(String name, Class<T> requiredType, Object... args) throws BeansException {
		return doGetBean(name, requiredType, args, false);
	}

	@Override
	public Object getBean(String name, Object... args) throws BeansException {
		return doGetBean(name, null, args, false);
	}

	@SuppressWarnings("unchecked")
	protected <T> T doGetBean(final String name, final Class<T> requiredType, final Object[] args,
			boolean typeCheckOnly) throws BeansException {

		final String beanName = transformedBeanName(name);
		Object bean;

		Object sharedInstance = getSingleton(beanName);
		if (sharedInstance != null && args == null) {
			if (logger.isDebugEnabled()) {
				if (isSingletonCurrentlyInCreation(beanName)) {
					logger.debug("Returning eagerly cached instance of singleton bean '" + beanName
							+ "' that is not fully initialized yet - a consequence of a circular reference");
				} else {
					logger.debug("Returning cached instance of singleton bean '" + beanName + "'");
				}
			}
			bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);
		} else {
			if (isPrototypeCurrentlyInCreation(beanName)) {
				throw new BeanCurrentlyInCreationException(beanName);
			}
			BeanFactory parentBeanFactory = getParentBeanFactory();
			if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
				String nameToLookUp = originalBeanName(name);
				if (args != null) {
					return (T) parentBeanFactory.getBean(nameToLookUp, args);
				} else {
					return parentBeanFactory.getBean(nameToLookUp, requiredType);
				}
			}
			if (!typeCheckOnly) {
				markBeanAsCreated(beanName);
			}
			final RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
			checkMergedBeanDefinition(mbd, beanName, args);

			String[] dependsOn = mbd.getDependsOn();
			if (dependsOn != null) {
				for (String dependsOnBean : dependsOn) {
					getBean(dependsOnBean);
					registerDependentBean(dependsOnBean, beanName);
				}
			}

			// Create bean instance.
			if (mbd.isSingleton()) {
				sharedInstance = getSingleton(beanName, new ObjectFactory<Object>() {
					@Override
					public Object getObject() throws BeansException {
						try {
							return createBean(beanName, mbd, args);
						} catch (BeansException ex) {
							destroySingleton(beanName);
							throw ex;
						}
					}
				});
				bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
			} else if (mbd.isPrototype()) {
				Object prototypeInstance = null;
				try {
					beforePrototypeCreation(beanName);
					prototypeInstance = createBean(beanName, mbd, args);
				} finally {
					afterPrototypeCreation(beanName);
				}
				bean = getObjectForBeanInstance(prototypeInstance, name, beanName, mbd);
			} else {
				String scopeName = mbd.getScope();
				final Scope scope = this.scopes.get(scopeName);
				if (scope == null) {
					throw new IllegalStateException("No Scope registered for scope '" + scopeName + "'");
				}
				try {
					Object scopeInstance = scope.get(beanName, new ObjectFactory<Object>() {
						@Override
						public Object getObject() throws BeansException {
							beforePrototypeCreation(beanName);
							try {
								return createBean(beanName, mbd, args);
							} finally {
								afterPrototypeCreation(beanName);
							}
						}
					});
					bean = getObjectForBeanInstance(scopeInstance, name, beanName, mbd);
				} catch (IllegalStateException ex) {
					throw new BeanCreationException(beanName, "Scope '" + scopeName
							+ "' is not active for the current thread; "
							+ "consider defining a scoped proxy for this bean if you intend to refer to it from a singleton",
							ex);
				}
			}
		}

		if (requiredType != null && bean != null && !requiredType.isAssignableFrom(bean.getClass())) {
			try {
				return getTypeConverter().convertIfNecessary(bean, requiredType);
			} catch (TypeMismatchException ex) {
				if (logger.isDebugEnabled()) {
					logger.debug("Failed to convert bean '" + name + "' to required type ["
							+ ClassUtils.getQualifiedName(requiredType) + "]", ex);
				}
				throw new BeanNotOfRequiedTypeException(name, requiredType, bean.getClass());
			}
		}
		return (T) bean;
	}

	@Override
	public boolean containsBean(String name) {
		String beanName = transformedBeanName(name);
		if (containsSingleton(beanName) || containsBeanDefinition(beanName)) {
			return (!BeanFactoryUtils.isFactoryDereference(name) || isFactoryBean(name));
		}
		BeanFactory parentBeanFactory = getParentBeanFactory();
		return (parentBeanFactory != null && parentBeanFactory.containsBean(originalBeanName(name)));
	}

	@Override
	public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
		String beanName = transformedBeanName(name);
		Object beanInstance = getSingleton(beanName);
		if (beanInstance != null) {
			if (beanInstance instanceof FactoryBean) {
				return (BeanFactoryUtils.isFactoryDereference(beanName)
						|| ((FactoryBean<?>) beanInstance).isSingleton());
			} else {
				return !BeanFactoryUtils.isFactoryDereference(beanName);
			}
		} else if (containsSingleton(beanName)) {
			return true;
		} else {
			BeanFactory parentBeanFactory = getParentBeanFactory();
			if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
				return parentBeanFactory.isSingleton(originalBeanName(name));
			}
			RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
			if (mbd.isSingleton()) {
				if (isFactoryBean(beanName, mbd)) {
					if (BeanFactoryUtils.isFactoryDereference(name)) {
						return true;
					}
					FactoryBean<?> factoryBean = (FactoryBean<?>) getBean(FACTORY_BEAN_PREFIX + beanName);
					return factoryBean.isSingleton();
				} else {
					return !BeanFactoryUtils.isFactoryDereference(name);
				}
			} else {
				return false;
			}
		}
	}

	@Override
	public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
		String beanName = transformedBeanName(name);
		BeanFactory parentBeanFactory = getParentBeanFactory();
		if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
			return parentBeanFactory.isPrototype(originalBeanName(name));
		}
		RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
		if (mbd.isPrototype()) {
			return (!BeanFactoryUtils.isFactoryDereference(beanName) || isFactoryBean(beanName, mbd));
		} else {
			if (BeanFactoryUtils.isFactoryDereference(beanName)) {
				return false;
			}
			if (isFactoryBean(beanName, mbd)) {
				final FactoryBean<?> factoryBean = (FactoryBean<?>) getBean(FACTORY_BEAN_PREFIX + beanName);
				if (System.getSecurityManager() != null) {
					return AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
						public Boolean run() {
							return ((factoryBean instanceof SmartFactoryBean
									&& ((SmartFactoryBean<?>) factoryBean).isPrototype())
									|| !factoryBean.isSingleton());
						}
					}, getAccessControlContext());
				} else {
					return ((factoryBean instanceof SmartFactoryBean
							&& ((SmartFactoryBean<?>) factoryBean).isPrototype()) || !factoryBean.isSingleton());
				}
			} else {
				return false;
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean isTypeMatch(String name, Class<?> targetType) throws NoSuchBeanDefinitionException {
		String beanName = transformedBeanName(name);
		Class<?> typeToMatch = (targetType != null ? targetType : Object.class);

		Object beanInstance = getSingleton(beanName, false);
		if (beanInstance != null) {
			if (beanInstance instanceof FactoryBean) {
				if (!BeanFactoryUtils.isFactoryDereference(name)) {
					Class<?> type = getTypeForFactoryBean((FactoryBean<?>) beanInstance);
					return (type != null && ClassUtils.isAssignable(typeToMatch, type));
				} else {
					return ClassUtils.isAssignableValue(typeToMatch, beanInstance);
				}
			} else {
				return !BeanFactoryUtils.isFactoryDereference(beanName)
						&& ClassUtils.isAssignableValue(typeToMatch, beanInstance);
			}
		} else if (containsSingleton(beanName) && !containsBeanDefinition(beanName)) {
			return false;
		} else {
			BeanFactory parentBeanFactory = getParentBeanFactory();
			if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
				return parentBeanFactory.isTypeMatch(originalBeanName(name), targetType);
			}
			RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
			Class[] typesToMatch = (FactoryBean.class.equals(typeToMatch) ? new Class[] { typeToMatch }
					: new Class[] { FactoryBean.class, typeToMatch });

			// Check decorated bean definition, if any: We assume it'll be easier
			// to determine the decorated bean's type than the proxy's type.
			BeanDefinitionHolder dbd = mbd.getDecoratedDefinition();
			if (dbd != null && !BeanFactoryUtils.isFactoryDereference(name)) {
				RootBeanDefinition tbd = getMergedBeanDefinition(dbd.getBeanName(), dbd.getBeanDefinition(), mbd);
				Class<?> targetClass = predictBeanType(dbd.getBeanName(), tbd, typesToMatch);
				if (targetClass != null && !FactoryBean.class.isAssignableFrom(targetClass)) {
					return typeToMatch.isAssignableFrom(targetClass);
				}
			}

			Class<?> beanType = predictBeanType(beanName, mbd, typesToMatch);
			if (beanType == null) {
				return false;
			}

			// Check bean class whether we're dealing with a FactoryBean.
			if (FactoryBean.class.isAssignableFrom(beanType)) {
				if (!BeanFactoryUtils.isFactoryDereference(name)) {
					// If it's a FactoryBean, we want to look at what it creates, not the factory
					// class.
					beanType = getTypeForFactoryBean(beanName, mbd);
					if (beanType == null) {
						return false;
					}
				}
			} else if (BeanFactoryUtils.isFactoryDereference(name)) {
				// Special case: A SmartInstantiationAwareBeanPostProcessor returned a
				// non-FactoryBean
				// type but we nevertheless are being asked to dereference a FactoryBean...
				// Let's check the original bean class and proceed with it if it is a
				// FactoryBean.
				beanType = predictBeanType(beanName, mbd, FactoryBean.class);
				if (beanType == null || !FactoryBean.class.isAssignableFrom(beanType)) {
					return false;
				}
			}

			return typeToMatch.isAssignableFrom(beanType);
		}
	}

	@Override
	public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
		String beanName = transformedBeanName(name);

		// Check manually registered singletons.
		Object beanInstance = getSingleton(beanName, false);
		if (beanInstance != null) {
			if (beanInstance instanceof FactoryBean && !BeanFactoryUtils.isFactoryDereference(name)) {
				return getTypeForFactoryBean((FactoryBean<?>) beanInstance);
			} else {
				return beanInstance.getClass();
			}
		} else if (containsSingleton(beanName) && !containsBeanDefinition(beanName)) {
			// null instance registered
			return null;
		}

		else {
			// No singleton instance found -> check bean definition.
			BeanFactory parentBeanFactory = getParentBeanFactory();
			if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
				// No bean definition found in this factory -> delegate to parent.
				return parentBeanFactory.getType(originalBeanName(name));
			}

			RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);

			// Check decorated bean definition, if any: We assume it'll be easier
			// to determine the decorated bean's type than the proxy's type.
			BeanDefinitionHolder dbd = mbd.getDecoratedDefinition();
			if (dbd != null && !BeanFactoryUtils.isFactoryDereference(name)) {
				RootBeanDefinition tbd = getMergedBeanDefinition(dbd.getBeanName(), dbd.getBeanDefinition(), mbd);
				Class<?> targetClass = predictBeanType(dbd.getBeanName(), tbd);
				if (targetClass != null && !FactoryBean.class.isAssignableFrom(targetClass)) {
					return targetClass;
				}
			}

			Class<?> beanClass = predictBeanType(beanName, mbd);

			// Check bean class whether we're dealing with a FactoryBean.
			if (beanClass != null && FactoryBean.class.isAssignableFrom(beanClass)) {
				if (!BeanFactoryUtils.isFactoryDereference(name)) {
					// If it's a FactoryBean, we want to look at what it creates, not at the factory
					// class.
					return getTypeForFactoryBean(beanName, mbd);
				} else {
					return beanClass;
				}
			} else {
				return (!BeanFactoryUtils.isFactoryDereference(name) ? beanClass : null);
			}
		}
	}

	public String[] getAliases(String name) {
		String beanName = transformedBeanName(name);
		List<String> aliases = new ArrayList<String>();
		boolean factoryPrefix = name.startsWith(FACTORY_BEAN_PREFIX);
		String fullBeanName = beanName;
		if (factoryPrefix) {
			fullBeanName = FACTORY_BEAN_PREFIX + beanName;
		}
		if (!fullBeanName.equals(name)) {
			aliases.add(fullBeanName);
		}
		String[] retrievedAliases = super.getAliases(beanName);
		for (String retrievedAlias : retrievedAliases) {
			String alias = (factoryPrefix ? FACTORY_BEAN_PREFIX : "") + retrievedAlias;
			if (!alias.equals(name)) {
				aliases.add(alias);
			}
		}
		if (!containsSingleton(beanName) && !containsBeanDefinition(beanName)) {
			BeanFactory parentBeanFactory = getParentBeanFactory();
			if (parentBeanFactory != null) {
				aliases.addAll(Arrays.asList(parentBeanFactory.getAliases(fullBeanName)));
			}
		}
		return StringUtils.toStringArray(aliases);
	}

	// ---------------------------------------------------------------------
	// Implementation of HierarchicalBeanFactory interface
	// ---------------------------------------------------------------------
	public BeanFactory getParentBeanFactory() {
		return this.parentBeanFactory;
	}

	public boolean containsLocalBean(String name) {
		String beanName = transformedBeanName(name);
		return ((containsSingleton(beanName) || containsBeanDefinition(beanName))
				&& (!BeanFactoryUtils.isFactoryDereference(name) || isFactoryBean(beanName)));
	}

	// ---------------------------------------------------------------------
	// Implementation of ConfigurableBeanFactory interface
	// ---------------------------------------------------------------------

	@Override
	public void setParentBeanFactory(BeanFactory parentBeanFactory) throws IllegalStateException {
		if (this.parentBeanFactory != null && this.parentBeanFactory != parentBeanFactory) {
			throw new IllegalStateException("Already associated with parent BeanFactory: " + this.parentBeanFactory);
		}
		this.parentBeanFactory = parentBeanFactory;
	}

	@Override
	public void setBeanClassLoader(ClassLoader beanClassLoader) {
		this.beanClassLoader = (beanClassLoader != null ? beanClassLoader : ClassUtils.getDefaultClassLoader());
	}

	@Override
	public ClassLoader getBeanClassLoader() {
		return this.beanClassLoader;
	}

	@Override
	public void setTempClassLoader(ClassLoader tempClassLoader) {
		this.tempClassLoader = tempClassLoader;
	}

	@Override
	public ClassLoader getTempClassLoader() {
		return this.tempClassLoader;
	}

	@Override
	public void setCacheBeanMetadata(boolean cacheBeanMetadata) {
		this.cacheBeanMetadata = cacheBeanMetadata;
	}

	@Override
	public boolean isCacheBeanMetadata() {
		return this.cacheBeanMetadata;
	}

	@Override
	public void setBeanExpressionResolver(BeanExpressionResolver resolver) {
		this.beanExpressionResolver = resolver;
	}

	@Override
	public BeanExpressionResolver getBeanExpressionResolver() {
		return this.beanExpressionResolver;
	}

	@Override
	public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	@Override
	public ConversionService getConversionService() {
		return this.conversionService;
	}

	@Override
	public void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar) {
		Assert.notNull(registrar, "PropertyEditorRegistrar must not be null");
		this.propertyEditorRegistrars.add(registrar);
	}

	public Set<PropertyEditorRegistrar> getPropertyEditorRegistrars() {
		return this.propertyEditorRegistrars;
	}

	@Override
	public void registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor> propertyEditorClass) {
		Assert.notNull(requiredType, "Required type must not be null");
		Assert.isAssignable(PropertyEditor.class, propertyEditorClass);
		this.customEditors.put(requiredType, propertyEditorClass);
	}

	@Override
	public void copyRegisteredEditorsTo(PropertyEditorRegistry registry) {
		registerCustomEditors(registry);
	}

	public Map<Class<?>, Class<? extends PropertyEditor>> getCustomEditors() {
		return this.customEditors;
	}

	@Override
	public void setTypeConverter(TypeConverter typeConverter) {
		this.typeConverter = typeConverter;
	}

	public TypeConverter getCustomTypeConverter() {
		return this.typeConverter;
	}

	@Override
	public TypeConverter getTypeConverter() {
		TypeConverter customTypeConverter = getCustomTypeConverter();
		if (customTypeConverter != null) {
			return customTypeConverter;
		} else {
			SimpleTypeConverter typeConverter = new SimpleTypeConverter();
			typeConverter.setConversionService(getConversionService());
			registerCustomEditors(typeConverter);
			return this.typeConverter;
		}
	}

	@Override
	public void addEmbededValueResolver(StringValueResolver valueResolver) {
		Assert.notNull(valueResolver, "StringValueResolver must not be null");
		this.embeddedValueResolvers.add(valueResolver);
	}

	@Override
	public String resolveEmbededValue(String value) {
		String result = value;
		for (StringValueResolver resolver : this.embeddedValueResolvers) {
			if (result == null) {
				return null;
			}
			result = resolver.resolveStringValue(result);
		}
		return result;
	}

	@Override
	public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
		Assert.notNull(beanPostProcessor, "BeanPostProcessor must not be null");
		this.beanPostProcessors.remove(beanPostProcessor);
		this.beanPostProcessors.add(beanPostProcessor);
		if (beanPostProcessors instanceof InstantiationAwareBeanPostProcessor) {
			this.hasInstantiationAwareBeanPostProcessors = true;
		}
		if (beanPostProcessors instanceof DestructionAwareBeanPostProcessor) {
			this.hasDestructionAwareBeanPostProcessors = true;
		}
	}

	@Override
	public int getBeanPostProcessorCount() {
		return this.beanPostProcessors.size();
	}

	public List<BeanPostProcessor> getBeanPostProcessors() {
		return this.beanPostProcessors;
	}

	protected boolean hasInstantiationAwareBeanPostProcessors() {
		return this.hasInstantiationAwareBeanPostProcessors;
	}

	protected boolean hasDestructionAwareBeanPostProcessors() {
		return this.hasDestructionAwareBeanPostProcessors;
	}

	@Override
	public void registerScope(String scopeName, Scope scope) {
		Assert.notNull(scopeName, "Scope identifier must not be null");
		Assert.notNull(scope, "Scope must not be null");
		if (SCOPE_SINGLETON.equals(scopeName) || SCOPE_PROTOTYPE.equals(scopeName)) {
			throw new IllegalArgumentException("Cannot replace existing scopes 'singleton' and 'prototype'");
		}
		this.scopes.put(scopeName, scope);
	}

	@Override
	public String[] getRegisteredScopeNames() {
		return StringUtils.toStringArray(this.scopes.keySet());
	}

	@Override
	public Scope getRegisteredScope(String scopeName) {
		Assert.notNull(scopeName, "Scope identifier must not be null");
		return this.scopes.get(scopeName);
	}

	public void setSecurityContextProvider(SecurityContextProvider securityContextProvider) {
		this.securityContextProvider = securityContextProvider;
	}

	public AccessControlContext getAccessControlContext() {
		return (this.securityContextProvider != null ? this.securityContextProvider.getAccessControlContext()
				: AccessController.getContext());
	}

	@Override
	public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory) {
		Assert.notNull(otherFactory, "BeanFactory must not be null");
		setBeanClassLoader(otherFactory.getBeanClassLoader());
		setCacheBeanMetadata(otherFactory.isCacheBeanMetadata());
		setBeanExpressionResolver(otherFactory.getBeanExpressionResolver());
		if (otherFactory instanceof AbstractBeanFactory) {
			AbstractBeanFactory otherAbstractFactory = (AbstractBeanFactory) otherFactory;
			this.customEditors.putAll(otherAbstractFactory.customEditors);
			this.propertyEditorRegistrars.addAll(otherAbstractFactory.propertyEditorRegistrars);
			this.beanPostProcessors.addAll(otherAbstractFactory.beanPostProcessors);
			this.hasInstantiationAwareBeanPostProcessors = this.hasInstantiationAwareBeanPostProcessors
					|| otherAbstractFactory.hasInstantiationAwareBeanPostProcessors;
			this.hasDestructionAwareBeanPostProcessors = this.hasDestructionAwareBeanPostProcessors
					|| otherAbstractFactory.hasDestructionAwareBeanPostProcessors;
			this.scopes.putAll(otherAbstractFactory.scopes);
			this.securityContextProvider = otherAbstractFactory.securityContextProvider;
		} else {
			setTypeConverter(otherFactory.getTypeConverter());
		}
	}

	@Override
	public BeanDefinition getMergedBeanDefinition(String beanName) throws BeansException {
		String transformBeanName = transformedBeanName(beanName);
		if (!containsBeanDefinition(transformBeanName) && getParentBeanFactory() instanceof ConfigurableBeanFactory) {
			return ((ConfigurableBeanFactory) getParentBeanFactory()).getMergedBeanDefinition(transformBeanName);
		}
		return getMergedLocalBeanDefinition(transformBeanName);
	}

	@Override
	public boolean isFactoryBean(String name) throws NoSuchBeanDefinitionException {
		String beanName = transformedBeanName(name);
		Object beanInstance = getSingleton(beanName, false);
		if (beanInstance != null) {
			return (beanInstance instanceof FactoryBean);
		} else if (containsSingleton(beanName)) {
			return false;
		}
		if (!containsBeanDefinition(beanName) && getParentBeanFactory() instanceof ConfigurableBeanFactory) {
			return ((ConfigurableBeanFactory) getParentBeanFactory()).isFactoryBean(beanName);
		}
		return isFactoryBean(beanName, getMergedLocalBeanDefinition(beanName));
	}

	@Override
	public boolean isActuallyInCreation(String beanName) {
		return isSingletonCurrentlyInCreation(beanName) || isPrototypeCurrentlyInCreation(beanName);
	}

	protected boolean isPrototypeCurrentlyInCreation(String beanName) {
		Object curval = this.prototypesCurrentlyInCreation.get();
		return (curval != null
				&& (curval.equals(beanName) || (curval instanceof Set && ((Set<?>) curval).contains(beanName))));
	}

	@SuppressWarnings("unchecked")
	protected void beforePrototypeCreation(String beanName) {
		Object curVal = this.prototypesCurrentlyInCreation.get();
		if (curVal == null) {
			this.prototypesCurrentlyInCreation.set(beanName);
		} else if (curVal instanceof String) {
			Set<String> beanNameSet = new HashSet<String>(2);
			beanNameSet.add((String) curVal);
			beanNameSet.add(beanName);
		} else {
			Set<String> beanNameSet = (Set<String>) curVal;
			beanNameSet.add(beanName);
		}
	}

	@SuppressWarnings("unchecked")
	protected void afterPrototypeCreation(String beanName) {

		Object curVal = this.prototypesCurrentlyInCreation.get();
		if (curVal instanceof String) {
			this.prototypesCurrentlyInCreation.remove();
		} else if (curVal instanceof Set) {
			Set<String> beanNameSet = (Set<String>) curVal;
			beanNameSet.remove(beanName);
			if (beanNameSet.isEmpty()) {
				this.prototypesCurrentlyInCreation.remove();
			}
		}
	}

	@Override
	public void destroyBean(String beanName, Object beanInstance) {
		destroyBean(beanName, beanInstance, getMergedLocalBeanDefinition(beanName));
	}

	protected void destroyBean(String beanName, Object beanInstance, RootBeanDefinition mbd) {
		new DisposableBeanAdapter(beanInstance, beanName, mbd, getBeanPostProcessors(), getAccessControlContext())
				.destroy();
	}

	@Override
	public void destroyScopedBean(String beanName) {
		RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
		if (mbd.isSingleton() || mbd.isPrototype()) {
			throw new IllegalArgumentException(
					"Bean name '" + beanName + "' does not correspond to an object in a mutable scope");
		}
		String scopeName = mbd.getScope();
		Scope scope = this.scopes.get(scopeName);
		if (scope == null) {
			throw new IllegalStateException("No Scope SPI registered for scope '" + scopeName + "'");
		}
		Object bean = scope.remove(scopeName);
		if (bean != null) {
			destroyBean(beanName, bean, mbd);
		}
	}

	// ---------------------------------------------------------------------
	// Implementation methods
	// ---------------------------------------------------------------------

	protected String transformedBeanName(String name) {
		return canonicalName(BeanFactoryUtils.transformedBeanName(name));
	}

	protected String originalBeanName(String name) {
		String beanName = transformedBeanName(name);
		if (name.startsWith(FACTORY_BEAN_PREFIX)) {
			beanName = FACTORY_BEAN_PREFIX + beanName;
		}
		return beanName;
	}

	protected void initBeanWrapper(BeanWrapper bw) {
		bw.setConversionService(getConversionService());
		registerCustomEditors(bw);
	}

	protected void registerCustomEditors(PropertyEditorRegistry registry) {
		PropertyEditorRegistrySupport registrySupport = (registry instanceof PropertyEditorRegistrySupport
				? (PropertyEditorRegistrySupport) registry
				: null);
		if (registrySupport != null) {
			registrySupport.setConfigValueEditors();
		}
		if (!this.propertyEditorRegistrars.isEmpty()) {
			for (PropertyEditorRegistrar registrar : this.propertyEditorRegistrars) {
				try {
					registrar.registerCustomEditors(registry);
				} catch (BeanCreationException ex) {
					Throwable rootCause = ex.getMostSpecificCause();
					if (rootCause instanceof BeanCurrentlyInCreationException) {
						BeanCreationException bce = (BeanCreationException) rootCause;
						if (isCurrentlyInCreation(bce.getBeanName())) {
							if (logger.isDebugEnabled()) {
								logger.debug("PropertyEditorRegistrar [" + registrar.getClass().getName()
										+ "] failed because it tried to obtain currently created bean '"
										+ ex.getBeanName() + "': " + ex.getMessage());
							}
							onSuppressedException(ex);
							continue;
						}
					}
					throw ex;
				}
			}
		}
		if (!this.customEditors.isEmpty()) {
			for (Map.Entry<Class<?>, Class<? extends PropertyEditor>> entry : this.customEditors.entrySet()) {
				Class<?> requiredType = entry.getKey();
				Class<? extends PropertyEditor> editorClass = entry.getValue();
				registry.registerCustomEditor(requiredType, BeanUtils.instantiate(editorClass));
			}
		}
	}

	protected RootBeanDefinition getMergedLocalBeanDefinition(String beanName) throws BeansException {
		RootBeanDefinition mbd = this.mergedBeanDefinitions.get(beanName);
		if (mbd != null) {
			return mbd;
		}
		return getMergedBeanDefinition(beanName, getBeanDefinition(beanName));
	}

	protected RootBeanDefinition getMergedBeanDefinition(String beanName, BeanDefinition bd)
			throws BeanDefinitionStoreException {
		return getMergedBeanDefinition(beanName, bd, null);
	}

	protected RootBeanDefinition getMergedBeanDefinition(String beanName, BeanDefinition bd,
			BeanDefinition containingBd) throws BeanDefinitionStoreException {
		synchronized (this.mergedBeanDefinitions) {
			RootBeanDefinition mbd = null;
			if (containingBd == null) {
				mbd = this.mergedBeanDefinitions.get(beanName);
			}
			if (mbd == null) {
				if (bd.getParentName() == null) {
					if (bd instanceof RootBeanDefinition) {
						mbd = ((RootBeanDefinition) bd).cloneBeanDefinition();
					} else {
						mbd = new RootBeanDefinition(bd);
					}
				} else {
					BeanDefinition pbd;
					try {
						String parentBeanName = transformedBeanName(bd.getParentName());
						if (!beanName.equals(parentBeanName)) {
							pbd = getMergedBeanDefinition(parentBeanName);
						} else {
							if (getParentBeanFactory() instanceof ConfigurableBeanFactory) {
								pbd = ((ConfigurableBeanFactory) getParentBeanFactory())
										.getMergedBeanDefinition(parentBeanName);
							} else {
								throw new NoSuchBeanDefinitionException(bd.getParentName(),
										"Parent name '" + bd.getParentName() + "' is equal to bean name '" + beanName
												+ "': cannot be resolved without an AbstractBeanFactory parent");
							}
						}
					} catch (NoSuchBeanDefinitionException ex) {
						throw new BeanDefinitionStoreException(bd.getResourceDescription(), beanName,
								"Could not resolve parent bean definition '" + bd.getParentName() + "'", ex);
					}
					mbd = new RootBeanDefinition(pbd);
					mbd.overrideFrom(bd);
				}

				if (!StringUtils.hasLength(mbd.getScope())) {
					mbd.setScope(RootBeanDefinition.SCOPE_SINGLETON);
				}
				if (containingBd != null && !containingBd.isSingleton() && mbd.isSingleton()) {
					mbd.setScope(containingBd.getScope());
				}
				if (containingBd == null && isCacheBeanMetadata() && isBeanEligibleForMetadataCaching(beanName)) {
					this.mergedBeanDefinitions.put(beanName, mbd);
				}
			}
			return mbd;
		}
	}

	protected void checkMergedBeanDefinition(RootBeanDefinition mbd, String beanName, Object[] args)
			throws BeanDefinitionStoreException {
		if (mbd.isAbstract()) {
			throw new BeanIsAbstractException(beanName);
		}
		if (args != null && !mbd.isPrototype()) {
			throw new BeanDefinitionStoreException(
					"Can only specify arguments for the getBean method when referring to a prototype bean definition");
		}
	}

	protected void clearMergedBeanDefinition(String beanName) {
		this.mergedBeanDefinitions.remove(beanName);
	}

	protected Class<?> resolveBeanClass(final RootBeanDefinition mbd, String beanName, final Class<?>... typesToMatch) {
		try {
			if (mbd.hasBeanClass()) {
				return mbd.getBeanClass();
			}
			if (System.getSecurityManager() != null) {
				return AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>() {
					public Class<?> run() throws Exception {
						return doResolveBeanClass(mbd, typesToMatch);
					}
				}, getAccessControlContext());
			} else {
				return doResolveBeanClass(mbd, typesToMatch);
			}
		} catch (PrivilegedActionException pae) {
			ClassNotFoundException ex = (ClassNotFoundException) pae.getException();
			throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), ex);
		} catch (ClassNotFoundException ex) {
			throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), ex);
		} catch (LinkageError err) {
			throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), err);
		}
	}

	protected Class<?> doResolveBeanClass(RootBeanDefinition mbd, Class<?>... typesToMatch)
			throws ClassNotFoundException {
		if (!ObjectUtils.isEmpty(typesToMatch)) {
			ClassLoader tempClassLoader = getTempClassLoader();
			if (tempClassLoader != null) {
				if (tempClassLoader instanceof DecoratingClassLoader) {
					DecoratingClassLoader dcl = (DecoratingClassLoader) tempClassLoader;
					for (Class<?> typeToMatch : typesToMatch) {
						dcl.excludeClass(typeToMatch.getName());
					}
				}
				String className = mbd.getBeanClassName();
				return (className != null ? ClassUtils.forName(className, tempClassLoader) : null);
			}
		}
		return mbd.resolveBeanClass(getBeanClassLoader());
	}

	protected Object evaluateBeanDefinitionString(String value, BeanDefinition beanDefinition) {
		if (this.beanExpressionResolver == null) {
			return value;
		}
		Scope scope = (beanDefinition != null ? getRegisteredScope(beanDefinition.getScope()) : null);
		return this.beanExpressionResolver.evaluate(value, new BeanExpressionContext(this, scope));
	}

	protected Class<?> predictBeanType(String beanName, RootBeanDefinition mbd, Class<?>... typesToMatch) {
		if (mbd.getFactoryMethodName() != null) {
			return null;
		}
		return resolveBeanClass(mbd, beanName, typesToMatch);
	}

	protected boolean isFactoryBean(String beanName, RootBeanDefinition mbd) {
		Class<?> beanType = predictBeanType(beanName, mbd, FactoryBean.class);
		return (beanType != null && FactoryBean.class.isAssignableFrom(beanType));
	}

	protected Class<?> getTypeForFactoryBean(String beanName, RootBeanDefinition mbd) {
		if (!mbd.isSingleton()) {
			return null;
		}
		try {
			FactoryBean<?> factoryBean = doGetBean(FACTORY_BEAN_PREFIX + beanName, FactoryBean.class, null, true);
			return getTypeForFactoryBean(factoryBean);
		} catch (BeanCreationException ex) {
			// Can only happen when getting a FactoryBean.
			if (logger.isDebugEnabled()) {
				logger.debug("Ignoring bean creation exception on FactoryBean type check: " + ex);
			}
			onSuppressedException(ex);
			return null;
		}
	}

	protected void markBeanAsCreated(String beanName) {
		this.alreadyCreated.put(beanName, Boolean.TRUE);
	}

	protected boolean isBeanEligibleForMetadataCaching(String beanName) {
		return this.alreadyCreated.containsKey(beanName);
	}

	protected boolean removeSingletonIfCreatedForTypeCheckOnly(String beanName) {
		if (!this.alreadyCreated.containsKey(beanName)) {
			removeSingleton(beanName);
			return true;
		} else {
			return false;
		}
	}

	protected Object getObjectForBeanInstance(Object beanInstance, String name, String beanName,
			RootBeanDefinition mbd) {
		if (BeanFactoryUtils.isFactoryDereference(name) && !(beanInstance instanceof FactoryBean)) {
			throw new BeanIsNotAFactoryException(transformedBeanName(name), beanInstance.getClass());
		}
		if (!(beanInstance instanceof FactoryBean) || BeanFactoryUtils.isFactoryDereference(name)) {
			return beanInstance;
		}
		Object object = null;
		if (mbd == null) {
			object = getCachedObjectForFactoryBean(beanName);
		}
		if (object == null) {
			FactoryBean<?> factory = (FactoryBean<?>) beanInstance;
			if (mbd == null && containsBeanDefinition(beanName)) {
				mbd = getMergedLocalBeanDefinition(beanName);
			}
			boolean synthetic = (mbd != null && mbd.isSynthetic());
			object = getObjectFromFactoryBean(factory, beanName, !synthetic);
		}
		return object;
	}

	public boolean isBeanNameInUse(String beanName) {
		return isAlias(beanName) || containsLocalBean(beanName) || hasDependentBean(beanName);
	}

	protected boolean requiresDestruction(Object bean, RootBeanDefinition mbd) {
		return (bean != null
				&& (DisposableBeanAdapter.hasDestroyMethod(bean, mbd) || hasDestructionAwareBeanPostProcessors()));
	}

	protected void registerDisposableBeanIfNecessary(String beanName, Object bean, RootBeanDefinition mbd) {
		AccessControlContext acc = (System.getSecurityManager() != null ? getAccessControlContext() : null);
		if (!mbd.isPrototype() && requiresDestruction(bean, mbd)) {
			if (mbd.isSingleton()) {
				registerDisposableBean(beanName,
						new DisposableBeanAdapter(bean, beanName, mbd, getBeanPostProcessors(), acc));
			} else {
				Scope scope = this.scopes.get(mbd.getScope());
				if (scope == null) {
					throw new IllegalStateException("No Scope registered for scope '" + mbd.getScope() + "'");
				}
				scope.registerDestructionCallback(beanName,
						new DisposableBeanAdapter(bean, beanName, mbd, getBeanPostProcessors(), acc));
			}
		}
	}

	// ---------------------------------------------------------------------
	// Abstract methods to be implemented by subclasses
	// ---------------------------------------------------------------------
	protected abstract boolean containsBeanDefinition(String beanName);

	protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

	protected abstract Object createBean(String beanName, RootBeanDefinition mbd, Object[] args)
			throws BeanCreationException;

}
