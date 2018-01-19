package com.yansheng.beans;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class AbstractPropertyAccessor extends TypeConverterSupport implements ConfigurablePropertyAccessor {

    private boolean extractOldValueForEditor = false;

    @Override
    public void setExtractOldValueForEditor(boolean extractOldValueForEditor) {
        this.extractOldValueForEditor = extractOldValueForEditor;
    }

    @Override
    public boolean isExtractOldValueForEditor() {
        return this.extractOldValueForEditor;
    }

    @Override
    public void setPropertyValue(PropertyValue pv) throws BeansException {
        setPropertyValue(pv.getName(), pv.getValue());
    }

    @Override
    public void setPropertyValues(Map<?, ?> map) throws BeansException {
        setPropertyValues(new MutablePropertyValues(map));
    }

    @Override
    public void setPropertyValues(PropertyValues pvs) throws BeansException {
        setPropertyValues(pvs, false, false);
    }

    @Override
    public void setPropertyValues(PropertyValues pvs, boolean ignoreUnknown) throws BeansException {
        setPropertyValues(pvs, ignoreUnknown, false);
    }

    @Override
    public void setPropertyValues(PropertyValues pvs, boolean ignoreUnknown, boolean ignoreInvalid)
            throws BeansException {

        List<PropertyAccessException> propertyAccessExceptions = null;
        List<PropertyValue> propertyValues = (pvs instanceof MutablePropertyValues
                ? ((MutablePropertyValues) pvs).getPropertyValueList() : Arrays.asList(pvs.getPropertyValues()));
        for (PropertyValue pv : propertyValues) {
            try {
                setPropertyValue(pv);
            } catch (NotWritablePropertyException e) {
                if (!ignoreUnknown) {
                    throw e;
                }
            } catch (NullValueInNestedPathException e) {
                if (!ignoreInvalid) {
                    throw e;
                }
            } catch (PropertyAccessException e) {
                if (propertyAccessExceptions == null) {
                    propertyAccessExceptions = new LinkedList<PropertyAccessException>();
                }
                propertyAccessExceptions.add(e);
            }
        }

        if (propertyAccessExceptions != null) {
            PropertyAccessException[] paeArray = propertyAccessExceptions
                    .toArray(new PropertyAccessException[propertyAccessExceptions.size()]);
            throw new PropertyBatchUpdateException(paeArray);
        }
    }

    @Override
    public Class<?> getPropertyType(String propertyPath) {
        return null;
    }

    @Override
    public abstract void setPropertyValue(String propertyName, Object value) throws BeansException;

    @Override
    public abstract Object getPropertyValue(String propertyName) throws BeansException;

}
