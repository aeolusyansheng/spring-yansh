package com.yansheng.beans.factory.parsing;

import org.springframework.util.Assert;

import com.yansheng.beans.BeanMetadataElement;

public class AliasDefinition implements BeanMetadataElement {

	private final String beanName;
	private final String alias;
	private final Object source;

	public AliasDefinition(String beanName, String alias) {
		this(beanName, alias, null);
	}

	public AliasDefinition(String beanName, String alias, Object source) {
		Assert.notNull(beanName, "Bean name 不能为null");
		Assert.notNull(alias, "Alias 不能为null");
		this.beanName = beanName;
		this.alias = alias;
		this.source = source;
	}

	public final String getBeanName() {
		return this.beanName;
	}

	public final String getAlias() {
		return this.alias;
	}

	@Override
	public final Object getSource() {
		return this.source;
	}

}
