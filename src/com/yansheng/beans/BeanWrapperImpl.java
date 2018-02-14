package com.yansheng.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.MethodInvocationException;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.CollectionFactory;
import org.springframework.core.GenericCollectionTypeResolver;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConverterNotFoundException;
import org.springframework.core.convert.Property;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class BeanWrapperImpl extends AbstractPropertyAccessor implements BeanWrapper {

	private static final Log logger = LogFactory.getLog(BeanWrapperImpl.class);

	private Object object;
	private String nestedPath = "";
	private Object rootObject;

	private AccessControlContext acc;

	private CachedIntrospectionResults cachedIntrospectionResults;

	private Map<String, BeanWrapperImpl> nestedBeanWrappers;

	private boolean autoGrowNestedPaths = false;
	private int autoGrowCollectionLimit = Integer.MAX_VALUE;

	public BeanWrapperImpl() {
		this(true);
	}

	public BeanWrapperImpl(boolean registerDefaultEditors) {
		if (registerDefaultEditors) {
			registerDefaultEditors();
		}
		this.typeConverterDelegate = new TypeConverterDelegate(this);
	}

	public BeanWrapperImpl(Object object) {
		registerDefaultEditors();
		setWrappedInstance(object);
	}

	public BeanWrapperImpl(Class<?> clazz) {
		registerDefaultEditors();
		setWrappedInstance(BeanUtils.instantiateClass(clazz));
	}

	public BeanWrapperImpl(Object object, String nestedPath, Object rootObject) {
		registerDefaultEditors();
		setWrappedInstance(object, nestedPath, rootObject);
	}

	private BeanWrapperImpl(Object object, String nestedPath, BeanWrapperImpl superBw) {
		setWrappedInstance(object, nestedPath, superBw.getWrapperInstance());
		setExtractOldValueForEditor(superBw.isExtractOldValueForEditor());
		setAutoGrowNestedPaths(superBw.isAutoGrowNestedPaths());
		setAutoGrowCollectionLimit(superBw.getAutoGrowCollectionLimit());
		setConversionService(superBw.getConversionService());
		setSecurityContext(superBw.getSecurityContext());
	}

	// ---------------------------------------------------------------------
	// Implementation of BeanWrapper interface
	// ---------------------------------------------------------------------

	public void setWrappedInstance(Object object) {
		setWrappedInstance(object, "", null);
	}

	public void setWrappedInstance(Object object, String nestedPath, Object rootObject) {
		Assert.notNull(object, "Bean object 不能为NULL。");
		this.object = object;
		this.nestedPath = (nestedPath != null ? nestedPath : "");
		this.rootObject = (!"".equals(nestedPath) ? rootObject : object);
		this.nestedBeanWrappers = null;
		this.typeConverterDelegate = new TypeConverterDelegate(this, object);
		setIntrospectionClass(object.getClass());
	}

	@Override
	public final Object getWrapperInstance() {
		return this.object;
	}

	@Override
	public final Class<?> getWrapperClass() {
		return (this.object != null ? this.object.getClass() : null);
	}

	public final String getNestedPath() {
		return this.nestedPath;
	}

	public final Object getRootInstance() {
		return this.rootObject;
	}

	public final Class<?> getRootClass() {
		return (this.rootObject != null ? this.rootObject.getClass() : null);
	}

	@Override
	public void setAutoGrowNestedPaths(boolean autoGrowNestedPaths) {
		this.autoGrowNestedPaths = autoGrowNestedPaths;
	}

	@Override
	public boolean isAutoGrowNestedPaths() {
		return this.autoGrowNestedPaths;
	}

	@Override
	public void setAutoGrowCollectionLimit(int autoGrowCollectionLimit) {
		this.autoGrowCollectionLimit = autoGrowCollectionLimit;
	}

	@Override
	public int getAutoGrowCollectionLimit() {
		return this.autoGrowCollectionLimit;
	}

	public void setSecurityContext(AccessControlContext acc) {
		this.acc = acc;
	}

	public AccessControlContext getSecurityContext() {
		return this.acc;
	}

	protected void setIntrospectionClass(Class<?> clazz) {
		if (this.cachedIntrospectionResults != null && !clazz.equals(this.cachedIntrospectionResults.getBeanClass())) {
			this.cachedIntrospectionResults = null;
		}
	}

	private CachedIntrospectionResults getCachedIntrospectionResults() {
		Assert.state(this.object != null, "BeanWrapper does not hold a bean instance");
		if (this.cachedIntrospectionResults == null) {
			this.cachedIntrospectionResults = CachedIntrospectionResults.forClass(getWrapperClass());
		}
		return this.cachedIntrospectionResults;
	}

	@Override
	public PropertyDescriptor[] getPropertyDescriptors() {
		return getCachedIntrospectionResults().getPropertyDescriptors();
	}

	@Override
	public PropertyDescriptor getPropertyDescriptor(String propertyName) throws InvalidPropertyException {
		PropertyDescriptor pd = getPropertyDescriptorInternal(propertyName);
		if (pd == null) {
			throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName,
					"No property '" + propertyName + "' found");
		}
		return pd;
	}

	protected PropertyDescriptor getPropertyDescriptorInternal(String propertyName) throws BeansException {
		Assert.notNull(propertyName, "Property name must not be null");
		BeanWrapperImpl nestedBw = getBeanWrapperForPropertyPath(propertyName);
		return nestedBw.getCachedIntrospectionResults().getPropertyDescriptor(getFinalPath(nestedBw, propertyName));
	}

	public Class<?> getPropertyType(String propertyName) throws BeansException {
		try {
			PropertyDescriptor pd = getPropertyDescriptorInternal(propertyName);
			if (pd != null) {
				return pd.getPropertyType();
			} else {
				Object value = getPropertyValue(propertyName);
				if (value != null) {
					return value.getClass();
				}
				Class<?> editorType = guessPropertyTypeFromEditors(propertyName);
				if (editorType != null) {
					return editorType;
				}
			}
		} catch (InvalidPropertyException e) {

		}
		return null;
	}

	public TypeDescriptor getPropertyTypeDescriptor(String propertyName) throws BeansException {
		try {
			BeanWrapperImpl nestedBw = getBeanWrapperForPropertyPath(propertyName);
			String finalPath = getFinalPath(nestedBw, propertyName);
			PropertyTokenHolder tokens = getPropertyNameTokens(finalPath);
			PropertyDescriptor pd = nestedBw.getCachedIntrospectionResults().getPropertyDescriptor(tokens.actualName);
			if (pd != null) {
				if (tokens.keys != null) {
					if (pd.getReadMethod() != null || pd.getWriteMethod() != null) {
						return TypeDescriptor.nested(property(pd), tokens.keys.length);
					}
				} else {
					if (pd.getReadMethod() != null || pd.getWriteMethod() != null) {
						return new TypeDescriptor(property(pd));
					}
				}
			}
		} catch (InvalidPropertyException e) {

		}
		return null;
	}

	public boolean isReadableProperty(String propertyName) {
		try {
			PropertyDescriptor pd = getPropertyDescriptorInternal(propertyName);
			if (pd != null) {
				if (pd.getReadMethod() != null) {
					return true;
				}
			} else {
				getPropertyValue(propertyName);
				return true;
			}
		} catch (InvalidPropertyException e) {

		}
		return false;
	}

	public boolean isWritableProperty(String propertyName) {
		try {
			PropertyDescriptor pd = getPropertyDescriptorInternal(propertyName);
			if (pd != null) {
				if (pd.getWriteMethod() != null) {
					return true;
				}
			} else {
				getPropertyValue(propertyName);
				return true;
			}
		} catch (InvalidPropertyException e) {

		}
		return false;
	}

	private Object convertIfNecessay(String propertyName, Object oldValue, Object newValue, Class<?> requiredType,
			TypeDescriptor td) throws TypeMismatchException {
		try {
			return this.typeConverterDelegate.convertIfNecessary(propertyName, oldValue, newValue, requiredType, td);
		} catch (ConverterNotFoundException ex) {
			PropertyChangeEvent pce = new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue,
					newValue);
			throw new ConversionNotSupportedException(pce, td.getType(), ex);
		} catch (ConversionException ex) {
			PropertyChangeEvent pce = new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue,
					newValue);
			throw new TypeMismatchException(pce, requiredType, ex);
		} catch (IllegalStateException ex) {
			PropertyChangeEvent pce = new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue,
					newValue);
			throw new ConversionNotSupportedException(pce, requiredType, ex);
		} catch (IllegalArgumentException ex) {
			PropertyChangeEvent pce = new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue,
					newValue);
			throw new TypeMismatchException(pce, requiredType, ex);
		}
	}

	public Object convertForProperty(Object value, String propertyName) throws TypeMismatchException {
		PropertyDescriptor pd = getPropertyDescriptorInternal(propertyName);
		if (pd == null) {
			throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName,
					"No property '" + propertyName + "' found");
		}
		return convertForProperty(propertyName, null, value, pd);
	}

	private Object convertForProperty(String propertyName, Object oldValue, Object newValue, PropertyDescriptor pd)
			throws TypeMismatchException {
		return convertIfNecessay(propertyName, oldValue, newValue, pd.getPropertyType(),
				new TypeDescriptor(property(pd)));
	}

	private Property property(PropertyDescriptor pd) {
		GenericTypeAwarePropertyDescriptor typeAware = (GenericTypeAwarePropertyDescriptor) pd;
		return new Property(typeAware.getBeanClass(), typeAware.getReadMethod(), typeAware.getWriteMethod(),
				typeAware.getName());
	}

	// ---------------------------------------------------------------------
	// Implementation methods
	// ---------------------------------------------------------------------
	private String getFinalPath(BeanWrapper bw, String nestedPath) {
		if (bw == this) {
			return nestedPath;
		}
		return nestedPath.substring(PropertyAccessorUtils.getLastNestedPropertySeparatorIndex(nestedPath) + 1);
	}

	protected BeanWrapperImpl getBeanWrapperForPropertyPath(String propertyPath) {
		int pos = PropertyAccessorUtils.getFirstNestedPropertySeparatorIndex(propertyPath);
		if (pos > -1) {
			String nestedProperty = propertyPath.substring(0, pos);
			String nestedPath = propertyPath.substring(pos + 1);
			BeanWrapperImpl nestBw = getNestedBeanWrapper(nestedProperty);
			return nestBw.getBeanWrapperForPropertyPath(nestedPath);
		} else {
			return this;
		}
	}

	private BeanWrapperImpl getNestedBeanWrapper(String nestedProperty) {
		if (this.nestedBeanWrappers == null) {
			this.nestedBeanWrappers = new HashMap<String, BeanWrapperImpl>();
		}
		PropertyTokenHolder tokens = getPropertyNameTokens(nestedProperty);
		String canonicalName = tokens.canonicalName;
		Object propertyValue = getPropertyValue(tokens);
		if (propertyValue == null) {
			if (this.autoGrowNestedPaths) {
				propertyValue = setDefaultValue(tokens);
			} else {
				throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + canonicalName);
			}
		}
		BeanWrapperImpl nestedBw = this.nestedBeanWrappers.get(canonicalName);
		if (nestedBw == null || nestedBw.getWrapperInstance() != propertyValue) {
			if (logger.isTraceEnabled()) {
				logger.trace("Creating new nested BeanWrapper for property '" + canonicalName + "'");
			}
			nestedBw = newNestedBeanWrapper(propertyValue, this.nestedPath + canonicalName + NESTED_PROPERTY_SEPARATOR);
			copyDefaultEditorsTo(nestedBw);
			copyCustomEditorsTo(nestedBw, canonicalName);
			this.nestedBeanWrappers.put(canonicalName, nestedBw);
		} else {
			if (logger.isTraceEnabled()) {
				logger.trace("Using cached nested BeanWrapper for property '" + canonicalName + "'");
			}
		}
		return nestedBw;
	}

	private Object setDefaultValue(String propertyName) {
		PropertyTokenHolder tokens = new PropertyTokenHolder();
		tokens.canonicalName = propertyName;
		tokens.actualName = propertyName;
		return setDefaultValue(tokens);
	}

	private Object setDefaultValue(PropertyTokenHolder tokens) {
		PropertyValue pv = createDefaultPropertyValue(tokens);
		setPropertyValue(tokens, pv);
		return getPropertyValue(tokens);
	}

	private PropertyValue createDefaultPropertyValue(PropertyTokenHolder tokens) {
		Class<?> type = getPropertyTypeDescriptor(tokens.canonicalName).getType();
		if (type == null) {
			throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + tokens.canonicalName,
					"Could not determine property type for auto-growing a default value");
		}
		Object defaultValue = newValue(type, tokens.canonicalName);
		return new PropertyValue(tokens.canonicalName, defaultValue);
	}

	private Object newValue(Class<?> type, String name) {
		try {
			if (type.isArray()) {
				Class<?> componentType = type.getComponentType();
				if (componentType.isArray()) {
					Object array = Array.newInstance(type, 1);
					Array.set(array, 0, Array.newInstance(componentType.getComponentType(), 0));
					return array;
				} else {
					return Array.newInstance(componentType, 0);
				}
			} else if (Collection.class.isAssignableFrom(type)) {
				return CollectionFactory.createCollection(type, 16);
			} else if (Map.class.isAssignableFrom(type)) {
				return CollectionFactory.createMap(type, 16);
			} else {
				return type.newInstance();
			}
		} catch (Exception e) {
			throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + name,
					"Could not instantiate property type [" + type.getName() + "] to auto-grow nested property path: "
							+ e);
		}
	}

	protected BeanWrapperImpl newNestedBeanWrapper(Object object, String nestedPath) {
		return new BeanWrapperImpl(object, nestedPath, null);
	}

	private PropertyTokenHolder getPropertyNameTokens(String propertyName) {
		PropertyTokenHolder tokens = new PropertyTokenHolder();
		String actualName = null;
		List<String> keys = new ArrayList<String>(2);
		int searchIndex = 0;
		while (searchIndex != -1) {
			int keyStart = propertyName.indexOf(PROPERTY_KEY_PREFIX, searchIndex);
			searchIndex = -1;
			if (keyStart != -1) {
				int keyEnd = propertyName.indexOf(PROPERTY_KEY_SUFFIX, keyStart + PROPERTY_KEY_PREFIX.length());
				if (keyEnd != -1) {
					if (actualName == null) {
						actualName = propertyName.substring(0, keyStart);
					}
					String key = propertyName.substring(keyStart + PROPERTY_KEY_PREFIX.length(), keyEnd);
					if ((key.startsWith("'") && key.endsWith("'")) || (key.startsWith("\"") && key.endsWith("\""))) {
						key = key.substring(1, key.length() - 1);
					}
					keys.add(key);
					searchIndex = keyEnd + PROPERTY_KEY_SUFFIX.length();
				}
			}
		}
		tokens.actualName = (actualName != null ? actualName : propertyName);
		tokens.canonicalName = tokens.actualName;
		if (!keys.isEmpty()) {
			tokens.canonicalName += PROPERTY_KEY_PREFIX
					+ StringUtils.collectionToDelimitedString(keys, PROPERTY_KEY_SUFFIX + PROPERTY_KEY_PREFIX)
					+ PROPERTY_KEY_SUFFIX;
			tokens.keys = StringUtils.toStringArray(keys);
		}
		return tokens;
	}

	// ---------------------------------------------------------------------
	// Implementation of PropertyAccessor interface
	// ---------------------------------------------------------------------

	@Override
	public Object getPropertyValue(String propertyName) throws BeansException {
		BeanWrapperImpl nestedBw = getBeanWrapperForPropertyPath(propertyName);
		PropertyTokenHolder tokens = getPropertyNameTokens(getFinalPath(nestedBw, propertyName));
		return nestedBw.getPropertyValue(tokens);
	}

	@SuppressWarnings("rawtypes")
	private Object getPropertyValue(PropertyTokenHolder tokens) throws BeansException {
		String propertyName = tokens.canonicalName;
		String actualName = tokens.actualName;
		PropertyDescriptor pd = getCachedIntrospectionResults().getPropertyDescriptor(actualName);
		if (pd == null || pd.getReadMethod() == null) {
			throw new NotReadablePropertyException(getRootClass(), this.nestedPath + propertyName);
		}
		final Method readMethod = pd.getReadMethod();
		try {
			if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers()) && !readMethod.isAccessible()) {
				if (System.getSecurityManager() != null) {
					AccessController.doPrivileged(new PrivilegedAction<Object>() {
						@Override
						public Object run() {
							readMethod.setAccessible(true);
							return null;
						}
					});
				} else {
					readMethod.setAccessible(true);
				}
			}
			Object value;
			if (System.getSecurityManager() != null) {
				try {
					value = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
						@Override
						public Object run() throws Exception {
							return readMethod.invoke(object, (Object[]) null);
						}
					}, acc);
				} catch (PrivilegedActionException pae) {
					throw pae.getException();
				}
			} else {
				value = readMethod.invoke(object, (Object[]) null);
			}

			if (tokens.keys != null) {
				if (value == null) {
					if (this.autoGrowNestedPaths) {
						value = setDefaultValue(tokens.actualName);
					} else {
						throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + propertyName,
								"Cannot access indexed value of property referenced in indexed " + "property path '"
										+ propertyName + "': returned null");
					}
				}
				String indexPropertyName = tokens.actualName;
				for (int i = 0; i < tokens.keys.length; i++) {
					String key = tokens.keys[i];
					if (value == null) {
						throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + propertyName,
								"Cannot access indexed value of property referenced in indexed " + "property path '"
										+ propertyName + "': returned null");
					} else if (value.getClass().isArray()) {
						int index = Integer.parseInt(key);
						value = growArrayIfNecessary(value, index, indexPropertyName);
						value = Array.get(value, index);
					} else if (value instanceof List) {
						int index = Integer.parseInt(key);
						List list = (List) value;
						growCollectionIfNecessary(list, index, indexPropertyName, pd, i + 1);
						value = list.get(index);
					} else if (value instanceof Set) {
						Set set = (Set) value;
						int index = Integer.parseInt(key);
						if (index < 0 || index >= set.size()) {
							throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName,
									"Cannot get element with index " + index + " from Set of size " + set.size()
											+ ", accessed using property path '" + propertyName + "'");
						}
						Iterator it = set.iterator();
						for (int j = 0; it.hasNext(); j++) {
							Object elem = it.next();
							if (j == index) {
								value = elem;
								break;
							}
						}
					} else if (value instanceof Map) {
						Map map = (Map) value;
						Class<?> mapKeyType = GenericCollectionTypeResolver.getMapKeyReturnType(pd.getReadMethod(),
								i + 1);
						TypeDescriptor typeDescriptor = (mapKeyType != null ? TypeDescriptor.valueOf(mapKeyType)
								: TypeDescriptor.valueOf(Object.class));
						Object convertedMapKey = convertIfNecessay(null, null, key, mapKeyType, typeDescriptor);
						value = map.get(convertedMapKey);
					} else {
						throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName,
								"Property referenced in indexed property path '" + propertyName
										+ "' is neither an array nor a List nor a Set nor a Map; returned value was ["
										+ value + "]");
					}
					indexPropertyName += PROPERTY_KEY_PREFIX + key + PROPERTY_KEY_SUFFIX;
				}
			}
			return value;
		} catch (IndexOutOfBoundsException ex) {
			throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName,
					"Index of out of bounds in property path '" + propertyName + "'", ex);
		} catch (NumberFormatException ex) {
			throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName,
					"Invalid index in property path '" + propertyName + "'", ex);
		} catch (TypeMismatchException ex) {
			throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName,
					"Invalid index in property path '" + propertyName + "'", ex);
		} catch (InvocationTargetException ex) {
			throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName,
					"Getter for property '" + actualName + "' threw exception", ex);
		} catch (Exception ex) {
			throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName,
					"Illegal attempt to get property '" + actualName + "' threw exception", ex);
		}
	}

	private Object growArrayIfNecessary(Object array, int index, String name) {
		if (!this.autoGrowNestedPaths) {
			return array;
		}
		int length = Array.getLength(array);
		if (index >= length && index < this.autoGrowCollectionLimit) {
			Class<?> componentType = array.getClass().getComponentType();
			Object newArray = Array.newInstance(componentType, index + 1);
			System.arraycopy(array, 0, newArray, 0, length);
			for (int i = length; i < Array.getLength(newArray); i++) {
				Array.set(newArray, i, newValue(componentType, name));
			}
			setPropertyValue(name, newArray);
			return getPropertyValue(name);
		} else {
			return array;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void growCollectionIfNecessary(Collection collection, int index, String name, PropertyDescriptor pd,
			int nestingLevel) {
		if (!this.autoGrowNestedPaths) {
			return;
		}
		int size = collection.size();
		if (index >= size && index < this.autoGrowCollectionLimit) {
			Class<?> elementType = GenericCollectionTypeResolver.getCollectionReturnType(pd.getReadMethod(),
					nestingLevel);
			if (elementType != null) {
				for (int i = size; i < index + 1; i++) {
					collection.add(newValue(elementType, name));
				}
			}
		}
	}

	@Override
	public void setPropertyValue(String propertyName, Object value) throws BeansException {
		BeanWrapperImpl nestedBw;
		try {
			nestedBw = getBeanWrapperForPropertyPath(propertyName);
		} catch (NotReadablePropertyException e) {
			throw new NotWritablePropertyException(getRootClass(), this.nestedPath + propertyName,
					"Nested property in path '" + propertyName + "' does not exist", e);
		}
		PropertyTokenHolder tokens = getPropertyNameTokens(getFinalPath(nestedBw, propertyName));
		nestedBw.setPropertyValue(tokens, new PropertyValue(propertyName, value));
	}

	public void setPropertyValue(PropertyValue pv) throws BeansException {
		PropertyTokenHolder tokens = (PropertyTokenHolder) pv.resolvedTokens;
		if (tokens == null) {
			String propertyName = pv.getName();
			BeanWrapperImpl nestedBw;
			try {
				nestedBw = getBeanWrapperForPropertyPath(propertyName);
			} catch (NotReadablePropertyException e) {
				throw new NotWritablePropertyException(getRootClass(), this.nestedPath + propertyName,
						"Nested property in path '" + propertyName + "' does not exist", e);
			}
			tokens = getPropertyNameTokens(getFinalPath(nestedBw, propertyName));
			if (nestedBw == this) {
				pv.getOriginalPropertyValue().resolvedTokens = tokens;
			}
			setPropertyValue(tokens, pv);
		} else {
			setPropertyValue(tokens, pv);
		}
	}

	private void setPropertyValue(PropertyTokenHolder tokens, PropertyValue pv) throws BeansException {
		String propertyName = tokens.canonicalName;
		String actualName = tokens.actualName;

		if (tokens.keys != null) {

		} else {
			PropertyDescriptor pd = pv.resolvedDescriptor;
			if (pd == null || !pd.getWriteMethod().getDeclaringClass().isInstance(this.object)) {
				pd = getCachedIntrospectionResults().getPropertyDescriptor(actualName);
				if (pd == null || pd.getWriteMethod() == null) {
					if (pv.isOptional()) {
						logger.debug("Ignoring optional value for property '" + actualName
								+ "' - property not found on bean class [" + getRootClass().getName() + "]");
						return;
					} else {
						PropertyMatches matches = PropertyMatches.forProperty(propertyName, getRootClass());
						throw new NotWritablePropertyException(getRootClass(), this.nestedPath + propertyName,
								matches.buildErrorMessage(), matches.getPossibleMatches());
					}
				}
				pv.getOriginalPropertyValue().resolvedDescriptor = pd;
			}

			Object oldValue = null;
			try {

			} catch (TypeMismatchException ex) {
				throw ex;
			} catch (InvocationTargetException ex) {
				PropertyChangeEvent propertyChangeEvent = new PropertyChangeEvent(this.rootObject,
						this.nestedPath + propertyName, oldValue, pv.getValue());
				if (ex.getTargetException() instanceof ClassCastException) {
					throw new TypeMismatchException(propertyChangeEvent, pd.getPropertyType(), ex.getTargetException());
				} else {
					throw new MethodInvocationException(propertyChangeEvent, ex.getTargetException());
				}
			} catch (Exception ex) {
				PropertyChangeEvent pce = new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName,
						oldValue, pv.getValue());
				throw new MethodInvocationException(pce, ex);
			}
		}
	}

	// ---------------------------------------------------------------------
	// Inner class for internal use
	// ---------------------------------------------------------------------
	private static class PropertyTokenHolder {
		public String canonicalName;
		public String actualName;
		public String[] keys;
	}

}
