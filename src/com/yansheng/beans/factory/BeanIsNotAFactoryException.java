package com.yansheng.beans.factory;

@SuppressWarnings("serial")
public class BeanIsNotAFactoryException extends BeanNotOfRequiedTypeException {

	@SuppressWarnings("rawtypes")
	public BeanIsNotAFactoryException(String name, Class actualType) {
		super(name, FactoryBean.class, actualType);
	}

}
