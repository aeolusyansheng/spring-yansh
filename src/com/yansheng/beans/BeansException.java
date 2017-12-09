package com.yansheng.beans;

import org.springframework.core.NestedRuntimeException;

public abstract class BeansException extends NestedRuntimeException {

    public BeansException(String msg) {
        super(msg);
    }
        
    //
    
    
}
