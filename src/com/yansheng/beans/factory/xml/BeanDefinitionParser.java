package com.yansheng.beans.factory.xml;

import org.w3c.dom.Element;

import com.yansheng.beans.factory.config.BeanDefinition;

public interface BeanDefinitionParser {

	BeanDefinition parse(Element element, ParserContext parseContext);
}
