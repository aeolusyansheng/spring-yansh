package com.yansheng.beans;

import java.beans.PropertyDescriptor;

public interface BeanWrapper extends ConfigurablePropertyAccessor {

	Object getWrapperInstance();

	Class<?> getWrapperClass();

	PropertyDescriptor[] getPropertyDescriptors();

	PropertyDescriptor getPropertyDescriptor(String propertyName) throws InvalidPropertyException;

	void setAutoGrowNestedPaths(boolean autoGrowNestedPaths);

	boolean isAutoGrowNestedPaths();

	void setAutoGrowCollectionLimit(int autoGrowCollectionLimit);

	int getAutoGrowCollectionLimit();
}
