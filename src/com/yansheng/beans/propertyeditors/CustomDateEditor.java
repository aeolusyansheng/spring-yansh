package com.yansheng.beans.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.springframework.util.StringUtils;

public class CustomDateEditor extends PropertyEditorSupport {

    private final DateFormat dateFormat;
    private final boolean allowEmpty;
    private final int exactDateLength;

    public CustomDateEditor(DateFormat dateFormat, boolean allowEmpty, int exactDateLength) {
        this.dateFormat = dateFormat;
        this.allowEmpty = allowEmpty;
        this.exactDateLength = exactDateLength;
    }

    public CustomDateEditor(DateFormat dateFormat, boolean allowEmpty) {
        this.dateFormat = dateFormat;
        this.allowEmpty = allowEmpty;
        this.exactDateLength = -1;
    }

    @Override
    public void setAsText(String text) {
        if (this.allowEmpty && !StringUtils.hasText(text)) {
            setValue(null);
        } else if (text != null && this.exactDateLength >= 0 && this.exactDateLength != text.length()) {
            throw new IllegalArgumentException("无法解析成date。字符的长度必须为" + this.exactDateLength);
        } else {
            try {
                setValue(this.dateFormat.parse(text));
            } catch (ParseException e) {
                throw new IllegalArgumentException("无法解析成date。" + e.getMessage(), e);
            }
        }
    }

    @Override
    public String getAsText() {
        Date value = (Date) getValue();
        return (value != null ? this.dateFormat.format(value) : "");
    }

}
