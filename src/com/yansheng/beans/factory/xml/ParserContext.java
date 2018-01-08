package com.yansheng.beans.factory.xml;

import java.util.Stack;

import com.yansheng.beans.factory.config.BeanDefinition;
import com.yansheng.beans.factory.parsing.BeanComponentDefinition;
import com.yansheng.beans.factory.parsing.ComponentDefinition;
import com.yansheng.beans.factory.parsing.CompositeComponentDefinition;
import com.yansheng.beans.factory.support.BeanDefinitionReaderUtils;
import com.yansheng.beans.factory.support.BeanDefinitionRegistry;

public final class ParserContext {

	private final XmlReaderContext readerContext;
	private final BeanDefinitionParserDelegate delegate;
	private BeanDefinition containingBeanDefinition;
	private final Stack<ComponentDefinition> containingComponents = new Stack<ComponentDefinition>();

	public ParserContext(XmlReaderContext readerContext, BeanDefinitionParserDelegate delegate,
			BeanDefinition containingBeanDefinition) {
		this.readerContext = readerContext;
		this.delegate = delegate;
		this.containingBeanDefinition = containingBeanDefinition;
	}

	public ParserContext(XmlReaderContext readerContext, BeanDefinitionParserDelegate delegate) {
		this.readerContext = readerContext;
		this.delegate = delegate;
	}

	public final XmlReaderContext getReaderContext() {
		return this.readerContext;
	}

	public final BeanDefinitionRegistry getRegistry() {
		return this.readerContext.getRegistry();
	}

	public final BeanDefinitionParserDelegate getDelegate() {
		return this.delegate;
	}

	public final BeanDefinition getContainingBeanDefinition() {
		return this.containingBeanDefinition;
	}

	public final boolean isNested() {
		return (this.containingBeanDefinition != null);
	}

	public boolean isDefaultLazyInit() {
		return BeanDefinitionParserDelegate.TRUE_VALUE.equals(this.delegate.getDefaults().getLazyInit());
	}

	public Object extractSource(Object sourceCandidate) {
		return this.readerContext.extractSource(sourceCandidate);
	}

	public CompositeComponentDefinition getContainingComponent() {
		return (!this.containingComponents.isEmpty()
				? (CompositeComponentDefinition) this.containingComponents.lastElement()
				: null);
	}

	public void pushContainingComponent(CompositeComponentDefinition containingComponent) {
		this.containingComponents.push(containingComponent);
	}

	public CompositeComponentDefinition popContainingComponent() {
		return (CompositeComponentDefinition) this.containingComponents.pop();
	}

	public void popAndRegisterContainingComponent() {
		registerComponent(popContainingComponent());
	}

	public void registerComponent(ComponentDefinition component) {
		CompositeComponentDefinition containingComponent = getContainingComponent();
		if (containingComponent != null) {
			containingComponent.addNestedComponents(component);
		} else {
			this.readerContext.fireComponentRegistered(component);
		}
	}

	public void registerBeanComponent(BeanComponentDefinition component) {
		BeanDefinitionReaderUtils.registerBeanDefinition(component, getRegistry());
		registerComponent(component);
	}

}
