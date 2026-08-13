# Listening for Events

This example demonstrates how to listen for Spring application events using the `@EventListener` annotation.

---

## Overview

In the previous example, we learned how to publish an event using `ApplicationEventPublisher`.

Publishing an event is only one side of Spring's event system. Another component can listen for that event and react when it is published.

The flow in this example is:

```text
OrderService
     |
     | publishEvent(...)
     ↓
OrderCreatedEvent
     |
     ↓
Spring Event Infrastructure
     |
     ↓
OrderNotificationService
     |
     | @EventListener
     ↓
handleOrderCreated(...)
```

The important part is that `OrderService` does not directly depend on `OrderNotificationService`.

---


## Learning Objectives

By completing this example, you should understand:

- What `@EventListener` does.
- How Spring discovers event listener methods.
- How a listener is associated with an event type.
- How Spring invokes listeners when an event is published.
- How event publishing and event listening are decoupled.
- How to test an event listener using a Spring application context.

---

## The Event

`OrderCreatedEvent` represents something that happened in the application: an order was created.

```java
public class OrderCreatedEvent {

    private final Long orderId;

    public OrderCreatedEvent(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }
}
```

The event contains the information that listeners may need.

In this example, that information is the ID of the newly created order.

The event is a simple Java object. It does not need to extend Spring's `ApplicationEvent`.

---

## Publishing the Event

`OrderService` publishes the event using `ApplicationEventPublisher`:

```java
public class OrderService {

    private final ApplicationEventPublisher eventPublisher;

    public OrderService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void createOrder(Long orderId) {

        System.out.println("Creating order: " + orderId);

        eventPublisher.publishEvent(new OrderCreatedEvent(orderId));
    }
}
```

The service only knows that it needs to publish an event.

It does not know:

- Who will listen for the event.
- How many listeners exist.
- What those listeners will do.
- Whether a listener exists at all.

This keeps the publisher loosely coupled to the consumers of the event.

---

## Listening with `@EventListener`

The listener is defined in `OrderNotificationService`:

```java
public class OrderNotificationService {

    private Long lastNotifiedOrderId;

    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {

        lastNotifiedOrderId = event.getOrderId();

        System.out.println("Sending notification for order: " + event.getOrderId());
    }

    public Long getLastNotifiedOrderId() {
        return lastNotifiedOrderId;
    }
}
```

The important part is:

```java
@EventListener
public void handleOrderCreated(OrderCreatedEvent event) { }
```

The `@EventListener` annotation tells Spring that this method should be invoked when a matching event is published.

Spring uses the method's parameter type to determine which event it handles.

In this case:

```text
OrderCreatedEvent
```

means the method is interested in `OrderCreatedEvent` events.

---

## How Spring Connects the Components

There is no direct call like this:

```java

orderNotificationService.handleOrderCreated(event);

```

Instead, the communication goes through Spring's event infrastructure:

```text
OrderService
     |
     | publishEvent()
     ↓
ApplicationEventPublisher
     |
     ↓
Spring Event Infrastructure
     |
     ↓
@EventListener
     |
     ↓
OrderNotificationService
```

This is an example of **loose coupling**.

The publisher knows about the event, but it doesn't know about the listener.

## `@EventListener`

`@EventListener` is a convenient way to register a method as an application event listener.

For example:

```java
@EventListener
public void handleOrderCreated(OrderCreatedEvent event) {
    // Handle the event
}
```

When Spring creates the bean containing this method, it detects the annotation and registers the method with the application's event infrastructure.

When an `OrderCreatedEvent` is published, Spring invokes the method.

---

## Event Type Matching

The parameter type of an `@EventListener` method determines which events it receives.

For example:

```java
@EventListener
public void handleOrderCreated(OrderCreatedEvent event) {
    // Handles OrderCreatedEvent
}
```

A listener for another event could look like:

```java
@EventListener
public void handlePaymentReceived(PaymentReceivedEvent event) {
    // Handles PaymentReceivedEvent
}
```

Spring routes events to the appropriate listener methods based on the event type.

---

## Configuration

The listener must be managed by Spring.

In this example, both services are registered as beans:

```java
@Configuration
public class AppConfig {

    @Bean
    public OrderService orderService(ApplicationEventPublisher eventPublisher) {

        return new OrderService(eventPublisher);
    }

    @Bean
    public OrderNotificationService orderNotificationService() {
        return new OrderNotificationService();
    }
}
```

This is important because Spring needs to create and manage `OrderNotificationService` in order to discover its `@EventListener` method.

Simply creating the object yourself with:

```java

new OrderNotificationService()

```

would not allow Spring to automatically register its `@EventListener` method.

---

## Publishing vs Listening

It is useful to distinguish these two responsibilities.

### Publishing

The publisher announces that something happened:

```java

eventPublisher.publishEvent(new OrderCreatedEvent(orderId));

```

### Listening

The listener reacts to that event:

```java
@EventListener
public void handleOrderCreated(OrderCreatedEvent event) {
    // React to the event
}
```

Together:

```text
Publisher
   |
   | publishEvent()
   ↓
Event
   |
   ↓
Listener
   |
   ↓
Reaction
```

Neither side needs a direct dependency on the other.

---

## Why Use Events?

Imagine an order service needs to trigger several actions after an order is created.

Without events:

```text
OrderService
    |
    ├── NotificationService
    ├── AuditService
    ├── AnalyticsService
    └── InventoryService
```

The `OrderService` would become directly coupled to every service that needs to react.

With events:

```text
OrderService
     |
     ↓
OrderCreatedEvent
     |
     ├── NotificationService
     ├── AuditService
     ├── AnalyticsService
     └── InventoryService
```

The order service only publishes the event.

New listeners can be added without modifying the publisher.

This is one of the main reasons application events can be useful for decoupling components.

---

## Important Considerations

Spring application events are generally intended for communication **within the same application context**.

They are not automatically a replacement for distributed messaging systems such as Kafka or RabbitMQ.

For communication between separate applications, a message broker or another distributed communication mechanism is usually more appropriate.

Also, event listeners are normally invoked synchronously unless asynchronous processing is explicitly configured.

As a result, the publisher may wait for the listener to finish.

The dedicated **Async Events** example later in this module will explore asynchronous event handling.

---

## Key Takeaways

- `@EventListener` marks a method as a Spring event listener.
- Spring discovers listener methods on Spring-managed beans.
- The event parameter determines which event the listener handles.
- The publisher does not need a direct dependency on the listener.
- Multiple listeners can react to the same event.
- Event publishing and event handling are separate responsibilities.
- Events help reduce coupling between components.
- Spring application events are primarily intended for communication within the application context.
- Event listeners are synchronous by default.

---

## Running the Example

Run the `Main` class from your IDE or using Maven.

Expected output:

```text
Creating order: 1001
Sending notification for order: 1001
```

The first message comes from `OrderService`.

The second message comes from `OrderNotificationService` after Spring invokes the `@EventListener` method.

## Running the Tests

Run:

```bash
mvn test
```

The tests verify that:

- The notification listener reacts when an `OrderCreatedEvent` is published.
- The listener has not executed before an event is published.

---

## Next Step

The next example will introduce **Custom Events** and explore how application-specific events can be designed for more meaningful communication between components.

```text
Publishing Events
       ↓
Listening for Events
       ↓
Custom Events
       ↓
Transactional Events
       ↓
Async Events
```