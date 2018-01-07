package com.yansheng.beans.factory.xml;

import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import com.yansheng.beans.factory.BeanDefinitionStoreException;
import com.yansheng.beans.factory.config.BeanDefinition;
import com.yansheng.beans.factory.config.BeanDefinitionHolder;
import com.yansheng.beans.factory.parsing.BeanComponentDefinition;
import com.yansheng.beans.factory.support.AbstractBeanDefinition;
import com.yansheng.beans.factory.support.BeanDefinitionReaderUtils;
import com.yansheng.beans.factory.support.BeanDefinitionRegistry;

public abstract class AbstractBeanDefinitionParser implements BeanDefinitionParser {

	public static final String ID_ATTRIBUTE = "id";
	public static final String NAME_ATTRIBUTE = "name";

	@Override
	public final BeanDefinition parse(Element element, ParserContext parseContext) {
		AbstractBeanDefinition definition = parseInternal(element, parseContext);
		if (definition != null && !parseContext.isNested()) {
			try {
				String id = resolveId(element, definition, parseContext);
				if (!StringUtils.hasText(id)) {
					parseContext.getReaderContext().error(
							"标签" + parseContext.getDelegate().getLocalName(element) + "作为顶层标签，需要指定id属性", element);
				}
				String[] aliases = null;
				String name = element.getAttribute(NAME_ATTRIBUTE);
				if (StringUtils.hasText(name)) {
					aliases = StringUtils.trimArrayElements(StringUtils.commaDelimitedListToStringArray(name));
				}
				BeanDefinitionHolder holder = new BeanDefinitionHolder(definition, id, aliases);
				registerBeanDefinition(holder, parseContext.getRegistry());
				if (shouldFireEvents()) {
					BeanComponentDefinition componentDefinition = new BeanComponentDefinition(holder);
					postProcessComponentDefinition(componentDefinition);
					parseContext.registerComponent(componentDefinition);
				}
			} catch (BeanDefinitionStoreException e) {
				parseContext.getReaderContext().error(e.getMessage(), element);
				return null;
			}
		}
		return definition;
	}

	protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parseContext)
			throws BeanDefinitionStoreException {
		if (shouldGenerateId()) {
			return parseContext.getReaderContext().generateBeanName(definition);
		} else {
			String id = element.getAttribute(ID_ATTRIBUTE);
			if (!StringUtils.hasText(id) && shouldGenerateIdAsFallback()) {
				id = parseContext.getReaderContext().generateBeanName(definition);
			}
			return id;
		}
	}

	protected void registerBeanDefinition(BeanDefinitionHolder definition, BeanDefinitionRegistry registry) {
		BeanDefinitionReaderUtils.registerBeanDefinition(definition, registry);
	}

	protected abstract AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext);

	protected boolean shouldGenerateId() {
		return false;
	}

	protected boolean shouldGenerateIdAsFallback() {
		return false;
	}

	protected boolean shouldFireEvents() {
		return true;
	}

	protected void postProcessComponentDefinition(BeanComponentDefinition componentDefinition) {

	}

}
