package com.yansheng.web.servlet.mvc.condition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMethod;

public final class RequestMethodsRequestCondition extends AbstractRequestCondition<RequestMethodsRequestCondition> {

    private final Set<RequestMethod> methods;

    public RequestMethodsRequestCondition(RequestMethod... requestMethods) {
        this(asList(requestMethods));
    }

    private static List<RequestMethod> asList(RequestMethod... requestMethods) {
        return requestMethods != null ? Arrays.asList(requestMethods) : Collections.<RequestMethod> emptyList();
    }

    private RequestMethodsRequestCondition(Collection<RequestMethod> requestMethods) {
        this.methods = Collections.unmodifiableSet(new LinkedHashSet<RequestMethod>(requestMethods));
    }

    public Set<RequestMethod> getMethods() {
        return this.methods;
    }

    @Override
    protected Collection<RequestMethod> getContent() {
        return this.methods;
    }

    @Override
    protected String getToStringInfix() {
        return " || ";
    }

    @Override
    public RequestMethodsRequestCondition combine(RequestMethodsRequestCondition other) {
        Set<RequestMethod> set = new LinkedHashSet<RequestMethod>(this.methods);
        set.addAll(other.methods);
        return new RequestMethodsRequestCondition(set);
    }

    @Override
    public RequestMethodsRequestCondition getMatchingCondition(HttpServletRequest request) {
        if (this.methods.isEmpty()) {
            return this;
        }
        RequestMethod incomingRequestMethod = getRequestMethod(request);
        if (incomingRequestMethod != null) {
            for (RequestMethod method : this.methods) {
                if (method.equals(incomingRequestMethod)) {
                    return new RequestMethodsRequestCondition(method);
                }
            }
        }
        return null;
    }

    private RequestMethod getRequestMethod(HttpServletRequest request) {
        try {
            return RequestMethod.valueOf(request.getMethod());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public int compareTo(RequestMethodsRequestCondition other, HttpServletRequest request) {
        return other.methods.size() - this.methods.size();
    }

}
