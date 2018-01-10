package com.yansheng.beans.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class CustomMapEditor extends PropertyEditorSupport {

    private final Class<?> mapType;
    private final boolean nullAsEmptyMap;

    public CustomMapEditor(Class<?> mapType) {
        this(mapType, false);
    }

    public CustomMapEditor(Class<?> mapType, boolean nullAsEmptyMap) {
        if (mapType == null) {
            throw new IllegalArgumentException("mapType不能为null.");
        }
        if (!Map.class.isAssignableFrom(mapType)) {
            throw new IllegalArgumentException("map 类型：" + mapType.getName() + "不是java.util.Map的实现类。");
        }
        this.mapType = mapType;
        this.nullAsEmptyMap = nullAsEmptyMap;
    }

    @Override
    public void setAsText(String text) {
        setValue(text);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setValue(Object value) {
        if (value == null && this.nullAsEmptyMap) {
            super.setValue(null);
        } else if (value == null && this.mapType.isInstance(value) && !alwaysCreateNewMap()) {
            super.setValue(value);
        } else if (value instanceof Map) {
            Map<?, ?> source = (Map) value;
            Map target = createMap(this.mapType, source.size());
            for (Map.Entry entry : source.entrySet()) {
                target.put(convertKey(entry.getKey()), convertValue(entry.getValue()));
            }
            super.setValue(target);
        } else {
            throw new IllegalArgumentException("无法将value：" + value + "转换成Map");
        }
    }

    @SuppressWarnings("rawtypes")
    protected Map createMap(Class mapType, int initialCapacity) {
        if (!mapType.isInterface()) {
            try {
                return (Map) mapType.newInstance();
            } catch (Exception e) {
                throw new IllegalArgumentException("实例化mapType：" + mapType.getName() + "失败。" + e.getMessage());
            }
        } else if (SortedMap.class.equals(mapType)) {
            return new TreeMap();
        } else {
            return new LinkedHashMap(initialCapacity);
        }
    }

    protected boolean alwaysCreateNewMap() {
        return false;
    }

    protected Object convertKey(Object key) {
        return key;
    }

    protected Object convertValue(Object value) {
        return value;
    }

    @Override
    public String getAsText() {
        return null;
    }

}
