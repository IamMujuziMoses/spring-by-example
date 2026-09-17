package com.springbyexample.defaultlistablebeanfactory;

import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * @author Mujuzi Moses
 */
public class DefaultListableBeanFactoryApplication {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // Register the service bean definition.
        beanFactory.registerBeanDefinition("greetingService", new RootBeanDefinition(GreetingService.class));
        RootBeanDefinition controllerDefinition = new RootBeanDefinition(GreetingController.class);

        // Tell the factory which bean should satisfy the constructor argument.
        controllerDefinition.getConstructorArgumentValues()
                .addGenericArgumentValue(new RuntimeBeanReference("greetingService"));

        beanFactory.registerBeanDefinition("greetingController", controllerDefinition);

        GreetingController greetingController = beanFactory.getBean("greetingController", GreetingController.class);

        System.out.println(greetingController.greet());
    }
}
