package com.yansheng.web.servlet.mvc.condition;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

public final class HeadersRequestCondition extends AbstractRequestCondition<HeadersRequestCondition> {
    private final Set<HeaderExpression> expressions;

    public HeadersRequestCondition(String... headers) {
        this(parseExpressions(headers));
    }

    public HeadersRequestCondition(Collection<HeaderExpression> conditions) {
        this.expressions = Collections.unmodifiableSet(new LinkedHashSet<HeaderExpression>(conditions));
    }

    private static Collection<HeaderExpression> parseExpressions(String... headers) {
        Set<HeaderExpression> expressions = new LinkedHashSet<HeaderExpression>();
        if (headers != null) {
            for (String header : headers) {
                HeaderExpression expr = new HeaderExpression(header);
                if ("Accept".equalsIgnoreCase(expr.getName()) ||
                        "Content-type".equalsIgnoreCase(expr.getName())) {
                    continue;
                }
                expressions.add(expr);
            }
        }
        return expressions;
    }

    public Set<NameValueExpression<String>> getExpressions() {
        return new LinkedHashSet<NameValueExpression<String>>(this.expressions);
    }

    @Override
    protected Collection<HeaderExpression> getContent() {
        return this.expressions;
    }

    @Override
    protected String getToStringInfix() {
        return " && ";
    }

    @Override
    public HeadersRequestCondition combine(HeadersRequestCondition other) {
        Set<HeaderExpression> set = new LinkedHashSet<HeaderExpression>(this.expressions);
        set.addAll(other.expressions);
        return new HeadersRequestCondition(set);
    }

    @Override
    public HeadersRequestCondition getMatchingCondition(HttpServletRequest request) {
        for (HeaderExpression expression : this.expressions) {
            if (!expression.match(request)) {
                return null;
            }
        }
        return this;
    }

    @Override
    public int compareTo(HeadersRequestCondition other, HttpServletRequest request) {
        return this.expressions.size() - other.expressions.size();
    }

    static class HeaderExpression extends AbstractNameValueExpression<String> {

        HeaderExpression(String expression) {
            super(expression);
        }

        @Override
        protected String parseValue(String valueExpression) {
            return valueExpression;
        }

        @Override
        protected boolean matchName(HttpServletRequest request) {
            return request.getHeader(name) != null;
        }

        @Override
        protected boolean matchValue(HttpServletRequest request) {
            return value.equals(request.getHeader(name));
        }

        @Override
        public int hashCode() {
            int result = name.toLowerCase().hashCode();
            result = 31 * result + (value != null ? value.hashCode() : 0);
            result = 31 * result + (isNegated ? 1 : 0);
            return result;
        }

    }

}
