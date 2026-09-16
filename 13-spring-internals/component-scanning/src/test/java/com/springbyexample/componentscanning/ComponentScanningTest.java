package com.springbyexample.componentscanning;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

/**
 * @author Mujuzi Moses
 */
public class ComponentScanningTest {

    @Test
    void shouldDiscoverAndRegisterComponent() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanFactory);

        scanner.scan("com.springbyexample.componentscanning");

        assertTrue(beanFactory.containsBeanDefinition("greetingService"));
    }

    @Test
    void shouldCreateBeanFromScannedComponent() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanFactory);

        scanner.scan("com.springbyexample.componentscanning");

        GreetingService greetingService = beanFactory.getBean(GreetingService.class);

        assertEquals("Hello from a scanned component!", greetingService.greet());
    }
}