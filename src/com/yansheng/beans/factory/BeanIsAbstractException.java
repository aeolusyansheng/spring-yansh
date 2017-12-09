package com.yansheng.beans.factory;

@SuppressWarnings("serial")
public class BeanIsAbstractException extends BeanCreationException {

	public BeanIsAbstractException(String beanName) {
		super(beanName, "该bean是抽象类");
	}
}
