package com.springbyexample.qualifier;

import org.springframework.stereotype.Component;

/**
 * @author Mujuzi Moses
 */
@Component
public class EmailNotificationService implements NotificationService {

    @Override
    public String send() {
        return "Sending email notification";
    }

}
