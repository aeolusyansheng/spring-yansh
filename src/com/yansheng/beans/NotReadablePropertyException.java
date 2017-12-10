package com.yansheng.beans;

@SuppressWarnings("serial")
public class NotReadablePropertyException extends InvalidPropertyException {

	public NotReadablePropertyException(Class<?> beanClass, String propertyName) {
		super(beanClass, propertyName, "属性" + propertyName + "不是可读属性或者没有提供getter方法。确认getter方法的返回值是否和setter方法匹配。");
	}

	public NotReadablePropertyException(Class<?> beanClass, String propertyName, String msg) {
		super(beanClass, propertyName, msg);
	}
}
