package com.springbyexample.autowired;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;

/**
 * @author Mujuzi Moses
 */
public class AutowiredApplication {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        beanFactory.registerBeanDefinition("greetingService", new RootBeanDefinition(GreetingService.class));
        beanFactory.registerBeanDefinition("greetingController", new RootBeanDefinition(GreetingController.class));

        AutowiredAnnotationBeanPostProcessor postProcessor = new AutowiredAnnotationBeanPostProcessor();

        postProcessor.setBeanFactory(beanFactory);
        beanFactory.addBeanPostProcessor(postProcessor);

        GreetingController controller = beanFactory.getBean("greetingController", GreetingController.class);

        System.out.println(controller.greet());
    }
}
