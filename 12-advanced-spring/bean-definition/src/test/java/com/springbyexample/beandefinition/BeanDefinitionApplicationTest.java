package com.springbyexample.beandefinition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * @author Mujuzi Moses
 */
public class BeanDefinitionApplicationTest {

    @Test
    void shouldCreateBeanFromBeanDefinition() {

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        RootBeanDefinition beanDefinition = new RootBeanDefinition(GreetingService.class);

        beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, "Hello from BeanDefinition!");
        beanFactory.registerBeanDefinition("greetingService", beanDefinition);

        GreetingService greetingService = beanFactory.getBean("greetingService", GreetingService.class);

        assertNotNull(greetingService);
        assertEquals("Hello from BeanDefinition!", greetingService.greet());
    }

    @Test
    void shouldStoreBeanMetadata() {

        RootBeanDefinition beanDefinition = new RootBeanDefinition(GreetingService.class);

        assertEquals(GreetingService.class, beanDefinition.getBeanClass());
    }
}
