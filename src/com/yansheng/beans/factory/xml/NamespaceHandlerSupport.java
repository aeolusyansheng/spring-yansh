package com.yansheng.beans.factory.xml;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.yansheng.beans.factory.config.BeanDefinition;
import com.yansheng.beans.factory.config.BeanDefinitionHolder;

public abstract class NamespaceHandlerSupport implements NamespaceHandler {

	private final Map<String, BeanDefinitionParser> parsers = new HashMap<String, BeanDefinitionParser>();

	private final Map<String, BeanDefinitionDecorator> decorators = new HashMap<String, BeanDefinitionDecorator>();

	private final Map<String, BeanDefinitionDecorator> attributeDecorators = new HashMap<String, BeanDefinitionDecorator>();

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		return findParserForElement(element, parserContext).parse(element, parserContext);
	}

	private BeanDefinitionParser findParserForElement(Element element, ParserContext parserContext) {
		String localName = parserContext.getDelegate().getLocalName(element);
		BeanDefinitionParser parser = this.parsers.get(localName);
		if (parser == null) {
			parserContext.getReaderContext().fatal("无法定位标签" + localName + "相应的BeanDefinitionParser", element);
		}
		return parser;
	}

	@Override
	public BeanDefinitionHolder decorate(Node source, BeanDefinitionHolder definition, ParserContext parserContext) {
		return findDecoratorForNode(source, parserContext).decorate(source, definition, parserContext);
	}

	private BeanDefinitionDecorator findDecoratorForNode(Node node, ParserContext parserContext) {
		BeanDefinitionDecorator decorator = null;
		String localName = parserContext.getDelegate().getLocalName(node);
		if (node instanceof Element) {
			decorator = this.decorators.get(localName);
		} else if (node instanceof Attr) {
			decorator = this.attributeDecorators.get(localName);
		} else {
			parserContext.getReaderContext().fatal("无法找到node" + node.getNodeName() + "对应的BeanDefinitionDecorator",
					node);
		}
		if (decorator == null) {
			parserContext.getReaderContext().fatal("无法定位标签" + localName + "相应的BeanDefinitionDecorator", node);
		}
		return decorator;
	}

	protected final void registerBeanDefinitionParser(String elementName, BeanDefinitionParser parser) {
		// 一种标签一个BeanDefinitionParser
		this.parsers.put(elementName, parser);
	}

	protected final void registerBeanDefinitionDecorator(String elementName, BeanDefinitionDecorator decorator) {
		this.decorators.put(elementName, decorator);
	}

	protected final void registerBeanDefinitionDecoratorForAttribute(String elementName,
			BeanDefinitionDecorator decorator) {
		this.attributeDecorators.put(elementName, decorator);
	}
}
