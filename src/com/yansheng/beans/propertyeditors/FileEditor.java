package com.yansheng.beans.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.io.File;
import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

public class FileEditor extends PropertyEditorSupport {

	private final ResourceEditor resourceEditor;

	public FileEditor(ResourceEditor resourceEditor) {
		Assert.notNull(resourceEditor, "ResourceEditor不能为null。");
		this.resourceEditor = resourceEditor;
	}

	public FileEditor() {
		this.resourceEditor = new ResourceEditor();
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (!StringUtils.hasText(text)) {
			setValue(null);
			return;
		}

		if (!ResourceUtils.isUrl(text)) {
			File file = new File(text);
			if (file.isAbsolute()) {
				setValue(file);
				return;
			}
		}

		this.resourceEditor.setAsText(text);
		Resource resource = (Resource) this.resourceEditor.getValue();

		if (ResourceUtils.isUrl(text) || resource.exists()) {
			try {
				setValue(resource.getFile());
			} catch (IOException e) {
				throw new IllegalArgumentException("无法将资源：" + text + "解析成File。" + e.getMessage());
			}
		} else {
			setValue(new File(text));
		}
	}

	public String getAsText() {
		File file = (File) getValue();
		return (file != null ? file.getPath() : "");
	}

}
