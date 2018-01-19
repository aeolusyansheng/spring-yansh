package com.yansheng.beans;

import java.lang.reflect.Field;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.MethodParameter;

public class TypeConverterDelegate {

    private static final Log logger = LogFactory.getLog(TypeConverterDelegate.class);
    private final PropertyEditorRegistrySupport propertyEditorRegistry;
    private final Object targetObject;

    public TypeConverterDelegate(PropertyEditorRegistrySupport propertyEditorRegistry) {
        this(propertyEditorRegistry, null);
    }

    public TypeConverterDelegate(PropertyEditorRegistrySupport propertyEditorRegistry, Object targetObject) {
        this.propertyEditorRegistry = propertyEditorRegistry;
        this.targetObject = targetObject;
    }

    public <T> T convertIfNecessary(Object value, Class<T> requiredType) throws IllegalArgumentException {
        return convertIfNecessary(value, requiredType, null, null);
    }

    public <T> T convertIfNecessary(Object value, Class<T> requiredType, MethodParameter methodParam)
            throws IllegalArgumentException {
        return convertIfNecessary(value, requiredType, methodParam, null);
    }

    public <T> T convertIfNecessary(Object value, Class<T> requiredType, Field field) throws IllegalArgumentException {
        return convertIfNecessary(value, requiredType, null, field);
    }

    public <T> T convertIfNecessary(Object value, Class<T> requiredType, MethodParameter methodParam, Field field)
            throws IllegalArgumentException {
        return null;
    }
}
