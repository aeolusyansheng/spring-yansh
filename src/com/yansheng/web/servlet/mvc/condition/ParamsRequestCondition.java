package com.yansheng.web.servlet.mvc.condition;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.WebUtils;

public final class ParamsRequestCondition extends AbstractRequestCondition<ParamsRequestCondition> {

	private final Set<paramExpression> expressions;

	public ParamsRequestCondition(String... expressions) {
		this(parseExressions(expressions));
	}

	public ParamsRequestCondition(Collection<paramExpression> expressions) {
		this.expressions = Collections.unmodifiableSet(new LinkedHashSet<paramExpression>(expressions));
	}

	private static Collection<paramExpression> parseExressions(String... params) {
		Set<paramExpression> expressions = new LinkedHashSet<paramExpression>();
		for (String param : params) {
			paramExpression expression = new paramExpression(param);
			expressions.add(expression);
		}
		return expressions;
	}

	public Set<NameValueExpression<String>> getExpressions() {
		return new LinkedHashSet<NameValueExpression<String>>(this.expressions);
	}

	@Override
	protected Collection<?> getContent() {
		return this.expressions;
	}

	@Override
	protected String getToStringInfix() {
		return " && ";
	}

	@Override
	public ParamsRequestCondition combine(ParamsRequestCondition other) {
		Set<paramExpression> set = new LinkedHashSet<paramExpression>(this.expressions);
		set.addAll(other.expressions);
		return new ParamsRequestCondition(set);
	}

	@Override
	public ParamsRequestCondition getMatchingCondition(HttpServletRequest request) {
		for (paramExpression expression : this.expressions) {
			if (!expression.match(request)) {
				return null;
			}
		}
		return this;
	}

	@Override
	public int compareTo(ParamsRequestCondition other, HttpServletRequest request) {
		return this.expressions.size() - other.expressions.size();
	}

	static class paramExpression extends AbstractNameValueExpression<String> {

		paramExpression(String expression) {
			super(expression);
		}

		@Override
		protected String parseValue(String valueExpression) {
			return valueExpression;
		}

		@Override
		protected boolean matchName(HttpServletRequest request) {
			return WebUtils.hasSubmitParameter(request, name);
		}

		@Override
		protected boolean matchValue(HttpServletRequest request) {
			return value.equals(request.getParameter(name));
		}

	}

}
