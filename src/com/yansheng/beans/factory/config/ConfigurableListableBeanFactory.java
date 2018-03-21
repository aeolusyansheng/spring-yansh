package com.yansheng.beans.factory.config;

import com.yansheng.beans.BeansException;
import com.yansheng.beans.factory.ListableBeanFactory;
import com.yansheng.beans.factory.NoSuchBeanDefinitionException;

public interface ConfigurableListableBeanFactory
		extends ListableBeanFactory, ConfigurableBeanFactory, AutowireCapableBeanFactory {

	void ignoreDependencyType(Class<?> type);

	void ignoreDependencyInterface(Class<?> ifc);

	void registerResolvableDependency(Class<?> dependencyType, Object autowiredValue);

	boolean isAutowireCandidate(String beanName, DependencyDescriptor descriptor) throws NoSuchBeanDefinitionException;

	BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

	void freezeConfiguration();

	boolean isConfigurationFrozen();

	void preInstantiateSingletons() throws BeansException;
}
