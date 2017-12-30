package com.yansheng.beans;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

public abstract class BeanUtils {

	private static final Log logger = LogFactory.getLog(BeanUtils.class);

	private static final Map<Class<?>, Boolean> unknownEditorTypes = Collections
			.synchronizedMap(new WeakHashMap<Class<?>, Boolean>());

	/**
	 * 利用无参数的构造函数创建对象
	 */
	public static <T> T instantiate(Class<T> clazz) throws BeanInstantiationException {
		Assert.notNull(clazz, "Class 不能为null");
		if (clazz.isInterface()) {
			throw new BeanInstantiationException(clazz, "指定的类是一个接口。");
		}
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new BeanInstantiationException(clazz, "指定的类可能是抽象类。", e);
		} catch (IllegalAccessException e) {
			throw new BeanInstantiationException(clazz, "无法访问构造方法。", e);
		}
	}

	public static <T> T instantiateClass(Class<T> clazz) throws BeanInstantiationException {
		Assert.notNull(clazz, "Class 不能为null");
		if (clazz.isInterface()) {
			throw new BeanInstantiationException(clazz, "指定的类是一个接口。");
		}
		try {
			return instantiateClass(clazz.getDeclaredConstructor());
		} catch (NoSuchMethodException e) {
			throw new BeanInstantiationException(clazz, "找不到默认的构造函数。");
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T instantiateClass(Class<?> clazz, Class<T> assignableTo) throws BeanInstantiationException {
		Assert.isAssignable(assignableTo, clazz);
		return (T) instantiateClass(clazz);
	}

	public static <T> T instantiateClass(Constructor<T> ctor, Object... args) throws BeanInstantiationException {
		Assert.notNull(ctor, "Constructor 不能为null");
		ReflectionUtils.makeAccessible(ctor);
		try {
			return ctor.newInstance(args);
		} catch (InstantiationException e) {
			throw new BeanInstantiationException(ctor.getDeclaringClass(), "可能是抽象类。", e);
		} catch (IllegalAccessException e) {
			throw new BeanInstantiationException(ctor.getDeclaringClass(), "无法访问构造函数。", e);
		} catch (IllegalArgumentException e) {
			throw new BeanInstantiationException(ctor.getDeclaringClass(), "访问构造函数参数无效。", e);
		} catch (InvocationTargetException e) {
			throw new BeanInstantiationException(ctor.getDeclaringClass(), "访问构造函数内部发生异常。", e.getTargetException());
		}
	}

	public static Method findMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
		try {
			return clazz.getMethod(methodName, paramTypes);
		} catch (NoSuchMethodException e) {
			return findDeclaredMethod(clazz, methodName, paramTypes);
		}
	}

	public static Method findDeclaredMethod(Class<?> clazz, String methodName, Class<?>[] paramTypes) {
		try {
			return clazz.getDeclaredMethod(methodName, paramTypes);
		} catch (NoSuchMethodException e) {
			// 在父类中找
			if (clazz.getSuperclass() != null) {
				return findDeclaredMethod(clazz.getSuperclass(), methodName, paramTypes);
			}
			return null;
		}
	}

	public static Method findMethodWithMinimalParamaters(Class<?> clazz, String methodName)
			throws IllegalArgumentException {
		Method targetMethod = findMethodWithMinimalParameters(clazz.getDeclaredMethods(), methodName);
		if (targetMethod == null) {
			targetMethod = findDeclaredMethodWithMinimalParamaters(clazz.getSuperclass(), methodName);
		}
		return targetMethod;
	}

	public static Method findDeclaredMethodWithMinimalParamaters(Class<?> clazz, String methodName)
			throws IllegalArgumentException {
		Method targetMethod = findMethodWithMinimalParameters(clazz.getDeclaredMethods(), methodName);
		if (targetMethod == null && clazz.getSuperclass() != null) {
			// 从父类中找
			targetMethod = findDeclaredMethodWithMinimalParamaters(clazz.getSuperclass(), methodName);
		}
		return targetMethod;
	}

	/**
	 * 指定方法名，寻找参数最少的方法。找到多个时，error。
	 */
	public static Method findMethodWithMinimalParameters(Method[] methods, String methodName) {
		Method targetMethod = null;
		int numMethodsFoundWithCurrentMininumArgs = 0;
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				int numParam = method.getParameterTypes().length;
				if (targetMethod == null || numParam < targetMethod.getParameterTypes().length) {
					targetMethod = method;
					numMethodsFoundWithCurrentMininumArgs = 1;
				} else {
					if (numParam == targetMethod.getParameterTypes().length) {
						numMethodsFoundWithCurrentMininumArgs++;
					}
				}
			}
		}
		if (numMethodsFoundWithCurrentMininumArgs > 1) {
			throw new IllegalArgumentException("无法根据方法名" + methodName + "找到唯一的方法。已经尝试从重载方法中寻找最少参数的方法，但是发现了"
					+ numMethodsFoundWithCurrentMininumArgs + "个。");
		}
		return targetMethod;
	}

}
