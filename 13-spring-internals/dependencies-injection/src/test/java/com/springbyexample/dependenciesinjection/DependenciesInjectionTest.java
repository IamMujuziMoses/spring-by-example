package com.springbyexample.dependenciesinjection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.MethodParameter;

/**
 * @author Mujuzi Moses
 */
public class DependenciesInjectionTest {

    @Test
    void shouldResolveConstructorDependency() throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        beanFactory.registerBeanDefinition("greetingService", new RootBeanDefinition(GreetingService.class));

        Constructor<GreetingController> constructor = GreetingController.class.getConstructor(GreetingService.class);

        MethodParameter methodParameter = new MethodParameter(constructor, 0);

        DependencyDescriptor descriptor = new DependencyDescriptor(methodParameter, true);

        Object dependency = beanFactory.resolveDependency(descriptor, "greetingController");

        assertInstanceOf(GreetingService.class, dependency);
    }

    @Test
    void shouldInjectResolvedDependency() throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        beanFactory.registerBeanDefinition("greetingService", new RootBeanDefinition(GreetingService.class));

        Constructor<GreetingController> constructor = GreetingController.class.getConstructor(GreetingService.class);

        MethodParameter methodParameter = new MethodParameter(constructor, 0);

        DependencyDescriptor descriptor = new DependencyDescriptor(methodParameter, true);

        GreetingService greetingService = (GreetingService) beanFactory
                .resolveDependency(descriptor, "greetingController");

        GreetingController controller = new GreetingController(greetingService);

        assertEquals("Hello from GreetingService!", controller.greet());
    }
}
