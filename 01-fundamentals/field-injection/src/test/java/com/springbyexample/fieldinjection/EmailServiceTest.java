package com.springbyexample.fieldinjection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class EmailServiceTest {

    @Test
    void shouldCreateEmailServiceBean() {

        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        EmailService emailService = context.getBean(EmailService.class);
        assertNotNull(emailService);
    }
}
