package com.yansheng.beans.factory.parsing;

import org.springframework.util.Assert;

public class ConstructorArgumentEntry implements ParseState.Entry {

	private final int index;

	public ConstructorArgumentEntry() {
		this.index = -1;
	}

	public ConstructorArgumentEntry(int index) {
		Assert.isTrue(index >= 0, "参数索引值必须大于0");
		this.index = index;
	}

	@Override
	public String toString() {
		return "参数索引值:" + (this.index >= 0 ? "#" + this.index : "");
	}
}
