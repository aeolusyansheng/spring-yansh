package com.yansheng.beans;

import java.beans.PropertyDescriptor;
import java.io.Serializable;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

@SuppressWarnings("serial")
public class PropertyValue extends BeanMetadataAttributeAccessor implements Serializable {

	private final String name;
	private final Object value;
	private Object source;
	private boolean optional = false;
	private boolean converted = false;
	private Object convertedValue;

	volatile Boolean conversionNecessary;
	volatile Object resolvedTokens;
	volatile PropertyDescriptor resolvedDescriptor;

	public PropertyValue(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	public PropertyValue(PropertyValue original) {
		Assert.notNull(original, "original 不能为null。");
		this.name = original.getName();
		this.value = original.getValue();
		this.source = original.getSource();
		this.optional = original.isOptional();
		this.converted = original.isConverted();
		this.convertedValue = original.convertedValue;
		this.conversionNecessary = original.conversionNecessary;
		this.resolvedTokens = original.resolvedTokens;
		this.resolvedDescriptor = original.resolvedDescriptor;
		copyAttributesFrom(original);
	}

	public PropertyValue(PropertyValue original,Object newValue) {
		Assert.notNull(original, "original 不能为null。");
		this.name = original.getName();
		this.value = newValue;
		this.source = original;
		this.optional = original.isOptional();
		this.conversionNecessary = original.conversionNecessary;
		this.resolvedTokens = original.resolvedTokens;
		this.resolvedDescriptor = original.resolvedDescriptor;
		copyAttributesFrom(original);
	}
	
	public String getName() {
		return this.name;
	}

	public Object getValue() {
		return this.value;
	}

	public PropertyValue getOriginalPropertyValue() {
		PropertyValue original = this;
		while (original.source instanceof PropertyValue && original.source != original) {
			original = (PropertyValue) original.source;
		}
		return original;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	public boolean isOptional() {
		return this.optional;
	}

	public synchronized boolean isConverted() {
		return this.converted;
	}

	public synchronized void setConvertedValue(Object value) {
		this.converted = true;
		this.convertedValue = value;
	}

	public synchronized Object getConvertedValue() {
		return this.convertedValue;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof PropertyValue)) {
			return false;
		}
		PropertyValue another = (PropertyValue) other;
		return (this.name.equals(another.name) && ObjectUtils.nullSafeEquals(this.value, another.value)
				&& ObjectUtils.nullSafeEquals(this.source, another.source));
	}

	@Override
	public int hashCode() {
		return this.name.hashCode() * 29 + ObjectUtils.nullSafeHashCode(this.value);
	}

	@Override
	public String toString() {
		return "bean property '" + this.name + "'";
	}
}
