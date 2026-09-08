package com.springbyexample.beandefinition;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * @author Mujuzi Moses
 */
public class BeanDefinitionApplication {

    public static void main(String[] args) {

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        RootBeanDefinition beanDefinition = new RootBeanDefinition(GreetingService.class);

        beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, "Hello from BeanDefinition!");
        beanFactory.registerBeanDefinition("greetingService", beanDefinition);

        GreetingService greetingService = beanFactory.getBean("greetingService", GreetingService.class);

        System.out.println(greetingService.greet());
    }
}
