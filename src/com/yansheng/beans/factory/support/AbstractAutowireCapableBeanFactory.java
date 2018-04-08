package com.yansheng.beans.factory.support;

import java.util.Set;

import com.yansheng.beans.BeansException;
import com.yansheng.beans.TypeConverter;
import com.yansheng.beans.factory.BeanCreationException;
import com.yansheng.beans.factory.config.AutowireCapableBeanFactory;
import com.yansheng.beans.factory.config.BeanDefinition;
import com.yansheng.beans.factory.config.DependencyDescriptor;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory
		implements AutowireCapableBeanFactory {

	@Override
	public <T> T getBean(Class<T> requiredType) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T createBean(Class<T> beanClass) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void autowireBean(Object existingBean) throws BeansException {
		// TODO Auto-generated method stub

	}

	@Override
	public Object configureBean(Object existingBean, String beanName) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object resolveDependency(DependencyDescriptor descriptor, String beanName) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object createBean(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object autowire(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void autowireBeanProperties(Object existingBean, int autowireMode, boolean dependencyCheck)
			throws BeansException {
		// TODO Auto-generated method stub

	}

	@Override
	public void applyBeanPropertyValues(Object existingBean, String beanName) throws BeansException {
		// TODO Auto-generated method stub

	}

	@Override
	public Object initializeBean(Object existingBean, String beanName) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object applyBeanPostProcessorBeforeInitialization(Object existingBean, String beanName)
			throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object applyBeanPostProcessorAfterInitialization(Object existingBean, String beanName)
			throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object resolveDependency(DependencyDescriptor descriptor, String beanName, Set<String> autowiredBeanNames,
			TypeConverter typeCpnverter) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean containsBeanDefinition(String beanName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected BeanDefinition getBeanDefinition(String beanName) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object createBean(String beanName, RootBeanDefinition mbd, Object[] args) throws BeanCreationException {
		// TODO Auto-generated method stub
		return null;
	}

}
