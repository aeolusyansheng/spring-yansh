package com.yansheng.beans.factory.xml;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import com.yansheng.beans.BeanUtils;
import com.yansheng.beans.FatalBeanException;

public class DefaultNamespaceHandlerResolver implements NamespaceHandlerResolver {

	public static final String DEFAULT_HANDLER_MAPPINGS_LOCATION = "META-INF/spring.handlers";

	protected final Log logger = LogFactory.getLog(getClass());

	protected final ClassLoader classLoader;

	private final String handlerMappingsLocation;

	/** 保存所有的名称空间解析器<URL,namespaceHandler 实例> */
	private volatile Map<String, Object> handlerMappings;

	public DefaultNamespaceHandlerResolver(ClassLoader classLoader, String handlerMappingsLocation) {
		Assert.notNull(handlerMappingsLocation, "Handler mappings location不能为null");
		this.classLoader = classLoader;
		this.handlerMappingsLocation = handlerMappingsLocation;
	}

	public DefaultNamespaceHandlerResolver() {
		this(null, DEFAULT_HANDLER_MAPPINGS_LOCATION);
	}

	public DefaultNamespaceHandlerResolver(ClassLoader classLoader) {
		this(classLoader, DEFAULT_HANDLER_MAPPINGS_LOCATION);
	}

	@Override
	public NamespaceHandler resolve(String namespaceUri) {
		Map<String, Object> handlerMappings = getHandlerMappings();
		Object handlerOrClassName = handlerMappings.get(namespaceUri);
		if (handlerOrClassName == null) {
			return null;
		} else if (handlerOrClassName instanceof NamespaceHandler) {
			return (NamespaceHandler) handlerOrClassName;
		} else {
			// class name
			// 用到某一个NamespaceHandler时，才从String转换成真正的处理Handler，最开始都是String
			String className = (String) handlerOrClassName;
			try {
				Class<?> handlerClass = ClassUtils.forName(className, this.classLoader);
				if (!NamespaceHandler.class.isAssignableFrom(handlerClass)) {
					throw new FatalBeanException("类" + className + "没有实现" + NamespaceHandler.class + "接口。");
				}
				NamespaceHandler namespaceHandler = (NamespaceHandler) BeanUtils.instantiateClass(handlerClass);
				namespaceHandler.init();
				// String -> namespaceHandler
				handlerMappings.put(namespaceUri, namespaceHandler);
				return namespaceHandler;
			} catch (ClassNotFoundException e) {
				throw new FatalBeanException("找不到名称空间" + namespaceUri + "对应的namespaceHandler类" + className, e);
			} catch (LinkageError e) {
				throw new FatalBeanException(
						"名称空间" + namespaceUri + "对应的namespaceHandler类" + className + "无效。类文件或者相关类有问题。", e);
			}
		}
	}

	/**
	 * 延迟一次性加载所有的 NamespaceHandler
	 */
	private Map<String, Object> getHandlerMappings() {
		if (this.handlerMappings == null) {
			synchronized (this) {
				// 防止被其他线程好做，再次判断
				if (this.handlerMappings == null) {
					try {
						Properties mappings = PropertiesLoaderUtils.loadAllProperties(this.handlerMappingsLocation,
								this.classLoader);
						if (logger.isDebugEnabled()) {
							logger.debug("加载的NamespaceHandler Mappings:" + mappings);
						}
						Map<String, Object> handlerMappings = new ConcurrentHashMap<String, Object>(mappings.size());
						CollectionUtils.mergePropertiesIntoMap(mappings, handlerMappings);
						this.handlerMappings = handlerMappings;
					} catch (IOException e) {
						throw new IllegalStateException(
								"从" + this.handlerMappingsLocation + "加载NamespaceHandler Mappings失败。");
					}
				}
			}
		}
		return this.handlerMappings;
	}

	@Override
	public String toString() {
		return "NamespaceHandlerResolver using mappings " + getHandlerMappings();
	}

}
