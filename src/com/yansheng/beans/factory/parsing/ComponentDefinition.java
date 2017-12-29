package com.yansheng.beans.factory.parsing;

import com.yansheng.beans.BeanMetadataElement;
import com.yansheng.beans.factory.config.BeanDefinition;
import com.yansheng.beans.factory.config.BeanReference;

public interface ComponentDefinition extends BeanMetadataElement {

	String getName();
	String getDescription();
	BeanDefinition[] getBeanDefinitions();
	BeanDefinition[] getInnerBeanDefinitions();
	BeanReference[] getBeanReferences();
}
