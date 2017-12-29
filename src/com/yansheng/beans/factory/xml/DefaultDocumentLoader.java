package com.yansheng.beans.factory.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.xml.XmlValidationModeDetector;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

public class DefaultDocumentLoader implements DocumentLoader {

	private static final String SCHEMA_LANGUAGE_ATTRIBUTE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	private static final String XSD_SCHEMA_LANGUAGE = "http://www.w3.org/2001/XMLSchema";

	private static final Log logger = LogFactory.getLog(DefaultDocumentLoader.class);

	@Override
	public Document loadDocument(InputSource inputSource, EntityResolver entityResolver, ErrorHandler errorHandler,
			int validationMode, boolean namespaceAware) throws Exception {
		DocumentBuilderFactory factory = createDocumentBuilderFactory(validationMode, namespaceAware);
		if (logger.isDebugEnabled()) {
			logger.debug("使用JAXP provider：" + factory.getClass().getName());
		}
		DocumentBuilder docBuilder = createDocumentBuilder(factory, entityResolver, errorHandler);
		return docBuilder.parse(inputSource);
	}

	protected DocumentBuilderFactory createDocumentBuilderFactory(int validationMode, boolean namespceAware)
			throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(namespceAware);
		if (validationMode != XmlValidationModeDetector.VALIDATION_NONE) {
			factory.setValidating(true);
			if (validationMode == XmlValidationModeDetector.VALIDATION_XSD) {
				factory.setNamespaceAware(true);
				try {
					factory.setAttribute(SCHEMA_LANGUAGE_ATTRIBUTE, XSD_SCHEMA_LANGUAGE);
				} catch (IllegalArgumentException e) {
					ParserConfigurationException pcex = new ParserConfigurationException(
							"无法使用XSD方式验证：你的JAXP不支持XML schema，确认JDK的版本是否为1.4以下。");
					pcex.initCause(e);
					throw pcex;
				}
			}
		}
		return factory;
	}

	protected DocumentBuilder createDocumentBuilder(DocumentBuilderFactory factory, EntityResolver entityResolver,
			ErrorHandler errorHandler) throws ParserConfigurationException {
		DocumentBuilder docBuilder = factory.newDocumentBuilder();
		if (entityResolver != null) {
			docBuilder.setEntityResolver(entityResolver);
		}
		if (errorHandler != null) {
			docBuilder.setErrorHandler(errorHandler);
		}
		return docBuilder;
	}
}
