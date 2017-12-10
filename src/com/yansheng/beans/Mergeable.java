package com.yansheng.beans;

public interface Mergeable {

	boolean isMergeEnable();

	Object merge(Object parent);
}
