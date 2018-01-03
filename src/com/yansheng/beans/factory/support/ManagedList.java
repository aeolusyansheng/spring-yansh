package com.yansheng.beans.factory.support;

import java.util.ArrayList;
import java.util.List;

import com.yansheng.beans.BeanMetadataElement;
import com.yansheng.beans.Mergeable;

@SuppressWarnings("serial")
public class ManagedList<E> extends ArrayList<E> implements Mergeable, BeanMetadataElement {

	private Object source;
	private String elementTypeName;
	private boolean mergeEnabled;

	public ManagedList() {

	}

	public ManagedList(int initialCapacity) {
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<E> merge(Object parent) {
		if (!this.mergeEnabled) {
			throw new IllegalStateException("mergeEnabled为false时不能merge");
		}
		if (parent == null) {
			return this;
		}
		if (!(parent instanceof List)) {
			throw new IllegalArgumentException("无法对" + parent.getClass() + "类型merge.");
		}
		List<E> merged = new ManagedList<E>();
		merged.addAll((List) parent);
		merged.addAll(this);
		return merged;
	}

}
