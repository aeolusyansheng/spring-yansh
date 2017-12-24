package com.yansheng.beans.factory.annotation;

import org.springframework.core.type.AnnotationMetadata;

import com.yansheng.beans.factory.config.BeanDefinition;

public interface AnotationBeanDefinition extends BeanDefinition {

	AnnotationMetadata getMetadata();
}
