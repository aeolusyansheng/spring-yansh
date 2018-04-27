package com.yansheng.web.servlet.mvc.method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.support.WebContentGenerator;

import com.yansheng.web.servlet.HandlerAdapter;
import com.yansheng.web.servlet.ModelAndView;

public abstract class AbstractHandlerMethodAdapter extends WebContentGenerator implements HandlerAdapter, Ordered {

    private int order = Ordered.LOWEST_PRECEDENCE;

    public AbstractHandlerMethodAdapter() {
        super(false);
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof HandlerMethod && supportInternal((HandlerMethod) handler));
    }

    protected abstract boolean supportInternal(HandlerMethod handlerMethod);

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        return handleInternal(request, response, (HandlerMethod) handler);
    }

    protected abstract ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response,
            HandlerMethod handlerMethod) throws Exception;

    @Override
    public long getLastModified(HttpServletRequest request, Object handler) {
        return getlastModifiedInternal(request, (HandlerMethod) handler);
    }

    protected abstract long getlastModifiedInternal(HttpServletRequest request, HandlerMethod handlerMethod);

}
