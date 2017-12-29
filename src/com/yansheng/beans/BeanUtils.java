package com.yansheng.beans;

public abstract class BeanUtils {

	public static <T> T instantiateClass(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException();
		} catch (IllegalAccessException e) {
			throw new RuntimeException();
		}
	}
}
