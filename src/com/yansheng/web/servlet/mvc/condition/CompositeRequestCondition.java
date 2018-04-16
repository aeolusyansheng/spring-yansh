package com.yansheng.web.servlet.mvc.condition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

public class CompositeRequestCondition extends AbstractRequestCondition<CompositeRequestCondition> {

    private final RequestConditionHolder[] requestConditions;

    public CompositeRequestCondition(RequestCondition<?>... requestConditions) {
        this.requestConditions = wrap(requestConditions);
    }

    private RequestConditionHolder[] wrap(RequestCondition<?>... rowConditions) {
        RequestConditionHolder[] wrappedConditions = new RequestConditionHolder[rowConditions.length];
        for (int i = 0; i < rowConditions.length; i++) {
            wrappedConditions[i] = new RequestConditionHolder(rowConditions[i]);
        }
        return wrappedConditions;
    }

    private CompositeRequestCondition(RequestConditionHolder[] requestConditions) {
        this.requestConditions = requestConditions;
    }

    public boolean isEmpty() {
        return ObjectUtils.isEmpty(this.requestConditions);
    }

    public List<RequestCondition<?>> getConditions() {
        return unWrap();
    }

    private List<RequestCondition<?>> unWrap() {
        List<RequestCondition<?>> result = new ArrayList<RequestCondition<?>>();
        for (RequestConditionHolder holder : this.requestConditions) {
            result.add(holder.getCondition());
        }
        return result;
    }

    @Override
    protected Collection<?> getContent() {
        return (isEmpty()) ? Collections.emptyList() : getConditions();
    }

    @Override
    protected String getToStringInfix() {
        return " && ";
    }

    private int getLength() {
        return this.requestConditions.length;
    }

    @Override
    public CompositeRequestCondition combine(CompositeRequestCondition other) {
        if (isEmpty() && other.isEmpty()) {
            return this;
        } else if (isEmpty()) {
            return other;
        } else if (other.isEmpty()) {
            return this;
        } else {
            assertNumberOfConditions(other);
            RequestConditionHolder[] combinedConditions = new RequestConditionHolder[getLength()];
            for (int i = 0; i < getLength(); i++) {
                combinedConditions[i] = this.requestConditions[i].combine(other.requestConditions[i]);
            }
            return new CompositeRequestCondition(combinedConditions);
        }
    }

    private void assertNumberOfConditions(CompositeRequestCondition other) {
        Assert.isTrue(getLength() == other.getLength(),
                "Cannot combine CompositeRequestConditions with a different number of conditions. "
                        + this.requestConditions + " and  " + other.requestConditions);
    }

    @Override
    public CompositeRequestCondition getMatchingCondition(HttpServletRequest request) {
        if (isEmpty()) {
            return this;
        }
        RequestConditionHolder[] matchingConditions = new RequestConditionHolder[getLength()];
        for (int i = 0; i < getLength(); i++) {
            matchingConditions[i] = this.requestConditions[i].getMatchingCondition(request);
            if (matchingConditions[i] == null) {
                return null;
            }
        }
        return new CompositeRequestCondition(matchingConditions);
    }

    @Override
    public int compareTo(CompositeRequestCondition other, HttpServletRequest request) {
        if (isEmpty() && other.isEmpty()) {
            return 0;
        } else if (isEmpty()) {
            return 1;
        } else if (other.isEmpty()) {
            return -1;
        } else {
            assertNumberOfConditions(other);
            for (int i = 0; i < getLength(); i++) {
                int result = this.requestConditions[i].compareTo(other.requestConditions[i], request);
                if (result != 0) {
                    return result;
                }
            }
            return 0;
        }
    }

}
