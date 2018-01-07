package com.yansheng.beans.factory.config;

import java.beans.PropertyDescriptor;

import com.yansheng.beans.BeansException;
import com.yansheng.beans.PropertyValues;

public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

	Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;

	Object postProcessAfterInstantiation(Class<?> beanClass, String beanName) throws BeansException;

	PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName)
			throws BeansException;
}
