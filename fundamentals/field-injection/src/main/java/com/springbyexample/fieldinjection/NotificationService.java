package com.springbyexample.fieldinjection;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Mujuzi Moses
 */
public class NotificationService {

    @Autowired
    private EmailService emailService;

    public void notify(String message) {
        emailService.send(message);
    }
}
