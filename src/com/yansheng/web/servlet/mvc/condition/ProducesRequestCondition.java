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
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.context.request.ServletWebRequest;

import com.yansheng.web.servlet.mvc.condition.HeadersRequestCondition.HeaderExpression;

public final class ProducesRequestCondition extends AbstractRequestCondition<ProducesRequestCondition> {

    private final List<ProduceMediaTypeExpression> expressions;
    private final ContentNegotiationManager contentNegotiationManager;

    public ProducesRequestCondition(String... produces) {
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
        try {
            List<MediaType> acceptedMediaTypes = getAcceptedMediaTypes(request);
            for (MediaType acceptedMediaType : acceptedMediaTypes) {
                int thisIndex = indexOfEqualMediaType(acceptedMediaType);
                int otherIndex = indexOfEqualMediaType(acceptedMediaType);
                int result = compareMatchingMediaTypes(this, thisIndex, other, otherIndex);
                if (result != 0) {
                    return result;
                }
                thisIndex = indexOfIncludedMediaType(acceptedMediaType);
                otherIndex = indexOfIncludedMediaType(acceptedMediaType);
                result = compareMatchingMediaTypes(this, thisIndex, other, otherIndex);
                if (result != 0) {
                    return result;
                }
            }
            return 0;
        } catch (HttpMediaTypeNotAcceptableException e) {
            throw new IllegalStateException("Cannot compare without having any requested media types");
        }
    }

    private List<MediaType> getAcceptedMediaTypes(HttpServletRequest request)
            throws HttpMediaTypeNotAcceptableException {
        List<MediaType> mediaTypes = this.contentNegotiationManager.resolveMediaTypes(new ServletWebRequest(request));
        return mediaTypes.isEmpty() ? Collections.singletonList(MediaType.ALL) : mediaTypes;
    }

    private int indexOfEqualMediaType(MediaType mediaType) {
        for (int i = 0; i < getExpressionsToCompare().size(); i++) {
            MediaType currentMediaType = getExpressionsToCompare().get(i).getMediaType();
            if (mediaType.getType().equalsIgnoreCase(currentMediaType.getType())
                    && mediaType.getSubtype().equalsIgnoreCase(currentMediaType.getSubtype())) {
                return i;
            }
        }
        return -1;
    }

    private int indexOfIncludedMediaType(MediaType mediaType) {
        for (int i = 0; i < getExpressionsToCompare().size(); i++) {
            if (mediaType.includes(getExpressionsToCompare().get(i).getMediaType())) {
                return i;
            }
        }
        return -1;
    }

    private int compareMatchingMediaTypes(ProducesRequestCondition condition1, int index1,
            ProducesRequestCondition condition2, int index2) {
        int result = 0;
        if (index1 != index2) {
            return index1 - index2;
        } else if (index1 != -1 && index2 != -1) {
            ProduceMediaTypeExpression expre1 = condition1.getExpressionsToCompare().get(index1);
            ProduceMediaTypeExpression expre2 = condition2.getExpressionsToCompare().get(index2);
            result = expre1.compareTo(expre2);
            result = (result != 0) ? result : expre1.getMediaType().compareTo(expre2.getMediaType());
        }
        return result;
    }

    private List<ProduceMediaTypeExpression> getExpressionsToCompare() {
        return this.expressions.isEmpty() ? MEDIA_TYPE_ALL_LIST : this.expressions;
    }

    private final List<ProduceMediaTypeExpression> MEDIA_TYPE_ALL_LIST = Collections
            .singletonList(new ProduceMediaTypeExpression("*/*"));

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
