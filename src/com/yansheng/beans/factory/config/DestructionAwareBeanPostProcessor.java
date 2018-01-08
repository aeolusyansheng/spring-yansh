package com.yansheng.beans.factory.config;

import com.yansheng.beans.BeansException;

public interface DestructionAwareBeanPostProcessor extends BeanPostProcessor {

	void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException;
}
