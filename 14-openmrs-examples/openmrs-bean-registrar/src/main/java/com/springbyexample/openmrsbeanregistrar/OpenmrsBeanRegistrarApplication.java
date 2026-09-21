package com.springbyexample.openmrsbeanregistrar;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * @author Mujuzi Moses
 */
public class OpenmrsBeanRegistrarApplication {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        OpenmrsBeanRegistrar registrar = new OpenmrsBeanRegistrar();
        registrar.registerBeans(beanFactory);

        GreetingService greetingService = beanFactory.getBean("greetingService", GreetingService.class);

        System.out.println(greetingService.greet());
    }
}