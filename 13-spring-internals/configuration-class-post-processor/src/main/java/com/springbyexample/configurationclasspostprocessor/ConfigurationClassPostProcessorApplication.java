package com.springbyexample.configurationclasspostprocessor;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;

/**
 * @author Mujuzi Moses
 */
public class ConfigurationClassPostProcessorApplication {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        beanFactory.registerBeanDefinition("appConfig", new RootBeanDefinition(AppConfig.class));

        ConfigurationClassPostProcessor postProcessor = new ConfigurationClassPostProcessor();
        postProcessor.processConfigBeanDefinitions(beanFactory);
        postProcessor.postProcessBeanFactory(beanFactory);

        beanFactory.preInstantiateSingletons();

        GreetingController controller = beanFactory.getBean(GreetingController.class);

        String applicationName = beanFactory.getBean("applicationName", String.class);

        System.out.println(controller.greet());
        System.out.println(applicationName);
    }
}
