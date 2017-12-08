package helloworld;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.StringUtils;

public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    private Logger logger = Logger.getLogger(getClass());

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (int i = 0; i < beanDefinitionNames.length; i++) {
            String bdName = beanDefinitionNames[i];
            BeanDefinition rbd = beanFactory.getBeanDefinition(bdName);
            if (!StringUtils.hasText(rbd.getFactoryBeanName())) {
                logger.debug("this is not a factory bean");
            }
        }
    }

}
