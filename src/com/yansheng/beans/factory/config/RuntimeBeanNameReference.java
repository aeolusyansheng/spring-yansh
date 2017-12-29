package com.yansheng.beans.factory.config;

import org.springframework.util.Assert;

public class RuntimeBeanNameReference implements BeanReference {

	private String beanName;
	private Object source;

	public RuntimeBeanNameReference(String beanName) {
		Assert.hasText(beanName, "bean name  不能为空。");
		this.beanName = beanName;
	}

	@Override
	public String getBeanName() {
		return this.beanName;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	@Override
	public Object getSource() {
		return this.source;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RuntimeBeanNameReference)) {
			return false;
		}
		RuntimeBeanNameReference another = (RuntimeBeanNameReference) other;
		return this.beanName.equals(another.beanName);
	}

	@Override
	public int hashCode() {
		return this.beanName.hashCode();
	}

	@Override
	public String toString() {
		return '<' + getBeanName() + '>';
	}

}
