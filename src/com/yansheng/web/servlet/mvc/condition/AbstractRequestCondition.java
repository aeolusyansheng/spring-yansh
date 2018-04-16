package com.yansheng.web.servlet.mvc.condition;

import java.util.Collection;
import java.util.Iterator;

public abstract class AbstractRequestCondition<T extends AbstractRequestCondition<T>> implements RequestCondition<T> {

    protected abstract Collection<?> getContent();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o != null && getClass().equals(o.getClass())) {
            AbstractRequestCondition<?> other = (AbstractRequestCondition<?>) o;
            return getContent().equals(other.getContent());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getContent().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");
        for (Iterator<?> iterator = getContent().iterator(); iterator.hasNext();) {
            Object expression = iterator.next();
            builder.append(expression.toString());
            if (iterator.hasNext()) {
                builder.append(getToStringInfix());
            }
        }
        builder.append("]");
        return builder.toString();
    }

    protected abstract String getToStringInfix();

}
