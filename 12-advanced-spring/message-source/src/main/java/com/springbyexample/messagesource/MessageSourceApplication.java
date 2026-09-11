package com.springbyexample.messagesource;

import java.util.Locale;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.MessageSource;

/**
 * @author Mujuzi Moses
 */
public class MessageSourceApplication {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(MessageSourceConfig.class)) {

            MessageSource messageSource = applicationContext.getBean(MessageSource.class);

            String englishGreeting = messageSource.getMessage("greeting", new Object[]{"Moses"}, Locale.ENGLISH);
            String frenchGreeting = messageSource.getMessage("greeting", new Object[]{"Moses"}, Locale.FRENCH);

            System.out.println(englishGreeting);
            System.out.println(frenchGreeting);
        }
    }
}