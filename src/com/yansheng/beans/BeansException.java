package com.yansheng.beans;

import org.springframework.core.NestedRuntimeException;

public abstract class BeansException extends NestedRuntimeException {

    private String msg;
    private int aa=0;

    public BeansException(String msg) {
        super(msg);
    }
}
