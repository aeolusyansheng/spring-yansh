package com.yansheng.beans.factory.support;

import org.springframework.util.StringUtils;

public class BeanDefinitionDefaults {

	private boolean lazyInit;
	private int dependencyCheck = AbstractBeanDefinition.DEPENDENCY_CHECK_NONE;
	private int autowireMode = AbstractBeanDefinition.AUTOWIRE_NO;
	private String initMethodName;
	private String destroyMethodName;

	public void setLazyInit(boolean lazyInit) {
		this.lazyInit = lazyInit;
	}

	public boolean isLazyInit() {
		return this.lazyInit;
	}

	public void setDependencyCheck(int dependencyCheck) {
		this.dependencyCheck = dependencyCheck;
	}

	public int getDependencyCheck() {
		return this.dependencyCheck;
	}

	public void setAutowireMode(int autowireMode) {
		this.autowireMode = autowireMode;
	}

	public int getAutowireMode() {
		return this.autowireMode;
	}

	public void setInitMethodName(String initMethodName) {
		// ""也当做null处理
		this.initMethodName = (StringUtils.hasText(initMethodName)) ? initMethodName : null;
	}

	public String getInitMethodName() {
		return this.initMethodName;
	}

	public void setDestroyMethodName(String destroyMethodName) {
		// ""也当做null处理
		this.destroyMethodName = (StringUtils.hasText(destroyMethodName)) ? destroyMethodName : null;
	}

	public String getDestroyMethodName() {
		return this.destroyMethodName;
	}
}
