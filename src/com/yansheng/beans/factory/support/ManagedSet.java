package com.yansheng.beans.factory.support;

import java.util.LinkedHashSet;
import java.util.Set;

import com.yansheng.beans.BeanMetadataElement;
import com.yansheng.beans.Mergeable;

@SuppressWarnings("serial")
public class ManagedSet<E> extends LinkedHashSet<E> implements Mergeable, BeanMetadataElement {

	private Object source;

	private String elementTypeName;

	private boolean mergeEnabled;

	public ManagedSet() {
	}

	public ManagedSet(int initialCapacity) {
		super(initialCapacity);
	}

	public void setSource(Object source) {
		this.source = source;
	}

	@Override
	public Object getSource() {
		return this.source;
	}

	public void setElementTypeName(String elementTypeName) {
		this.elementTypeName = elementTypeName;
	}

	public String getElementTypeName() {
		return this.elementTypeName;
	}

	public void setMergeEnabled(boolean mergeEnabled) {
		this.mergeEnabled = mergeEnabled;
	}

	@Override
	public boolean isMergeEnable() {
		return this.mergeEnabled;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Set<E> merge(Object parent) {
		if (!this.mergeEnabled) {
			throw new IllegalStateException("mergeEnabled为false时不能merge");
		}
		if (parent == null) {
			return this;
		}
		if (!(parent instanceof Set)) {
			throw new IllegalArgumentException("无法merge类型" + parent.getClass());
		}
		Set<E> merged = new ManagedSet<E>();
		merged.addAll((Set) parent);
		merged.addAll(this);
		return merged;
	}

}
