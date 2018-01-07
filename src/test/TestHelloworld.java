package test;

import java.util.Locale;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.ObjectUtils;

import helloworld.MyBean;
import helloworld.MyBeanFactoryPostProcessor;

public class TestHelloworld {

	// private Logger logger_commons= Logger.getLogger(getClass());
	private Logger logger = Logger.getLogger(getClass());

	@SuppressWarnings("resource")
	@Test
	public void testMessage() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("Root-Context.xml");
		String message1 = context.getMessage("lang", null, Locale.ENGLISH);
		String message2 = context.getMessage("lang", null, Locale.CHINA);
		String message3 = context.getMessage("lang", null, Locale.JAPAN);
		String message4 = context.getMessage("lang", null, Locale.getDefault());
		logger.debug(message2);
		logger.debug(message3);
		logger.debug(message4);
	}

	@Test
	public void testHelloworld() {

		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
		reader.loadBeanDefinitions("Root-Context.xml");
		factory.preInstantiateSingletons();
		MyBean myBean = factory.getBean("myBean", MyBean.class);
		// System.out.println(myBean.getSex());
		logger.debug("this is commons log for " + myBean.getSex());

	}

	@SuppressWarnings("resource")
	@Test
	public void testBeanFactoryPostProcess() {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "Root-Context.xml" },
				false);
		context.addBeanFactoryPostProcessor(new MyBeanFactoryPostProcessor());
		context.refresh();
		MyBean mybean = context.getBean("myBean", MyBean.class);
		logger.debug("this is commons log for " + mybean.getSex());
	}

	@Test
	public void testAop() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("Root-Context.xml");
		MyBean mybean = context.getBean("myBean", MyBean.class);
		mybean.toString2(mybean.getSex());
	}

	@Test
	public void testHex() {
		String aa = "qqq";
		String hex = ObjectUtils.getIdentityHexString(aa);
		System.out.println(hex);
	}

	@Test
	public void testParseBeanDefinition() {
		com.yansheng.beans.factory.support.DefaultListableBeanFactory factory = new com.yansheng.beans.factory.support.DefaultListableBeanFactory();
		com.yansheng.beans.factory.xml.XmlBeanDefinitionReader reader = new com.yansheng.beans.factory.xml.XmlBeanDefinitionReader(
				factory);
		reader.loadBeanDefinitions("Root-Context.xml");
		System.out.println(factory.getBeanDefinition("myBean").getPropertyValues().toString());
	}

}
