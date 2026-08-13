# Publishing Events

This example demonstrates how to publish application events using Spring's `ApplicationEventPublisher`.

## Overview

Spring provides an event system that allows components to communicate without requiring the publisher to know which components will eventually handle the event.

Instead of directly calling another service, a component can publish an event describing something that happened.

For example, when an order is created:

```text
OrderService
     |
     | publishEvent(...)
     ↓
ApplicationEventPublisher
     |
     ↓
OrderCreatedEvent
```
The publisher does not need to know whether another component is interested in the event.

This creates a more loosely coupled design and allows listeners to be added independently.

# How it works

## 1. Define an Event

`OrderCreatedEvent` represents something that happened in the application: an order was created

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

The event contains information that other components may need, such as the ID of the newly created order.

The event is immutable because an event represents something that has already happened.

## 2. Inject ApplicationEventPublisher

`OrderService` receives an `ApplicationEventPublisher` through constructor injection:

```java
public OrderService(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
}
```

`ApplicationEventPublisher` is Spring’s abstraction for publishing application events.

The service doesn’t need to create the publisher itself. Spring provides it through the application context.

## 3. Publish the Event

When the order is created, the service publishes an `OrderCreatedEvent`:

```java
eventPublisher.publishEvent(new OrderCreatedEvent(orderId));
```

The important point is that `OrderService` doesn’t directly call another service.

It simply tells Spring:

> An order was created.

Spring then takes care of delivering the event to interested listeners.

# Publisher and Listener

The publisher and listener have different responsibilities.

The publisher:

```text
OrderService
     |
     ↓
publishEvent()
```

The listener eventually:

```text
publishEvent()
     |
     ↓
OrderCreatedEvent
     |
     ├── Email notification
     ├── Audit logging
     └── Other processing
```

The publisher does not need to know which listeners exist.

This is one of the main benefits of Spring’s event system: components can communicate without creating direct dependencies on every component that needs to react to an event.

# ApplicationEventPublisher

ApplicationEventPublisher provides the publishEvent() method used to publish events.

```java
eventPublisher.publishEvent(event);
```

Spring’s application context implements the event publishing infrastructure, so application components normally receive `ApplicationEventPublisher` through dependency injection.

You don’t need to create an `ApplicationEventPublisher` manually.

# Events Do Not Have to Extend ApplicationEvent

Older Spring examples often define events by extending `ApplicationEvent`.

Modern Spring also supports publishing arbitrary objects as events.

This example therefore uses:

```java

public class OrderCreatedEvent { }

```

rather than:

```java

public class OrderCreatedEvent extends ApplicationEvent { }

```

This keeps the event as a simple domain object and avoids coupling the event class directly to Spring’s `ApplicationEvent` type.

Spring can publish the object directly:

```java

eventPublisher.publishEvent(new OrderCreatedEvent(orderId));

```

# Why Use Events?

Without events, a service might need direct dependencies on every component that needs to react to an action:

```text
OrderService
    |
    ├── EmailService
    ├── AuditService
    ├── NotificationService
    └── AnalyticsService
```

As more reactions are added, the service becomes increasingly coupled to other components.

With events:

```text
OrderService
     |
     ↓
ApplicationEventPublisher
     |
     ↓
OrderCreatedEvent
     |
     ├── EmailService
     ├── AuditService
     ├── NotificationService
     └── AnalyticsService
```

The `OrderService` only needs to know how to publish the event.

The components interested in the event can be added independently.

## Important Note

Publishing an event and handling an event are two separate concepts.

This example focuses only on **publishing**.

The next example will introduce Spring’s **@EventListener** annotation and demonstrate how components can listen for events published by another component.

# Key Takeaways

* `ApplicationEventPublisher` is Spring’s abstraction for publishing application events.
* Events allow components to communicate without direct dependencies between the publisher and listeners.
* Events can be simple Java objects.
* `publishEvent()` sends an event into Spring’s event infrastructure.
* The publisher doesn’t need to know who will handle the event.
* Mockito can be used to verify that an event was published without starting a Spring context.
* Event publishing and event listening are separate concerns.

# Running the Example

Run the application:

```bash
mvn exec:java
```

Or run the `Main` class directly from your IDE.

You should see:

```text
Creating order: 1001
```

At this stage, there is no visible event handling because this example only demonstrates publishing.

# Next Step

The next example will build on this one by introducing event listeners with ***@EventListener***.

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
