package com.springbyexample.asyncevents;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class Main {

    public static void main(String[] args) {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            var orderService = context.getBean(OrderService.class);

            orderService.createOrder(1001L);

            // Give the asynchronous listener time to complete before closing the context.
            Thread.sleep(500);
        }
        catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }
}
