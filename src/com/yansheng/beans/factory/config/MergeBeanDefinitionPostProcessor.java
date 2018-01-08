package com.yansheng.beans.factory.config;

import com.yansheng.beans.factory.support.RootBeanDefinition;

public interface MergeBeanDefinitionPostProcessor extends BeanPostProcessor {

	void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName);
}
