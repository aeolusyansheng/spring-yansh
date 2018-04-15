package com.yansheng.web.servlet.handler;

import org.springframework.util.PathMatcher;
import org.springframework.web.context.request.WebRequestInterceptor;

import com.yansheng.web.servlet.HandlerInterceptor;

public final class MappedInterceptor {
	private final String[] includePatterns;
	private final String[] excludePatterns;
	private final HandlerInterceptor interceptor;

	public MappedInterceptor(String[] includePatterns, HandlerInterceptor interceptor) {
		this(includePatterns, null, interceptor);
	}

	public MappedInterceptor(String[] includePatterns, String[] excludePatterns, HandlerInterceptor interceptor) {
		this.includePatterns = includePatterns;
		this.excludePatterns = excludePatterns;
		this.interceptor = interceptor;
	}

	public MappedInterceptor(String[] includePatterns, WebRequestInterceptor interceptor) {
		this(includePatterns, null, interceptor);
	}

	public MappedInterceptor(String[] includePatterns, String[] excludePatterns, WebRequestInterceptor interceptor) {
		this(includePatterns, excludePatterns, new WebRequestHandlerInterceptorAdapter(interceptor));
	}

	public String[] getPathPatterns() {
		return this.includePatterns;
	}

	public HandlerInterceptor getInterceptor() {
		return this.interceptor;
	}

	public boolean matches(String lookUpPath, PathMatcher pathMatcher) {
		if (this.excludePatterns != null) {
			for (String pattern : this.excludePatterns) {
				if (pathMatcher.match(pattern, lookUpPath)) {
					return false;
				}
			}
		}
		if (this.includePatterns == null) {
			return true;
		} else {
			for (String patter : this.includePatterns) {
				if (pathMatcher.match(patter, lookUpPath)) {
					return true;
				}
			}
			return false;
		}

	}
}
