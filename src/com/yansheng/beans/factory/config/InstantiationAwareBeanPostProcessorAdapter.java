package com.yansheng.beans.factory.config;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;

import com.yansheng.beans.BeansException;
import com.yansheng.beans.PropertyValues;

public abstract class InstantiationAwareBeanPostProcessorAdapter implements SmartInstantiationAwareBeanPostProcessor {

	@Override
	public Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
		return null;
	}

	@Override
	public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) throws BeansException {
		return null;
	}

	@Override
	public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		return null;
	}

	@Override
	public Object postProcessAfterInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		return null;
	}

	@Override
	public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean,
			String beanName) throws BeansException {
		return pvs;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
