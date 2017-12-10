package com.yansheng.beans;

@SuppressWarnings("serial")
public class NullValueInNestedPathException extends InvalidPropertyException {

	public NullValueInNestedPathException(Class<?> beanClass, String propertyName) {
		super(beanClass, propertyName, "内置属性" + propertyName + "的值为null。");
	}

	public NullValueInNestedPathException(Class<?> beanClass, String propertyName, String msg) {
		super(beanClass, propertyName, msg);
	}
}
