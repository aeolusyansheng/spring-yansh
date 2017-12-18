package com.yansheng.beans.factory.support;

import java.lang.reflect.Method;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

public class LookupOverride extends MethodOverride {

	private final String beanName;

	public LookupOverride(String methodName, String beanName) {
		super(methodName);
		Assert.notNull(beanName, "beanName不能为null。");
		this.beanName = beanName;
	}

	public String getBeanName() {
		return this.beanName;
	}

	@Override
	public boolean matches(Method method) {
		return (method.getName().equals(getMethodName()) && method.getParameterTypes().length == 0);
	}

	@Override
	public String toString() {
		return "LookupOverride for method '" + getMethodName() + "';will return bean '" + this.beanName + "'";
	}

	@Override
	public boolean equals(Object other) {
		return (other instanceof LookupOverride && super.equals(other)
				&& ObjectUtils.nullSafeEquals(this.beanName, ((LookupOverride) other).beanName));
	}

	public int hashCode() {
		return (29 * super.hashCode() + ObjectUtils.nullSafeHashCode(this.beanName));
	}
}
