package com.yansheng.beans.factory.annotation;

import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.util.Assert;

import com.yansheng.beans.factory.support.GenericBeanDefinition;

@SuppressWarnings("serial")
public class AnotatedGenericBeanDefinition extends GenericBeanDefinition implements AnotationBeanDefinition {

	private final AnnotationMetadata metadata;

	public AnotatedGenericBeanDefinition(Class<?> beanClass) {
		setBeanClass(beanClass);
		this.metadata = new StandardAnnotationMetadata(beanClass, true);
	}

	public AnotatedGenericBeanDefinition(AnnotationMetadata metadata) {
		Assert.notNull(metadata, "AnnotationMetadata不能为null。");
		if (metadata instanceof StandardAnnotationMetadata) {
			setBeanClass(((StandardAnnotationMetadata) metadata).getIntrospectedClass());
		} else {
			setBeanClassName(metadata.getClassName());
		}
		this.metadata = metadata;
	}

	@Override
	public AnnotationMetadata getMetadata() {
		return this.metadata;
	}

}
