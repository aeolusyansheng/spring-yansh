package com.yansheng.beans;

public class NotWritablePropertyException extends InvalidPropertyException {

	private String[] possibleMatches = null;

	public NotWritablePropertyException(Class<?> beanClass, String propertyName) {
		super(beanClass, propertyName, "属性" + propertyName + "不是可写属性或者没有提供setter方法。确认getter方法的返回值是否和setter方法匹配。");
	}

	public NotWritablePropertyException(Class<?> beanClass, String propertyName, String msg) {
		super(beanClass, propertyName, msg);
	}

	public NotWritablePropertyException(Class<?> beanClass, String propertyName, String msg, Throwable cause) {
		super(beanClass, propertyName, msg, cause);
	}

	public NotWritablePropertyException(Class<?> beanClass, String propertyName, String msg, String[] possibleMathes) {
		super(beanClass, propertyName, msg);
		this.possibleMatches = possibleMathes;
	}

	public String[] getPossibleMatches() {
		return this.possibleMatches;
	}

}
