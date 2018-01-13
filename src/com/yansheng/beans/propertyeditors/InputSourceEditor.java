package com.yansheng.beans.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;
import org.springframework.util.Assert;
import org.xml.sax.InputSource;

public class InputSourceEditor extends PropertyEditorSupport {

	private final ResourceEditor resourceEditor;

	public InputSourceEditor() {
		this.resourceEditor = new ResourceEditor();
	}

	public InputSourceEditor(ResourceEditor resourceEditor) {
		Assert.notNull(resourceEditor, "ResourceEditor不能为null。");
		this.resourceEditor = resourceEditor;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		this.resourceEditor.setAsText(text);
		Resource resource = (Resource) this.resourceEditor.getValue();
		if (resource != null) {
			try {
				setValue(new InputSource(resource.getURI().toString()));
			} catch (IOException e) {
				throw new IllegalArgumentException("无法将资源：" + text + "转换成URL。" + e.getMessage());
			}
		} else {
			setValue(null);
		}
	}

	@Override
	public String getAsText() {
		InputSource value = (InputSource) getValue();
		return (value != null ? value.getSystemId() : "");
	}
}
