package com.yansheng.web.servlet.mvc.condition;

public interface NameValueExpression<T> {

    String getName();
    T getValue();
    boolean isNegated();
}
