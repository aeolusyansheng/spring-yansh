package com.yansheng.web.servlet;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

public class HandlerExecutionChain {

    private static final Log logger = LogFactory.getLog(HandlerExecutionChain.class);
    private final Object handler;
    private HandlerInteceptor[] inteceptors;
    private List<HandlerInteceptor> inteceptorList;
    private int inteceptorIndex = -1;

    public HandlerExecutionChain(Object handler) {
        this(handler, null);
    }

    public HandlerExecutionChain(Object handler, HandlerInteceptor[] inteceptors) {
        if (handler instanceof HandlerExecutionChain) {
            HandlerExecutionChain originChain = (HandlerExecutionChain) handler;
            this.handler = originChain.getHandler();
            this.inteceptorList = new ArrayList<HandlerInteceptor>();
            CollectionUtils.mergeArrayIntoCollection(originChain.getInteceptors(), this.inteceptorList);
            CollectionUtils.mergeArrayIntoCollection(inteceptors, this.inteceptorList);
        } else {
            this.handler = handler;
            this.inteceptors = inteceptors;
        }
    }

    public Object getHandler() {
        return this.handler;
    }

    public HandlerInteceptor[] getInteceptors() {
        if (this.inteceptors == null && this.inteceptorList != null) {
            this.inteceptors = this.inteceptorList.toArray(new HandlerInteceptor[this.inteceptorList.size()]);
        }
        return this.inteceptors;
    }
}
