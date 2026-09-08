package com.springbyexample.beandefinitionregistry;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * @author Mujuzi Moses
 */
public class BeanDefinitionRegistryApplication {

    public static void main(String[] args) {

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        RootBeanDefinition beanDefinition = new RootBeanDefinition(GreetingService.class);

        beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, "Hello from BeanDefinitionRegistry!");
        beanFactory.registerBeanDefinition("greetingService", beanDefinition);

        GreetingService greetingService = beanFactory.getBean("greetingService", GreetingService.class);

        System.out.println(greetingService.greet());
    }
}
