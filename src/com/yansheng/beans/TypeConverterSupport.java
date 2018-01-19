package com.yansheng.beans;

import java.lang.reflect.Field;

import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConverterNotFoundException;

public abstract class TypeConverterSupport extends PropertyEditorRegistrySupport implements TypeConverter {

    TypeConverterDelegate typeConverterDelegate;

    @Override
    public <T> T convertIfNecessary(Object value, Class<T> requiredType) throws TypeMismatchException {
        return doConvert(value, requiredType, null, null);
    }

    @Override
    public <T> T convertIfNecessary(Object value, Class<T> requiredType, MethodParameter methodParam)
            throws TypeMismatchException {
        return doConvert(value, requiredType, methodParam, null);
    }

    @Override
    public <T> T convertIfNecessary(Object value, Class<T> requiredType, Field field) throws TypeMismatchException {
        return doConvert(value, requiredType, null, field);
    }

    private <T> T doConvert(Object value, Class<T> requiredType, MethodParameter methodParam, Field field)
            throws TypeMismatchException {
        try {
            if (field != null) {
                return this.typeConverterDelegate.convertIfNecessary(value, requiredType, field);
            } else {
                return this.typeConverterDelegate.convertIfNecessary(value, requiredType, methodParam);
            }
        } catch (ConverterNotFoundException e) {
            throw new ConversionNotSupportedException(value, requiredType, e);
        } catch (ConversionException e) {
            throw new TypeMismatchException(value, requiredType, e);
        } catch (IllegalStateException e) {
            throw new ConversionNotSupportedException(value, requiredType, e);
        } catch (IllegalArgumentException e) {
            throw new TypeMismatchException(value, requiredType, e);
        }
    }

}
