package com.yansheng.beans.factory.xml;

import java.io.IOException;

import org.springframework.util.Assert;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DelegatingEntityResolver implements EntityResolver {

	public static final String DTD_SUFFIX = ".dtd";
	public static final String XSD_SUFFIX = ".xsd";

	private final EntityResolver dtdResolver;
	private final EntityResolver schemaResolver;

	public DelegatingEntityResolver(ClassLoader classLoader) {
		this.dtdResolver = new BeansDtdResolver();
		this.schemaResolver = new PluggableSchemaResolver(classLoader);
	}

	public DelegatingEntityResolver(EntityResolver dtdResolver, EntityResolver schemaResolver) {
		Assert.notNull(dtdResolver, "dtdResolver不能为null");
		Assert.notNull(schemaResolver, "schemaResolver不能为null");
		this.dtdResolver = dtdResolver;
		this.schemaResolver = schemaResolver;
	}

	@Override
	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
		if (systemId != null) {
			if (systemId.endsWith(DTD_SUFFIX)) {
				return this.dtdResolver.resolveEntity(publicId, systemId);
			} else if (systemId.endsWith(XSD_SUFFIX)) {
				return this.schemaResolver.resolveEntity(publicId, systemId);
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "EntityResolver delegating " + XSD_SUFFIX + " to " + this.schemaResolver + " and " + DTD_SUFFIX + " to "
				+ this.dtdResolver;
	}

}
