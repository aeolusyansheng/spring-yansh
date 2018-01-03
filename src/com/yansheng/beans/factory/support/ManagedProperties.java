package com.yansheng.beans.factory.support;

import java.util.Properties;

import com.yansheng.beans.BeanMetadataElement;
import com.yansheng.beans.Mergeable;

@SuppressWarnings("serial")
public class ManagedProperties extends Properties implements Mergeable, BeanMetadataElement {
	private Object source;

	private boolean mergeEnabled;

	public void setSource(Object source) {
		this.source = source;
	}

	@Override
	public Object getSource() {
		return this.source;
	}

	public void setMergeEnabled(boolean mergeEnabled) {
		this.mergeEnabled = mergeEnabled;
	}

	@Override
	public boolean isMergeEnable() {
		return this.mergeEnabled;
	}

	@Override
	public Object merge(Object parent) {
		if (!this.mergeEnabled) {
			throw new IllegalStateException("mergeEnabled为false时不能merge");
		}
		if (parent == null) {
			return this;
		}
		if (!(parent instanceof Properties)) {
			throw new IllegalArgumentException("无法merge类型" + parent.getClass());
		}
		Properties merged = new ManagedProperties();
		merged.putAll((Properties) parent);
		merged.putAll(this);
		return merged;
	}

}
