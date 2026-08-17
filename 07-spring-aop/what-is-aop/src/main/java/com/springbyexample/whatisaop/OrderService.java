package com.springbyexample.whatisaop;

/**
 * @author Mujuzi Moses
 */
public class OrderService {

    private final LoggingService loggingService;

    public OrderService(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    public void createOrder(Long orderId) {
        loggingService.log("Creating order: " + orderId);

        System.out.println("Order created: " + orderId);

        loggingService.log("Finished creating order: " + orderId);
    }
}
