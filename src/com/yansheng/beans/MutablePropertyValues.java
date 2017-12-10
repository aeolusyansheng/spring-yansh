package com.yansheng.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;

@SuppressWarnings("serial")
public class MutablePropertyValues implements PropertyValues, Serializable {

	/* 保存所有的属性 */
	private final List<PropertyValue> propertyValueList;
	private Set<String> processedProperties;
	private volatile boolean converted = false;

	public MutablePropertyValues() {
		this.propertyValueList = new ArrayList<PropertyValue>(0);
	}

	public MutablePropertyValues(PropertyValues original) {
		if (original != null) {
			PropertyValue[] pvs = original.getPropertyValues();
			this.propertyValueList = new ArrayList<PropertyValue>(pvs.length);
			for (PropertyValue pv : pvs) {
				this.propertyValueList.add(new PropertyValue(pv));
			}
		} else {
			this.propertyValueList = new ArrayList<PropertyValue>(0);
		}
	}

	public MutablePropertyValues(Map<?, ?> original) {
		if (original != null) {
			this.propertyValueList = new ArrayList<PropertyValue>(original.size());
			for (Map.Entry<?, ?> entry : original.entrySet()) {
				this.propertyValueList.add(new PropertyValue(entry.getKey().toString(), entry.getValue()));
			}
		} else {
			this.propertyValueList = new ArrayList<PropertyValue>(0);
		}
	}

	public MutablePropertyValues(List<PropertyValue> propertyValueList) {
		this.propertyValueList = (propertyValueList != null ? propertyValueList : new ArrayList<PropertyValue>());
	}

	public List<PropertyValue> getPropertyValueList() {
		return this.propertyValueList;
	}

	public int size() {
		return this.propertyValueList.size();
	}

	public MutablePropertyValues addPropertyValues(PropertyValues other) {
		if (other != null) {
			PropertyValue[] pvs = other.getPropertyValues();
			for (PropertyValue pv : pvs) {
				addPropertyValue(new PropertyValue(pv));
			}
		}
		return this;
	}

	public MutablePropertyValues addPropertyValues(Map<?, ?> other) {
		if (other != null) {
			for (Map.Entry<?, ?> entry : other.entrySet()) {
				addPropertyValue(new PropertyValue(entry.getKey().toString(), entry.getValue()));
			}
		}
		return this;
	}

	public MutablePropertyValues addPropertyValue(PropertyValue pv) {
		for (int i = 0; i < this.propertyValueList.size(); i++) {
			PropertyValue currentPv = this.propertyValueList.get(i);
			if (currentPv.getName().equals(pv.getName())) {
				pv = mergeIfRequired(pv, currentPv);
				setPropertyValueAt(pv, i);
				return this;
			}
		}
		// 没有找到匹配的，加在最后
		this.propertyValueList.add(pv);
		return this;
	}

	public void addPropertyValue(String propertyName, Object propertyValue) {
		addPropertyValue(new PropertyValue(propertyName, propertyValue));
	}

	public MutablePropertyValues add(String propertyName, Object propertyValue) {
		addPropertyValue(new PropertyValue(propertyName, propertyValue));
		return this;
	}

	public void setPropertyValueAt(PropertyValue pv, int i) {
		this.propertyValueList.set(i, pv);
	}

	private PropertyValue mergeIfRequired(PropertyValue newPv, PropertyValue currentPv) {
		Object value = newPv.getValue();
		if (value instanceof Mergeable) {
			Mergeable mergeable = (Mergeable) value;
			if (mergeable.isMergeEnable()) {
				Object merged = mergeable.merge(currentPv.getValue());
				return new PropertyValue(newPv.getName(), merged);
			}
		}
		return newPv;
	}

	public void removePropertyValue(PropertyValue pv) {
		this.propertyValueList.remove(pv);
	}

	public void removePropertyValue(String propertyName) {
		this.propertyValueList.remove(getPropertyValue(propertyName));
	}

	@Override
	public PropertyValue getPropertyValue(String propertyName) {
		for (PropertyValue pv : this.propertyValueList) {
			if (pv.getName().equals(propertyName)) {
				return pv;
			}
		}
		return null;
	}

	@Override
	public PropertyValue[] getPropertyValues() {
		return this.propertyValueList.toArray(new PropertyValue[this.propertyValueList.size()]);
	}

	@Override
	public PropertyValues changesSince(PropertyValues old) {
		MutablePropertyValues changes = new MutablePropertyValues();
		if (this == old) {
			// 改变的属性为0
			return changes;
		}
		for (PropertyValue newPv : this.propertyValueList) {
			PropertyValue pvOld = old.getPropertyValue(newPv.getName());
			if (pvOld == null) {
				// 找不到，追加到改变中
				changes.addPropertyValue(newPv);
			} else if (!pvOld.equals(newPv)) {
				// name不一致，追加到变化中
				changes.addPropertyValue(newPv);
			}
		}
		return changes;
	}

	@Override
	public boolean contains(String propertyName) {
		return (getPropertyValue(propertyName) != null
				|| this.processedProperties != null && this.processedProperties.contains(propertyName));
	}

	@Override
	public boolean isEmpty() {
		return this.propertyValueList.isEmpty();
	}

	public void registerProcessedProperty(String propertyName) {
		if (this.processedProperties == null) {
			this.processedProperties = new HashSet<String>();
		}
		this.processedProperties.add(propertyName);
	}

	public void setConvered() {
		this.converted = true;
	}

	public boolean isConverted() {
		return this.converted;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MutablePropertyValues)) {
			return false;
		}
		MutablePropertyValues another = (MutablePropertyValues) other;
		return this.propertyValueList.equals(another.propertyValueList);
	}

	@Override
	public int hashCode() {
		return this.propertyValueList.hashCode();
	}

	@Override
	public String toString() {
		PropertyValue[] pvs = getPropertyValues();
		StringBuilder sb = new StringBuilder("propertyValues:lenth=").append(pvs.length);
		if (pvs.length > 0) {
			sb.append(";").append(StringUtils.arrayToDelimitedString(pvs, ";"));
		}
		return sb.toString();
	}

}
