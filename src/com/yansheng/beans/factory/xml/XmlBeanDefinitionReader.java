package com.yansheng.beans.factory.xml;

import java.util.Set;

import org.springframework.core.Constants;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.xml.SimpleSaxErrorHandler;
import org.springframework.util.xml.XmlValidationModeDetector;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

import com.yansheng.beans.factory.BeanDefinitionStoreException;
import com.yansheng.beans.factory.parsing.EmptyReaderEventListener;
import com.yansheng.beans.factory.parsing.FailFastProblemReporter;
import com.yansheng.beans.factory.parsing.NullSourceExtractor;
import com.yansheng.beans.factory.parsing.ProblemReporter;
import com.yansheng.beans.factory.parsing.ReaderEventListener;
import com.yansheng.beans.factory.parsing.SourceExtractor;
import com.yansheng.beans.factory.support.AbstractBeanDefinitonReader;
import com.yansheng.beans.factory.support.BeanDefinitionRegistry;

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
	
	

	@Override
	public int loadBeanDefinition(Resource resource) throws BeanDefinitionStoreException {
		// TODO Auto-generated method stub
		return 0;
	}

}
