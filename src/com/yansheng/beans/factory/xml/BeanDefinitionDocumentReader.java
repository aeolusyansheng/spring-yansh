package com.yansheng.beans.factory.xml;

import org.springframework.core.env.Environment;
import org.w3c.dom.Document;

public interface BeanDefinitionDocumentReader {

	void setEnvironment(Environment environment);
	//void registerBeanDefinitions(Document doc,)
}
