package com.yansheng.beans.factory.support;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.Assert;

import com.yansheng.beans.MutablePropertyValues;
import com.yansheng.beans.factory.config.BeanDefinition;
import com.yansheng.beans.factory.config.BeanDefinitionHolder;
import com.yansheng.beans.factory.config.ConstructorArgumentValues;

@SuppressWarnings("serial")
public class RootBeanDefinition extends AbstractBeanDefinition {

	private final Map<Member, Boolean> externallyManagedConfigMembers = new ConcurrentHashMap<Member, Boolean>(0);
	private final Map<String, Boolean> externallyManagedInitMethods = new ConcurrentHashMap<String, Boolean>(0);
	private final Map<String, Boolean> externallyManagedDestroyMethods = new ConcurrentHashMap<String, Boolean>(0);
	private BeanDefinitionHolder decoratedDefinition;
	boolean allowCaching = true;
	private volatile Class<?> targetType;
	boolean isFactoryMethodUnique = false;

	final Object constructorArgumentLock = new Object();
	Object resolvedConstructorOrFactoryMethod;
	boolean constructorArgumentsResolved = false;
	Object[] resolvedConstructorArguments;
	Object[] preparedConstructorArguments;

	final Object postProcessingLock = new Object();
	boolean postProcessed = false;
	volatile Boolean beforeInstantiationResolved;

	public RootBeanDefinition() {
		super();
	}

	public RootBeanDefinition(Class<?> beanClass) {
		super();
		setBeanClass(beanClass);
	}

	public RootBeanDefinition(Class<?> beanClass, int autowireMode, boolean dependencyCheck) {
		super();
		setAutowireMode(autowireMode);
		setBeanClass(beanClass);
		if (dependencyCheck && getResolvedAutowireMode() != AUTOWIRE_CONSTRUCTOR) {
			setDependencyCheck(DEPENDENCY_CHECK_OBJECTS);
		}
	}

	public RootBeanDefinition(Class<?> beanClass, ConstructorArgumentValues cargs, MutablePropertyValues pvs) {
		super(cargs, pvs);
		setBeanClass(beanClass);
	}

	public RootBeanDefinition(String beanClassName) {
		setBeanClassName(beanClassName);
	}

	public RootBeanDefinition(String beanClassName, ConstructorArgumentValues cargs, MutablePropertyValues pvs) {
		super(cargs, pvs);
		setBeanClassName(beanClassName);
	}

	public RootBeanDefinition(RootBeanDefinition original) {
		super(original);
		this.decoratedDefinition = original.decoratedDefinition;
		this.allowCaching = original.allowCaching;
		this.targetType = original.targetType;
		this.isFactoryMethodUnique = original.isFactoryMethodUnique;
	}

	public RootBeanDefinition(BeanDefinition original) {
		super(original);
	}

	@Override
	public String getParentName() {
		return null;
	}

	@Override
	public void setParentName(String parentName) {
		if (parentName != null) {
			throw new IllegalStateException("RootBeanDefinition不能设定parentName。");
		}
	}

	public void setTargetType(Class<?> targetType) {
		this.targetType = targetType;
	}

	public Class<?> getTargetType() {
		return this.targetType;
	}

	public void setUniqueFactoryMethodName(String name) {
		Assert.hasText(name, "Factory method name 不能为空。");
		setFactoryMethodName(name);
		this.isFactoryMethodUnique = true;
	}

	public boolean isFactoryMethod(Method candidate) {
		return (candidate != null && (candidate.getName().equals(getFactoryMethodName())));
	}

	public Method getResolvedFactoryMethod() {
		synchronized (this.constructorArgumentLock) {
			Object candidate = this.resolvedConstructorOrFactoryMethod;
			return (candidate instanceof Method ? (Method) candidate : null);
		}
	}

	public void registerExternallyManagedConfigMember(Member configMember) {
		this.externallyManagedConfigMembers.put(configMember, Boolean.TRUE);
	}

	public boolean isExternallyManagedConfigMember(Member configMember) {
		return this.externallyManagedConfigMembers.containsKey(configMember);
	}

	public void registerExternallyManagedInitMethod(String initMethod) {
		this.externallyManagedInitMethods.put(initMethod, Boolean.TRUE);
	}

	public boolean isExternallyManagedInitMethod(String initMethod) {
		return this.externallyManagedInitMethods.containsKey(initMethod);
	}

	public void registerExternallyManagedDestroyMethod(String destroyMethod) {
		this.externallyManagedDestroyMethods.put(destroyMethod, Boolean.TRUE);
	}

	public boolean isExternallyManagedDestroyMethod(String destroyMethod) {
		return this.externallyManagedDestroyMethods.containsKey(destroyMethod);
	}

	public void setDecoratedDefinition(BeanDefinitionHolder decoratedDefinition) {
		this.decoratedDefinition = decoratedDefinition;
	}

	public BeanDefinitionHolder getDecoratedDefinition() {
		return this.decoratedDefinition;
	}

	@Override
	public RootBeanDefinition cloneBeanDefinition() {
		return new RootBeanDefinition(this);
	}

	@Override
	public boolean equals(Object other) {
		return (this == other || (other instanceof RootBeanDefinition && super.equals(other)));
	}

	@Override
	public String toString() {
		return "Root bean: " + super.toString();
	}
}
