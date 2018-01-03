package com.yansheng.beans.factory.xml;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.yansheng.beans.factory.BeanDefinitionStoreException;
import com.yansheng.beans.factory.config.BeanDefinitionHolder;
import com.yansheng.beans.factory.parsing.BeanComponentDefinition;
import com.yansheng.beans.factory.support.BeanDefinitionReaderUtils;

public class DefaultBeanDefinitionDocumentReader implements BeanDefinitionDocumentReader {

	public static final String BEAN_ELEMENT = BeanDefinitionParserDelegate.BEAN_ELEMENT;
	public static final String NESTED_BEANS_ELEMENT = "beans";
	public static final String ALIAS_ELEMENT = "alias";
	public static final String NAME_ATTRIBUTE = "name";
	public static final String ALIAS_ATTRIBUTE = "alias";
	public static final String IMPORT_ELEMENT = "import";
	public static final String RESOURCE_ATTRIBUTE = "resource";
	public static final String PROFILE_ATTRIBUTE = "profile";

	protected final Log logger = LogFactory.getLog(getClass());

	private Environment environment;
	private XmlReaderContext readerContext;
	private BeanDefinitionParserDelegate delegate;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void registerBeanDefinitions(Document doc, XmlReaderContext readerContext)
			throws BeanDefinitionStoreException {

		this.readerContext = readerContext;
		logger.debug("开始加载Bean definitions");
		Element root = doc.getDocumentElement();
		doRegisterBeanDefinitions(root);
	}

	protected void doRegisterBeanDefinitions(Element root) {
		// 处理profile属性
		String profileSpec = root.getAttribute(PROFILE_ATTRIBUTE);
		if (StringUtils.hasText(profileSpec)) {
			Assert.state(this.environment != null, "必须设定Environment来解析profile");
			String[] specifiedProfiles = StringUtils.tokenizeToStringArray(profileSpec,
					BeanDefinitionParserDelegate.MUTI_VALUE_ATTRIBUTE_DELIMITERS);
			if (!this.environment.acceptsProfiles(specifiedProfiles)) {
				return;
			}
		}
		BeanDefinitionParserDelegate parent = this.delegate;
		this.delegate = createHelper(this.readerContext, root, parent);
		preProcessXml(root);
		// 解析bean definitions
		parseBeanDefinitions(root, this.delegate);
		postProcessXml(root);

	}

	protected void parseBeanDefinitions(Element root, BeanDefinitionParserDelegate delegate) {
		if (delegate.isDefaultNamespace(root)) {
			NodeList nl = root.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node node = nl.item(i);
				//  只处理元素，不处理属性
				if (node instanceof Element) {
					Element ele = (Element) node;
					if (delegate.isDefaultNamespace(ele)) {
						// 解析<bean>标签
						parseDefaultElement(ele, delegate);
					} else {
						// 解析自定义标签
						delegate.parseCustomElement(ele);
					}
				}
			}
		} else {
			delegate.parseCustomElement(root);
		}

	}

	private void parseDefaultElement(Element ele, BeanDefinitionParserDelegate delegate) {
		if (delegate.nodeNameEquals(ele, IMPORT_ELEMENT)) {
			// <import>
			importBeanDefinitionResource(ele);
		} else if (delegate.nodeNameEquals(ele, ALIAS_ELEMENT)) {
			// <alias>
			processAliasRegistration(ele);
		} else if (delegate.nodeNameEquals(ele, BEAN_ELEMENT)) {
			// <bean>
			processBeanDefinition(ele, delegate);
		} else if (delegate.nodeNameEquals(ele, NESTED_BEANS_ELEMENT)) {
			// <beans>
			// 递归
			doRegisterBeanDefinitions(ele);
		}
	}

	protected BeanDefinitionParserDelegate createHelper(XmlReaderContext readerContext, Element root,
			BeanDefinitionParserDelegate parent) {
		BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegate(readerContext, this.environment);
		delegate.initDefaults(root, parent);
		return delegate;
	}

	protected final XmlReaderContext getReaderContext() {
		return this.readerContext;
	}

	protected Object extractSource(Element ele) {
		return this.readerContext.extractSource(ele);
	}

	protected void importBeanDefinitionResource(Element ele) {
		// 取resource属性
		String location = ele.getAttribute(RESOURCE_ATTRIBUTE);
		if (!StringUtils.hasText(location)) {
			getReaderContext().error("resource属性不能为空。", ele);
			return;
		}
		location = environment.resolveRequiredPlaceholders(location);
		Set<Resource> actualResources = new LinkedHashSet<Resource>(4);
		boolean absoluteLocation = false;
		try {
			absoluteLocation = ResourcePatternUtils.isUrl(location) || ResourceUtils.toURI(location).isAbsolute();
		} catch (URISyntaxException e) {
			// 当做相对路径处理
		}

		// 绝对路径还是相对路径?
		if (absoluteLocation) {
			try {
				int importCount = getReaderContext().getReader().loadBeanDefinition(location, actualResources);
				if (logger.isDebugEnabled()) {
					logger.debug("从" + location + "加载了" + importCount + "个bean definitions");
				}
			} catch (BeanDefinitionStoreException e) {
				getReaderContext().error("无法从" + location + "加载bean definitions", ele, e);
			}
		} else {
			try {
				int importCount;
				// 根据相对路径寻找相对资源
				Resource relativeResource = getReaderContext().getResource().createRelative(location);
				if (relativeResource.exists()) {
					importCount = getReaderContext().getReader().loadBeanDefinitions(relativeResource);
					actualResources.add(relativeResource);
				} else {
					String baseLocation = getReaderContext().getResource().getURL().toString();
					// 直接拼接在当前资源的URL之后
					importCount = getReaderContext().getReader()
							.loadBeanDefinition(StringUtils.applyRelativePath(baseLocation, location), actualResources);
					if (logger.isDebugEnabled()) {
						logger.debug("从" + location + "加载了" + importCount + "个bean definitions");
					}
				}
			} catch (BeanDefinitionStoreException e) {
				getReaderContext().error("无法从" + location + "加载bean definitions", ele, e);
			} catch (IOException e) {
				getReaderContext().error("无法解析资源路径location", ele, e);
			}
		}
		Resource[] actResArray = actualResources.toArray(new Resource[actualResources.size()]);
		getReaderContext().fireImportProcessed(location, actResArray, extractSource(ele));
	}

	protected void processAliasRegistration(Element ele) {
		String name = ele.getAttribute(NAME_ATTRIBUTE);
		String alias = ele.getAttribute(ALIAS_ATTRIBUTE);
		boolean valid = true;
		if (!StringUtils.hasText(name)) {
			getReaderContext().error("name元素不能为空", ele);
			valid = false;
		}
		if (!StringUtils.hasText(alias)) {
			getReaderContext().error("alias元素不能为空", ele);
			valid = false;
		}
		if (valid) {
			try {
				getReaderContext().getRegistry().registerAlias(name, alias);
			} catch (Exception e) {
				getReaderContext().error("注册alias:" + alias + "name:" + name + "失败", ele, e);
			}
			getReaderContext().fireAliasRegistered(name, alias, extractSource(ele));
		}
	}

	protected void processBeanDefinition(Element ele, BeanDefinitionParserDelegate delegate) {
		BeanDefinitionHolder bdHolder = delegate.parseBeanDefinitionElement(ele);
		if (bdHolder != null) {
			bdHolder = delegate.decorateBeanDefinitionIfRequired(ele, bdHolder);
			try {
				// 注册包装完成的bean definition
				BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, getReaderContext().getRegistry());
			} catch (BeanDefinitionStoreException e) {
				getReaderContext().error("注册bean definition失败。name:" + bdHolder.getBeanName(), ele, e);
			}
			getReaderContext().fireComponentRegistered(new BeanComponentDefinition(bdHolder));
		}
	}

	protected void preProcessXml(Element root) {

	}

	protected void postProcessXml(Element root) {

	}

}
