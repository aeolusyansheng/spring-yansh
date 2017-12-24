package com.yansheng.beans.factory.support;

import org.springframework.util.ObjectUtils;

import com.yansheng.beans.MutablePropertyValues;
import com.yansheng.beans.factory.config.ConstructorArgumentValues;

@SuppressWarnings("serial")
public class ChildBeanDefinition extends AbstractBeanDefinition {

	private String parentName;

	public ChildBeanDefinition(String parentName) {
		super();
		this.parentName = parentName;
	}

	public ChildBeanDefinition(String parentName, MutablePropertyValues pvs) {
		super(null, pvs);
		this.parentName = parentName;
	}

	public ChildBeanDefinition(String parentName, ConstructorArgumentValues cargs, MutablePropertyValues pvs) {
		super(cargs, pvs);
		this.parentName = parentName;
	}

	public ChildBeanDefinition(ChildBeanDefinition original) {
		super(original);
	}

	public ChildBeanDefinition(String parentName, Class<?> beanClass, ConstructorArgumentValues cargs,
			MutablePropertyValues pvs) {
		super(cargs, pvs);
		this.parentName = parentName;
		setBeanClass(beanClass);
	}

	public ChildBeanDefinition(String parentName, String beanClassName, ConstructorArgumentValues cargs,
			MutablePropertyValues pvs) {
		super(cargs, pvs);
		this.parentName = parentName;
		setBeanClassName(beanClassName);
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getParentName() {
		return this.parentName;
	}

	@Override
	public void validate() throws BeanDefinitionValidationException {
		super.validate();
		if (this.parentName == null) {
			throw new BeanDefinitionValidationException("ChildBeanDefinition 必须设定parentName属性。");
		}
	}

	@Override
	public AbstractBeanDefinition cloneBeanDefinition() {
		return new ChildBeanDefinition(this);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ChildBeanDefinition)) {
			return false;
		}
		ChildBeanDefinition that = (ChildBeanDefinition) other;
		return (ObjectUtils.nullSafeEquals(this.parentName, that.parentName) && super.equals(other));
	}

	@Override
	public int hashCode() {
		return ObjectUtils.nullSafeHashCode(this.parentName) * 29 + super.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Child bean with parent '");
		sb.append(this.parentName).append("': ").append(super.toString());
		return sb.toString();
	}

}
