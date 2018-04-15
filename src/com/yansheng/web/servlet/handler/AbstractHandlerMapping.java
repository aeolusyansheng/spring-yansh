package com.yansheng.web.servlet.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.core.Ordered;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.util.UrlPathHelper;

import com.yansheng.beans.BeansException;
import com.yansheng.web.servlet.HandlerExecutionChain;
import com.yansheng.web.servlet.HandlerInterceptor;
import com.yansheng.web.servlet.HandlerMapping;

public abstract class AbstractHandlerMapping extends WebApplicationObjectSupport implements HandlerMapping, Ordered {

	private int order = Integer.MAX_VALUE;
	private Object defaultHandler;
	private UrlPathHelper urlPathHelper = new UrlPathHelper();
	private PathMatcher pathMatcher = new AntPathMatcher();
	private final List<Object> interceptors = new ArrayList<Object>();
	private final List<HandlerInterceptor> adaptedInterceptors = new ArrayList<HandlerInterceptor>();
	private final List<MappedInterceptor> mappedInterceptors = new ArrayList<MappedInterceptor>();
	
	//aaa

	public final void setOrder(int order) {
		this.order = order;
	}

	@Override
	public final int getOrder() {
		return this.order;
	}

	public void setDefaultHandler(Object defaultHandler) {
		this.defaultHandler = defaultHandler;
	}

	public Object getDefaultHandler() {
		return this.defaultHandler;
	}

	public void setAlwaysUseFullPath(boolean alwaysUseFullPath) {
		this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
	}

	public void setUrlDecode(boolean urlDecode) {
		this.urlPathHelper.setUrlDecode(urlDecode);
	}

	public void setRemoveSemicolonContent(boolean removeSemicolonContent) {
		this.urlPathHelper.setRemoveSemicolonContent(removeSemicolonContent);
	}

	public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
		Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
		this.urlPathHelper = urlPathHelper;
	}

	public UrlPathHelper getUrlPathHelper() {
		return urlPathHelper;
	}

	public void setPathMatcher(PathMatcher pathMatcher) {
		Assert.notNull(pathMatcher, "PathMatcher must not be null");
		this.pathMatcher = pathMatcher;
	}

	public PathMatcher getPathMatcher() {
		return this.pathMatcher;
	}

	public void setInterceptors(Object[] interceptors) {
		this.interceptors.addAll(Arrays.asList(interceptors));
	}

	@Override
	protected void initApplicationContext() throws BeansException {
		extendInterceptors(this.interceptors);
		detectMappedInterceptors(this.mappedInterceptors);
		initInterceptors();
	}

	protected void extendInterceptors(List<Object> interceptors) {
	}

	protected void detectMappedInterceptors(List<MappedInterceptor> mappedInterceptors) {
		mappedInterceptors.addAll(BeanFactoryUtils
				.beansOfTypeIncludingAncestors(getApplicationContext(), MappedInterceptor.class, true, false).values());
	}

	protected void initInterceptors() {
		if (!this.interceptors.isEmpty()) {
			for (int i = 0; i < this.interceptors.size(); i++) {
				Object interceptor = this.interceptors.get(i);
				if (interceptor == null) {
					throw new IllegalArgumentException("Entry number " + i + " in interceptors array is null");
				}
				if (interceptor instanceof MappedInterceptor) {
					this.mappedInterceptors.add((MappedInterceptor) interceptor);
				} else {
					this.adaptedInterceptors.add(adaptInterceptor(interceptor));
				}
			}
		}
	}

	protected HandlerInterceptor adaptInterceptor(Object interceptor) {
		if (interceptor instanceof HandlerInterceptor) {
			return (HandlerInterceptor) interceptor;
		} else if (interceptor instanceof WebRequestHandlerInterceptorAdapter) {
			return new WebRequestHandlerInterceptorAdapter((WebRequestInterceptor) interceptor);
		} else {
			throw new IllegalArgumentException("Interceptor type not supported: " + interceptor.getClass().getName());
		}
	}

	protected final HandlerInterceptor[] getAdaptedInterceptors() {
		int count = this.adaptedInterceptors.size();
		return (count > 0 ? this.adaptedInterceptors.toArray(new HandlerInterceptor[count]) : null);
	}

	protected final MappedInterceptor[] getMappedIntercptors() {
		int count = this.mappedInterceptors.size();
		return (count > 0 ? this.mappedInterceptors.toArray(new MappedInterceptor[count]) : null);
	}

	@Override
	public final HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
		Object handler = getHandlerInternal(request);
		if (handler == null) {
			handler = getDefaultHandler();
		}
		if (handler == null) {
			return null;
		}
		if (handler instanceof String) {
			String handlerName = (String) handler;
			handler = getApplicationContext().getBean(handlerName);
		}

		return getHandlerExecutionChain(handler, request);
	}

	protected abstract Object getHandlerInternal(HttpServletRequest request) throws Exception;

	protected HandlerExecutionChain getHandlerExecutionChain(Object handler, HttpServletRequest request) {
		HandlerExecutionChain chain = (handler instanceof HandlerExecutionChain ? (HandlerExecutionChain) handler
				: new HandlerExecutionChain(handler));
		chain.addInterceptors(getAdaptedInterceptors());
		String lookUpPath = this.urlPathHelper.getLookupPathForRequest(request);
		for (MappedInterceptor mappedInterceptor : this.mappedInterceptors) {
			if (mappedInterceptor.matches(lookUpPath, this.pathMatcher)) {
				chain.addInterceptor(mappedInterceptor.getInterceptor());
			}
		}
		return chain;
	}
}
