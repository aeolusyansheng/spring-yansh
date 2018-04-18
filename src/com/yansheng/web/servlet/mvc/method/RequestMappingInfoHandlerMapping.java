package com.yansheng.web.servlet.mvc.method;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.yansheng.web.servlet.handler.AbstractHandlerMethodMapping;

public class RequestMappingInfoHandlerMapping extends AbstractHandlerMethodMapping<RequestMappingInfo> {

    @Override
    protected boolean isHandler(Class<?> beanType) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Set<String> getMappingPathPattern(RequestMappingInfo mapping) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected RequestMappingInfo getMatchingMapping(RequestMappingInfo mapping, HttpServletRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Comparator<RequestMappingInfo> getMappingComparator(HttpServletRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

}
