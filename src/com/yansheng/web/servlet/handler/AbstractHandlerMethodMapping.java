package com.yansheng.web.servlet.handler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
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

import com.yansheng.web.servlet.HandlerMapping;

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
		Set<String> patterns = getMappingPathPatterns(mapping);
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

	protected abstract Set<String> getMappingPathPatterns(T mapping);

	@Override
	protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
		String lookUpPath = getUrlPathHelper().getLookupPathForRequest(request);
		if (logger.isDebugEnabled()) {
			logger.debug("Looking up handler method for path " + lookUpPath);
		}
		HandlerMethod handlerMethod = lookupHandlerMethod(lookUpPath, request);
		if (logger.isDebugEnabled()) {
			if (handlerMethod != null) {
				logger.debug("Returning handler method [" + handlerMethod + "]");
			} else {
				logger.debug("Did not find handler method for [" + lookUpPath + "]");
			}
		}
		return (handlerMethod != null ? handlerMethod.createWithResolvedBean() : null);
	}

	protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) throws Exception {
		List<Match> matches = new ArrayList<Match>();
		List<T> directPathMatches = this.urlMap.get(lookupPath);
		if (directPathMatches != null) {
			addMatchingMappings(directPathMatches, matches, request);
		}
		if (matches.isEmpty()) {
			addMatchingMappings(this.handlerMethods.keySet(), matches, request);
		}
		if (!matches.isEmpty()) {
			Comparator<Match> comparator = new MatchComparator(getMappingComparator(request));
			Collections.sort(matches, comparator);
			if (logger.isTraceEnabled()) {
				logger.trace("Found " + matches.size() + " matching mapping(s) for [" + lookupPath + "] : " + matches);
			}
			Match bestMatch = matches.get(0);
			if (matches.size() > 1) {
				Match seecondBestMatch = matches.get(1);
				if (comparator.compare(bestMatch, seecondBestMatch) == 0) {
					Method m1 = bestMatch.handlerMethod.getMethod();
					Method m2 = seecondBestMatch.handlerMethod.getMethod();
					throw new IllegalStateException("Ambiguous handler methods mapped for HTTP path '"
							+ request.getRequestURL() + "': {" + m1 + ", " + m2 + "}");
				}
			}
			handlerMatch(bestMatch.mapping, lookupPath, request);
			return bestMatch.handlerMethod;
		} else {
			return handlerNoMatch(this.handlerMethods.keySet(), lookupPath, request);
		}
	}

	private void addMatchingMappings(Collection<T> mappings, List<Match> matches, HttpServletRequest request) {
		for (T mapping : mappings) {
			T match = getMatchingMapping(mapping, request);
			if (match != null) {
				matches.add(new Match(match, this.handlerMethods.get(mapping)));
			}
		}
	}

	protected abstract T getMatchingMapping(T mapping, HttpServletRequest request);

	protected abstract Comparator<T> getMappingComparator(HttpServletRequest request);

	protected void handlerMatch(T mapping, String lookupPath, HttpServletRequest request) {
		request.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, lookupPath);
	}

	protected HandlerMethod handlerNoMatch(Set<T> mappings, String lookupPath, HttpServletRequest request)
			throws Exception {
		return null;
	}

	private class Match {
		private final T mapping;
		private final HandlerMethod handlerMethod;

		private Match(T mapping, HandlerMethod handlerMethod) {
			this.mapping = mapping;
			this.handlerMethod = handlerMethod;
		}

		@Override
		public String toString() {
			return this.mapping.toString();
		}
	}

	private class MatchComparator implements Comparator<Match> {

		private final Comparator<T> comparator;

		public MatchComparator(Comparator<T> comparator) {
			this.comparator = comparator;
		}

		@Override
		public int compare(Match match1, Match match2) {
			return this.comparator.compare(match1.mapping, match2.mapping);
		}

	}
}
