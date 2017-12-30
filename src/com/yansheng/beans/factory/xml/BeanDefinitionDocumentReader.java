package com.yansheng.beans.factory.xml;

import org.springframework.core.env.Environment;
import org.w3c.dom.Document;

import com.yansheng.beans.factory.BeanDefinitionStoreException;

public interface BeanDefinitionDocumentReader {

	void setEnvironment(Environment environment);

	void registerBeanDefinitions(Document doc, XmlReaderContext readerContext) throws BeanDefinitionStoreException;
}
