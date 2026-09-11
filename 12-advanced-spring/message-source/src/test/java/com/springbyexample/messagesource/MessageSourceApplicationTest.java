package com.springbyexample.messagesource;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class MessageSourceApplicationTest {

    @Test
    void shouldResolveEnglishMessage() {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(MessageSourceConfig.class)) {

            MessageSource messageSource = applicationContext.getBean(MessageSource.class);

            String message = messageSource.getMessage("greeting", new Object[]{"Moses"}, Locale.ENGLISH);

            assertEquals("Hello, Moses!", message);
        }
    }

    @Test
    void shouldResolveFrenchMessage() {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(MessageSourceConfig.class)) {

            MessageSource messageSource = applicationContext.getBean(MessageSource.class);

            String message = messageSource.getMessage("greeting", new Object[]{"Moses"}, Locale.FRENCH);

            assertEquals("Bonjour, Moses !", message);
        }
    }

    @Test
    void shouldResolveMessageWithArgument() {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(MessageSourceConfig.class)) {

            MessageSource messageSource = applicationContext.getBean(MessageSource.class);

            String message = messageSource.getMessage("greeting", new Object[]{"Moses"}, Locale.ENGLISH);

            assertEquals("Hello, Moses!", message);
        }
    }
}
