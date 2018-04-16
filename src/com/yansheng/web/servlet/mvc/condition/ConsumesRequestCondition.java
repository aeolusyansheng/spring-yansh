package com.yansheng.web.servlet.mvc.condition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpMediaTypeNotSupportedException;

import com.yansheng.web.servlet.mvc.condition.HeadersRequestCondition.HeaderExpression;

public class ConsumesRequestCondition extends AbstractRequestCondition<ConsumesRequestCondition> {

	private final List<ComsumeMediaTypeExpression> expressions;

	public ConsumesRequestCondition(String... consumes) {
		this(consumes, null);
	}

	public ConsumesRequestCondition(String[] consumes, String[] headers) {
		this(parseExpressions(consumes, headers));
	}

	public ConsumesRequestCondition(Collection<ComsumeMediaTypeExpression> expressions) {
		this.expressions = new ArrayList<ComsumeMediaTypeExpression>(expressions);
		Collections.sort(this.expressions);
	}

	private static Set<ComsumeMediaTypeExpression> parseExpressions(String[] consumes, String[] headers) {
		Set<ComsumeMediaTypeExpression> result = new LinkedHashSet<ComsumeMediaTypeExpression>();
		if (headers != null) {
			for (String header : headers) {
				HeaderExpression expr = new HeaderExpression(header);
				if ("Content-Type".equalsIgnoreCase(expr.name)) {
					for (MediaType mediaType : MediaType.parseMediaTypes(expr.value)) {
						result.add(new ComsumeMediaTypeExpression(mediaType, expr.isNegated));
					}
				}
			}
		}
		if (consumes != null) {
			for (String consume : consumes) {
				result.add(new ComsumeMediaTypeExpression(consume));
			}
		}
		return result;
	}

	public Set<MediaTypeExpression> getExpressions() {
		return new LinkedHashSet<MediaTypeExpression>(this.expressions);
	}

	public Set<MediaType> getConsumableMediaType() {
		Set<MediaType> result = new LinkedHashSet<MediaType>();
		for (ComsumeMediaTypeExpression expression : this.expressions) {
			if (!expression.isNegated()) {
				result.add(expression.getMediaType());
			}
		}
		return result;
	}

	public boolean isEmpty() {
		return this.expressions.isEmpty();
	}

	@Override
	protected Collection<ComsumeMediaTypeExpression> getContent() {
		return this.expressions;
	}

	@Override
	protected String getToStringInfix() {
		return " || ";
	}

	@Override
	public ConsumesRequestCondition combine(ConsumesRequestCondition other) {
		return !other.expressions.isEmpty() ? other : this;
	}

	@Override
	public ConsumesRequestCondition getMatchingCondition(HttpServletRequest request) {
		if (isEmpty()) {
			return this;
		}
		Set<ComsumeMediaTypeExpression> result = new LinkedHashSet<ComsumeMediaTypeExpression>(this.expressions);
		for (Iterator<ComsumeMediaTypeExpression> it = result.iterator(); it.hasNext();) {
			ComsumeMediaTypeExpression expression = it.next();
			if (!expression.match(request)) {
				it.remove();
			}
		}
		return (result.isEmpty()) ? null : new ConsumesRequestCondition(result);
	}

	@Override
	public int compareTo(ConsumesRequestCondition other, HttpServletRequest request) {
		if (this.expressions.isEmpty() && other.expressions.isEmpty()) {
			return 0;
		} else if (this.expressions.isEmpty()) {
			return 1;
		} else if (other.expressions.isEmpty()) {
			return -1;
		} else {
			return this.expressions.get(0).compareTo(other.expressions.get(0));
		}
	}

	static class ComsumeMediaTypeExpression extends AbstractMediaTypeExpression {

		ComsumeMediaTypeExpression(String expression) {
			super(expression);
		}

		ComsumeMediaTypeExpression(MediaType mediaType, boolean negated) {
			super(mediaType, negated);
		}

		@Override
		protected boolean matchMediaType(HttpServletRequest request) throws HttpMediaTypeNotSupportedException {
			try {
				MediaType contentType = StringUtils.hasLength(request.getContentType())
						? MediaType.parseMediaType(request.getContentType())
						: MediaType.APPLICATION_OCTET_STREAM;
				return getMediaType().includes(contentType);
			} catch (IllegalArgumentException ex) {
				throw new HttpMediaTypeNotSupportedException(
						"Can't parse Content-Type [" + request.getContentType() + "]: " + ex.getMessage());
			}
		}
	}
}
