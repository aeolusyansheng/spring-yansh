package com.yansheng.beans.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class CustomCollectionEditor extends PropertyEditorSupport {

    private final Class<?> collectionType;
    private final boolean nullAsEmptyCollection;

    public CustomCollectionEditor(Class<?> collectionType) {
        this(collectionType, false);
    }

    public CustomCollectionEditor(Class<?> collectionType, boolean nullAsEmptyCollection) {
        if (collectionType == null) {
            throw new IllegalArgumentException("collectionType 不能为null");
        } else if (!Collection.class.isAssignableFrom(collectionType)) {
            throw new IllegalArgumentException(
                    "collectionType:" + collectionType.getName() + "不是java.util.Collection的实现类");
        }
        this.collectionType = collectionType;
        this.nullAsEmptyCollection = nullAsEmptyCollection;
    }

    @Override
    public void setAsText(String value) {
        setValue(value);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setValue(Object value) {
        if (value == null && this.nullAsEmptyCollection) {
            super.setValue(createCollection(this.collectionType, 0));
        } else if (value == null || (this.collectionType.isInstance(value) && !alwaysCreateNewCollection())) {
            super.setValue(value);
        } else if (value instanceof Collection) {
            Collection source = (Collection) value;
            Collection target = createCollection(this.collectionType, source.size());
            for (Object element : source) {
                target.add(convertElement(element));
            }
            super.setValue(target);
        } else if (value.getClass().isArray()) {
            int length = Array.getLength(value);
            Collection target = createCollection(this.collectionType, length);
            for (int i = 0; i < length; i++) {
                target.add(convertElement(Array.get(value, i)));
            }
            setValue(target);
        } else {
            Collection target = createCollection(this.collectionType, 1);
            target.add(convertElement(value));
            setValue(target);
        }
    }

    @SuppressWarnings("rawtypes")
    protected Collection createCollection(Class collectionType, int initialCapacity) {
        if (!collectionType.isInterface()) {
            try {
                return (Collection) (collectionType.newInstance());
            } catch (Exception e) {
                throw new IllegalArgumentException(
                        "无法实例化 collection class [" + collectionType.getName() + "]: " + e.getMessage());
            }
        } else if (List.class.equals(collectionType)) {
            return new ArrayList(initialCapacity);
        } else if (SortedSet.class.equals(collectionType)) {
            return new TreeSet();
        } else {
            return new LinkedHashSet(initialCapacity);
        }
    }

    protected boolean alwaysCreateNewCollection() {
        return false;
    }

    protected Object convertElement(Object element) {
        return element;
    }

    @Override
    public String getAsText() {
        return null;
    }
}
