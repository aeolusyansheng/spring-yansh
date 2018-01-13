package com.yansheng.beans.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class ResourceBoundleEditor extends PropertyEditorSupport {

	public static final String BASE_NAME_SEPARATOR = "_";

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		Assert.hasText(text, "text不能为空。");
		ResourceBundle bundle;
		String rawBaseName = text.trim();
		int indexOfBeanNameSeparator = text.indexOf(BASE_NAME_SEPARATOR);
		if (indexOfBeanNameSeparator == -1) {
			bundle = ResourceBundle.getBundle(rawBaseName);
		} else {
			String baseName = text.substring(0, indexOfBeanNameSeparator);
			if (!StringUtils.hasText(baseName)) {
				throw new IllegalArgumentException("无法转换成ResourceBundle。");
			}
			String localString = text.substring(indexOfBeanNameSeparator + 1);
			Locale locale = StringUtils.parseLocaleString(localString);
			bundle = (StringUtils.hasText(localString) ? ResourceBundle.getBundle(baseName, locale)
					: ResourceBundle.getBundle(baseName));
		}
		setValue(bundle);
	}
}
