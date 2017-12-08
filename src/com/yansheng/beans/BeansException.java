package com.yansheng.beans;

import org.springframework.core.NestedRuntimeException;

public abstract class BeansException extends NestedRuntimeException {

    private String msg;

    public BeansException(String msg) {
        super(msg);
    }
}
