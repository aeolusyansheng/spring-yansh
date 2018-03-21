package com.yansheng.beans.factory.config;

import java.util.Set;

import com.yansheng.beans.BeansException;
import com.yansheng.beans.TypeConverter;
import com.yansheng.beans.factory.BeanFactory;

public interface AutowireCapableBeanFactory extends BeanFactory {

	int AUTOWIRE_NO = 0;
	int AUTOWIRE_BY_NAME = 1;
	int AUTOWIRE_BY_TYPE = 2;
	int AUTOWIRE_CONSTRUCTOR = 3;
	int AUTOWIRE_AUTODETECT = 4;

	<T> T createBean(Class<T> beanClass) throws BeansException;

	void autowireBean(Object existingBean) throws BeansException;

	Object configureBean(Object existingBean, String beanName) throws BeansException;

	Object resolveDependency(DependencyDescriptor descriptor, String beanName) throws BeansException;

	Object createBean(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException;

	Object autowire(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException;

	void autowireBeanProperties(Object existingBean, int autowireMode, boolean dependencyCheck) throws BeansException;

	void applyBeanPropertyValues(Object existingBean, String beanName) throws BeansException;

	Object initializeBean(Object existingBean, String beanName) throws BeansException;

	Object applyBeanPostProcessorBeforeInitialization(Object existingBean, String beanName) throws BeansException;

	Object applyBeanPostProcessorAfterInitialization(Object existingBean, String beanName) throws BeansException;

	Object resolveDependency(DependencyDescriptor descriptor, String beanName, Set<String> autowiredBeanNames,
			TypeConverter typeCpnverter) throws BeansException;
}
