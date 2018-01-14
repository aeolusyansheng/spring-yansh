package com.yansheng.beans;

import org.springframework.core.convert.ConversionService;

public interface ConfigurablePropertyAccessor extends PropertyAccessor, PropertyEditorRegistry, TypeConverter {

	void setConversionService(ConversionService conversionService);

	ConversionService getConversionService();

	void setExtractOldValueForEditor(boolean extractOldValueForEditor);

	boolean isExtractOldValueForEditor();
}
