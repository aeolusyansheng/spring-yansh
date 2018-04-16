package com.yansheng.web.servlet.mvc.condition;

import org.springframework.http.MediaType;

public interface MediaTypeExpression {

    MediaType getMediaType();
    boolean isNegated();
}
