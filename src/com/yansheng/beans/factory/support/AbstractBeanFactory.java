package com.yansheng.beans.factory.support;

import java.beans.PropertyEditor;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.beans.factory.BeanIsAbstractException;
import org.springframework.beans.factory.BeanIsNotAFactoryException;
import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.core.DecoratingClassLoader;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.convert.ConversionService;
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
import com.yansheng.beans.TypeConverter;
import com.yansheng.beans.factory.BeanCreationException;
import com.yansheng.beans.factory.BeanDefinitionStoreException;
import com.yansheng.beans.factory.BeanFactory;
import com.yansheng.beans.factory.BeanFactoryUtils;
import com.yansheng.beans.factory.FactoryBean;
import com.yansheng.beans.factory.NoSuchBeanDefinitionException;
import com.yansheng.beans.factory.config.BeanDefinition;
import com.yansheng.beans.factory.config.BeanExpressionContext;
import com.yansheng.beans.factory.config.BeanExpressionResolver;
import com.yansheng.beans.factory.config.BeanPostProcessor;
import com.yansheng.beans.factory.config.ConfigurableBeanFactory;
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
	public BeanFactory getParentBeanFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsLocalBean(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getBean(String name) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getBean(Class<T> requiredType) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getBean(String name, Object... args) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	protected <T> T doGetBean(final String name, final Class<T> requiredType, final Object[] args,
			boolean typeCheckOnly) throws BeansException {

	}

	@Override
	public boolean containsBean(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTypeMatch(String name, Class<?> targetType) throws NoSuchBeanDefinitionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParentBeanFactory(BeanFactory parentBeanFactory) throws IllegalStateException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBeanClassLoader(ClassLoader beanClassLoader) {
		// TODO Auto-generated method stub

	}

	@Override
	public ClassLoader getBeanClassLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTempClassLoader(ClassLoader tempClassLoader) {
		// TODO Auto-generated method stub

	}

	@Override
	public ClassLoader getTempClassLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCacheBeanMetadata(boolean cacheBeanMetadata) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isCacheBeanMetadata() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setBeanExpressionResolver(BeanExpressionResolver resolver) {
		// TODO Auto-generated method stub

	}

	@Override
	public BeanExpressionResolver getBeanExpressionResolver() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setConversionService(ConversionService conversionService) {
		// TODO Auto-generated method stub

	}

	@Override
	public ConversionService getConversionService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor> propertyEditorClass) {
		// TODO Auto-generated method stub

	}

	@Override
	public void copyRegisteredEditorsTo(PropertyEditorRegistry registry) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTypeConverter(TypeConverter typeConverter) {
		// TODO Auto-generated method stub

	}

	@Override
	public TypeConverter getTypeConverter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addEmbededValueResolver(StringValueResolver valueResolver) {
		// TODO Auto-generated method stub

	}

	@Override
	public String resolveEmbededValue(String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getBeanPostProcessorCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void registerScope(String scopeName, Scope scope) {
		// TODO Auto-generated method stub

	}

	@Override
	public String[] getRegisteredScopeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Scope getRegisteredScope(String scopeName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory) {
		// TODO Auto-generated method stub

	}

	@Override
	public BeanDefinition getMergedBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isFactoryBean(String name) throws NoSuchBeanDefinitionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void destroyBean(String beanName, Object beanInstance) {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroyScopedBean(String beanName) {
		// TODO Auto-generated method stub

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
		getMergedBeanDefinition(beanName, bd, null);
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

	protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

	protected abstract boolean containsBeanDefinition(String beanName);

}
