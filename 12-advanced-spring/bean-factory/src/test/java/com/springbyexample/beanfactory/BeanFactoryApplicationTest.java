package com.springbyexample.beanfactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * @author Mujuzi Moses
 */
public class BeanFactoryApplicationTest {

    @Test
    void shouldCreateAndRetrieveBean() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        beanFactory.registerBeanDefinition("greetingService", new RootBeanDefinition(GreetingService.class));

        GreetingService greetingService = beanFactory.getBean("greetingService", GreetingService.class);

        assertNotNull(greetingService);
        assertEquals("Hello from BeanFactory!", greetingService.greet());
    }

    @Test
    void shouldReturnSameSingletonInstance() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        beanFactory.registerBeanDefinition("greetingService", new RootBeanDefinition(GreetingService.class));

        GreetingService first = beanFactory.getBean(GreetingService.class);
        GreetingService second = beanFactory.getBean(GreetingService.class);

        assertEquals(first, second);
    }
}