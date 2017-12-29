package com.yansheng.beans.factory.parsing;

import com.yansheng.beans.factory.config.BeanDefinition;
import com.yansheng.beans.factory.config.BeanReference;

public abstract class AbstractComponentDefinition implements ComponentDefinition {

	@Override
	public String getDescription() {
		return getName();
	}

	@Override
	public BeanDefinition[] getBeanDefinitions() {
		return new BeanDefinition[0];
	}

	@Override
	public BeanDefinition[] getInnerBeanDefinitions() {
		return new BeanDefinition[0];
	}

	@Override
	public BeanReference[] getBeanReferences() {
		return new BeanReference[0];
	}

}
