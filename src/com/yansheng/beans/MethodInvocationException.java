package com.yansheng.beans;

import java.beans.PropertyChangeEvent;

@SuppressWarnings("serial")
public class MethodInvocationException extends PropertyAccessException {

	public static final String ERROR_CODE = "methodInvocation";

	public MethodInvocationException(PropertyChangeEvent propertyChangeEvent, Throwable cause) {
		super(propertyChangeEvent, "属性:" + propertyChangeEvent.getPropertyName() + "发生异常。", cause);
	}

	@Override
	public String getErrorCode() {
		return ERROR_CODE;
	}
}
