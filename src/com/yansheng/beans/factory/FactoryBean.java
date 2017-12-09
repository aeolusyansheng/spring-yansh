package com.yansheng.beans.factory;

public interface FactoryBean<T> {

	/**
	 * 通过工厂方法生成对象
	 */
	T getObject() throws Exception;

	Class<?> getObjectType();

	boolean isSingleton();

}
