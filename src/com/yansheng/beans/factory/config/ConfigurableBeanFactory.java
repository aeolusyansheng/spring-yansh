package com.yansheng.beans.factory.config;

import java.beans.PropertyEditor;
import java.security.AccessControlContext;

import org.springframework.core.convert.ConversionService;
import org.springframework.util.StringValueResolver;

import com.yansheng.beans.PropertyEditorRegistrar;
import com.yansheng.beans.PropertyEditorRegistry;
import com.yansheng.beans.TypeConverter;
import com.yansheng.beans.factory.BeanDefinitionStoreException;
import com.yansheng.beans.factory.BeanFactory;
import com.yansheng.beans.factory.HierarchicalBeanFactory;
import com.yansheng.beans.factory.NoSuchBeanDefinitionException;

public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

	String SCOPE_SINGLETON = "singleton";
	String SCOPE_PROTOTYPE = "prototype";

	void setParentBeanFactory(BeanFactory parentBeanFactory) throws IllegalStateException;

	void setBeanClassLoader(ClassLoader beanClassLoader);

	ClassLoader getBeanClassLoader();

	void setTempClassLoader(ClassLoader tempClassLoader);

	ClassLoader getTempClassLoader();

	void setCacheBeanMetadata(boolean cacheBeanMetadata);

	boolean isCacheBeanMetadata();

	void setBeanExpressionResolver(BeanExpressionResolver resolver);

	BeanExpressionResolver getBeanExpressionResolver();

	void setConversionService(ConversionService conversionService);

	ConversionService getConversionService();

	void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar);

	void registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor> propertyEditorClass);

	void copyRegisteredEditorsTo(PropertyEditorRegistry registry);

	void setTypeConverter(TypeConverter typeConverter);

	TypeConverter getTypeConverter();

	void addEmbededValueResolver(StringValueResolver valueResolver);

	String resolveEmbededValue(String value);

	void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

	int getBeanPostProcessorCount();

	void registerScope(String scopeName, Scope scope);

	String[] getRegisteredScopeNames();

	Scope getRegisteredScope(String scopeName);

	AccessControlContext getAccessControlContext();

	void copyConfigurationFrom(ConfigurableBeanFactory otherFactory);

	void registerAlias(String beanName, String alias) throws BeanDefinitionStoreException;

	void resolveAliases(StringValueResolver valueResolver);

	BeanDefinition getMergedBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

	boolean isFactoryBean(String name) throws NoSuchBeanDefinitionException;

	void setCurrentlyInCreation(String beanName, boolean inCreation);

	boolean isCurrentlyInCreation(String beanName);

	void registerDependentBean(String beanName, String dependentBeanName);

	String[] getDependentBeans(String beanName);

	String[] getDependenciesForBean(String beanName);

	void destroyBean(String beanName, Object beanInstance);

	void destroyScopedBean(String beanName);

	void destroySingletons();

}
