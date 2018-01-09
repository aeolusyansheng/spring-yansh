package com.yansheng.beans.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.net.URL;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;

public class URLEditor extends PropertyEditorSupport {

    private final ResourceEditor resourceEditor;

    public URLEditor() {
        this.resourceEditor = new ResourceEditor();
    }

    public URLEditor(ResourceEditor resourceEditor) {
        this.resourceEditor = resourceEditor;
    }

    @Override
    public void setAsText(String text) {
        this.resourceEditor.setAsText(text);
        Resource resource = (Resource) this.resourceEditor.getValue();
        try {
            setValue(resource != null ? resource.getURL() : null);
        } catch (IOException e) {
            throw new IllegalArgumentException("无法从resource:" + resource + "取得URL.");
        }
    }

    @Override
    public String getAsText() {
        URL value = (URL) getValue();
        return (value != null ? value.toExternalForm() : "");
    }
}
