package com.yansheng.beans.factory.annotation;

import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.Assert;

import com.yansheng.beans.factory.support.GenericBeanDefinition;

@SuppressWarnings("serial")
public class ScannedGenericBeanDefinition extends GenericBeanDefinition implements AnotationBeanDefinition {

	private final AnnotationMetadata metadata;

	public ScannedGenericBeanDefinition(MetadataReader metadataReader) {
		Assert.notNull(metadataReader, "MetadataReader不能为null。");
		this.metadata = metadataReader.getAnnotationMetadata();
		setBeanClassName(this.metadata.getClassName());
	}

	@Override
	public final AnnotationMetadata getMetadata() {
		return this.metadata;
	}

}
