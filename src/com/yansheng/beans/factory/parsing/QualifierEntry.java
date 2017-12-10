package com.yansheng.beans.factory.parsing;

import org.springframework.util.StringUtils;

public class QualifierEntry implements ParseState.Entry {

	private String typeName;

	public QualifierEntry(String typeName) {
		if (!StringUtils.hasText(typeName)) {
			throw new IllegalArgumentException("无效的Qualifier类型:" + typeName);
		}
		this.typeName = typeName;
	}

	@Override
	public String toString() {
		return "Qualifier类型:" + this.typeName;
	}
}
