package com.yansheng.beans.factory.xml;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ResourceEntityResolver extends DelegatingEntityResolver {

	private static final Log logger = LogFactory.getLog(ResourceEntityResolver.class);
	private final ResourceLoader resourceLoader;

	public ResourceEntityResolver(ResourceLoader resourceLoader) {
		super(resourceLoader.getClassLoader());
		this.resourceLoader = resourceLoader;
	}

	@SuppressWarnings("deprecation")
	@Override
	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
		InputSource source = super.resolveEntity(publicId, systemId);
		if (source == null && systemId != null) {
			String resourcePath = null;
			try {
				String decodedSystemId = URLDecoder.decode(systemId);
				String givenUrl = new URL(decodedSystemId).toString();
				String systemRootUrl = new File("").toURI().toURL().toString();
				if (givenUrl.startsWith(systemRootUrl)) {
					resourcePath = givenUrl.substring(systemRootUrl.length());
				}
			} catch (Exception e) {
				if (logger.isDebugEnabled()) {
					logger.debug("无法从system root URL解析XML entity:" + systemId + "");
				}
				resourcePath = systemId;
			}
			if (resourcePath != null) {
				if (logger.isTraceEnabled()) {
					logger.trace("尝试从路径" + resourcePath + "加载资源。");
				}
				Resource resource = this.resourceLoader.getResource(resourcePath);
				source = new InputSource(resource.getInputStream());
				source.setSystemId(systemId);
				source.setPublicId(publicId);
				if (logger.isDebugEnabled()) {
					logger.debug("发现XML entity [" + systemId + "]:" + resource);
				}
			}
		}
		return source;
	}
}
