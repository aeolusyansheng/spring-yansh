package com.yansheng.beans.factory.support;

import com.yansheng.beans.factory.config.BeanDefinition;

public class DefaultBeanNameGenerator implements BeanNameGenerator {

	@Override
	public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
		return BeanDefinitionReaderUtils.generateBeanName(definition, registry);
	}

}
