package com.yansheng.beans.factory.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.yansheng.beans.factory.BeanDefinitionStoreException;
import com.yansheng.beans.factory.NoSuchBeanDefinitionException;
import com.yansheng.beans.factory.config.BeanDefinition;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry {

	// <String, BeanDefinition>的方式保存所有的beanDefinition
	private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>(64);

	// 按注册的顺序保存所有的beanDefinition的名称
	private final List<String> beanDefinitionNames = new ArrayList<String>();

	private boolean allowBeanDefinitionOverriding = true;

	private String[] frozenBeanDefinitionNames;

	// ---------------------------------------------------------------------
	// Implementation of BeanDefinitionRegistry interface
	// ---------------------------------------------------------------------

	@Override
	public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
			throws BeanDefinitionStoreException {

		Assert.hasText(beanName, "bean name 不能为空。");
		Assert.notNull(beanDefinition, "beanDefinition不能为null。");

		// 校验
		if (beanDefinition instanceof AbstractBeanDefinition) {
			try {
				((AbstractBeanDefinition) beanDefinition).validate();
			} catch (BeanDefinitionValidationException e) {
				throw new BeanDefinitionStoreException(beanDefinition.getResourceDescription(), beanName,
						"bean definition校验失败。", e);
			}
		}

		synchronized (this.beanDefinitionMap) {
			Object oldBeanDifinition = this.beanDefinitionMap.get(beanName);
			if (oldBeanDifinition != null) {
				if (!this.allowBeanDefinitionOverriding) {
					throw new BeanDefinitionStoreException(beanDefinition.getResourceDescription(), beanName,
							"bean definition [" + beanDefinition + "]注册失败。已经存在同名的定义");
				} else {
					// if (this.logger.isInfoEnabled()) {
					// this.logger.info("Overriding bean definition for bean '" + beanName +
					// "': replacing [" + oldBeanDefinition + "] with [" + beanDefinition + "]");
					// }
				}
			} else {
				this.beanDefinitionNames.add(beanName);
				this.frozenBeanDefinitionNames = null;
			}
			this.beanDefinitionMap.put(beanName, beanDefinition);
		}
		// resetBeanDefinition(beanName);
	}

	@Override
	public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
		Assert.hasText(beanName, "bean name不能为空。");

		synchronized (this.beanDefinitionMap) {
			BeanDefinition bd = this.beanDefinitionMap.remove(beanName);
			if (bd == null) {
				// if (this.logger.isTraceEnabled()) {
				// this.logger.trace("No bean named '" + beanName + "' found in " + this);
				// }
				throw new NoSuchBeanDefinitionException(beanName);
			}
			this.beanDefinitionNames.remove(beanName);
			this.frozenBeanDefinitionNames = null;
		}
		// resetBeanDefinition(beanName);
	}

	@Override
	public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
		BeanDefinition bd = this.beanDefinitionMap.get(beanName);
		if (bd == null) {
			// if (this.logger.isTraceEnabled()) {
			// this.logger.trace("No bean named '" + beanName + "' found in " + this);
			// }
			throw new NoSuchBeanDefinitionException(beanName);
		}
		return bd;
	}

	@Override
	public boolean containsBeanDefinition(String beanName) {
		Assert.notNull(beanName, "bean name 不能为null。");
		return this.beanDefinitionMap.containsKey(beanName);
	}

	@Override
	public String[] getBeanDefinitionNames() {
		synchronized (this.beanDefinitionMap) {
			if (this.frozenBeanDefinitionNames != null) {
				return this.frozenBeanDefinitionNames;
			} else {
				return StringUtils.toStringArray(this.beanDefinitionNames);
			}
		}
	}

	@Override
	public int getBeanDefinitonCount() {
		return this.beanDefinitionMap.size();
	}

	

}
