package com.yansheng.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;

public interface BeanInfoFactory {

    //qqq
	BeanInfo getBeanInfo(Class<?> beanClass) throws IntrospectionException;
}
