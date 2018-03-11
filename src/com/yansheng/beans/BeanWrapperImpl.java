package com.yansheng.beans;

import java.security.AccessControlContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BeanWrapperImpl extends AbstractPropertyAccessor implements BeanWrapper {

	private static final Log logger = LogFactory.getLog(BeanWrapperImpl.class);
	
	private Object object;
	private String nestedPath = "";
	private Object rootObject;
	
	private AccessControlContext acc;
	
	
}
