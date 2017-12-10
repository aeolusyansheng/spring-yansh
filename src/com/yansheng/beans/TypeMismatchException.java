package com.yansheng.beans;

import java.beans.PropertyChangeEvent;

import org.springframework.util.ClassUtils;

@SuppressWarnings("serial")
public class TypeMismatchException extends PropertyAccessException {

	public static final String ERROR_CODE = "typeMismatch";
	private transient Object value;
	private Class<?> requiredType;

	public TypeMismatchException(PropertyChangeEvent propertyChangeEvent, Class<?> requiredType) {
		this(propertyChangeEvent, requiredType, null);
	}

	public TypeMismatchException(PropertyChangeEvent propertyChangeEvent, Class<?> requiredType, Throwable cause) {

		super(propertyChangeEvent, "对值类型" + ClassUtils.getDescriptiveType(propertyChangeEvent.getNewValue())
				+ "的类型转换失败。" + (requiredType != null ? "需要转换的类型:" + ClassUtils.getQualifiedName(requiredType) : "")
				+ (propertyChangeEvent.getPropertyName() != null ? "属性:" + propertyChangeEvent.getPropertyName() : ""),
				cause);
		this.requiredType = requiredType;
		this.value = (propertyChangeEvent != null ? propertyChangeEvent.getNewValue() : null);
	}

	public TypeMismatchException(Object value, Class<?> requiredType) {
		this(value, requiredType, null);
	}

	public TypeMismatchException(Object value, Class<?> requiredType, Throwable cause) {
		super("对值类型" + ClassUtils.getDescriptiveType(value) + "的类型转换失败。"
				+ (requiredType != null ? "需要转换的类型:" + ClassUtils.getQualifiedName(requiredType) : ""), cause);
		this.requiredType = requiredType;
		this.value = value;
	}

	public Class<?> getRequiredType() {
		return this.requiredType;
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public String getErrorCode() {
		return ERROR_CODE;
	}

}
