package com.springbyexample.defaultlistablebeanfactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * @author Mujuzi Moses
 */
public class DefaultListableBeanFactoryTest {

    @Test
    void shouldRegisterAndRetrieveBean() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("greetingService", new RootBeanDefinition(GreetingService.class));

        assertTrue(beanFactory.containsBeanDefinition("greetingService"));

        GreetingService greetingService = beanFactory.getBean("greetingService", GreetingService.class);

        assertNotNull(greetingService);
        assertEquals("Hello from GreetingService!", greetingService.greet());
    }

    @Test
    void shouldListRegisteredBeans() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("greetingService", new RootBeanDefinition(GreetingService.class));

        assertTrue(beanFactory.containsBean("greetingService"));
        assertEquals(1, beanFactory.getBeanDefinitionCount());
    }

    @Test
    void shouldResolveConstructorDependency() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("greetingService", new RootBeanDefinition(GreetingService.class));

        RootBeanDefinition controllerDefinition = new RootBeanDefinition(GreetingController.class);
        controllerDefinition.getConstructorArgumentValues()
                .addGenericArgumentValue(new RuntimeBeanReference("greetingService"));

        beanFactory.registerBeanDefinition("greetingController", controllerDefinition);

        GreetingController controller = beanFactory.getBean("greetingController", GreetingController.class);
        GreetingService greetingService = beanFactory.getBean("greetingService", GreetingService.class);

        assertSame(greetingService, controller.greetingService());
        assertEquals("Hello from GreetingService!", controller.greet());
    }
}
