package com.yansheng.beans.factory.support;

import org.springframework.util.Assert;

@SuppressWarnings("serial")
public class ManagedArray extends ManagedList<Object> {

	volatile Class<?> resolvedElementType;

	public ManagedArray(String elementTypeName, int size) {
		super(size);
		Assert.notNull(elementTypeName, "elementTypeName不能为null");
		setElementTypeName(elementTypeName);
	}
}
