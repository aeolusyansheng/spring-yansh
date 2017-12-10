package com.yansheng.beans.factory;

import org.springframework.util.StringUtils;

import com.yansheng.beans.BeansException;

public class NoSuchBeanDefinitionException extends BeansException {

	private String beanName;
	private Class<?> beanType;

	public NoSuchBeanDefinitionException(String beanName) {
		super("bean名称" + beanName + "没定义。");
		this.beanName = beanName;
	}

	public NoSuchBeanDefinitionException(String beanName, String msg) {
		super("bean名称" + beanName + "没定义。错误消息:" + msg);
		this.beanName = beanName;
	}

	public NoSuchBeanDefinitionException(Class<?> type) {
		super("bean类型" + type.getName() + "没定义。");
		this.beanType = type;
	}

	public NoSuchBeanDefinitionException(Class<?> type, String msg) {
		super("bean类型" + type.getName() + "没定义。错误消息:" + msg);
		this.beanType = type;
	}

	public NoSuchBeanDefinitionException(Class<?> type, String dependencyDescription, String msg) {
		super("依赖bean类型" + type.getName() + "没定义。依赖bean描述:"
				+ (StringUtils.hasLength(dependencyDescription) ? dependencyDescription : "") + "。错误消息:" + msg);
		this.beanType = type;
	}

	public String getBeanName() {
		return this.beanName;
	}

	public Class<?> getBeanType() {
		return this.beanType;
	}

	public int getNumberOfBeansFound() {
		//子类重写
		return 0;
	}

}
