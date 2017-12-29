package com.yansheng.beans.factory.parsing;

import java.util.ArrayList;
import java.util.List;

import com.yansheng.beans.PropertyValue;
import com.yansheng.beans.PropertyValues;
import com.yansheng.beans.factory.config.BeanDefinition;
import com.yansheng.beans.factory.config.BeanDefinitionHolder;
import com.yansheng.beans.factory.config.BeanReference;

public class BeanComponentDefinition extends BeanDefinitionHolder implements ComponentDefinition {

	private BeanDefinition[] innerBeanDefinitions;
	private BeanReference[] beanReferences;

	public BeanComponentDefinition(BeanDefinition beanDefinition, String beanName) {
		super(beanDefinition, beanName);
		findInnerBeanDefinitionsAndBeanReferences(beanDefinition);
	}

	public BeanComponentDefinition(BeanDefinition beanDefinition, String beanName, String[] aliases) {
		super(beanDefinition, beanName, aliases);
		findInnerBeanDefinitionsAndBeanReferences(beanDefinition);
	}

	public BeanComponentDefinition(BeanDefinitionHolder beanDefinitionHolder) {
		super(beanDefinitionHolder);
		findInnerBeanDefinitionsAndBeanReferences(beanDefinitionHolder.getBeanDefinition());
	}

	private void findInnerBeanDefinitionsAndBeanReferences(BeanDefinition beanDefinition) {
		List<BeanDefinition> innerBeans = new ArrayList<BeanDefinition>();
		List<BeanReference> references = new ArrayList<BeanReference>();
		PropertyValues pvs = beanDefinition.getPropertyValues();
		for (PropertyValue pv : pvs.getPropertyValues()) {
			Object value = pv.getValue();
			if (value instanceof BeanDefinitionHolder) {
				innerBeans.add(((BeanDefinitionHolder) value).getBeanDefinition());
			} else if (value instanceof BeanDefinition) {
				innerBeans.add((BeanDefinition) value);
			} else if (value instanceof BeanReference) {
				references.add((BeanReference) value);
			}
		}
		this.innerBeanDefinitions = innerBeans.toArray(new BeanDefinition[innerBeans.size()]);
		this.beanReferences = references.toArray(new BeanReference[references.size()]);
	}

	@Override
	public String getName() {
		return getBeanName();
	}

	@Override
	public String getDescription() {
		return getShortDescription();
	}

	@Override
	public BeanDefinition[] getBeanDefinitions() {
		return new BeanDefinition[] { getBeanDefinition() };
	}

	@Override
	public BeanDefinition[] getInnerBeanDefinitions() {
		return this.innerBeanDefinitions;
	}

	@Override
	public BeanReference[] getBeanReferences() {
		return this.beanReferences;
	}

	@Override
	public String toString() {
		return getDescription();
	}

	@Override
	public boolean equals(Object other) {
		return (this == other || (other instanceof BeanComponentDefinition && super.equals(other)));
	}
}
