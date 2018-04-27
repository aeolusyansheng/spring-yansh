package com.yansheng.web.servlet.mvc.method.annotation;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;

import com.yansheng.web.servlet.ModelAndView;
import com.yansheng.web.servlet.mvc.method.AbstractHandlerMethodAdapter;

public class RequestMappingHandlerAdapter extends AbstractHandlerMethodAdapter
        implements BeanFactoryAware, InitializingBean {

    private List<HandlerMethodArgumentResolver> customArgumentResolvers;
    private HandlerMethodArgumentResolverComposite argumentResolvers;
    private HandlerMethodArgumentResolverComposite initBinderArgumentResolvers;
    private List<HandlerMethodReturnValueHandler> customReturnValueHandlers;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean supportInternal(HandlerMethod handlerMethod) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response,
            HandlerMethod handlerMethod) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected long getlastModifiedInternal(HttpServletRequest request, HandlerMethod handlerMethod) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub

    }

}
