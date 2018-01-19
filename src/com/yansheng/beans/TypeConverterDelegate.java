package com.yansheng.beans;

import java.beans.PropertyEditor;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.CollectionFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

public class TypeConverterDelegate {

    private static final Log logger = LogFactory.getLog(TypeConverterDelegate.class);
    private final PropertyEditorRegistrySupport propertyEditorRegistry;
    private final Object targetObject;

    public TypeConverterDelegate(PropertyEditorRegistrySupport propertyEditorRegistry) {
        this(propertyEditorRegistry, null);
    }

    public TypeConverterDelegate(PropertyEditorRegistrySupport propertyEditorRegistry, Object targetObject) {
        this.propertyEditorRegistry = propertyEditorRegistry;
        this.targetObject = targetObject;
    }

    public <T> T convertIfNecessary(Object newValue, Class<T> requiredType, MethodParameter methodParam)
            throws IllegalArgumentException {
        return convertIfNecessary(null, null, newValue, requiredType,
                (methodParam != null ? new TypeDescriptor(methodParam) : TypeDescriptor.valueOf(requiredType)));
    }

    public <T> T convertIfNecessary(Object newValue, Class<T> requiredType, Field field)
            throws IllegalArgumentException {
        return convertIfNecessary(null, null, newValue, requiredType,
                (field != null ? new TypeDescriptor(field) : TypeDescriptor.valueOf(requiredType)));
    }

    public <T> T convertIfNecessary(String propertyName, Object oldValue, Object newValue, Class<T> requiredType)
            throws IllegalArgumentException {
        return convertIfNecessary(propertyName, oldValue, newValue, requiredType, TypeDescriptor.valueOf(requiredType));
    }

    public <T> T convertIfNecessary(String propertyName, Object oldValue, Object newValue, Class<T> requiredType,
            TypeDescriptor typeDescriptor)
            throws IllegalArgumentException {

        Object convertedValue = newValue;
        PropertyEditor editor = this.propertyEditorRegistry.findCustomEditor(requiredType, propertyName);
        ConversionService conversionService = this.propertyEditorRegistry.getConversionService();

        ConversionFailedException firstAttemptEx = null;

        if (editor == null && conversionService != null && convertedValue != null && typeDescriptor != null) {
            TypeDescriptor souorceTypeDesc = TypeDescriptor.forObject(newValue);
            TypeDescriptor targetTypeDesc = typeDescriptor;
            if (conversionService.canConvert(souorceTypeDesc, targetTypeDesc)) {
                try {
                    return (T) conversionService.convert(convertedValue, souorceTypeDesc, targetTypeDesc);
                } catch (ConversionFailedException e) {
                    //尝试使用默认的转换器
                    firstAttemptEx = e;
                }
            }
        }

        //转换的值和类型不匹配
        if (editor != null || (requiredType != null && !ClassUtils.isAssignableValue(requiredType, convertedValue))) {
            if (requiredType != null && Collection.class.isAssignableFrom(requiredType)
                    && convertedValue instanceof String) {
                TypeDescriptor elementType = typeDescriptor.getElementTypeDescriptor();
                if (elementType != null && Enum.class.isAssignableFrom(elementType.getType())) {
                    convertedValue = StringUtils.commaDelimitedListToStringArray((String) convertedValue);
                }
            }
            if (editor == null) {
                editor = findDefaultEditor(requiredType);
            }
            convertedValue = doConvertValue(oldValue, convertedValue, requiredType, editor);
        }

        boolean standardConversion = false;

        if (requiredType != null) {
            if (convertedValue != null) {
                if (requiredType.isArray()) {
                    if (convertedValue instanceof String &&
                            Enum.class.isAssignableFrom(requiredType.getComponentType())) {
                        convertedValue = StringUtils.commaDelimitedListToStringArray((String) convertedValue);
                    }
                    return (T) convertToTypedArray(convertedValue, propertyName, requiredType);
                } else if (convertedValue instanceof Collection) {
                    convertedValue = convertToTypedCollection((Collection) convertedValue, propertyName, requiredType,
                            typeDescriptor);
                    standardConversion = true;
                } else if (convertedValue instanceof Map) {
                    convertedValue = convertToTypedMap((Map) convertedValue, propertyName, requiredType,
                            typeDescriptor);
                    standardConversion = true;
                }

                if (convertedValue.getClass().isArray() && Array.getLength(convertedValue) == 1) {
                    convertedValue = Array.get(convertedValue, 0);
                    standardConversion = true;
                }

                if (String.class.equals(requiredType) && ClassUtils.isPrimitiveOrWrapper(convertedValue.getClass())) {
                    return (T) convertedValue.toString();
                } else if (convertedValue instanceof String && !requiredType.isInstance(convertedValue)) {
                    if (firstAttemptEx == null && !requiredType.isInterface() && !requiredType.isEnum()) {
                        try {
                            Constructor strCtor = requiredType.getConstructor(String.class);
                            return (T) BeanUtils.instantiateClass(strCtor, convertedValue);
                        } catch (NoSuchMethodException e) {
                            if (logger.isTraceEnabled()) {
                                logger.trace("No String constructor found on type [" + requiredType.getName() + "]", e);
                            }
                        } catch (Exception e) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Construction via String failed for type [" + requiredType.getName() + "]",
                                        e);
                            }
                        }
                    }
                    String trimmedValue = ((String)convertedValue).trim();
                    if(requiredType.isEnum() && "".equals(trimmedValue)){
                        return null;
                    }
                    //attemptToConvertStringToEnum

                }
            }
        }

        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Map convertToTypedMap(Map original, String propertyName, Class requiredType,
            TypeDescriptor typeDescriptor) {
        if (!Map.class.isAssignableFrom(requiredType)) {
            return original;
        }
        boolean approximable = CollectionFactory.isApproximableMapType(requiredType);
        if (!approximable && !canCreateCopy(requiredType)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Custom Map type [" + original.getClass().getName() +
                        "] does not allow for creating a copy - injecting original Map as-is");
            }
            return original;
        }

        boolean originalAllowed = requiredType.isInstance(original);
        typeDescriptor = typeDescriptor.narrow(original);
        TypeDescriptor keyType = typeDescriptor.getMapKeyTypeDescriptor();
        TypeDescriptor valueType = typeDescriptor.getMapValueTypeDescriptor();
        if (keyType == null && valueType == null && originalAllowed &&
                !this.propertyEditorRegistry.hasCustomEditorForElement(null, propertyName)) {
            return original;
        }

        Iterator it;
        try {
            it = original.entrySet().iterator();
            if (it == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Cannot access Map of type [" + original.getClass().getName() +
                            "] - injecting original Map as-is: ");
                }
                return original;
            }
        } catch (Throwable e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Cannot access Map of type [" + original.getClass().getName() +
                        "] - injecting original Map as-is: " + e);
            }
            return original;
        }

        Map convertedCopy;
        try {
            if (approximable) {
                convertedCopy = CollectionFactory.createApproximateMap(original, original.size());
            } else {
                convertedCopy = (Map) requiredType.newInstance();
            }
        } catch (Throwable e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Cannot create copy of Map type [" + original.getClass().getName() +
                        "] - injecting original Map as-is: " + e);
            }
            return original;
        }

        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            String keyedPropertyName = buildKeyedPropertyName(propertyName, key);
            Object convertedKey = convertIfNecessary(keyedPropertyName, null, key,
                    keyType != null ? keyType.getType() : null, keyType);
            Object convertedValue = convertIfNecessary(keyedPropertyName, null, value,
                    valueType != null ? valueType.getType() : null, valueType);
            try {
                convertedCopy.put(convertedKey, convertedValue);
            } catch (Throwable e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Map type [" + original.getClass().getName() +
                            "] seems to be read-only - injecting original Map as-is: " + e);
                }
                return original;
            }
            originalAllowed = originalAllowed && (key == convertedKey) && (value == convertedValue);
        }
        return (originalAllowed ? original : convertedCopy);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Collection convertToTypedCollection(Collection original, String propertyName, Class requiredType,
            TypeDescriptor typeDescriptor) {
        if (!Collection.class.isAssignableFrom(requiredType)) {
            return original;
        }
        boolean approximable = CollectionFactory.isApproximableCollectionType(requiredType);
        if (!approximable && !canCreateCopy(requiredType)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Custom Collection type [" + original.getClass().getName() +
                        "] does not allow for creating a copy - injecting original Collection as-is");
            }
            return original;
        }
        boolean originalAllowed = requiredType.isInstance(original);
        typeDescriptor = typeDescriptor.narrow(original);
        TypeDescriptor elementType = typeDescriptor.getElementTypeDescriptor();
        if (elementType == null &&
                originalAllowed &&
                !this.propertyEditorRegistry.hasCustomEditorForElement(null, propertyName)) {
            return original;
        }

        Iterator it;
        try {
            it = original.iterator();
            if (it == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Collection of type [" + original.getClass().getName() +
                            "] returned null Iterator - injecting original Collection as-is");
                }
                return original;
            }
        } catch (Throwable e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Collection of type [" + original.getClass().getName() +
                        "] returned null Iterator - injecting original Collection as-is");
            }
            return original;
        }

        Collection convertedCopy;
        try {
            if (approximable) {
                convertedCopy = CollectionFactory.createApproximateCollection(original, original.size());
            } else {
                convertedCopy = (Collection) requiredType.newInstance();
            }
        } catch (Throwable e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Cannot create copy of Collection type [" + original.getClass().getName() +
                        "] - injecting original Collection as-is: " + e);
            }
            return original;
        }

        int i = 0;
        for (; it.hasNext(); i++) {
            Object element = it.next();
            String indexedPropertyName = buildIndexedPropertyName(propertyName, i);
            Object convertedElement = convertIfNecessary(indexedPropertyName, null, element,
                    (elementType != null ? elementType.getType() : null), elementType);
            try {
                convertedCopy.add(convertedElement);
            } catch (Throwable e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Collection type [" + original.getClass().getName() +
                            "] seems to be read-only - injecting original Collection as-is: " + e);
                }
                return original;
            }
            originalAllowed = originalAllowed && (element == convertedElement);
        }
        return (originalAllowed ? original : convertedCopy);
    }

    @SuppressWarnings("rawtypes")
    private Object convertToTypedArray(Object input, String propertyName, Class<?> componentType) {
        if (input instanceof Collection) {
            Collection coll = (Collection) input;
            Object result = Array.newInstance(componentType, coll.size());
            int i = 0;
            for (Iterator it = coll.iterator(); it.hasNext();) {
                Object value = convertIfNecessary(buildIndexedPropertyName(propertyName, i), null, it.next(),
                        componentType);
                Array.set(result, i, value);
            }
            return result;
        } else if (input.getClass().isArray()) {
            if (componentType.equals(input.getClass().getComponentType()) &&
                    !this.propertyEditorRegistry.hasCustomEditorForElement(componentType, propertyName)) {
                return input;
            }
            int arrayLength = Array.getLength(input);
            Object result = Array.newInstance(getClass(), arrayLength);
            for (int i = 0; i < arrayLength; i++) {
                Object value = convertIfNecessary(buildIndexedPropertyName(propertyName, i), null, Array.get(input, i),
                        componentType);
                Array.set(result, i, value);
            }
            return result;
        } else {
            Object result = Array.newInstance(componentType, 1);
            Object value = convertIfNecessary(buildIndexedPropertyName(propertyName, 0), null, input, componentType);
            Array.set(result, 0, value);
            return result;
        }
    }

    private PropertyEditor findDefaultEditor(Class<?> requiredType) {
        PropertyEditor editor = null;
        if (requiredType != null) {
            editor = this.propertyEditorRegistry.getDefaultEditor(requiredType);
            if (editor == null && !String.class.equals(requiredType)) {
                editor = BeanUtils.findEditorByConvention(requiredType);
            }
        }
        return editor;
    }

    private Object doConvertValue(Object oldValue, Object newValue, Class<?> requiredType, PropertyEditor editor) {

        Object convertedValue = newValue;
        boolean sharedEditor = false;

        if (editor != null) {
            sharedEditor = this.propertyEditorRegistry.isSharedEditor(editor);
        }
        //newValue不为String类型
        if (editor != null && !(convertedValue instanceof String)) {
            try {
                Object newConvertedValue;
                if (sharedEditor) {
                    //需要加锁
                    synchronized (editor) {
                        editor.setValue(convertedValue);
                        newConvertedValue = editor.getValue();
                    }
                } else {
                    editor.setValue(convertedValue);
                    newConvertedValue = editor.getValue();
                }
                if (newConvertedValue != convertedValue) {
                    convertedValue = newConvertedValue;
                    //已经转换完毕
                    editor = null;
                }
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("PropertyEditor [" + editor.getClass().getName() + "] does not support setValue call",
                            e);
                }
            }
        }

        Object returnValue = convertedValue;
        if (requiredType != null && !requiredType.isArray() && convertedValue instanceof String[]) {
            //目标类型不是数组，目标值是数组
            if (logger.isDebugEnabled()) {
                logger.debug("将String数组转换成带逗号的字符串。");
            }
            convertedValue = StringUtils.arrayToCommaDelimitedString((String[]) convertedValue);
        }
        //newValue为String类型
        if (convertedValue instanceof String) {
            if (editor != null) {
                //利用setAsText转换
                String newTextValue = (String) convertedValue;
                if (sharedEditor) {
                    synchronized (editor) {
                        return doConvertTextValue(oldValue, newTextValue, editor);
                    }
                } else {
                    return doConvertTextValue(oldValue, newTextValue, editor);
                }
            } else if (String.class.equals(requiredType)) {
                returnValue = convertedValue;
            }
        }

        return returnValue;
    }

    private Object doConvertTextValue(Object oldValue, String newTextValue, PropertyEditor editor) {
        try {
            editor.setValue(oldValue);
        } catch (Exception e) {
            if (logger.isTraceEnabled()) {
                logger.trace("PropertyEditor [" + editor.getClass().getName() + "] does not support setValue call", e);
            }
        }
        editor.setAsText(newTextValue);
        return editor.getValue();
    }

    private String buildIndexedPropertyName(String propertyName, int index) {
        return (propertyName != null
                ? (propertyName + PropertyAccessor.PROPERTY_KEY_PREFIX + index + PropertyAccessor.PROPERTY_KEY_SUFFIX)
                : null);
    }

    private String buildKeyedPropertyName(String propertyName, Object key) {
        return (propertyName != null
                ? (propertyName + PropertyAccessor.PROPERTY_KEY_PREFIX + key + PropertyAccessor.PROPERTY_KEY_SUFFIX)
                : null);
    }

    private boolean canCreateCopy(Class<?> requiredType) {
        return (!requiredType.isInterface() &&
                !Modifier.isAbstract(requiredType.getModifiers()) &&
                Modifier.isPublic(requiredType.getModifiers()) &&
                ClassUtils.hasConstructor(requiredType));
    }

}
