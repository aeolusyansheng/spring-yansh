package com.yansheng.beans.factory.support;

import java.beans.PropertyEditor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringValueResolver;

import com.yansheng.beans.BeanUtils;
import com.yansheng.beans.BeanWrapper;
import com.yansheng.beans.BeansException;
import com.yansheng.beans.PropertyEditorRegistrar;
import com.yansheng.beans.PropertyEditorRegistry;
import com.yansheng.beans.PropertyEditorRegistrySupport;
import com.yansheng.beans.TypeConverter;
import com.yansheng.beans.factory.BeanCreationException;
import com.yansheng.beans.factory.BeanFactory;
import com.yansheng.beans.factory.BeanFactoryUtils;
import com.yansheng.beans.factory.NoSuchBeanDefinitionException;
import com.yansheng.beans.factory.config.BeanDefinition;
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

    //---------------------------------------------------------------------
    // Implementation of BeanFactory interface
    //---------------------------------------------------------------------

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

    //---------------------------------------------------------------------
    // Implementation methods
    //---------------------------------------------------------------------

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
                ? (PropertyEditorRegistrySupport) registry : null);
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
                                logger.debug("PropertyEditorRegistrar [" + registrar.getClass().getName() +
                                        "] failed because it tried to obtain currently created bean '" +
                                        ex.getBeanName() + "': " + ex.getMessage());
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
}
