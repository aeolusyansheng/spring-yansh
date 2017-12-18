package com.yansheng.beans.factory.support;

import java.lang.reflect.Method;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import com.yansheng.beans.BeanMetadataElement;

public abstract class MethodOverride implements BeanMetadataElement {

	private final String methodName;
	private boolean overLoaded = true;
	private Object source;

	public MethodOverride(String methodName) {
		Assert.notNull(methodName, "methodName不能为null。");
		this.methodName = methodName;
	}

	public String getMethodName() {
		return this.methodName;
	}

	protected void setOverLoaded(boolean overLoaded) {
		this.overLoaded = overLoaded;
	}

	protected boolean isOverLoaded() {
		return this.overLoaded;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	@Override
	public Object getSource() {
		return this.source;
	}

	// 子类实现
	public abstract boolean matches(Method method);

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MethodOverride)) {
			return false;
		}
		MethodOverride another = (MethodOverride) other;
		return (ObjectUtils.nullSafeEquals(this.methodName, another.methodName))
				&& this.overLoaded == another.overLoaded && (ObjectUtils.nullSafeEquals(this.source, another.source));
	}

	@Override
	public int hashCode() {
		int hashCode = ObjectUtils.nullSafeHashCode(this.methodName);
		hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.source);
		hashCode = 29 * hashCode + (this.overLoaded ? 1 : 0);
		return hashCode;
	}
}
