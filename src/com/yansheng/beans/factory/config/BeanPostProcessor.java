package com.yansheng.beans.factory.config;

import com.yansheng.beans.BeansException;

public interface BeanPostProcessor {

	Object postProcessBeforeInitialization(Object bean,String beanName) throws BeansException;
	Object postProcessAfterInitialization(Object bean,String beanName) throws BeansException;
	
}
