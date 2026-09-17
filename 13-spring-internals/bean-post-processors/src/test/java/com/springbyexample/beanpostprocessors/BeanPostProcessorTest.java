package com.springbyexample.beanpostprocessors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * @author Mujuzi Moses
 */
public class BeanPostProcessorTest {

    @Test
    void shouldProcessBeanBeforeAndAfterInitialization() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("greetingService", new RootBeanDefinition(GreetingService.class));

        RecordingBeanPostProcessor postProcessor = new RecordingBeanPostProcessor();
        beanFactory.addBeanPostProcessor(postProcessor);

        GreetingService greetingService = beanFactory.getBean("greetingService", GreetingService.class);

        assertNotNull(greetingService);
        assertEquals("greetingService", postProcessor.getBeforeBeanName());
        assertEquals("greetingService", postProcessor.getAfterBeanName());
        assertSame(greetingService, postProcessor.getBeforeBean());
        assertSame(greetingService, postProcessor.getAfterBean());
    }

    @Test
    void shouldAllowPostProcessorToReplaceBean() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // Register the original bean definition with the factory.
        beanFactory.registerBeanDefinition("greetingService", new RootBeanDefinition(GreetingService.class));

        GreetingService replacement = new GreetingService();

        // A post processor can return a different object after initialization.
        beanFactory.addBeanPostProcessor(new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) {
                return replacement;
            }
        });

        // The object returned by getBean() is the post-processed object.
        GreetingService greetingService = beanFactory.getBean("greetingService", GreetingService.class);

        assertSame(replacement, greetingService);
    }

    private static class RecordingBeanPostProcessor implements BeanPostProcessor {

        private Object beforeBean;

        private Object afterBean;

        private String beforeBeanName;

        private String afterBeanName;

        @Override
        public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
            beforeBean = bean;
            beforeBeanName = beanName;

            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            afterBean = bean;
            afterBeanName = beanName;

            return bean;
        }

        public Object getBeforeBean() {
            return beforeBean;
        }

        public Object getAfterBean() {
            return afterBean;
        }

        public String getBeforeBeanName() {
            return beforeBeanName;
        }

        public String getAfterBeanName() {
            return afterBeanName;
        }
    }
}
