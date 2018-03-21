package com.yansheng.beans.factory.config;

import com.yansheng.beans.BeansException;

public interface BeanExpressionResolver {
	Object evaluate(String value, BeanExpressionContext evalContext) throws BeansException;
}
