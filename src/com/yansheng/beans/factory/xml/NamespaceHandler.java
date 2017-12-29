package com.yansheng.beans.factory.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.yansheng.beans.factory.config.BeanDefinition;
import com.yansheng.beans.factory.config.BeanDefinitionHolder;

public interface NamespaceHandler {

	void init();

	BeanDefinition parse(Element element, ParserContext parserContext);

	BeanDefinitionHolder decorate(Node source, BeanDefinitionHolder definition, ParserContext parserContext);
}
