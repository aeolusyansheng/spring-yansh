package com.yansheng.beans.factory.support;

import com.yansheng.beans.FatalBeanException;

@SuppressWarnings("serial")
public class BeanDefinitionValidationException extends FatalBeanException {

	public BeanDefinitionValidationException(String msg) {
		super(msg);
	}

	public BeanDefinitionValidationException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
