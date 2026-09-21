package com.springbyexample.openmrsbeanregistrar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.springbyexample.openmrsbeanregistrar.impl.GreetingServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * @author Mujuzi Moses
 */
public class OpenmrsBeanRegistrarTest {

    @Test
    void shouldRegisterBeanDefinition() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        OpenmrsBeanRegistrar registrar = new OpenmrsBeanRegistrar();
        registrar.registerBeans(beanFactory);

        assertTrue(beanFactory.containsBeanDefinition("greetingService"));
    }

    @Test
    void shouldRegisterCorrectBeanDefinition() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        OpenmrsBeanRegistrar registrar = new OpenmrsBeanRegistrar();
        registrar.registerBeans(beanFactory);

        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("greetingService");

        assertEquals(GreetingServiceImpl.class.getName(), beanDefinition.getBeanClassName());
    }

    @Test
    void shouldCreateRegisteredBean() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        OpenmrsBeanRegistrar registrar = new OpenmrsBeanRegistrar();
        registrar.registerBeans(beanFactory);

        GreetingService greetingService = beanFactory.getBean("greetingService", GreetingService.class);

        assertNotNull(greetingService);
        assertEquals(GreetingServiceImpl.class, greetingService.getClass());
    }

    @Test
    void shouldInvokeRegisteredBean() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        OpenmrsBeanRegistrar registrar = new OpenmrsBeanRegistrar();
        registrar.registerBeans(beanFactory);

        GreetingService greetingService = beanFactory.getBean("greetingService", GreetingService.class);

        assertEquals("Hello from an OpenMRS registered component!", greetingService.greet());
    }

    @Test
    void shouldReturnSameBeanInstance() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        OpenmrsBeanRegistrar registrar = new OpenmrsBeanRegistrar();
        registrar.registerBeans(beanFactory);

        GreetingService first = beanFactory.getBean("greetingService", GreetingService.class);
        GreetingService second = beanFactory.getBean("greetingService", GreetingService.class);

        assertSame(first, second);
    }
}