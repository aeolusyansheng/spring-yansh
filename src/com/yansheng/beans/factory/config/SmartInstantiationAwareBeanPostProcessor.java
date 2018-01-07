package com.yansheng.beans.factory.config;

import java.lang.reflect.Constructor;

import com.yansheng.beans.BeansException;

public interface SmartInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessor {

	Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException;

	Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) throws BeansException;

	Object getEarlyBeanReference(Object bean, String beanName) throws BeansException;
}
