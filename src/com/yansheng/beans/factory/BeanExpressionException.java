package com.yansheng.beans.factory;

import com.yansheng.beans.FatalBeanException;

@SuppressWarnings("serial")
public class BeanExpressionException extends FatalBeanException {

	public BeanExpressionException(String msg) {
		super(msg);
	}

	public BeanExpressionException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
