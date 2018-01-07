package com.yansheng.beans.propertyeditors;

import java.beans.PropertyEditorSupport;

import org.springframework.util.StringUtils;

public class CharacterEditor extends PropertyEditorSupport {

	private static final String UNICODE_PREFIX = "\\u";
	private static final int UNICODE_LENGTH = 6;
	private final boolean allowEmpty;

	public CharacterEditor(boolean allowEmpty) {
		this.allowEmpty = allowEmpty;
	}

	@Override
	public void setAsText(String text) {
		// char和unicode两种处理
		if (this.allowEmpty && !StringUtils.hasLength(text)) {
			setValue(null);
		} else if (text == null) {
			throw new IllegalArgumentException("String 不能为null");
		} else if (isUnicodeCharacterSequence(text)) {
			setAsUnicode(text);
		} else if (text.length() != 1) {
			throw new IllegalArgumentException("String:" + text + "的长度不为1，不能转换成char类型。");
		} else {
			setValue(new Character(text.charAt(0)));
		}
	}

	@Override
	public String getAsText() {
		Object value = getValue();
		return (value != null ? value.toString() : "");
	}

	private boolean isUnicodeCharacterSequence(String sequence) {
		return (sequence.startsWith(UNICODE_PREFIX) && sequence.length() == UNICODE_LENGTH);
	}

	private void setAsUnicode(String text) {
		// 转成16进制整数
		int code = Integer.parseInt(text.substring(UNICODE_PREFIX.length()), 16);
		setValue(new Character((char) code));
	}
}
