package com.yansheng.beans.factory.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.SimpleAliasRegistry;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.yansheng.beans.factory.BeanDefinitionStoreException;
import com.yansheng.beans.factory.NoSuchBeanDefinitionException;
import com.yansheng.beans.factory.config.BeanDefinition;

public class SimpleBeanDefinitionRegistry extends SimpleAliasRegistry implements BeanDefinitionRegistry {

	private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>(64);

	@Override
	public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
			throws BeanDefinitionStoreException {
		Assert.hasText(beanName, "beanName不能为空。");
		Assert.notNull(beanDefinition, "beanDefinition不能为null");
		this.beanDefinitionMap.put(beanName, beanDefinition);
	}

	@Override
	public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
		if (this.beanDefinitionMap.remove(beanName) == null) {
			throw new NoSuchBeanDefinitionException(beanName);
		}
	}

	@Override
	public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
		BeanDefinition bd = this.beanDefinitionMap.get(beanName);
		if (bd == null) {
			throw new NoSuchBeanDefinitionException(beanName);
		}
		return bd;
	}

	@Override
	public boolean containsBeanDefinition(String beanName) {
		return this.beanDefinitionMap.containsKey(beanName);
	}

	@Override
	public String[] getBeanDefinitionNames() {
		return StringUtils.toStringArray(this.beanDefinitionMap.keySet());
	}

	@Override
	public int getBeanDefinitonCount() {
		return this.beanDefinitionMap.size();
	}

	@Override
	public boolean isBeanNameInUse(String beanName) {
		return isAlias(beanName) || containsBeanDefinition(beanName);
	}

}
