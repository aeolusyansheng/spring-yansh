package com.yansheng.beans.factory.xml;

import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.w3c.dom.Element;

public class BeanDefinitionParserDelegate {

	public static final String BEAN_ELEMENT = "bean";

	private final XmlReaderContext readerContext;
	private Environment environment;
	private final DocumentDefaultsDefinition defaults = new DocumentDefaultsDefinition();

	public BeanDefinitionParserDelegate(XmlReaderContext readerContext, Environment environment) {
		Assert.notNull(environment, "Environment不能为null");
		Assert.notNull(readerContext, "XmlReaderContext不能为null");
		this.readerContext = readerContext;
		this.environment = environment;
	}

	public void initDefaults(Element root, BeanDefinitionParserDelegate parent) {
		// TODO
	}

}
