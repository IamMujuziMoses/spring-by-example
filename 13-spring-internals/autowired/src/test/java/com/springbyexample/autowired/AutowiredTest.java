package com.springbyexample.autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * @author Mujuzi Moses
 */
public class AutowiredTest {

    @Test
    void shouldProcessAutowiredField() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        beanFactory.registerBeanDefinition("greetingService", new RootBeanDefinition(GreetingService.class));
        beanFactory.registerBeanDefinition("greetingController", new RootBeanDefinition(GreetingController.class));

        AutowiredAnnotationBeanPostProcessor postProcessor = new AutowiredAnnotationBeanPostProcessor();

        postProcessor.setBeanFactory(beanFactory);
        beanFactory.addBeanPostProcessor(postProcessor);

        GreetingController controller = beanFactory.getBean("greetingController", GreetingController.class);

        assertNotNull(controller.getGreetingService());
    }

    @Test
    void shouldInjectCorrectBean() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        beanFactory.registerBeanDefinition("greetingService", new RootBeanDefinition(GreetingService.class));
        beanFactory.registerBeanDefinition("greetingController", new RootBeanDefinition(GreetingController.class));

        AutowiredAnnotationBeanPostProcessor postProcessor = new AutowiredAnnotationBeanPostProcessor();

        postProcessor.setBeanFactory(beanFactory);
        beanFactory.addBeanPostProcessor(postProcessor);

        GreetingService greetingService = beanFactory.getBean("greetingService", GreetingService.class);
        GreetingController controller = beanFactory.getBean("greetingController", GreetingController.class);

        assertSame(greetingService, controller.getGreetingService());
        assertEquals("Hello from GreetingService!", controller.greet());
    }
}
