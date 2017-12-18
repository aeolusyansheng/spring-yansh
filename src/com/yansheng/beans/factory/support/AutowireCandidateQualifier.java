package com.yansheng.beans.factory.support;

import org.springframework.util.Assert;

import com.yansheng.beans.BeanMetadataAttributeAccessor;

@SuppressWarnings("serial")
public class AutowireCandidateQualifier extends BeanMetadataAttributeAccessor {

	public static String VALUE_KEY = "value";
	private final String typeName;

	@SuppressWarnings("rawtypes")
	public AutowireCandidateQualifier(Class type) {
		this(type.getName());
	}

	public AutowireCandidateQualifier(String typeName) {
		Assert.notNull(typeName, "typeName不能为null。");
		this.typeName = typeName;
	}

	@SuppressWarnings("rawtypes")
	public AutowireCandidateQualifier(Class type, Object value) {
		this(type.getName(), value);
	}

	public AutowireCandidateQualifier(String typeName, Object value) {
		Assert.notNull(typeName, "typeName不能为null。");
		this.typeName = typeName;
		setAttribute(typeName, value);
	}

	public String getTypeName() {
		return this.typeName;
	}

}
