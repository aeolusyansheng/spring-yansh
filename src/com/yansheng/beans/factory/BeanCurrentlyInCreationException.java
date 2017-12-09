package com.yansheng.beans.factory;

@SuppressWarnings("serial")
public class BeanCurrentlyInCreationException extends BeanCreationException {

	public BeanCurrentlyInCreationException(String beanName) {
		super(beanName, "要求的bean当前正处于创建中:请确认该bean是否处于循环参照中？");
	}

	public BeanCurrentlyInCreationException(String beanName, String msg) {
		super(beanName, msg);
	}
}
