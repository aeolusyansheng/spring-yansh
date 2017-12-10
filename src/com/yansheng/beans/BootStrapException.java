package com.yansheng.beans;

@SuppressWarnings("serial")
public class BootStrapException extends FatalBeanException {

	public BootStrapException(String msg) {
		super(msg);
	}

	public BootStrapException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
