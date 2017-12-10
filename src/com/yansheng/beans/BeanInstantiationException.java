package com.yansheng.beans;

@SuppressWarnings("serial")
public class BeanInstantiationException extends FatalBeanException {

	private Class<?> beanClass;

	public BeanInstantiationException(Class<?> beanClass, String msg) {
		this(beanClass, msg, null);
	}

	public BeanInstantiationException(Class<?> beanClass, String msg, Throwable cause) {
		super("bean类无法实例化。类型:" + beanClass.getName() + "。错误消息:" + msg, cause);
		this.beanClass = beanClass;
	}

	public Class<?> getBeanClass() {
		return this.beanClass;
	}
}
