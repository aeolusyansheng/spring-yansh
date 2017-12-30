package com.yansheng.beans.factory.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.yansheng.beans.factory.config.BeanDefinition;
import com.yansheng.beans.factory.config.BeanDefinitionHolder;

public abstract class NamespaceHandlerSupport implements NamespaceHandler {

	
	
	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BeanDefinitionHolder decorate(Node source, BeanDefinitionHolder definition, ParserContext parserContext) {
		// TODO Auto-generated method stub
		return null;
	}

}
