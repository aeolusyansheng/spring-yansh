package com.yansheng.beans.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class PropertiesEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		Properties props = new Properties();
		if (text != null) {
			try {
				props.load(new ByteArrayInputStream(text.getBytes("ISO-8859-1")));
			} catch (IOException e) {
				throw new IllegalArgumentException(text + "解析成Properties失败。");
			}
		}
		setValue(props);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setValue(Object value) {
		if (!(value instanceof Properties) && (value instanceof Map)) {
			Properties props = new Properties();
			props.putAll((Map) value);
			super.setValue(props);
		} else {
			setValue(value);
		}
	}

}
