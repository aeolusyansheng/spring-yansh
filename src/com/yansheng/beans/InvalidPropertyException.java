package com.yansheng.beans;

@SuppressWarnings("serial")
public class InvalidPropertyException extends FatalBeanException {

	private Class<?> beanClass;
	private String propertyName;

	public InvalidPropertyException(Class<?> beanClass, String propertyName, String msg) {
		this(beanClass, propertyName, msg, null);
	}

	public InvalidPropertyException(Class<?> beanClass, String propertyName, String msg, Throwable cause) {
		super("类" + beanClass.getName() + "中含有无效的属性" + propertyName + "。消息:" + msg, cause);
	}

	public Class<?> getBeanClass() {
		return beanClass;
	}

	public String getPropertyName() {
		return propertyName;
	}
}
