package com.yansheng.beans;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

public class BeanMetadataAttribute implements BeanMetadataElement {

	private final String name;
	private final Object value;
	private Object source;

	public BeanMetadataAttribute(String name, Object value) {
		Assert.notNull("name", "name不能为null。");
		this.name = name;
		this.value = value;
	}

	@Override
	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BeanMetadataAttribute)) {
			return false;
		}
		BeanMetadataAttribute another = (BeanMetadataAttribute) other;
		if (this.name.equals(another.name) && ObjectUtils.nullSafeEquals(this.value, another.value)
				&& ObjectUtils.nullSafeEquals(this.source, another.source)) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.name.hashCode() * 29 + ObjectUtils.nullSafeHashCode(this.value);
	}

	@Override
	public String toString() {
		return "metadate attribute '" + this.name + "'";
	}

}
