package com.springbyexample.beanfactorypostprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author Mujuzi Moses
 */
public class ReportBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        BeanDefinition definition = beanFactory.getBeanDefinition("reportService");

        MutablePropertyValues properties = definition.getPropertyValues();

        properties.add("reportName", "Monthly Sales Report");
    }
}
