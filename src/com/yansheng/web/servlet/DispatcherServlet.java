package com.yansheng.web.servlet;

import org.springframework.web.context.WebApplicationContext;

public class DispatcherServlet extends FrameworkServlet {

    public DispatcherServlet() {
        super();
    }

    public DispatcherServlet(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }
}
