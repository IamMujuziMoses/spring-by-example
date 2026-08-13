package com.springbyexample.publishingevents;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

/**
 * @author Mujuzi Moses
 */
public class OrderServiceTest {

    private ApplicationEventPublisher eventPublisher;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        // Mock the publisher so the test can verify the event without starting a Spring context.
        eventPublisher = mock(ApplicationEventPublisher.class);
        orderService = new OrderService(eventPublisher);
    }

    @Test
    void shouldPublishOrderCreatedEvent() {
        orderService.createOrder(1001L);

        // Verify that creating the order publishes an OrderCreatedEvent.
        verify(eventPublisher).publishEvent(any(OrderCreatedEvent.class));
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    void shouldPublishEventWithCorrectOrderId() {
        orderService.createOrder(1001L);

        var captor = ArgumentCaptor.forClass(OrderCreatedEvent.class);

        // Capture the published event so its data can be verified.
        verify(eventPublisher).publishEvent(captor.capture());

        assertEquals(1001L, captor.getValue().getOrderId());
    }

}
