package com.yansheng.beans.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;
import org.springframework.util.Assert;

public class InputStreamEditor extends PropertyEditorSupport {

	private final ResourceEditor resourceEditor;

	public InputStreamEditor() {
		this.resourceEditor = new ResourceEditor();
	}

	public InputStreamEditor(ResourceEditor resourceEditor) {
		Assert.notNull(resourceEditor, "ResourceEditor不能为null。");
		this.resourceEditor = resourceEditor;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		this.resourceEditor.setAsText(text);
		Resource resource = (Resource) this.resourceEditor.getValue();
		if (resource != null) {
			try {
				setValue(resource.getInputStream());
			} catch (IOException e) {
				throw new IllegalArgumentException("无法将资源：" + text + "转换成InputStream。" + e.getMessage());
			}
		} else {
			setValue(null);
		}
	}

	@Override
	public String getAsText() {
		return null;
	}
}
