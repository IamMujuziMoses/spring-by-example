package com.springbyexample.choosinginjectionstrategy;

/**
 * @author Mujuzi Moses
 */
public class NotificationService {

    private final EmailService emailService;

    public NotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void notify(String message) {
        emailService.send(message);
    }
}
