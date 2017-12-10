package com.yansheng.beans.factory;

import com.yansheng.beans.FatalBeanException;

@SuppressWarnings("serial")
public class BeanDefinitionStoreException extends FatalBeanException {

	private String resourceDescription;
	private String beanName;

	public BeanDefinitionStoreException(String msg) {
		super(msg);
	}

	public BeanDefinitionStoreException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public BeanDefinitionStoreException(String resourceDescripition, String msg) {
		super(msg);
		this.resourceDescription = resourceDescripition;
	}

	public BeanDefinitionStoreException(String resourceDescripition, String msg, Throwable cause) {
		super(msg, cause);
		this.resourceDescription = resourceDescripition;
	}

	public BeanDefinitionStoreException(String resourceDescripition, String beanName, String msg) {
		this(resourceDescripition, beanName, msg, null);
	}

	public BeanDefinitionStoreException(String resourceDescripition, String beanName, String msg, Throwable cause) {
		super("无效的bean定义。bean名称:" + beanName + " bean描述:" + resourceDescripition + "错误消息:" + msg, cause);
	}

	public String getResouceDescription() {
		return this.resourceDescription;
	}

	public String getBeanName() {
		return this.beanName;
	}
}
