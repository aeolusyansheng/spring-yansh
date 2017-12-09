package com.yansheng.beans.factory;

import org.springframework.util.ClassUtils;

@SuppressWarnings("serial")
public class UnsatisfiedDependencyException extends BeanCreationException {

	public UnsatisfiedDependencyException(String resourceDescription, String beanName, String propertyName,
			String msg) {
		super(resourceDescription, beanName, "bean中含有不满足依赖条件的属性:" + propertyName + (msg != null ? msg : ""));
	}

	public UnsatisfiedDependencyException(String resourceDescription, String beanName, String propertyName,
			Throwable ex) {
		this(resourceDescription, beanName, propertyName, (ex != null ? ex.getMessage() : ""));
		initCause(ex);
	}

	@SuppressWarnings("rawtypes")
	public UnsatisfiedDependencyException(String resourceDescription, String beanName, int ctorArgIndex,
			Class ctorArgType, String msg) {
		super(resourceDescription, beanName, "bean中含有不满足依赖条件的构造函数参数。参数位置:" + ctorArgIndex + " 参数类型:"
				+ ClassUtils.getQualifiedName(ctorArgType) + (msg != null ? msg : ""));
	}

	@SuppressWarnings("rawtypes")
	public UnsatisfiedDependencyException(String resourceDescription, String beanName, int ctorArgIndex,
			Class ctorArgType, Throwable ex) {
		this(resourceDescription, beanName, ctorArgIndex, ctorArgType, (ex != null ? ex.getMessage() : ""));
		initCause(ex);
	}

}
