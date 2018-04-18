package com.yansheng.web.servlet.mvc.method;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.util.WebUtils;

import com.yansheng.web.servlet.HandlerMapping;
import com.yansheng.web.servlet.handler.AbstractHandlerMethodMapping;
import com.yansheng.web.servlet.mvc.condition.NameValueExpression;
import com.yansheng.web.servlet.mvc.condition.ParamsRequestCondition;

public abstract class RequestMappingInfoHandlerMapping extends AbstractHandlerMethodMapping<RequestMappingInfo> {

	@Override
	protected Set<String> getMappingPathPatterns(RequestMappingInfo mapping) {
		return mapping.getPatternsCondition().getPatterns();
	}

	@Override
	protected RequestMappingInfo getMatchingMapping(RequestMappingInfo mapping, HttpServletRequest request) {
		return mapping.getMatchingCondition(request);
	}

	@Override
	protected Comparator<RequestMappingInfo> getMappingComparator(HttpServletRequest request) {
		return new Comparator<RequestMappingInfo>() {
			@Override
			public int compare(RequestMappingInfo info1, RequestMappingInfo info2) {
				return info1.compareTo(info2, request);
			}
		};
	}

	@Override
	protected void handlerMatch(RequestMappingInfo info, String lookupPath, HttpServletRequest request) {
		super.handlerMatch(info, lookupPath, request);
		Set<String> patterns = info.getPatternsCondition().getPatterns();
		String bestPattern = patterns.isEmpty() ? lookupPath : patterns.iterator().next();
		request.setAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE, bestPattern);

		Map<String, String> uriVariables = getPathMatcher().extractUriTemplateVariables(bestPattern, lookupPath);
		Map<String, String> decodeUriVairables = getUrlPathHelper().decodePathVariables(request, uriVariables);
		request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, decodeUriVairables);

		if (isMatrixVariableContentAvailable()) {
			request.setAttribute(MATRIX_VARIABLES_ATTRIBUTE, extractMatrixVariables(request, uriVariables));
		}

		if (!info.getProducesCondition().getProducibleMediaTypes().isEmpty()) {
			Set<MediaType> mediaTypes = info.getProducesCondition().getProducibleMediaTypes();
			request.setAttribute(PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, mediaTypes);
		}
	}

	private boolean isMatrixVariableContentAvailable() {
		return !getUrlPathHelper().shouldRemoveSemicolonContent();
	}

	private Map<String, MultiValueMap<String, String>> extractMatrixVariables(HttpServletRequest request,
			Map<String, String> uriVariables) {
		Map<String, MultiValueMap<String, String>> result = new LinkedHashMap<String, MultiValueMap<String, String>>();
		for (Entry<String, String> uriVar : uriVariables.entrySet()) {
			String uriVarValue = uriVar.getValue();
			int equalsIndex = uriVarValue.indexOf('=');
			if (equalsIndex == -1) {
				continue;
			}
			String matrixVariables;
			int semicolonIndex = uriVarValue.indexOf(';');
			if ((semicolonIndex == -1) || (semicolonIndex == 0) || (equalsIndex < semicolonIndex)) {
				matrixVariables = uriVarValue;
			} else {
				matrixVariables = uriVarValue.substring(semicolonIndex + 1);
				uriVariables.put(uriVar.getKey(), uriVarValue.substring(0, semicolonIndex));
			}
			MultiValueMap<String, String> vars = WebUtils.parseMatrixVariables(matrixVariables);
			result.put(uriVar.getKey(), getUrlPathHelper().decodeMatrixVariables(request, vars));
		}
		return result;
	}

	@Override
	protected HandlerMethod handlerNoMatch(Set<RequestMappingInfo> requestMappingInfos, String lookupPath,
			HttpServletRequest request) throws ServletException {

		Set<String> allowedMethods = new LinkedHashSet<String>(4);
		Set<RequestMappingInfo> patternMatches = new HashSet<RequestMappingInfo>();
		Set<RequestMappingInfo> patternAndMethodMatches = new HashSet<RequestMappingInfo>();

		for (RequestMappingInfo info : requestMappingInfos) {
			if (info.getPatternsCondition().getMatchingCondition(request) != null) {
				patternMatches.add(info);
				if (info.getMethodsCondition().getMatchingCondition(request) != null) {
					patternAndMethodMatches.add(info);
				} else {
					for (RequestMethod method : info.getMethodsCondition().getMethods()) {
						allowedMethods.add(method.name());
					}
				}
			}
		}
		if (patternMatches.isEmpty()) {
			return null;
		} else if (patternAndMethodMatches.isEmpty() && !allowedMethods.isEmpty()) {
			throw new HttpRequestMethodNotSupportedException(request.getMethod(), allowedMethods);
		}

		Set<MediaType> comsumableMediaTypes;
		Set<MediaType> produciableMediaTypes;
		Set<String> paramConditions;

		if (patternAndMethodMatches.isEmpty()) {
			comsumableMediaTypes = getComsumableMediaTypes(request, patternMatches);
			produciableMediaTypes = getProducibleMediaTypes(request, patternMatches);
			paramConditions = getRequestParams(request, patternMatches);
		} else {
			comsumableMediaTypes = getComsumableMediaTypes(request, patternAndMethodMatches);
			produciableMediaTypes = getProducibleMediaTypes(request, patternAndMethodMatches);
			paramConditions = getRequestParams(request, patternAndMethodMatches);
		}

		if (!comsumableMediaTypes.isEmpty()) {
			MediaType contentType = null;
			if (StringUtils.hasLength(request.getContentType())) {
				try {
					contentType = MediaType.parseMediaType(request.getContentType());
				} catch (IllegalArgumentException ex) {
					throw new HttpMediaTypeNotSupportedException(ex.getMessage());
				}
			}
			throw new HttpMediaTypeNotSupportedException(contentType, new ArrayList<MediaType>(comsumableMediaTypes));
		} else if (!produciableMediaTypes.isEmpty()) {
			throw new HttpMediaTypeNotAcceptableException(new ArrayList<MediaType>(produciableMediaTypes));
		} else if (CollectionUtils.isEmpty(paramConditions)) {
			String[] params = paramConditions.toArray(new String[paramConditions.size()]);
			throw new UnsatisfiedServletRequestParameterException(params, request.getParameterMap());
		} else {
			return null;
		}
	}

	private Set<MediaType> getComsumableMediaTypes(HttpServletRequest request, Set<RequestMappingInfo> partialMatches) {
		Set<MediaType> result = new HashSet<MediaType>();
		for (RequestMappingInfo partialMatch : partialMatches) {
			if (partialMatch.getConsumesCondition().getMatchingCondition(request) == null) {
				result.addAll(partialMatch.getConsumesCondition().getConsumableMediaType());
			}
		}
		return result;
	}

	private Set<MediaType> getProducibleMediaTypes(HttpServletRequest request, Set<RequestMappingInfo> partialMatches) {
		Set<MediaType> result = new HashSet<MediaType>();
		for (RequestMappingInfo partialMatch : partialMatches) {
			if (partialMatch.getProducesCondition().getMatchingCondition(request) == null) {
				result.addAll(partialMatch.getProducesCondition().getProducibleMediaTypes());
			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	private Set<String> getRequestParams(HttpServletRequest request, Set<RequestMappingInfo> partialMatches) {
		for (RequestMappingInfo partialMatch : partialMatches) {
			ParamsRequestCondition condition = partialMatch.getParamsCondition();
			if (!CollectionUtils.isEmpty(condition.getExpressions())
					&& (condition.getMatchingCondition(request) == null)) {
				Set<String> expressions = new HashSet<String>();
				for (NameValueExpression expr : condition.getExpressions()) {
					expressions.add(expr.toString());
				}
				return expressions;
			}
		}
		return null;
	}
}
