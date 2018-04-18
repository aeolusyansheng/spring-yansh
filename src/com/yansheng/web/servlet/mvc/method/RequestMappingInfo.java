package com.yansheng.web.servlet.mvc.method;

import javax.servlet.http.HttpServletRequest;

import com.yansheng.web.servlet.mvc.condition.ConsumesRequestCondition;
import com.yansheng.web.servlet.mvc.condition.HeadersRequestCondition;
import com.yansheng.web.servlet.mvc.condition.ParamsRequestCondition;
import com.yansheng.web.servlet.mvc.condition.PatternsRequestCondition;
import com.yansheng.web.servlet.mvc.condition.ProducesRequestCondition;
import com.yansheng.web.servlet.mvc.condition.RequestCondition;
import com.yansheng.web.servlet.mvc.condition.RequestConditionHolder;
import com.yansheng.web.servlet.mvc.condition.RequestMethodsRequestCondition;

public final class RequestMappingInfo implements RequestCondition<RequestMappingInfo> {

    private final PatternsRequestCondition patternsCondition;
    private final RequestMethodsRequestCondition methodsCondition;
    private final ParamsRequestCondition paramsCondition;
    private final HeadersRequestCondition headersCondition;
    private final ConsumesRequestCondition consumesCondition;
    private final ProducesRequestCondition producesCondition;
    private final RequestConditionHolder customConditionHolder;
    private int hash;

    public RequestMappingInfo(PatternsRequestCondition patterns,
            RequestMethodsRequestCondition methods,
            ParamsRequestCondition params,
            HeadersRequestCondition headers,
            ConsumesRequestCondition consumes,
            ProducesRequestCondition produces,
            RequestCondition<?> custom) {
        this.patternsCondition = patterns != null ? patterns : new PatternsRequestCondition();
        this.methodsCondition = methods != null ? methods : new RequestMethodsRequestCondition();
        this.paramsCondition = params != null ? params : new ParamsRequestCondition();
        this.headersCondition = headers != null ? headers : new HeadersRequestCondition();
        this.consumesCondition = consumes != null ? consumes : new ConsumesRequestCondition();
        this.producesCondition = produces != null ? produces : new ProducesRequestCondition();
        this.customConditionHolder = new RequestConditionHolder(custom);
    }

    public RequestMappingInfo(RequestMappingInfo info, RequestCondition<?> customRequestCondition) {
        this(info.patternsCondition, info.methodsCondition, info.paramsCondition, info.headersCondition,
                info.consumesCondition, info.producesCondition, customRequestCondition);
    }

    public PatternsRequestCondition getPatternsCondition() {
        return patternsCondition;
    }

    public RequestMethodsRequestCondition getMethodsCondition() {
        return methodsCondition;
    }

    public ParamsRequestCondition getParamsCondition() {
        return paramsCondition;
    }

    public HeadersRequestCondition getHeadersCondition() {
        return headersCondition;
    }

    public ConsumesRequestCondition getConsumesCondition() {
        return consumesCondition;
    }

    public ProducesRequestCondition getProducesCondition() {
        return producesCondition;
    }

    public RequestCondition<?> getCustomCondition() {
        return customConditionHolder.getCondition();
    }

    @Override
    public RequestMappingInfo combine(RequestMappingInfo other) {
        PatternsRequestCondition patterns = this.patternsCondition.combine(other.patternsCondition);
        RequestMethodsRequestCondition methods = this.methodsCondition.combine(other.methodsCondition);
        ParamsRequestCondition params = this.paramsCondition.combine(other.paramsCondition);
        HeadersRequestCondition headers = this.headersCondition.combine(other.headersCondition);
        ConsumesRequestCondition consumes = this.consumesCondition.combine(other.consumesCondition);
        ProducesRequestCondition produces = this.producesCondition.combine(other.producesCondition);
        RequestConditionHolder custom = this.customConditionHolder.combine(other.customConditionHolder);
        return new RequestMappingInfo(patterns, methods, params, headers, consumes, produces, custom.getCondition());
    }

    @Override
    public RequestMappingInfo getMatchingCondition(HttpServletRequest request) {
        RequestMethodsRequestCondition methods = methodsCondition.getMatchingCondition(request);
        ParamsRequestCondition params = paramsCondition.getMatchingCondition(request);
        HeadersRequestCondition headers = headersCondition.getMatchingCondition(request);
        ConsumesRequestCondition consumes = consumesCondition.getMatchingCondition(request);
        ProducesRequestCondition produces = producesCondition.getMatchingCondition(request);
        if (methods == null || params == null || headers == null || consumes == null || produces == null) {
            return null;
        }

        PatternsRequestCondition patterns = patternsCondition.getMatchingCondition(request);
        if (patterns == null) {
            return null;
        }

        RequestConditionHolder custom = customConditionHolder.getMatchingCondition(request);
        if (custom == null) {
            return null;
        }
        return new RequestMappingInfo(patterns, methods, params, headers, consumes, produces, custom.getCondition());
    }

    @Override
    public int compareTo(RequestMappingInfo other, HttpServletRequest request) {
        int result = patternsCondition.compareTo(other.getPatternsCondition(), request);
        if (result != 0) {
            return result;
        }
        result = paramsCondition.compareTo(other.getParamsCondition(), request);
        if (result != 0) {
            return result;
        }
        result = headersCondition.compareTo(other.getHeadersCondition(), request);
        if (result != 0) {
            return result;
        }
        result = consumesCondition.compareTo(other.getConsumesCondition(), request);
        if (result != 0) {
            return result;
        }
        result = producesCondition.compareTo(other.getProducesCondition(), request);
        if (result != 0) {
            return result;
        }
        result = methodsCondition.compareTo(other.getMethodsCondition(), request);
        if (result != 0) {
            return result;
        }
        result = customConditionHolder.compareTo(other.customConditionHolder, request);
        if (result != 0) {
            return result;
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && obj instanceof RequestMappingInfo) {
            RequestMappingInfo other = (RequestMappingInfo) obj;
            return (this.patternsCondition.equals(other.patternsCondition) &&
                    this.methodsCondition.equals(other.methodsCondition) &&
                    this.paramsCondition.equals(other.paramsCondition) &&
                    this.headersCondition.equals(other.headersCondition) &&
                    this.consumesCondition.equals(other.consumesCondition) &&
                    this.producesCondition.equals(other.producesCondition) &&
                    this.customConditionHolder.equals(other.customConditionHolder));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = hash;
        if (result == 0) {
            result = patternsCondition.hashCode();
            result = 31 * result + methodsCondition.hashCode();
            result = 31 * result + paramsCondition.hashCode();
            result = 31 * result + headersCondition.hashCode();
            result = 31 * result + consumesCondition.hashCode();
            result = 31 * result + producesCondition.hashCode();
            result = 31 * result + customConditionHolder.hashCode();
            hash = result;
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        builder.append(patternsCondition);
        builder.append(",methods=").append(methodsCondition);
        builder.append(",params=").append(paramsCondition);
        builder.append(",headers=").append(headersCondition);
        builder.append(",consumes=").append(consumesCondition);
        builder.append(",produces=").append(producesCondition);
        builder.append(",custom=").append(customConditionHolder);
        builder.append('}');
        return builder.toString();
    }
}
