package com.springbyexample.dependenciesinjection;

import java.lang.reflect.Constructor;

import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.MethodParameter;

/**
 * Demonstrates how Spring resolves a constructor dependency.
 *
 * @author Mujuzi Moses
 */
public class DependenciesInjectionApplication {

    public static void main(String[] args) throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        beanFactory.registerBeanDefinition("greetingService", new RootBeanDefinition(GreetingService.class));

        Constructor<GreetingController> constructor = GreetingController.class.getConstructor(GreetingService.class);

        MethodParameter methodParameter = new MethodParameter(constructor, 0);

        DependencyDescriptor descriptor = new DependencyDescriptor(methodParameter, true);

        Object dependency = beanFactory.resolveDependency(descriptor, "greetingController");

        GreetingController controller = new GreetingController((GreetingService) dependency);

        System.out.println(controller.greet());
    }
}