package com.yansheng.web.servlet.mvc.method.annotation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringValueResolver;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yansheng.web.servlet.mvc.condition.ConsumesRequestCondition;
import com.yansheng.web.servlet.mvc.condition.HeadersRequestCondition;
import com.yansheng.web.servlet.mvc.condition.ParamsRequestCondition;
import com.yansheng.web.servlet.mvc.condition.PatternsRequestCondition;
import com.yansheng.web.servlet.mvc.condition.ProducesRequestCondition;
import com.yansheng.web.servlet.mvc.condition.RequestCondition;
import com.yansheng.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import com.yansheng.web.servlet.mvc.method.RequestMappingInfo;
import com.yansheng.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

public class RequestMappingHandlerMapping extends RequestMappingInfoHandlerMapping
		implements EmbeddedValueResolverAware {

	private boolean useSuffixPatternMatch = true;
	private boolean useRegisteredSuffixPatternMatch = false;
	private boolean useTrailingSlashMatch = true;
	private ContentNegotiationManager contentNegotiationManager = new ContentNegotiationManager();
	private final List<String> fileExtensions = new ArrayList<String>();
	private StringValueResolver embeddedValueResolver;

	public void setUseSuffixPatternMatch(boolean useSuffixPatternMatch) {
		this.useSuffixPatternMatch = useSuffixPatternMatch;
	}

	public void setUseRegisteredSuffixPatternMatch(boolean useRegsiteredSuffixPatternMatch) {
		this.useRegisteredSuffixPatternMatch = useRegsiteredSuffixPatternMatch;
		this.useSuffixPatternMatch = useRegsiteredSuffixPatternMatch ? true : this.useSuffixPatternMatch;
	}

	public void setUseTrailingSlashMatch(boolean useTrailingSlashMatch) {
		this.useTrailingSlashMatch = useTrailingSlashMatch;
	}

	@Override
	public void setEmbeddedValueResolver(StringValueResolver resolver) {
		this.embeddedValueResolver = resolver;
	}

	public void setContentNegotiationManager(ContentNegotiationManager contentNegotiationManager) {
		Assert.notNull(contentNegotiationManager);
		this.contentNegotiationManager = contentNegotiationManager;
	}

	public boolean useSuffixPatternMatch() {
		return this.useSuffixPatternMatch;
	}

	public boolean useRegisteredSuffixPatternMatch() {
		return useRegisteredSuffixPatternMatch;
	}

	public boolean useTrailingSlashMatch() {
		return this.useTrailingSlashMatch;
	}

	public ContentNegotiationManager getContentNegotiationManager() {
		return this.contentNegotiationManager;
	}

	public List<String> getFileExtensions() {
		return this.fileExtensions;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (this.useRegisteredSuffixPatternMatch) {
			this.fileExtensions.addAll(this.contentNegotiationManager.getAllFileExtensions());
		}
		super.afterPropertiesSet();
	}

	@Override
	protected boolean isHandler(Class<?> beanType) {
		return ((AnnotationUtils.findAnnotation(beanType, Controller.class) != null)
				|| (AnnotationUtils.findAnnotation(beanType, RequestMapping.class) != null));
	}

	@Override
	protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
		RequestMappingInfo info = null;
		RequestMapping methodAnnotation = AnnotationUtils.findAnnotation(method, RequestMapping.class);
		if (methodAnnotation != null) {
			RequestCondition<?> methodCondition = getCustomMethodCondition(method);
			info = createRequestMappingInfo(methodAnnotation, methodCondition);
			RequestMapping typeAnnotation = AnnotationUtils.findAnnotation(handlerType, RequestMapping.class);
			if (typeAnnotation != null) {
				RequestCondition<?> typeCondition = getCustomTypeCondition(handlerType);
				info = createRequestMappingInfo(typeAnnotation, typeCondition).combine(info);
			}
		}
		return info;
	}

	protected RequestCondition<?> getCustomMethodCondition(Method method) {
		return null;
	}

	protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
		return null;
	}

	private RequestMappingInfo createRequestMappingInfo(RequestMapping annotation,
			RequestCondition<?> customCondidtion) {
		String[] patterns = resolveEmbeddedValuesInPatterns(annotation.value());
		return new RequestMappingInfo(
				new PatternsRequestCondition(patterns, getUrlPathHelper(), getPathMatcher(), this.useSuffixPatternMatch,
						this.useTrailingSlashMatch, this.fileExtensions),
				new RequestMethodsRequestCondition(annotation.method()),
				new ParamsRequestCondition(annotation.params()), new HeadersRequestCondition(annotation.headers()),
				new ConsumesRequestCondition(annotation.consumes(), annotation.headers()), new ProducesRequestCondition(
						annotation.produces(), annotation.headers(), getContentNegotiationManager()),
				customCondidtion);
	}

	protected String[] resolveEmbeddedValuesInPatterns(String[] patterns) {
		if (this.embeddedValueResolver == null) {
			return patterns;
		} else {
			String[] resolveredPatterns = new String[patterns.length];
			for (int i = 0; i < patterns.length; i++) {
				resolveredPatterns[i] = this.embeddedValueResolver.resolveStringValue(patterns[i]);
			}
			return resolveredPatterns;
		}
	}

}
