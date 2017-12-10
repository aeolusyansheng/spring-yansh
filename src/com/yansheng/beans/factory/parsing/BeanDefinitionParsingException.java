package com.yansheng.beans.factory.parsing;

import com.yansheng.beans.factory.BeanDefinitionStoreException;

@SuppressWarnings("serial")
public class BeanDefinitionParsingException extends BeanDefinitionStoreException {

	public BeanDefinitionParsingException(Problem problem) {
		super(problem.getResourceDescription(), problem.toString(), problem.getRootCause());
	}
}
