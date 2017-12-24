package com.yansheng.beans.factory.support;

import com.yansheng.beans.factory.config.BeanDefinition;

@SuppressWarnings("serial")
public class GenericBeanDefinition extends AbstractBeanDefinition {

	private String parentName;

	public GenericBeanDefinition() {
		super();
	}

	public GenericBeanDefinition(BeanDefinition original) {
		super(original);
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getParentName() {
		return this.parentName;
	}

	@Override
	public AbstractBeanDefinition cloneBeanDefinition() {
		return new GenericBeanDefinition(this);
	}

	@Override
	public boolean equals(Object other) {
		return (this == other || (other instanceof GenericBeanDefinition && super.equals(other)));
	}

	@Override
	public String toString() {
		return "Generic bean: " + super.toString();
	}

}
