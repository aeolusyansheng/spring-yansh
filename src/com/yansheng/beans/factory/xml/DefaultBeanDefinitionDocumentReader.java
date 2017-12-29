package com.yansheng.beans.factory.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.Environment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.yansheng.beans.factory.BeanDefinitionStoreException;

public class DefaultBeanDefinitionDocumentReader implements BeanDefinitionDocumentReader {

	public static final String BEAN_ELEMENT = BeanDefinitionParserDelegate.BEAN_ELEMENT;
	public static final String NESTED_BEANS_ELEMENT = "beans";
	public static final String ALIAS_ELEMENT = "alias";
	public static final String NAME_ELEMENT = "name";
	public static final String ALIAS_ATTRIBUTE = "alias";
	public static final String IMPORT_ELEMENT = "import";
	public static final String RESOURCE_ATTRIBUTE = "resource";
	public static final String PROFILE_ATTRIBUTE = "profile";

	protected final Log logger = LogFactory.getLog(getClass());

	private Environment environment;
	private XmlReaderContext readerContext;
	private BeanDefinitionParserDelegate delegate;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void registerBeanDefinitions(Document doc, XmlReaderContext readerContext)
			throws BeanDefinitionStoreException {

		this.readerContext = readerContext;
		logger.debug("开始加载Bean definitions");
		Element root = doc.getDocumentElement();
		doRegisterBeanDefinitions(root);
	}

	protected void doRegisterBeanDefinitions(Element root) {
		// TODO
	}

	protected BeanDefinitionParserDelegate createHelper(XmlReaderContext readerContext, Element root,
			BeanDefinitionParserDelegate parent) {
		BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegate(readerContext, this.environment);
		delegate.initDefaults(root, parent);
		return delegate;
	}

	protected final XmlReaderContext getReaderContext() {
		return this.readerContext;
	}
	
	protected Object extractSource(Element ele) {
		return this.readerContext.extractSource(ele);
	}
	
	

}
