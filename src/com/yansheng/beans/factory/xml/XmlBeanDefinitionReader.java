package com.yansheng.beans.factory.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.core.Constants;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.io.DescriptiveResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.Assert;
import org.springframework.util.xml.SimpleSaxErrorHandler;
import org.springframework.util.xml.XmlValidationModeDetector;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.yansheng.beans.BeanUtils;
import com.yansheng.beans.factory.BeanDefinitionStoreException;
import com.yansheng.beans.factory.parsing.EmptyReaderEventListener;
import com.yansheng.beans.factory.parsing.FailFastProblemReporter;
import com.yansheng.beans.factory.parsing.NullSourceExtractor;
import com.yansheng.beans.factory.parsing.ProblemReporter;
import com.yansheng.beans.factory.parsing.ReaderEventListener;
import com.yansheng.beans.factory.parsing.SourceExtractor;
import com.yansheng.beans.factory.support.AbstractBeanDefinitonReader;
import com.yansheng.beans.factory.support.BeanDefinitionReader;
import com.yansheng.beans.factory.support.BeanDefinitionRegistry;

import jdk.internal.org.xml.sax.SAXParseException;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitonReader {

	public static final int VALIDATION_NONE = XmlValidationModeDetector.VALIDATION_NONE;
	public static final int VALIDATION_AUTO = XmlValidationModeDetector.VALIDATION_AUTO;
	public static final int VALIDATION_DTD = XmlValidationModeDetector.VALIDATION_DTD;
	public static final int VALIDATION_XSD = XmlValidationModeDetector.VALIDATION_XSD;

	private static final Constants constants = new Constants(XmlBeanDefinitionReader.class);

	private Class<?> documentReaderClass = DefaultBeanDefinitionDocumentReader.class;
	private ProblemReporter problemReporter = new FailFastProblemReporter();
	private ReaderEventListener eventListener = new EmptyReaderEventListener();
	private SourceExtractor sourceExtractor = new NullSourceExtractor();
	private NamespaceHandlerResolver namespaceHandlerResolver;

	private int validationMode = VALIDATION_AUTO;
	private boolean namespaceAware = false;
	private DocumentLoader documentLoader = new DefaultDocumentLoader();
	private EntityResolver entityResolver;
	private ErrorHandler errorHandler = new SimpleSaxErrorHandler(logger);
	private final XmlValidationModeDetector validationModeDetector = new XmlValidationModeDetector();

	private final ThreadLocal<Set<EncodedResource>> resourcesCurrentlyBeingLoaded = new NamedThreadLocal<Set<EncodedResource>>(
			"XML bean definition resources currently being loaded");

	protected XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
		super(registry);
	}

	public void setValidating(boolean validating) {
		this.validationMode = (validating ? VALIDATION_AUTO : VALIDATION_NONE);
		this.namespaceAware = !validating;
	}

	public void setValidationMode(int validationMode) {
		this.validationMode = validationMode;
	}

	public void setValidationModeName(String validationName) {
		setValidationMode(constants.asNumber(validationName).intValue());
	}

	public int getValidationMode() {
		return this.validationMode;
	}

	public void setNamespaceAware(boolean namespaceAware) {
		this.namespaceAware = namespaceAware;
	}

	public boolean isNamespaceAware() {
		return this.namespaceAware;
	}

	public void setProblemReporter(ProblemReporter problemReporter) {
		this.problemReporter = (problemReporter != null ? problemReporter : new FailFastProblemReporter());
	}

	public void setEventListener(ReaderEventListener eventListener) {
		this.eventListener = (eventListener != null ? eventListener : new EmptyReaderEventListener());
	}

	public void setSourceExtractor(SourceExtractor sourceExtractor) {
		this.sourceExtractor = (sourceExtractor != null ? sourceExtractor : new NullSourceExtractor());
	}

	public void setNamespaceHandlerResolver(NamespaceHandlerResolver namespaceHandlerResolver) {
		this.namespaceHandlerResolver = namespaceHandlerResolver;
	}

	public void setDocumentLoader(DocumentLoader documentLoader) {
		this.documentLoader = (documentLoader != null ? documentLoader : new DefaultDocumentLoader());
	}

	public void setEntityResolver(EntityResolver entityResolver) {
		this.entityResolver = entityResolver;
	}

	public EntityResolver getEntityResolver() {
		if (this.entityResolver == null) {
			ResourceLoader resourceLoader = getResourceLoader();
			if (resourceLoader != null) {
				this.entityResolver = new ResourceEntityResolver(resourceLoader);
			} else {
				this.entityResolver = new DelegatingEntityResolver(getBeanClassLoader());
			}
		}
		return this.entityResolver;
	}

	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	public void setDocumentReaderClass(Class<?> documentReaderClass) {
		if (documentReaderClass == null || !BeanDefinitionReader.class.isAssignableFrom(documentReaderClass)) {
			throw new IllegalArgumentException("documentReaderClass必须实现BeanDefinitionReader接口。");
		}
		this.documentReaderClass = documentReaderClass;
	}

	@Override
	public int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException {
		return loadBeanDefinition(new EncodedResource(resource));
	}

	public int loadBeanDefinition(EncodedResource encodedResource) throws BeanDefinitionStoreException {
		Assert.notNull(encodedResource, "EncodedResource不能为null");
		if (logger.isInfoEnabled()) {
			logger.info("从" + encodedResource.getResource() + "加载XML bean definitions");
		}
		Set<EncodedResource> currentResources = this.resourcesCurrentlyBeingLoaded.get();
		if (currentResources == null) {
			currentResources = new HashSet<EncodedResource>(4);
			this.resourcesCurrentlyBeingLoaded.set(currentResources);
		}
		if (!currentResources.add(encodedResource)) {
			// set里已经存在
			throw new BeanDefinitionStoreException("检测到" + encodedResource + " XML 文件被循环加载。检查import标签配置");
		}
		try {
			InputStream inputStream = encodedResource.getResource().getInputStream();
			try {
				InputSource inputSource = new InputSource(inputStream);
				if (encodedResource.getEncoding() != null) {
					inputSource.setEncoding(encodedResource.getEncoding());
				}
				return doLoadBeanDefinitions(inputSource, encodedResource.getResource());
			} finally {
				inputStream.close();
			}
		} catch (IOException e) {
			throw new BeanDefinitionStoreException("从" + encodedResource.getResource() + "解析XML文件时发生IO异常。", e);
		} finally {
			currentResources.remove(encodedResource);
			if (currentResources.isEmpty()) {
				this.resourcesCurrentlyBeingLoaded.remove();
			}
		}
	}

	public int loadBeanDefinitions(InputSource inputSource) throws BeanDefinitionStoreException {
		return loadBeanDefinitions(inputSource, "直接通过SAX InputSource加载。");
	}

	public int loadBeanDefinitions(InputSource inputSource, String resourceDescription)
			throws BeanDefinitionStoreException {
		return doLoadBeanDefinitions(inputSource, new DescriptiveResource(resourceDescription));
	}

	public int doLoadBeanDefinitions(InputSource inputSource, Resource resource) throws BeanDefinitionStoreException {
		int validationMode = getValidationModeForResource(resource);
		try {
			Document doc = this.documentLoader.loadDocument(inputSource, getEntityResolver(), this.errorHandler,
					validationMode, isNamespaceAware());
			return registerBeanDefinitions(doc, resource);
		} catch (BeanDefinitionStoreException e) {
			throw e;
		} catch (SAXParseException e) {
			throw new BeanDefinitionStoreException(resource.getDescription(), "XML文件的第" + e.getLineNumber() + "行不正确。",
					e);
		} catch (SAXException e) {
			throw new BeanDefinitionStoreException(resource.getDescription(), "XML文件" + resource + "不正确。", e);
		} catch (ParserConfigurationException ex) {
			throw new BeanDefinitionStoreException(resource.getDescription(),
					"Parser configuration exception parsing XML from " + resource, ex);
		} catch (IOException e) {
			throw new BeanDefinitionStoreException(resource.getDescription(), "解析XML文件" + resource + "发生IO异常。", e);
		} catch (Throwable e) {
			throw new BeanDefinitionStoreException(resource.getDescription(), "解析XML文件" + resource + "发生意外异常。", e);
		}
	}

	public int registerBeanDefinitions(Document doc, Resource resource) throws BeanDefinitionStoreException {
		BeanDefinitionDocumentReader documentReader = createBeanDefinitionDocumentReader();
		documentReader.setEnvironment(this.getEnvironment());
		int countBefore = getRegistry().getBeanDefinitonCount();
		documentReader.registerBeanDefinitions(doc, createReaderContext(resource));
		return getRegistry().getBeanDefinitonCount() - countBefore;
	}

	protected BeanDefinitionDocumentReader createBeanDefinitionDocumentReader() {
		return BeanDefinitionDocumentReader.class.cast(BeanUtils.instantiateClass(this.documentReaderClass));
	}

	protected int getValidationModeForResource(Resource resource) {
		int validationModeToUse = getValidationMode();
		if (validationModeToUse != VALIDATION_AUTO) {
			return validationModeToUse;
		}
		int detectedMode = detectValidationMode(resource);
		if (detectedMode != VALIDATION_AUTO) {
			return detectedMode;
		}

		// 无法判断，就使用XSD吧
		return VALIDATION_XSD;
	}

	protected int detectValidationMode(Resource resource) {
		// 解析使用DTD还是XSD方式
		if (resource.isOpen()) {
			throw new BeanDefinitionStoreException("资源" + resource + "处于打开状态，无法解析。");
		}
		InputStream inputStream;
		try {
			inputStream = resource.getInputStream();
		} catch (IOException e) {
			throw new BeanDefinitionStoreException(
					"无法打开资源" + resource + "是否直接指定" + "SAX InputSource而没有指定validationMode？");
		}
		try {
			return this.validationModeDetector.detectValidationMode(inputStream);
		} catch (IOException e) {
			throw new BeanDefinitionStoreException("无法通过资源" + resource + "解析校验模式:读取inputStream时发生错误。");
		}
	}

	protected XmlReaderContext createReaderContext(Resource resource) {
		if (this.namespaceHandlerResolver == null) {
			this.namespaceHandlerResolver = createDefaultNamespaceHandlerResolver();
		}
		return new XmlReaderContext(resource, this.problemReporter, this.eventListener, this.sourceExtractor, this,
				this.namespaceHandlerResolver);
	}

	protected NamespaceHandlerResolver createDefaultNamespaceHandlerResolver() {
		return new DefaultNamespaceHandlerResolver(getResourceLoader().getClassLoader());
	}

}
