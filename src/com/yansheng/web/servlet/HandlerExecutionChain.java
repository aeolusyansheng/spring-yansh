package com.yansheng.web.servlet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

public class HandlerExecutionChain {

	private static final Log logger = LogFactory.getLog(HandlerExecutionChain.class);
	private final Object handler;
	private HandlerInterceptor[] interceptors;
	private List<HandlerInterceptor> interceptorList;
	private int interceptorIndex = -1;

	public HandlerExecutionChain(Object handler) {
		this(handler, null);
	}

	public HandlerExecutionChain(Object handler, HandlerInterceptor[] inteceptors) {
		if (handler instanceof HandlerExecutionChain) {
			HandlerExecutionChain originChain = (HandlerExecutionChain) handler;
			this.handler = originChain.getHandler();
			this.interceptorList = new ArrayList<HandlerInterceptor>();
			CollectionUtils.mergeArrayIntoCollection(originChain.getInterceptors(), this.interceptorList);
			CollectionUtils.mergeArrayIntoCollection(inteceptors, this.interceptorList);
		} else {
			this.handler = handler;
			this.interceptors = inteceptors;
		}
	}

	public Object getHandler() {
		return this.handler;
	}

	public void addInterceptor(HandlerInterceptor interceptor) {
		initInterceptorList();
		this.interceptorList.add(interceptor);
	}

	public void addInterceptors(HandlerInterceptor[] interceptors) {
		if (interceptors != null) {
			initInterceptorList();
			this.interceptorList.addAll(Arrays.asList(interceptors));
		}
	}

	private void initInterceptorList() {
		if (this.interceptorList == null) {
			this.interceptorList = new ArrayList<HandlerInterceptor>();
		}
		if (this.interceptors != null) {
			this.interceptorList.addAll(Arrays.asList(this.interceptors));
			this.interceptors = null;
		}
	}

	public HandlerInterceptor[] getInterceptors() {
		if (this.interceptors == null && this.interceptorList != null) {
			this.interceptors = this.interceptorList.toArray(new HandlerInterceptor[this.interceptorList.size()]);
		}
		return this.interceptors;
	}

	boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (getInterceptors() != null) {
			for (int i = 0; i < getInterceptors().length; i++) {
				HandlerInterceptor interceptor = getInterceptors()[i];
				if (!interceptor.preHandler(request, response, this.handler)) {
					triggerAfterCompletion(request, response, null);
					return false;
				}
				this.interceptorIndex = i;
			}
		}
		return true;
	}

	void applyPostHandle(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) throws Exception {
		if (getInterceptors() == null) {
			return;
		}
		for (int i = getInterceptors().length - 1; i >= 0; i--) {
			HandlerInterceptor interceptor = getInterceptors()[i];
			interceptor.postHandler(request, response, this.handler, mv);
		}
	}

	void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, Exception ex)
			throws Exception {
		if (getInterceptors() == null) {
			return;
		}
		for (int i = this.interceptorIndex; i >= 0; i--) {
			HandlerInterceptor interceptor = getInterceptors()[i];
			try {
				interceptor.afterCompletion(request, response, this.handler, ex);
			} catch (Throwable ex2) {
				logger.error("HandlerInterceptor.afterCompletion threw exception", ex2);
			}

		}
	}

	void applyAfterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response) {
		if (getInterceptors() == null) {
			return;
		}
		for (int i = getInterceptors().length - 1; i >= 0; i--) {
			if (this.interceptors[i] instanceof AsyncHandlerInterceptor) {
				try {
					AsyncHandlerInterceptor asyncInterceptor = (AsyncHandlerInterceptor) this.interceptors[i];
					asyncInterceptor.afterConcurrentHandlingStarted(request, response, this.handler);
				} catch (Throwable ex) {
					logger.error("Interceptor [" + interceptors[i] + "] failed in afterConcurrentHandlingStarted", ex);
				}
			}
		}
	}

	@Override
	public String toString() {
		if (this.handler == null) {
			return "HandlerExecutionChain with no handler";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("HandlerExecutionChain with handler [").append(this.handler).append("]");
		if (!CollectionUtils.isEmpty(this.interceptorList)) {
			sb.append(" and ").append(this.interceptorList.size()).append(" interceptor");
			if (this.interceptorList.size() > 1) {
				sb.append("s");
			}
		}
		return sb.toString();
	}
}
