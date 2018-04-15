package com.yansheng.web.servlet.handler;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ClassUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ReflectionUtils.MethodFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.HandlerMethodSelector;

public abstract class AbstractHandlerMethodMapping<T> extends AbstractHandlerMapping implements InitializingBean {

	private boolean detectHandlerMethodsInAncestorContexts = false;
	private final Map<T, HandlerMethod> handlerMethods = new LinkedHashMap<T, HandlerMethod>();
	private final MultiValueMap<String, T> urlMap = new LinkedMultiValueMap<String, T>();

	public void setDetectHandlerMethodsInAncestorContexts(boolean detectHandlerMethodsInAncestorContexts) {
		this.detectHandlerMethodsInAncestorContexts = detectHandlerMethodsInAncestorContexts;
	}

	public Map<T, HandlerMethod> getHandlerMethods() {
		return Collections.unmodifiableMap(this.handlerMethods);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initHandlerMethods();
	}

	protected void initHandlerMethods() {
		if (logger.isDebugEnabled()) {
			logger.debug("Looking for request mappings in application context: " + getApplicationContext());
		}
		String[] beanNames = (this.detectHandlerMethodsInAncestorContexts
				? BeanFactoryUtils.beanNamesForTypeIncludingAncestors(getApplicationContext(), Object.class)
				: getApplicationContext().getBeanNamesForType(Object.class));
		for (String beanName : beanNames) {
			if (isHandler(getApplicationContext().getType(beanName))) {
				detectHandlerMethods(beanName);
			}
		}
		handlerMethodsInitialized(getHandlerMethods());
	}

	protected abstract boolean isHandler(Class<?> beanType);

	protected void handlerMethodsInitialized(Map<T, HandlerMethod> handlerMethods) {

	}

	protected void detectHandlerMethods(final Object handler) {
		Class<?> handlerType = (handler instanceof String ? getApplicationContext().getType((String) handler)
				: handler.getClass());
		final Class<?> userType = ClassUtils.getUserClass(handlerType);
		Set<Method> methods = HandlerMethodSelector.selectMethods(userType, new MethodFilter() {
			@Override
			public boolean matches(Method method) {
				return getMappingForMethod(method, userType) != null;
			}
		});

		for (Method method : methods) {
			T mapping = getMappingForMethod(method, userType);
			registerHandlerMethod(handler, method, mapping);
		}
	}

	protected abstract T getMappingForMethod(Method method, Class<?> handlerType);

	protected void registerHandlerMethod(Object handler, Method method, T mapping) {
		HandlerMethod newHandlerMethod = createHandlerMethod(handler, method);
		HandlerMethod oldHandlerMethod = this.handlerMethods.get(mapping);
		if (oldHandlerMethod != null && !oldHandlerMethod.equals(newHandlerMethod)) {
			throw new IllegalStateException("Ambiguous mapping found. Cannot map '" + newHandlerMethod.getBean()
					+ "' bean method \n" + newHandlerMethod + "\nto " + mapping + ": There is already '"
					+ oldHandlerMethod.getBean() + "' bean method\n" + oldHandlerMethod + " mapped.");
		}
		this.handlerMethods.put(mapping, newHandlerMethod);
		if (logger.isInfoEnabled()) {
			logger.info("Mapped \"" + mapping + "\" onto " + newHandlerMethod);
		}
		Set<String> patterns = getMappingPathPattern(mapping);
		for (String pattern : patterns) {
			if (!getPathMatcher().isPattern(pattern)) {
				this.urlMap.add(pattern, mapping);
			}
		}
	}

	protected HandlerMethod createHandlerMethod(Object handler, Method method) {
		HandlerMethod handlerMethod;
		if (handler instanceof String) {
			String beanName = (String) handler;
			handlerMethod = new HandlerMethod(beanName, getApplicationContext(), method);
		} else {
			handlerMethod = new HandlerMethod(handler, method);
		}
		return handlerMethod;
	}

	protected abstract Set<String> getMappingPathPattern(T mapping);

	@Override
	protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
