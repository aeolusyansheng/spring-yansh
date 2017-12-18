package com.yansheng.beans.factory.support;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

public class ReplaceOverride extends MethodOverride {

	private final String methodReplacerBeanName;
	private List<String> typeIdentifiers = new LinkedList<String>();

	public ReplaceOverride(String methodName, String methodReplacerBeanName) {
		super(methodName);
		Assert.notNull(methodReplacerBeanName, "methodReplacerBeanName不能为null。");
		this.methodReplacerBeanName = methodReplacerBeanName;
	}

	public String getMethodReplacerBeanName() {
		return this.methodReplacerBeanName;
	}

	public void addTypeIdentifier(String identifier) {
		this.typeIdentifiers.add(identifier);
	}

	@Override
	public boolean matches(Method method) {
		if (!method.getName().equals(getMethodName())) {
			return false;
		}
		if (!isOverLoaded()) {
			return true;
		}
		if (this.typeIdentifiers.size() != method.getParameterTypes().length) {
			return false;
		}
		for (int i = 0; i < this.typeIdentifiers.size(); i++) {
			String identifier = this.typeIdentifiers.get(i);
			if (!method.getParameterTypes()[i].getName().contains(identifier)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "Replace override for method '" + getMethodName() + "; will call bean '" + this.methodReplacerBeanName
				+ "'";
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof ReplaceOverride) || !super.equals(other)) {
			return false;
		}
		ReplaceOverride another = (ReplaceOverride) other;
		return (ObjectUtils.nullSafeEquals(this.methodReplacerBeanName, another.methodReplacerBeanName)
				&& ObjectUtils.nullSafeEquals(this.typeIdentifiers, another.typeIdentifiers));
	}

	@Override
	public int hashCode() {
		int hashCode = super.hashCode();
		hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.methodReplacerBeanName);
		hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.typeIdentifiers);
		return hashCode;
	}
}
