package com.yansheng.beans.factory.xml;

import org.w3c.dom.Node;

import com.yansheng.beans.factory.config.BeanDefinitionHolder;

public interface BeanDefinitionDecorator {

	BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parseContext);
}
