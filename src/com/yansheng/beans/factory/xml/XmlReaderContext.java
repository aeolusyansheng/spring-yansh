package com.yansheng.beans.factory.xml;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.yansheng.beans.factory.config.BeanDefinition;
import com.yansheng.beans.factory.parsing.ProblemReporter;
import com.yansheng.beans.factory.parsing.ReaderEventListener;
import com.yansheng.beans.factory.parsing.SourceExtractor;
import com.yansheng.beans.factory.support.BeanDefinitionRegistry;

public class XmlReaderContext extends ReaderContext {

	private final XmlBeanDefinitionReader reader;
	private final NamespaceHandlerResolver namespaceHandlerResolver;

	public XmlReaderContext(Resource reource, ProblemReporter problemReporter, ReaderEventListener eventlistener,
			SourceExtractor sourceExtractor, XmlBeanDefinitionReader reader,
			NamespaceHandlerResolver namespaceHandlerResolver) {
		super(reource, problemReporter, eventlistener, sourceExtractor);
		this.reader = reader;
		this.namespaceHandlerResolver = namespaceHandlerResolver;
	}

	public final XmlBeanDefinitionReader getReader() {
		return this.reader;
	}

	public final BeanDefinitionRegistry getRegistry() {
		return this.reader.getRegistry();
	}

	public final ResourceLoader getResourceLoader() {
		return this.reader.getResourceLoader();
	}

	public final ClassLoader getBeanClassLoader() {
		return this.reader.getBeanClassLoader();
	}

	public final NamespaceHandlerResolver getNamespaceHandlerResolver() {
		return this.namespaceHandlerResolver;
	}

	public String generateBeanName(BeanDefinition beanDefinition) {
		return this.reader.getBeanNameGenerator().generateBeanName(beanDefinition, getRegistry());
	}

	public String registerWithGeneratedName(BeanDefinition beanDefinition) {
		String generatedName = generateBeanName(beanDefinition);
		getRegistry().registerBeanDefinition(generatedName, beanDefinition);
		return generatedName;
	}
}
