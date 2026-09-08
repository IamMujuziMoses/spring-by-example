package com.springbyexample.factorybean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class FactoryBeanApplicationTest {

    @Test
    void shouldReturnProductCreatedByFactoryBean() {

        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(FactoryBeanConfig.class)) {

            Object bean = applicationContext.getBean("greetingService");

            assertNotNull(bean);
            assertInstanceOf(GreetingService.class, bean);

            GreetingService greetingService = (GreetingService) bean;

            assertEquals("Hello from FactoryBean!", greetingService.greet());
        }
    }

    @Test
    void shouldRetrieveFactoryBeanUsingAmpersandPrefix() {

        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(FactoryBeanConfig.class)) {

            Object factory = applicationContext.getBean("&greetingService");

            assertNotNull(factory);
            assertInstanceOf(GreetingServiceFactoryBean.class, factory);
        }
    }
}