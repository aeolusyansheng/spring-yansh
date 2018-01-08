package com.yansheng.beans.factory.xml;

import org.springframework.core.Conventions;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import com.yansheng.beans.factory.support.BeanDefinitionBuilder;

public abstract class AbstractSimpleBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		NamedNodeMap attributes = element.getAttributes();
		for (int i = 0; i < attributes.getLength(); i++) {
			Attr attribute = (Attr) attributes.item(i);
			if (isEligibleAttribute(attribute, parserContext)) {
				// "id"意外的属性
				String propertyName = extractPropertyName(attribute.getLocalName());
				Assert.state(StringUtils.hasText(propertyName), "无效的属性名，转成驼峰后属性名为空。");
				builder.addPropertyValue(propertyName, attribute.getValue());
			}
		}
		postProcess(builder, element);
	}

	protected boolean isEligibleAttribute(Attr attribute, ParserContext parserContext) {
		boolean eligible = isEligibleAttribute(attribute);
		if (!eligible) {
			String fullName = attribute.getName();
			eligible = (!fullName.equals("xmlns") && !fullName.startsWith("xmlns:")
					&& isEligibleAttribute(parserContext.getDelegate().getLocalName(attribute)));
		}
		return eligible;
	}

	protected boolean isEligibleAttribute(Attr attribute) {
		return false;
	}

	protected boolean isEligibleAttribute(String attributeName) {
		return !ID_ATTRIBUTE.equals(attributeName);
	}

	protected String extractPropertyName(String attributeName) {
		// 转成驼峰型
		return Conventions.attributeNameToPropertyName(attributeName);
	}

	protected void postProcess(BeanDefinitionBuilder beanDefinition, Element element) {

	}
}
