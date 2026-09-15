package com.springbyexample.beansregistration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * @author Mujuzi Moses
 */
public class BeansRegistrationTest {

    @Test
    void shouldRegisterBeanDefinition() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        BeanDefinition beanDefinition = new RootBeanDefinition(GreetingService.class);

        beanFactory.registerBeanDefinition("greetingService", beanDefinition);

        assertTrue(beanFactory.containsBeanDefinition("greetingService"));
    }

    @Test
    void shouldCreateBeanFromRegisteredDefinition() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        BeanDefinition beanDefinition = new RootBeanDefinition(GreetingService.class);

        beanFactory.registerBeanDefinition("greetingService", beanDefinition);

        GreetingService greetingService = beanFactory.getBean("greetingService", GreetingService.class);

        assertNotNull(greetingService);
        assertEquals("Hello from a Registered Bean!", greetingService.greet());
    }
}
