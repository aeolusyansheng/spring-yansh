package com.yansheng.beans.factory.config;

import com.yansheng.beans.BeansException;

public interface BeanFactoryPostProcessor {

	void postProcessBeanFacotry(ConfigurableListableBeanFactory beanFactory) throws BeansException;
}
