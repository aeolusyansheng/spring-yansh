package com.yansheng.beans.factory.xml;

import org.w3c.dom.Element;

import com.yansheng.beans.factory.support.AbstractBeanDefinition;
import com.yansheng.beans.factory.support.BeanDefinitionBuilder;

public abstract class AbstractSingleBeanDefinitionParser extends AbstractBeanDefinitionParser {

	@Override
	protected final AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
		String parentName = getParentName(element);
		if (parentName != null) {
			builder.getRowBeanDefinition().setParentName(parentName);
		}
		Class<?> beanClass = getBeanClass(element);
		if (beanClass != null) {
			builder.getRowBeanDefinition().setBeanClass(beanClass);
		} else {
			String beanClassName = getBeanClassName(element);
			if (beanClassName != null) {
				builder.getRowBeanDefinition().setBeanClassName(beanClassName);
			}
		}
		builder.getRowBeanDefinition().setSource(parserContext.extractSource(element));
		if (parserContext.isNested()) {
			builder.setScope(parserContext.getContainingBeanDefinition().getScope());
		}
		if (parserContext.isDefaultLazyInit()) {
			builder.setLazyInit(true);
		}
		doParse(element, parserContext, builder);
		return builder.getBeanDefinition();
	}

	protected String getParentName(Element element) {
		return null;
	}

	protected Class<?> getBeanClass(Element element) {
		return null;
	}

	protected String getBeanClassName(Element element) {
		return null;
	}

	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		doParse(element, builder);
	}

	protected void doParse(Element element, BeanDefinitionBuilder builder) {

	}
}
