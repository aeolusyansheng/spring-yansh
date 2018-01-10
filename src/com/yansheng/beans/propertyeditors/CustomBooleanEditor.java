package com.yansheng.beans.propertyeditors;

import java.beans.PropertyEditorSupport;

import org.springframework.util.StringUtils;

public class CustomBooleanEditor extends PropertyEditorSupport {

    public static final String VALUE_TRUE = "true";
    public static final String VALUE_FALSE = "false";

    public static final String VALUE_ON = "on";
    public static final String VALUE_OFF = "off";

    public static final String VALUE_YES = "yes";
    public static final String VALUE_NO = "no";

    public static final String VALUE_1 = "1";
    public static final String VALUE_0 = "0";

    private final String trueString;
    private final String falseString;

    private final boolean allowEmpty;

    public CustomBooleanEditor(boolean allowEmpty) {
        this(null, null, allowEmpty);
    }

    public CustomBooleanEditor(String trueString, String falseString, boolean allowEmpty) {
        this.trueString = trueString;
        this.falseString = falseString;
        this.allowEmpty = allowEmpty;
    }

    @Override
    public void setAsText(String text) {
        String input = text.trim();
        if (this.allowEmpty && !StringUtils.hasLength(input)) {
            setValue(null);
        } else if (this.trueString != null && input.equals(this.trueString)) {
            setValue(Boolean.TRUE);
        } else if (this.falseString != null && input.equals(this.falseString)) {
            setValue(Boolean.FALSE);
        } else if (this.trueString == null &&
                (VALUE_TRUE.equals(input) || VALUE_1.equals(input) ||
                        VALUE_ON.equals(input) || VALUE_YES.equals(input))) {
            setValue(Boolean.TRUE);
        } else if (this.falseString == null &&
                (VALUE_FALSE.equals(input) || VALUE_0.equals(input) ||
                        VALUE_OFF.equals(input) || VALUE_NO.equals(input))) {
            setValue(Boolean.FALSE);
        } else {
            throw new IllegalArgumentException("无效的布尔类型:" + text);
        }
    }

    @Override
    public String getAsText() {
        if (Boolean.TRUE.equals(getValue())) {
            return (this.trueString != null ? this.trueString : VALUE_TRUE);
        } else if (Boolean.FALSE.equals(getValue())) {
            return (this.falseString != null ? this.falseString : VALUE_FALSE);
        } else {
            return "";
        }
    }

}
