package com.yansheng.beans.factory.support;

import org.springframework.core.AliasRegistry;

import com.yansheng.beans.factory.BeanDefinitionStoreException;
import com.yansheng.beans.factory.NoSuchBeanDefinitionException;
import com.yansheng.beans.factory.config.BeanDefinition;

public interface BeanDefinitionRegistry extends AliasRegistry {

	void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException;

	void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

	BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

	boolean containsBeanDefinition(String beanName);

	String[] getBeanDefinitionNames();

	int getBeanDefinitonCount();

	boolean isBeanNameInUse(String beanName);

}
