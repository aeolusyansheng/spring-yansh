package com.yansheng.beans.factory.support;

import com.yansheng.beans.factory.config.BeanDefinition;

public interface BeanNameGenerator {

	String generateBeanName(BeanDefinition definiton, BeanDefinitionRegistry registry);
}
