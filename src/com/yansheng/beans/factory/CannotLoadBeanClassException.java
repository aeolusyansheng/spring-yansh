package com.yansheng.beans.factory;

import com.yansheng.beans.FatalBeanException;

@SuppressWarnings("serial")
public class CannotLoadBeanClassException extends FatalBeanException {

	private String resourceDescription;
	private String beanName;
	private String beanClassName;

	public CannotLoadBeanClassException(String resourceDescription, String beanName, String beanClassName,
			ClassNotFoundException cause) {
		super("找不到类" + beanClassName + "。bean名:" + beanName + "。资源:" + resourceDescription, cause);
		this.resourceDescription = resourceDescription;
		this.beanName = beanName;
		this.beanClassName = beanClassName;
	}

	public CannotLoadBeanClassException(String resourceDescription, String beanName, String beanClassName,
			LinkageError cause) {
		super("加载类" + beanClassName + "失败。bean名:" + beanName + "。资源:" + resourceDescription + "类文件或相关类可能存在问题。", cause);
		this.resourceDescription = resourceDescription;
		this.beanName = beanName;
	}

	public String getResourceDescription() {
		return this.resourceDescription;
	}

	public String getBeanName() {
		return this.beanName;
	}

	public String getBeanClassName() {
		return this.beanClassName;
	}
}
