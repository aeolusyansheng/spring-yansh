package com.yansheng.beans.factory.xml;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.CollectionUtils;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class PluggableSchemaResolver implements EntityResolver {

	public static final String DEFAULT_SCHEMA_MAPPINGS_LOCATION = "META-INF/spring.schemas";
	private static final Log logger = LogFactory.getLog(PluggableSchemaResolver.class);
	private final ClassLoader classLoader;
	private final String schemaMappingsLocation;

	private volatile Map<String, String> schemaMappings;

	public PluggableSchemaResolver(ClassLoader classLoader) {
		this.classLoader = classLoader;
		this.schemaMappingsLocation = DEFAULT_SCHEMA_MAPPINGS_LOCATION;
	}

	public PluggableSchemaResolver(ClassLoader classLoader, String schemaMappingsLocation) {
		this.classLoader = classLoader;
		this.schemaMappingsLocation = schemaMappingsLocation;
	}

	@Override
	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
		if (logger.isTraceEnabled()) {
			logger.trace("尝试解析XML entity.public id [" + publicId + "].system id [" + systemId + "]");
		}
		if (systemId != null) {
			String resourceLocation = getSchemaMappings().get(systemId);
			if (resourceLocation != null) {
				Resource resource = new ClassPathResource(resourceLocation, this.classLoader);
				try {
					InputSource source = new InputSource(resource.getInputStream());
					source.setSystemId(systemId);
					source.setPublicId(publicId);
					if (logger.isDebugEnabled()) {
						logger.debug("在类路径下找到 XML schema [" + systemId + "]");
					}
					return source;
				} catch (IOException e) {
					if (logger.isDebugEnabled()) {
						logger.debug("在类路径下找到 XML schema [" + systemId + "]:" + resource, e);
					}
				}
			}
		}
		return null;
	}

	private Map<String, String> getSchemaMappings() {
		if (this.schemaMappings == null) {
			synchronized (this) {
				// 防止其他线程
				if (this.schemaMappings == null) {
					if (logger.isDebugEnabled()) {
						logger.debug("从" + this.schemaMappingsLocation + "加载schema mappings");
					}
					try {
						Properties mappings = PropertiesLoaderUtils.loadAllProperties(this.schemaMappingsLocation,
								this.classLoader);
						Map<String, String> schemaMappings = new ConcurrentHashMap<String, String>(mappings.size());
						CollectionUtils.mergePropertiesIntoMap(mappings, schemaMappings);
						this.schemaMappings = schemaMappings;
					} catch (IOException e) {
						throw new IllegalStateException("无法从路径" + this.schemaMappingsLocation + "加载 schema mappings ",
								e);
					}
				}
			}
		}
		return this.schemaMappings;
	}

	@Override
	public String toString() {
		return "EntityResolver using mappings " + getSchemaMappings();
	}

}
