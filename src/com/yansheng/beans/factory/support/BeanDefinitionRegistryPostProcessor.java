package com.yansheng.beans.factory.support;

import com.yansheng.beans.BeansException;
import com.yansheng.beans.factory.config.BeanFactoryPostProcessor;

public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor {

	void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException;
}
