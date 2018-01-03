package com.yansheng.beans.factory.support;

import java.util.LinkedHashMap;
import java.util.Map;

import com.yansheng.beans.BeanMetadataElement;
import com.yansheng.beans.Mergeable;

@SuppressWarnings("serial")
public class ManagedMap<K, V> extends LinkedHashMap<K, V> implements Mergeable, BeanMetadataElement {

	private Object source;
	private String keyTypeName;
	private String valueTypeName;
	private boolean mergeEnabled;

	public ManagedMap() {

	}

	public ManagedMap(int initialCapacity) {
		super(initialCapacity);
	}

	public void setSource(Object source) {
		this.source = source;
	}

	@Override
	public Object getSource() {
		return this.source;
	}

	public void setKeyTypeName(String keyTypeName) {
		this.keyTypeName = keyTypeName;
	}

	public String getKeyTypeName() {
		return this.keyTypeName;
	}

	public void setValueTypeName(String valueTypeName) {
		this.valueTypeName = valueTypeName;
	}

	public String getValueTypeName() {
		return this.valueTypeName;
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
	public Map<K, V> merge(Object parent) {
		if (!this.mergeEnabled) {
			throw new IllegalStateException("mergeEnabled为false时不能merge");
		}
		if (parent == null) {
			return this;
		}
		if (!(parent instanceof Map)) {
			throw new IllegalArgumentException("无法merge类型" + parent.getClass());
		}
		Map<K, V> merged = new ManagedMap<K, V>();
		merged.putAll((Map) parent);
		merged.putAll(this);
		return merged;
	}

}
