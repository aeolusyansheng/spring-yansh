package com.yansheng.beans;

import java.util.Map;

import org.springframework.core.convert.TypeDescriptor;

public interface PropertyAccessor {

	String NESTED_PROPERTY_SEPARATOR = ".";
	char NESTED_PROPERTY_SEPARATOR_CHAR = '.';

	String PROPERTY_KEY_PREFIX = "[";
	char PROPERTY_KEY_PREFIX_CHAR = '[';

	String PROPERTY_KEY_SUFFIX = "[";
	char PROPERTY_KEY_SUFFIX_CHAR = '[';

	boolean isReadableProperty(String propertyName);

	boolean isWritableProperty(String propertyName);

	Class<?> getPropertyType(String propertyName) throws BeansException;

	TypeDescriptor getPropertyTypeDescriptor(String propertyName) throws BeansException;

	Object getPropertyValue(String propertyName) throws BeansException;

	void setPropertyValue(String propertyName, Object value) throws BeansException;

	void setPropertValue(PropertyValue pv) throws BeansException;

	void setPropertyValue(Map<?, ?> map) throws BeansException;

	void setPropertyValue(PropertyValues pvs) throws BeansException;

	void setPropertyValue(PropertyValues pvs, boolean ignoreUnknown) throws BeansException;

	void setPropertyValue(PropertyValues pvs, boolean ignoreUnknown, boolean ignoreInvalid) throws BeansException;

}
