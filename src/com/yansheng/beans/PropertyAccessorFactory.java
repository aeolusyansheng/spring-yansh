package com.yansheng.beans;

public abstract class PropertyAccessorFactory {

    public static BeanWrapper forBeanPropertyAccess(Object target) {
        return new BeanWrapperImpl(target);
    }

}
