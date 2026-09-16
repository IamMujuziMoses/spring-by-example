package com.springbyexample.componentscanning;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

/**
 * @author Mujuzi Moses
 */
public class ComponentScanningApplication {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanFactory);

        scanner.scan("com.springbyexample.componentscanning");

        GreetingService greetingService = beanFactory.getBean(GreetingService.class);

        System.out.println(greetingService.greet());
    }
}
