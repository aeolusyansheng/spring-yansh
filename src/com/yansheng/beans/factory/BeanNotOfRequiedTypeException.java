package com.yansheng.beans.factory;

import com.yansheng.beans.BeansException;

@SuppressWarnings("serial")
public class BeanNotOfRequiedTypeException extends BeansException {

	private String beanName;
	@SuppressWarnings("rawtypes")
	private Class requiredType;
	@SuppressWarnings("rawtypes")
	private Class actualType;

	@SuppressWarnings("rawtypes")
	public BeanNotOfRequiedTypeException(String beanName, Class requiredType, Class actualType) {
		super("bean:" + beanName + ",必须是" + requiredType.getName() + "的类型,实际是" + actualType.getName() + "的类型");
		this.beanName = beanName;
		this.requiredType = requiredType;
		this.actualType = actualType;
	}

	public String getBeanName() {
		return this.beanName;
	}

	@SuppressWarnings("rawtypes")
	public Class getRequiredType() {
		return this.requiredType;
	}

	@SuppressWarnings("rawtypes")
	public Class getActualType() {
		return this.actualType;
	}
}
