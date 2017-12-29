package com.yansheng.beans.factory.xml;

import org.springframework.core.Constants;
import org.springframework.core.io.Resource;
import org.springframework.util.xml.XmlValidationModeDetector;

import com.yansheng.beans.factory.BeanDefinitionStoreException;
import com.yansheng.beans.factory.support.AbstractBeanDefinitonReader;
import com.yansheng.beans.factory.support.BeanDefinitionRegistry;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitonReader {

	public static final int VALIDATION_NONE = XmlValidationModeDetector.VALIDATION_NONE;
	public static final int VALIDATION_AUTO = XmlValidationModeDetector.VALIDATION_AUTO;
	public static final int VALIDATION_DTD = XmlValidationModeDetector.VALIDATION_DTD;
	public static final int VALIDATION_XSD = XmlValidationModeDetector.VALIDATION_XSD;

	private static final Constants constants = new Constants(XmlBeanDefinitionReader.class);
	
	private int validationMode = VALIDATION_AUTO;
	private boolean namespaceAware = false;
	

	protected XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
		super(registry);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int loadBeanDefinition(Resource resource) throws BeanDefinitionStoreException {
		// TODO Auto-generated method stub
		return 0;
	}

}
