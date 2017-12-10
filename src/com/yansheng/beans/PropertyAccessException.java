package com.yansheng.beans;

import java.beans.PropertyChangeEvent;

import org.springframework.core.ErrorCoded;

@SuppressWarnings("serial")
public abstract class PropertyAccessException extends BeansException implements ErrorCoded {

	// transient序列化时不序列化
	private transient PropertyChangeEvent propertyChangeEvent;

	public PropertyAccessException(PropertyChangeEvent propertyChangeEvent, String msg, Throwable cause) {
		super(msg, cause);
		this.propertyChangeEvent = propertyChangeEvent;
	}

	public PropertyAccessException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public PropertyChangeEvent getPropertyChangeEvent() {
		return this.propertyChangeEvent;
	}

	public String getPropertyName() {
		return (this.propertyChangeEvent != null ? this.propertyChangeEvent.getPropertyName() : null);
	}

	public Object getValue() {
		return (this.propertyChangeEvent != null ? this.propertyChangeEvent.getNewValue() : null);
	}

}
