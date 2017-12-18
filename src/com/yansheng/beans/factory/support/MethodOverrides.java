package com.yansheng.beans.factory.support;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class MethodOverrides {

	private final Set<MethodOverride> overrides = new HashSet<MethodOverride>(0);

	public MethodOverrides() {

	}

	public MethodOverrides(MethodOverrides other) {
		addOverrides(other);
	}

	public void addOverrides(MethodOverrides other) {
		if (other != null) {
			this.overrides.addAll(other.overrides);
		}
	}

	public void addOverride(MethodOverride override) {
		this.overrides.add(override);
	}

	public Set<MethodOverride> getOverrides() {
		return this.overrides;
	}

	public boolean isEmpty() {
		return this.overrides.isEmpty();
	}

	public MethodOverride getOverride(Method method) {
		for (MethodOverride override : this.overrides) {
			if (override.matches(method)) {
				return override;
			}
		}
		return null;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MethodOverrides)) {
			return false;
		}
		MethodOverrides another = (MethodOverrides) other;
		return this.overrides.equals(another.overrides);
	}

	@Override
	public int hashCode() {
		return this.overrides.hashCode();
	}
}
