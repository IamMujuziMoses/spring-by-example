package com.springbyexample.beanpostprocessor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author Mujuzi Moses
 */
public class LoggingBeanPostProcessor implements BeanPostProcessor {

    private final List<String> events = new ArrayList<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if ("reportService".equals(beanName)) {
            events.add("before:" + beanName);
        }

        System.out.println("Before initialization: " + beanName);

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if ("reportService".equals(beanName)) {
            events.add("after:" + beanName);
        }

        System.out.println("After initialization: " + beanName);

        return bean;
    }

    public List<String> getEvents() {
        return List.copyOf(events);
    }
}
