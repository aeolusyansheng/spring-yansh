package com.yansheng.beans.factory.support;

import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.yansheng.beans.factory.BeanDefinitionStoreException;
import com.yansheng.beans.factory.BeanFactoryUtils;
import com.yansheng.beans.factory.config.BeanDefinition;
import com.yansheng.beans.factory.config.BeanDefinitionHolder;

public class BeanDefinitionReaderUtils {
	public static final String GENERATED_BEAN_NAME_SEPARATOR = BeanFactoryUtils.GENERATED_BEAN_NAME_SEPARATOR;

	public static AbstractBeanDefinition createBeanDefinition(String parentName, String className,
			ClassLoader classLoader) throws ClassNotFoundException {
		GenericBeanDefinition bd = new GenericBeanDefinition();
		bd.setParentName(parentName);
		if (className != null) {
			if (classLoader != null) {
				bd.setBeanClass(ClassUtils.forName(className, classLoader));
			} else {
				bd.setBeanClassName(className);
			}
		}
		return bd;
	}

	public static String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry,
			boolean isInnerBean) throws BeanDefinitionStoreException {
		String generatedBeanName = definition.getBeanClassName();
		if (generatedBeanName == null) {
			if (definition.getParentName() != null) {
				generatedBeanName = definition.getParentName() + "$child";
			} else if (definition.getFactoryBeanName() != null) {
				generatedBeanName = definition.getFactoryBeanName() + "$created";
			}
		}
		if (!StringUtils.hasText(generatedBeanName)) {
			throw new BeanDefinitionStoreException("Bean definition中没有指定class或者parent或者factory-bean，无法创建bean name");
		}
		String id = generatedBeanName;
		if (isInnerBean) {
			id = generatedBeanName + GENERATED_BEAN_NAME_SEPARATOR + ObjectUtils.getIdentityHexString(definition);
		} else {
			int counter = -1;
			while (counter == -1 || registry.containsBeanDefinition(id)) {
				counter++;
				id = generatedBeanName + GENERATED_BEAN_NAME_SEPARATOR + counter;
			}
		}
		return id;
	}

	public static String generateBeanName(BeanDefinition beanDefinition, BeanDefinitionRegistry registry)
			throws BeanDefinitionStoreException {
		return generateBeanName(beanDefinition, registry, false);
	}

	public static void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry) {
		String beanName = definitionHolder.getBeanName();
		registry.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition());
		String[] aliases = definitionHolder.getAliases();
		if (aliases != null) {
			for (String alias : aliases) {
				registry.registerAlias(beanName, alias);
			}
		}
	}

	public static String registerWithGeneratedName(AbstractBeanDefinition definition, BeanDefinitionRegistry registry)
			throws BeanDefinitionStoreException {
		String generatedName = generateBeanName(definition, registry, false);
		registry.registerBeanDefinition(generatedName, definition);
		return generatedName;
	}
}
