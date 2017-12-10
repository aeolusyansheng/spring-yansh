package com.yansheng.beans.factory;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.util.StringUtils;

@SuppressWarnings("serial")
public class NoUniqueBeanDefinitionException extends NoSuchBeanDefinitionException {

	private int numberOfBeanFound;

	public NoUniqueBeanDefinitionException(Class<?> type, int numberOfBeanFound, String msg) {
		super(type, msg);
		this.numberOfBeanFound = numberOfBeanFound;
	}

	public NoUniqueBeanDefinitionException(Class<?> type, Collection<String> beanNamesFound) {
		this(type, beanNamesFound.size(), "希望只找到一个bean，但找到了" + beanNamesFound.size() + "个。找到的bean为:"
				+ StringUtils.collectionToCommaDelimitedString(beanNamesFound));
	}

	public NoUniqueBeanDefinitionException(Class<?> type, String... beanNamesFound) {
		this(type, Arrays.asList(beanNamesFound));
	}

	@Override
	public int getNumberOfBeansFound() {
		return this.numberOfBeanFound;
	}
}
