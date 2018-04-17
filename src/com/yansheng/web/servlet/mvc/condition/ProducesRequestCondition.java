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
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.context.request.ServletWebRequest;

import com.yansheng.web.servlet.mvc.condition.HeadersRequestCondition.HeaderExpression;

public class ProducesRequestCondition extends AbstractRequestCondition<ProducesRequestCondition> {

    private final List<ProduceMediaTypeExpression> expressions;
    private final ContentNegotiationManager contentNegotiationManager;

    public ProducesRequestCondition(String[] produces) {
        this(produces, (String[]) null);
    }

    public ProducesRequestCondition(String[] produces, String[] headers) {
        this(produces, headers, null);
    }

    public ProducesRequestCondition(String[] produces, String[] headers,
            ContentNegotiationManager contentNegotiationManager) {
        this.expressions = new ArrayList<ProduceMediaTypeExpression>(parseExpressions(produces, headers));
        Collections.sort(this.expressions);
        this.contentNegotiationManager = (contentNegotiationManager != null) ? contentNegotiationManager
                : new ContentNegotiationManager();
    }

    private Set<ProduceMediaTypeExpression> parseExpressions(String[] produces, String[] headers) {
        Set<ProduceMediaTypeExpression> result = new LinkedHashSet<ProduceMediaTypeExpression>();
        if (headers != null) {
            {
                for (String header : headers) {
                    HeaderExpression expr = new HeaderExpression(header);
                    if ("Accept".equalsIgnoreCase(expr.name)) {
                        for (MediaType mediaType : MediaType.parseMediaTypes(expr.value)) {
                            result.add(new ProduceMediaTypeExpression(mediaType, expr.isNegated));
                        }
                    }
                }
            }
        }
        if (produces != null) {
            for (String produce : produces) {
                result.add(new ProduceMediaTypeExpression(produce));
            }
        }
        return result;
    }

    private ProducesRequestCondition(Collection<ProduceMediaTypeExpression> expressions,
            ContentNegotiationManager contentNegotiationManager) {
        this.expressions = new ArrayList<ProduceMediaTypeExpression>(expressions);
        Collections.sort(this.expressions);
        this.contentNegotiationManager = (contentNegotiationManager != null) ? contentNegotiationManager
                : new ContentNegotiationManager();
    }

    public Set<MediaTypeExpression> getExpressions() {
        return new LinkedHashSet<MediaTypeExpression>(this.expressions);
    }

    public Set<MediaType> getProducibleMediaTypes() {
        Set<MediaType> result = new LinkedHashSet<MediaType>();
        for (ProduceMediaTypeExpression exression : this.expressions) {
            if (!exression.isNegated()) {
                result.add(exression.getMediaType());
            }
        }
        return result;
    }

    public boolean isEmpty() {
        return this.expressions.isEmpty();
    }

    @Override
    protected List<ProduceMediaTypeExpression> getContent() {
        return this.expressions;
    }

    @Override
    protected String getToStringInfix() {
        return " || ";
    }

    @Override
    public ProducesRequestCondition combine(ProducesRequestCondition other) {
        return !other.expressions.isEmpty() ? other : this;
    }

    @Override
    public ProducesRequestCondition getMatchingCondition(HttpServletRequest request) {
        if (isEmpty()) {
            return this;
        }
        Set<ProduceMediaTypeExpression> result = new LinkedHashSet<ProduceMediaTypeExpression>(this.expressions);
        for (Iterator<ProduceMediaTypeExpression> it = result.iterator(); it.hasNext();) {
            ProduceMediaTypeExpression expression = it.next();
            if (!expression.match(request)) {
                it.remove();
            }
        }
        return (result.isEmpty()) ? null : new ProducesRequestCondition(result, this.contentNegotiationManager);
    }

    @Override
    public int compareTo(ProducesRequestCondition other, HttpServletRequest request) {
        // TODO Auto-generated method stub
        return 0;
    }

    private List<MediaType> getAcceptedMediaTypes(HttpServletRequest request)
            throws HttpMediaTypeException {
        List<MediaType> mediaTypes = this.contentNegotiationManager.resolveMediaTypes(new ServletWebRequest(request));
        return mediaTypes.isEmpty() ? Collections.singletonList(MediaType.ALL) : mediaTypes;
    }

    class ProduceMediaTypeExpression extends AbstractMediaTypeExpression {

        ProduceMediaTypeExpression(MediaType mediaType, boolean isNegated) {
            super(mediaType, isNegated);
        }

        ProduceMediaTypeExpression(String expression) {
            super(expression);
        }

        @Override
        protected boolean matchMediaType(HttpServletRequest request) throws HttpMediaTypeException {
            List<MediaType> acceptedMediaTypes = getAcceptedMediaTypes(request);
            for (MediaType mediaType : acceptedMediaTypes) {
                if (getMediaType().isCompatibleWith(mediaType)) {
                    return true;
                }
            }
            return false;
        }
    }

}
