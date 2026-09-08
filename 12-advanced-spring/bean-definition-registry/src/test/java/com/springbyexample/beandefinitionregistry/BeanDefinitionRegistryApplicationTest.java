package com.springbyexample.beandefinitionregistry;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * @author Mujuzi Moses
 */
public class BeanDefinitionRegistryApplicationTest {

    @Test
    void shouldRegisterBeanDefinition() {

        BeanDefinitionRegistry registry = new DefaultListableBeanFactory();
        RootBeanDefinition beanDefinition = new RootBeanDefinition(GreetingService.class);

        registry.registerBeanDefinition("greetingService", beanDefinition);

        assertTrue(registry.containsBeanDefinition("greetingService"));
    }

    @Test
    void shouldRetrieveRegisteredBeanDefinition() {

        BeanDefinitionRegistry registry = new DefaultListableBeanFactory();
        RootBeanDefinition beanDefinition = new RootBeanDefinition(GreetingService.class);

        registry.registerBeanDefinition("greetingService", beanDefinition);

        assertSame(beanDefinition, registry.getBeanDefinition("greetingService"));
    }

    @Test
    void shouldReturnRegisteredBeanDefinitionNames() {

        BeanDefinitionRegistry registry = new DefaultListableBeanFactory();

        registry.registerBeanDefinition("greetingService", new RootBeanDefinition(GreetingService.class));

        assertArrayEquals(new String[]{"greetingService"}, registry.getBeanDefinitionNames());
    }

    @Test
    void shouldRemoveBeanDefinition() {

        BeanDefinitionRegistry registry = new DefaultListableBeanFactory();

        registry.registerBeanDefinition("greetingService", new RootBeanDefinition(GreetingService.class));
        registry.removeBeanDefinition("greetingService");

        assertFalse(registry.containsBeanDefinition("greetingService"));
    }
}
