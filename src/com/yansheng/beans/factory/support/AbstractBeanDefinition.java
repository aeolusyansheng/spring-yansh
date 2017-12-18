package com.yansheng.beans.factory.support;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.support.MethodOverrides;
import org.springframework.core.io.DescriptiveResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

import com.yansheng.beans.BeanMetadataAttributeAccessor;
import com.yansheng.beans.MutablePropertyValues;
import com.yansheng.beans.factory.config.AutowireCapableBeanFactory;
import com.yansheng.beans.factory.config.BeanDefinition;
import com.yansheng.beans.factory.config.ConstructorArgumentValues;

@SuppressWarnings("serial")
public abstract class AbstractBeanDefinition extends BeanMetadataAttributeAccessor
		implements BeanDefinition, Cloneable {

	public static final String SCOPE_DEFAULT = "";
	public static final int AUTOWIRE_NO = AutowireCapableBeanFactory.AUTOWIRE_NO;
	public static final int AUTOWIRE_BY_NAME = AutowireCapableBeanFactory.AUTOWIRE_BY_NAME;
	public static final int AUTOWIRE_BY_TYPE = AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE;
	public static final int AUTOWIRE_CONSTRUCTOR = AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR;

	public static final int DEPENDENCY_CHECK_NONE = 0;
	public static final int DEPENDENCY_CHECK_OBJECTS = 1;
	public static final int DEPENDENCY_CHECK_SIMPLE = 2;
	public static final int DEPENDENCY_CHECK_ALL = 3;

	public static final String INFER_METHOD = "(inferred)";

	private volatile Object beanClass;
	private String scope = SCOPE_DEFAULT;
	private boolean singleton = true;
	private boolean prototype = false;
	private boolean abstractFlag = false;
	private boolean lazyInit = false;
	private int autowireMode = AUTOWIRE_NO;
	private int dependencyCheck = DEPENDENCY_CHECK_NONE;
	private String[] dependsOn;
	private boolean autowireCandidate = true;
	private boolean primary = false;

	private final Map<String, AutowireCandidateQualifier> qualifiers = new LinkedHashMap<String, AutowireCandidateQualifier>(
			0);
	private boolean nonPublicAccessAllowed = true;
	private boolean lenientConstructorResolution = true;
	private ConstructorArgumentValues constructorArgumentValues;
	private MutablePropertyValues propertyValues;
	private MethodOverrides methodOverrides = new MethodOverrides();

	private String factoryBeanName;
	private String factoryMethodName;
	private String initMethodName;
	private String destroyMethodName;
	private boolean enforceInitMethod = true;
	private boolean enforceDestroyMethod = true;
	private boolean synthetic = false;
	private int role = BeanDefinition.ROLE_APPLICATION;
	private String description;
	private Resource resource;

	public boolean hasBeanClass() {
		return (this.beanClass instanceof Class);
	}

	public void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
	}

	public Class<?> getBeanClass() throws IllegalStateException {
		Object beanClassObject = this.beanClass;
		if (beanClassObject == null) {
			throw new IllegalStateException("bean definition中还没有指定bean class。");
		}
		if (!(beanClassObject instanceof Class)) {
			throw new IllegalStateException("Bean class 名 [" + beanClassObject + "] 不是一个类（class）。");
		}
		return (Class<?>) beanClassObject;
	}

	public void setBeanClassName(String beanClassName) {
		this.beanClass = beanClassName;
	}

	public String getBeanClassName() {
		Object beanClassObject = this.beanClass;
		if (beanClassObject instanceof Class) {
			return ((Class<?>) beanClassObject).getName();
		} else {
			return (String) beanClassObject;
		}
	}

	public Class<?> resolveBeanClass(ClassLoader classLoader) throws ClassNotFoundException {
		String className = getBeanClassName();
		if (className == null) {
			return null;
		}
		Class<?> resolveClass = ClassUtils.forName(className, classLoader);
		this.beanClass = resolveClass;
		return resolveClass;
	}

	public void setScope(String scope) {
		this.scope = scope;
		this.singleton = SCOPE_SINGLETON.equals(scope) || SCOPE_DEFAULT.equals(scope);
		this.prototype = SCOPE_PROPOTYPE.equals(scope);
	}

	public String getScope() {
		return this.scope;
	}

	public boolean isSingleton() {
		return this.singleton;
	}

	public boolean isPrototype() {
		return this.prototype;
	}

	public void setAbstract(boolean abstractFlag) {
		this.abstractFlag = abstractFlag;
	}

	public boolean isAbstract() {
		return this.abstractFlag;
	}

	public void setLazyInit(boolean lazyInit) {
		this.lazyInit = lazyInit;
	}

	public boolean isLazyInit() {
		return this.lazyInit;
	}

	public void setAutowireMode(int autowireMode) {
		this.autowireMode = autowireMode;
	}

	public int getAutowireMode() {
		return this.autowireMode;
	}

	public void setDependencyCheck(int dependencyCheck) {
		this.dependencyCheck = dependencyCheck;
	}

	public int getDependencyCheck() {
		return this.dependencyCheck;
	}

	public void setDependsOn(String[] dependsOn) {
		this.dependsOn = dependsOn;
	}

	public String[] getDependsOn() {
		return this.dependsOn;
	}

	public void setAutowireCandidate(boolean autowireCandidate) {
		this.autowireCandidate = autowireCandidate;
	}

	public boolean isAutowireCandidate() {
		return this.autowireCandidate;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	public boolean isPrimary() {
		return this.primary;
	}

	public void addQualifier(AutowireCandidateQualifier qualifier) {
		this.qualifiers.put(qualifier.getTypeName(), qualifier);
	}

	public boolean hasQualifier(String typeName) {
		return this.qualifiers.keySet().contains(typeName);
	}

	public AutowireCandidateQualifier getQualifier(String typeName) {
		return this.qualifiers.get(typeName);
	}

	public Set<AutowireCandidateQualifier> getQualifiers() {
		// new 一个新HashSet
		return new LinkedHashSet<AutowireCandidateQualifier>(this.qualifiers.values());
	}

	public void copyQualifiersFrom(AbstractBeanDefinition source) {
		this.qualifiers.putAll(source.qualifiers);
	}

	public void setNonPublicAccessAllowed(boolean nonPublicAccessAllowed) {
		this.nonPublicAccessAllowed = nonPublicAccessAllowed;
	}

	public boolean isNonPublicAccessAllowed() {
		return this.nonPublicAccessAllowed;
	}

	public void setLenientConstructorResolution(boolean lenientConstructorResolution) {
		this.lenientConstructorResolution = lenientConstructorResolution;
	}

	public boolean isLenientConstructorResolution() {
		return this.lenientConstructorResolution;
	}

	public void setConstructorArgumentValues(ConstructorArgumentValues constructorArgumentValues) {
		this.constructorArgumentValues = (constructorArgumentValues != null ? constructorArgumentValues
				: new ConstructorArgumentValues());
	}

	public ConstructorArgumentValues getConstructorArgumentValues() {
		return this.constructorArgumentValues;
	}

	public boolean hasConstructorArgumentValues() {
		return (this.constructorArgumentValues != null ? (!this.constructorArgumentValues.isEmpty()) : false);
	}

	public void setPropertyValues(MutablePropertyValues propertyvalues) {
		this.propertyValues = (propertyvalues != null ? propertyvalues : new MutablePropertyValues());
	}

	public MutablePropertyValues getPropertyValues() {
		return this.propertyValues;
	}

	public void setMethodOverrides(MethodOverrides methodOverrides) {
		this.methodOverrides = (methodOverrides != null ? methodOverrides : new MethodOverrides());
	}

	public MethodOverrides getMethodOverrides() {
		return this.methodOverrides;
	}

	public void setFactoryBeanName(String factoryBeanName) {
		this.factoryBeanName = factoryBeanName;
	}

	public String getFactoryBeanName() {
		return this.factoryBeanName;
	}

	public void setFactoryMethodName(String factoryMethodName) {
		this.factoryMethodName = factoryMethodName;
	}

	public String getFactoryMethodName() {
		return this.factoryMethodName;
	}

	public void setInitMethodName(String initMethodName) {
		this.initMethodName = initMethodName;
	}

	public String getInitMethodName() {
		return this.initMethodName;
	}

	public void setEnforceInitMethod(boolean enforceInitMethod) {
		this.enforceInitMethod = enforceInitMethod;
	}

	public boolean isEnforceInitMethod() {
		return this.enforceInitMethod;
	}

	public void setDestroyMethodName(String destroyMethodName) {
		this.destroyMethodName = destroyMethodName;
	}

	public String getDestroyMethodName() {
		return this.destroyMethodName;
	}

	public void setEnforceDestroyMethod(boolean enforceDestroyMethod) {
		this.enforceDestroyMethod = enforceDestroyMethod;
	}

	public boolean isEnforceDestroyMethod() {
		return this.enforceDestroyMethod;
	}

	public void setSynthetic(boolean synthetic) {
		this.synthetic = synthetic;
	}

	public boolean isSynthetic() {
		return this.synthetic;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getRole() {
		return this.role;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Resource getResource() {
		return this.resource;
	}

	public void setReourceDescription(String reourceDescription) {
		this.resource = new DescriptiveResource(reourceDescription);
	}

	public String getReourceDescription() {
		return (this.resource != null ? this.resource.getDescription() : null);
	}

	public void setOriginatingBeanDefinition(BeanDefinition originatingBd) {
		this.resource = new BeanDefinitionResource(originatingBd);
	}

	public BeanDefinition getOriginatingBeanDefinition() {
		return (this.resource instanceof BeanDefinitionResource
				? ((BeanDefinitionResource) this.resource).getBeanDefinition()
				: null);
	}
}
