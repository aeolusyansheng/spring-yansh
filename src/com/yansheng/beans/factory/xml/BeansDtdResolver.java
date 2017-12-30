package com.yansheng.beans.factory.xml;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class BeansDtdResolver implements EntityResolver {

	private static final String DTD_EXTENSION = ".dtd";
	private static final String[] DTD_NAMES = { "spring-beans-2.0", "spring-beans" };

	private static final Log logger = LogFactory.getLog(BeansDtdResolver.class);

	@Override
	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
		if (logger.isTraceEnabled()) {
			logger.trace("尝试使用public ID" + publicId + "system ID" + systemId + "解析XML entity");
		}
		if (systemId != null && systemId.endsWith(DTD_EXTENSION)) {
			int lastPathSeparator = systemId.lastIndexOf("/");
			for (String DTD_NAME : DTD_NAMES) {
				int dtdNameStart = systemId.indexOf(DTD_NAME);
				if (dtdNameStart > lastPathSeparator) {
					String dtdFile = systemId.substring(dtdNameStart);
					if (logger.isTraceEnabled()) {
						logger.trace("尝试从spring jar中加载" + dtdFile);
					}
					try {
						Resource resource = new ClassPathResource(dtdFile, getClass());
						InputSource source = new InputSource(resource.getInputStream());
						source.setSystemId(systemId);
						source.setPublicId(publicId);
						if (logger.isDebugEnabled()) {
							logger.debug("在类路径" + dtdFile + "发现beans DTD [" + systemId + "[");
						}
						return source;
					} catch (IOException e) {
						if (logger.isDebugEnabled()) {
							logger.debug("无法解析beans DTD [" + systemId + "]:在类路径中找不到。", e);
						}
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "EntityResolver for DTDs " + Arrays.toString(DTD_NAMES);
	}
}
