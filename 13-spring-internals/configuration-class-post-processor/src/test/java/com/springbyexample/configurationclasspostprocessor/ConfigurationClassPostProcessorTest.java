package com.springbyexample.configurationclasspostprocessor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;

/**
 * @author Mujuzi Moses
 */
public class ConfigurationClassPostProcessorTest {

    @Test
    void shouldProcessConfigurationClass() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("appConfig", new RootBeanDefinition(AppConfig.class));

        ConfigurationClassPostProcessor postProcessor = new ConfigurationClassPostProcessor();
        postProcessor.processConfigBeanDefinitions(beanFactory);

        assertTrue(beanFactory.containsBeanDefinition("greetingController"));
        assertTrue(beanFactory.containsBeanDefinition("additionalConfig"));
        assertTrue(beanFactory.containsBeanDefinition("applicationName"));
        assertTrue(beanFactory.containsBeanDefinition("greetingService"));
    }

    @Test
    void shouldCreateBeansFromProcessedConfiguration() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("appConfig", new RootBeanDefinition(AppConfig.class));

        ConfigurationClassPostProcessor postProcessor = new ConfigurationClassPostProcessor();
        postProcessor.processConfigBeanDefinitions(beanFactory);
        postProcessor.postProcessBeanFactory(beanFactory);

        beanFactory.preInstantiateSingletons();

        GreetingController controller = beanFactory.getBean(GreetingController.class);

        String applicationName = beanFactory.getBean("applicationName", String.class);

        assertNotNull(controller);
        assertEquals("Hello from GreetingService!", controller.greet());
        assertEquals("Spring by Example", applicationName);
    }

    @Test
    void shouldProcessImportedConfiguration() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("appConfig", new RootBeanDefinition(AppConfig.class));

        ConfigurationClassPostProcessor postProcessor = new ConfigurationClassPostProcessor();
        postProcessor.processConfigBeanDefinitions(beanFactory);

        assertTrue(beanFactory.containsBeanDefinition("applicationName"));
    }
}
