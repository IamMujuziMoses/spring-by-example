package com.springbyexample.primary;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @author Mujuzi Moses
 */
@Primary
@Component
public class EmailNotificationService implements NotificationService {

    @Override
    public String send() {
        return "Sending email notification";
    }

}
