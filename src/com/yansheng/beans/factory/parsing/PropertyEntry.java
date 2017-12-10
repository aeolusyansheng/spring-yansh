package com.yansheng.beans.factory.parsing;

import org.springframework.util.StringUtils;

public class PropertyEntry implements ParseState.Entry {

	private final String name;

	public PropertyEntry(String name) {
		if (!StringUtils.hasText(name)) {
			throw new IllegalArgumentException("无效的属性名:" + name);
		}
		this.name = name;
	}

	@Override
	public String toString() {
		return "属性名:" + this.name;
	}
}
