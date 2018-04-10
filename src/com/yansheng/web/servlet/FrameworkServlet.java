package com.yansheng.web.servlet;

import java.util.ArrayList;

import javax.servlet.ServletException;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

public abstract class FrameworkServlet extends HttpServletBean {

    public static final String DEFAULT_NAMESPACE_SUFFIX = "-servlet";
    public static final Class<?> DEFAULT_CONTEXT_CLASS = XmlWebApplicationContext.class;
    public static final String SERVLET_CONTEXT_PREFIX = FrameworkServlet.class.getName() + ".CONTEXT.";
    private static final String INIT_PARAM_DELIMETERS = ",;\t\n";
    private String contextAttribute;
    private Class<?> contextClass = DEFAULT_CONTEXT_CLASS;
    private String contextId;
    private String namespace;
    private String contextConfigLocation;
    private boolean publishContext;
    private boolean publishEvents = true;
    private boolean threadContextInheritable = false;
    private boolean dispatchOptionsRequest = false;
    private boolean dispatchTraceRequest = false;
    private WebApplicationContext webApplicationContext;
    private boolean refreshEventReceived = false;
    private String contextInitializerClasses;
    private ArrayList<ApplicationContextInitializer<ConfigurableApplicationContext>> contextInitializers = new ArrayList<ApplicationContextInitializer<ConfigurableApplicationContext>>();

    public FrameworkServlet() {

    }

    public FrameworkServlet(WebApplicationContext WebApplicationContext) {
        this.webApplicationContext = WebApplicationContext;
    }

    public void setContextAttribute(String contextAttribute) {
        this.contextAttribute = contextAttribute;
    }

    public String getContextAttribute() {
        return this.contextAttribute;
    }

    public void setContextClass(Class<?> contextClass) {
        this.contextClass = contextClass;
    }

    public Class<?> getContextClass() {
        return this.contextClass;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public String getContextId() {
        return this.contextId;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return (this.namespace != null ? this.namespace : getServletName() + DEFAULT_NAMESPACE_SUFFIX);
    }

    public void setContextInitializerClasses(String contextInitializerClasses) {
        this.contextInitializerClasses = contextInitializerClasses;
    }

    public void setContextInitializers(
            ApplicationContextInitializer<ConfigurableApplicationContext>... contextInitializers) {
        for (ApplicationContextInitializer<ConfigurableApplicationContext> initializer : contextInitializers) {
            this.contextInitializers.add(initializer);
        }
    }

    public void setContextConfigLocation(String contextConfigLocation) {
        this.contextConfigLocation = contextConfigLocation;
    }

    public String getContextConfigLocation() {
        return this.contextConfigLocation;
    }

    public void setPublishContext(boolean publishContext) {
        this.publishContext = publishContext;
    }

    public void setPublishEvents(boolean publishEvents) {
        this.publishEvents = publishEvents;
    }

    public void setThreadContextInheritable(boolean threadContextInheritable) {
        this.threadContextInheritable = threadContextInheritable;
    }

    public void setDispatchOptionsRequest(boolean dispatchOptionsRequest) {
        this.dispatchOptionsRequest = dispatchOptionsRequest;
    }

    public void setDispatchTraceRequest(boolean dispatchTraceRequest) {
        this.dispatchTraceRequest = dispatchTraceRequest;
    }

    @Override
    protected final void initServletBean() throws ServletException {
        getServletContext().log("Initializing Spring FrameworkServlet '" + getServletName() + "'");
        if (this.logger.isInfoEnabled()) {
            this.logger.info("FrameworkServlet '" + getServletName() + "': initialization started");
        }
        long startTime = System.currentTimeMillis();
        try {
            this.webApplicationContext = initWebApplicationContext();
            initFrameworkServlet();
        } catch (ServletException ex) {
            this.logger.error("Context initialization failed", ex);
            throw ex;
        } catch (RuntimeException ex) {
            this.logger.error("Context initialization failed", ex);
            throw ex;
        }
        if (this.logger.isInfoEnabled()) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            this.logger.info("FrameworkServlet '" + getServletName() + "': initialization completed in " +
                    elapsedTime + " ms");
        }
    }

    protected WebApplicationContext initWebApplicationContext() {
        return null;
    }

    protected void initFrameworkServlet() throws ServletException {

    }

}
