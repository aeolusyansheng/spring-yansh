package com.yansheng.beans;

import java.beans.PropertyEditor;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceArrayPropertyEditor;
import org.springframework.util.ClassUtils;
import org.xml.sax.InputSource;

import com.yansheng.beans.propertyeditors.ByteArrayPropertyEditor;
import com.yansheng.beans.propertyeditors.CharArrayPropertyEditor;
import com.yansheng.beans.propertyeditors.CharacterEditor;
import com.yansheng.beans.propertyeditors.CharsetEditor;
import com.yansheng.beans.propertyeditors.ClassArrayEditor;
import com.yansheng.beans.propertyeditors.ClassEditor;
import com.yansheng.beans.propertyeditors.CurrencyEditor;
import com.yansheng.beans.propertyeditors.CustomBooleanEditor;
import com.yansheng.beans.propertyeditors.CustomCollectionEditor;
import com.yansheng.beans.propertyeditors.CustomNumberEditor;
import com.yansheng.beans.propertyeditors.FileEditor;
import com.yansheng.beans.propertyeditors.InputSourceEditor;
import com.yansheng.beans.propertyeditors.InputStreamEditor;
import com.yansheng.beans.propertyeditors.LocaleEditor;
import com.yansheng.beans.propertyeditors.PatternEditor;
import com.yansheng.beans.propertyeditors.PropertiesEditor;
import com.yansheng.beans.propertyeditors.StringArrayPropertyEditor;
import com.yansheng.beans.propertyeditors.TimeZoneEditor;
import com.yansheng.beans.propertyeditors.URIEditor;
import com.yansheng.beans.propertyeditors.URLEditor;
import com.yansheng.beans.propertyeditors.UUIDEditor;

public class PropertyEditorRegistrySupport implements PropertyEditorRegistry {

    private ConversionService conversionService;
    private boolean defaultEditorsActive = false;
    private boolean configValueEditorsActive = false;
    private Map<Class<?>, PropertyEditor> defaultEditors;
    private Map<Class<?>, PropertyEditor> overriddenDefaultEditors;
    private Map<Class<?>, PropertyEditor> customEditors;
    private Map<String, CustomEditorHolder> customEditorsForPath;
    private Set<PropertyEditor> sharedEditors;
    private Map<Class<?>, PropertyEditor> customEditorForCathe;

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public ConversionService getConversionService() {
        return this.conversionService;
    }

    protected void registerDefaultEditors() {
        this.defaultEditorsActive = true;
    }

    public void setConfigValueEditors() {
        this.configValueEditorsActive = true;
    }

    public void overrideDefaultEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
        if (this.overriddenDefaultEditors == null) {
            this.overriddenDefaultEditors = new HashMap<Class<?>, PropertyEditor>();
        }
        this.overriddenDefaultEditors.put(requiredType, propertyEditor);
    }

    private void createDefaultEditors() {
        this.defaultEditors = new HashMap<Class<?>, PropertyEditor>();

        this.defaultEditors.put(Charset.class, new CharsetEditor());
        this.defaultEditors.put(Class.class, new ClassEditor());
        this.defaultEditors.put(Class[].class, new ClassArrayEditor());
        this.defaultEditors.put(Currency.class, new CurrencyEditor());
        this.defaultEditors.put(File.class, new FileEditor());
        this.defaultEditors.put(InputStream.class, new InputStreamEditor());
        this.defaultEditors.put(InputSource.class, new InputSourceEditor());
        this.defaultEditors.put(Locale.class, new LocaleEditor());
        this.defaultEditors.put(Pattern.class, new PatternEditor());
        this.defaultEditors.put(Properties.class, new PropertiesEditor());
        this.defaultEditors.put(Resource[].class, new ResourceArrayPropertyEditor());
        this.defaultEditors.put(TimeZone.class, new TimeZoneEditor());
        this.defaultEditors.put(URI.class, new URIEditor());
        this.defaultEditors.put(URL.class, new URLEditor());
        this.defaultEditors.put(UUID.class, new UUIDEditor());

        //collection editors
        this.defaultEditors.put(Collection.class, new CustomCollectionEditor(Collection.class));
        this.defaultEditors.put(Set.class, new CustomCollectionEditor(Set.class));
        this.defaultEditors.put(SortedSet.class, new CustomCollectionEditor(SortedSet.class));
        this.defaultEditors.put(List.class, new CustomCollectionEditor(List.class));
        this.defaultEditors.put(SortedMap.class, new CustomCollectionEditor(SortedMap.class));

        //primitive array
        this.defaultEditors.put(byte[].class, new ByteArrayPropertyEditor());
        this.defaultEditors.put(char[].class, new CharArrayPropertyEditor());

        //char
        this.defaultEditors.put(char.class, new CharacterEditor(false));
        this.defaultEditors.put(Character.class, new CharacterEditor(true));

        //boolean
        this.defaultEditors.put(boolean.class, new CustomBooleanEditor(false));
        this.defaultEditors.put(Boolean.class, new CustomBooleanEditor(true));

        //number
        this.defaultEditors.put(byte.class, new CustomNumberEditor(byte.class, false));
        this.defaultEditors.put(Byte.class, new CustomNumberEditor(Byte.class, true));
        this.defaultEditors.put(short.class, new CustomNumberEditor(short.class, false));
        this.defaultEditors.put(Short.class, new CustomNumberEditor(Short.class, true));
        this.defaultEditors.put(int.class, new CustomNumberEditor(int.class, false));
        this.defaultEditors.put(Integer.class, new CustomNumberEditor(Integer.class, true));
        this.defaultEditors.put(long.class, new CustomNumberEditor(long.class, false));
        this.defaultEditors.put(Long.class, new CustomNumberEditor(Long.class, true));
        this.defaultEditors.put(float.class, new CustomNumberEditor(float.class, false));
        this.defaultEditors.put(Float.class, new CustomNumberEditor(Float.class, true));
        this.defaultEditors.put(double.class, new CustomNumberEditor(double.class, false));
        this.defaultEditors.put(Double.class, new CustomNumberEditor(Double.class, true));
        this.defaultEditors.put(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, true));
        this.defaultEditors.put(BigInteger.class, new CustomNumberEditor(BigInteger.class, true));

        if (this.configValueEditorsActive) {
            StringArrayPropertyEditor sae = new StringArrayPropertyEditor();
            this.defaultEditors.put(String[].class, sae);
            this.defaultEditors.put(short[].class, sae);
            this.defaultEditors.put(int[].class, sae);
            this.defaultEditors.put(long[].class, sae);
        }
    }

    @Override
    public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
        // TODO Auto-generated method stub

    }

    @Override
    public void registerCustomEditor(Class<?> requiredType, String propertyPath, PropertyEditor propertyEditor) {
        // TODO Auto-generated method stub

    }

    @Override
    public PropertyEditor findCustomEditor(Class<?> requiredType, String propertyPath) {
        // TODO Auto-generated method stub
        return null;
    }

    private static class CustomEditorHolder {

        private final PropertyEditor propertyEditor;
        private final Class<?> registeredType;

        private CustomEditorHolder(PropertyEditor propertyEditor, Class<?> registeredType) {
            this.propertyEditor = propertyEditor;
            this.registeredType = registeredType;
        }

        private PropertyEditor getPropertyEditor() {
            return this.propertyEditor;
        }

        private Class<?> getRegisteredType() {
            return this.registeredType;
        }

        private PropertyEditor getPropertyEditor(Class<?> requiredType) {
            if (this.registeredType == null ||
                    (requiredType != null && (ClassUtils.isAssignable(this.registeredType, requiredType)
                            || ClassUtils.isAssignable(requiredType, this.registeredType)))
                    || (requiredType == null && (!Collection.class.isAssignableFrom(this.registeredType)
                            && !this.registeredType.isArray()))) {
                return this.propertyEditor;
            } else {
                return null;
            }
        }
    }

}
