package com.yansheng.beans.factory;

import com.yansheng.beans.FatalBeanException;

@SuppressWarnings("serial")
public class FactoryBeanNotInitializedException extends FatalBeanException {

	public FactoryBeanNotInitializedException() {
		super("工厂bean尚未初始化。");
	}

	public FactoryBeanNotInitializedException(String msg) {
		super(msg);
	}
}
