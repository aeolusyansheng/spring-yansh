package com.yansheng.beans.factory.xml;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.yansheng.beans.factory.BeanDefinitionStoreException;

@SuppressWarnings("serial")
public class XmlBeanDefinitionStoreException extends BeanDefinitionStoreException {

	public XmlBeanDefinitionStoreException(String resourceDescription, String msg, SAXException cause) {
		super(resourceDescription, msg, cause);
	}

	public int getlineNumber() {
		Throwable cause = getCause();
		if (cause instanceof SAXParseException) {
			return ((SAXParseException) cause).getLineNumber();
		}
		return -1;
	}
}
