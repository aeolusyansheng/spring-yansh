package com.yansheng.beans.factory.support;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.yansheng.beans.factory.BeanDefinitionStoreException;

public interface BeanDefinitionReader {

	BeanDefinitionRegistry getRegistry();

	ResourceLoader getResourceLoader();

	ClassLoader getBeanClassLoader();

	BeanNameGenerator getBeanNameGenerator();

	int loadBeanDefinition(Resource resource) throws BeanDefinitionStoreException;

	int loadBeanDefinition(Resource... resources) throws BeanDefinitionStoreException;

	int loadBeanDefinition(String location) throws BeanDefinitionStoreException;

	int loadBeanDefinition(String... locations) throws BeanDefinitionStoreException;
}
