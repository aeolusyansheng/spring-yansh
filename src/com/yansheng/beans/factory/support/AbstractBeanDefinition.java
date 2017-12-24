package com.yansheng.beans.factory.support;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.core.io.DescriptiveResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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

	protected AbstractBeanDefinition() {
		this(null, null);
	}

	protected AbstractBeanDefinition(ConstructorArgumentValues cargs, MutablePropertyValues pvs) {
		setConstructorArgumentValues(cargs);
		setPropertyValues(pvs);
	}

	protected AbstractBeanDefinition(AbstractBeanDefinition original) {
		this((BeanDefinition) original);
	}

	protected AbstractBeanDefinition(BeanDefinition original) {
		setParentName(original.getParentName());
		setBeanClassName(original.getBeanClassName());
		setFactoryBeanName(original.getFactoryBeanName());
		setFactoryMethodName(original.getFactoryMethodName());
		setScope(original.getScope());
		setAbstract(original.isAbstract());
		setLazyInit(original.isLazyInit());
		setRole(original.getRole());
		setConstructorArgumentValues(new ConstructorArgumentValues(original.getConstructorArgumentValues()));
		setPropertyValues(new MutablePropertyValues(original.getPropertyValues()));
		setSource(original.getSource());
		copyAttributesFrom(original);

		if (original instanceof AbstractBeanDefinition) {
			AbstractBeanDefinition originalAbd = (AbstractBeanDefinition) original;
			if (originalAbd.hasBeanClass()) {
				setBeanClass(originalAbd.getBeanClass());
			}
			setAutowireMode(originalAbd.getAutowireMode());
			setDependencyCheck(originalAbd.getDependencyCheck());
			setDependsOn(originalAbd.getDependsOn());
			setAutowireCandidate(originalAbd.isAutowireCandidate());
			copyQualifiersFrom(originalAbd);
			setPrimary(originalAbd.isPrimary());
			setNonPublicAccessAllowed(originalAbd.isNonPublicAccessAllowed());
			setLenientConstructorResolution(originalAbd.isLenientConstructorResolution());
			setInitMethodName(originalAbd.getInitMethodName());
			setEnforceInitMethod(originalAbd.isEnforceInitMethod());
			setDestroyMethodName(originalAbd.getDestroyMethodName());
			setEnforceDestroyMethod(originalAbd.isEnforceDestroyMethod());
			setMethodOverrides(new MethodOverrides(originalAbd.getMethodOverrides()));
			setSynthetic(originalAbd.isSynthetic());
			setResource(originalAbd.getResource());
		} else {
			setResourceDescription(original.getResourceDescription());
		}
	}

	public void overrideFrom(BeanDefinition other) {

		// merge bean definition
		if (StringUtils.hasLength(other.getBeanClassName())) {
			setBeanClassName(other.getBeanClassName());
		}
		if (StringUtils.hasLength(other.getFactoryBeanName())) {
			setFactoryBeanName(other.getFactoryBeanName());
		}
		if (StringUtils.hasLength(other.getFactoryMethodName())) {
			setFactoryMethodName(other.getFactoryMethodName());
		}
		if (StringUtils.hasLength(other.getScope())) {
			setScope(other.getScope());
		}
		setAbstract(other.isAbstract());
		setLazyInit(other.isLazyInit());
		setRole(other.getRole());
		getConstructorArgumentValues().addArgumentValues(other.getConstructorArgumentValues());
		getPropertyValues().addPropertyValues(other.getPropertyValues());
		setSource(other.getSource());
		copyAttributesFrom(other);
		if (other instanceof AbstractBeanDefinition) {
			AbstractBeanDefinition otherAbd = (AbstractBeanDefinition) other;
			if (otherAbd.hasBeanClass()) {
				setBeanClass(otherAbd.getBeanClass());
			}
			setAutowireCandidate(otherAbd.isAutowireCandidate());
			setAutowireMode(otherAbd.getAutowireMode());
			copyQualifiersFrom(otherAbd);
			setPrimary(otherAbd.isPrimary());
			setDependencyCheck(otherAbd.getDependencyCheck());
			setDependsOn(otherAbd.getDependsOn());
			setNonPublicAccessAllowed(otherAbd.isNonPublicAccessAllowed());
			setLenientConstructorResolution(otherAbd.isLenientConstructorResolution());
			if (StringUtils.hasLength(otherAbd.getInitMethodName())) {
				setInitMethodName(otherAbd.getInitMethodName());
				setEnforceInitMethod(otherAbd.isEnforceInitMethod());
			}
			if (StringUtils.hasLength(otherAbd.getDestroyMethodName())) {
				setDestroyMethodName(otherAbd.getDestroyMethodName());
				setEnforceDestroyMethod(otherAbd.isEnforceDestroyMethod());
			}
			getMethodOverrides().addOverrides(otherAbd.getMethodOverrides());
			setSynthetic(otherAbd.isSynthetic());
			setResource(otherAbd.getResource());
		} else {
			setResourceDescription(other.getResourceDescription());
		}
	}

	public void applyDefaults(BeanDefinitionDefaults defaults) {
		setLazyInit(defaults.isLazyInit());
		setAutowireMode(defaults.getAutowireMode());
		setDependencyCheck(defaults.getDependencyCheck());
		setInitMethodName(defaults.getInitMethodName());
		setEnforceInitMethod(false);
		setDestroyMethodName(defaults.getDestroyMethodName());
		setEnforceDestroyMethod(false);
	}

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

	@Override
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

	public int getResolvedAutowireMode() {
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

	public void setResourceDescription(String reourceDescription) {
		this.resource = new DescriptiveResource(reourceDescription);
	}

	public String getResourceDescription() {
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

	public void validate() throws BeanDefinitionValidationException {
		if (!getMethodOverrides().isEmpty() && getFactoryMethodName() != null) {
			throw new BeanDefinitionValidationException("不能将静态工厂方法与方法重写相结合：静态工厂方法必须创建实例。");
		}
		if (hasBeanClass()) {
			prepareMethodOverrides();
		}
	}

	public void prepareMethodOverrides() throws BeanDefinitionValidationException {
		MethodOverrides methodOverrides = getMethodOverrides();
		if (!methodOverrides.isEmpty()) {
			for (MethodOverride mo : methodOverrides.getOverrides()) {
				prepareMethodOverride(mo);
			}
		}
	}

	public void prepareMethodOverride(MethodOverride mo) throws BeanDefinitionValidationException {
		int count = ClassUtils.getMethodCountForName(getBeanClass(), mo.getMethodName());
		if (count == 0) {
			throw new BeanDefinitionValidationException(
					"无效的方法重写:类" + getBeanClassName() + "里没有找到方法" + mo.getMethodName());
		} else if (count == 1) {
			mo.setOverLoaded(false);
		}
	}

	@Override
	public Object clone() {
		return cloneBeanDefinition();
	}

	public abstract AbstractBeanDefinition cloneBeanDefinition();

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof AbstractBeanDefinition)) {
			return false;
		}
		AbstractBeanDefinition another = (AbstractBeanDefinition) other;
		if (!ObjectUtils.nullSafeEquals(this.getBeanClassName(), another.getBeanClassName()))
			return false;
		if (!ObjectUtils.nullSafeEquals(this.scope, another.scope))
			return false;
		if (this.abstractFlag != another.abstractFlag)
			return false;
		if (this.lazyInit != another.lazyInit)
			return false;

		if (this.autowireMode != another.autowireMode)
			return false;
		if (this.dependencyCheck != another.dependencyCheck)
			return false;
		if (!Arrays.equals(this.dependsOn, another.dependsOn))
			return false;
		if (this.autowireCandidate != another.autowireCandidate)
			return false;
		if (!ObjectUtils.nullSafeEquals(this.qualifiers, another.qualifiers))
			return false;
		if (this.primary != another.primary)
			return false;
		if (this.nonPublicAccessAllowed != another.nonPublicAccessAllowed)
			return false;
		if (this.lenientConstructorResolution != another.lenientConstructorResolution)
			return false;
		if (!ObjectUtils.nullSafeEquals(this.constructorArgumentValues, another.constructorArgumentValues))
			return false;
		if (!ObjectUtils.nullSafeEquals(this.propertyValues, another.propertyValues))
			return false;
		if (!ObjectUtils.nullSafeEquals(this.methodOverrides, another.methodOverrides))
			return false;
		if (!ObjectUtils.nullSafeEquals(this.factoryBeanName, another.factoryBeanName))
			return false;
		if (!ObjectUtils.nullSafeEquals(this.factoryMethodName, another.factoryMethodName))
			return false;
		if (!ObjectUtils.nullSafeEquals(this.initMethodName, another.initMethodName))
			return false;
		if (this.enforceInitMethod != another.enforceInitMethod)
			return false;
		if (!ObjectUtils.nullSafeEquals(this.destroyMethodName, another.destroyMethodName))
			return false;
		if (this.enforceDestroyMethod != another.enforceDestroyMethod)
			return false;
		if (this.synthetic != another.synthetic)
			return false;
		if (this.role != another.role)
			return false;

		return super.equals(other);
	}

	@Override
	public int hashCode() {
		int hashCode = ObjectUtils.nullSafeHashCode(getBeanClassName());
		hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.scope);
		hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.constructorArgumentValues);
		hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.propertyValues);
		hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.factoryBeanName);
		hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.factoryMethodName);
		hashCode = 29 * hashCode + super.hashCode();
		return hashCode;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("class [");
		sb.append(getBeanClassName()).append("]");
		sb.append("; scope=").append(this.scope);
		sb.append("; abstract=").append(this.abstractFlag);
		sb.append("; lazyInit=").append(this.lazyInit);
		sb.append("; autowireMode=").append(this.autowireMode);
		sb.append("; dependencyCheck=").append(this.dependencyCheck);
		sb.append("; autowireCandidate=").append(this.autowireCandidate);
		sb.append("; primary=").append(this.primary);
		sb.append("; factoryBeanName=").append(this.factoryBeanName);
		sb.append("; factoryMethodName=").append(this.factoryMethodName);
		sb.append("; initMethodName=").append(this.initMethodName);
		sb.append("; destroyMethodName=").append(this.destroyMethodName);
		if (this.resource != null) {
			sb.append("; defined in ").append(this.resource.getDescription());
		}
		return sb.toString();
	}
}
