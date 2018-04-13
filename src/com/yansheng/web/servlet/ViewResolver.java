package com.yansheng.web.servlet;

import java.util.Locale;

public interface ViewResolver {

    View resolverViewName(String viewName, Locale locale) throws Exception;
}
