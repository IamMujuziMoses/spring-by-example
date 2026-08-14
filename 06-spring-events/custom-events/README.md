# Custom Events

This example demonstrates how to create and use custom application events with Spring.

It builds on the previous **Publishing Events** and **Listening for Events** examples by showing how to define an application-specific event that carries meaningful domain information.

---

## Overview

Spring provides the infrastructure for publishing and listening to events, but the events themselves can be designed around the needs of the application.

In this example, a `PaymentService` publishes a `PaymentCompletedEvent` when a payment is completed.

A `PaymentNotificationService` listens for that event and reacts to it.

```text
PaymentService
      |
      | publishEvent()
      ↓
PaymentCompletedEvent
      |
      ↓
Spring Event Infrastructure
      |
      ↓
@EventListener
      |
      ↓
PaymentNotificationService
```

The publisher does not need to know which components are interested in the event.

---

## What Is a Custom Event?

A custom event is an application-specific object that represents something meaningful that happened in the application.

For example:

```java
public class PaymentCompletedEvent {

    private final Payment payment;

    public PaymentCompletedEvent(Payment payment) {
        this.payment = payment;
    }

    public Payment getPayment() {
        return payment;
    }
}
```

`PaymentCompletedEvent` is not something provided by Spring.

It is defined by the application because the application needs to communicate the fact that a payment has completed.

Spring provides the infrastructure that allows this event to be published and consumed.

---

## The Payment Object

The event contains a Payment object:

```java
public class Payment {

    private final Long id;
    private final double amount;

    public Payment(Long id, double amount) {
        this.id = id;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }
}
```

This gives listeners access to information associated with the completed payment.

For this example, the payment contains:

* Payment ID
* Payment amount

In a real application, an event might contain other relevant information such as customer ID, currency, or payment method.

---

## Publishing the Custom Event

`PaymentService` receives an `ApplicationEventPublisher` through constructor injection:

```java
public PaymentService(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
}
```

When the payment is completed, the service publishes the event:

```java
eventPublisher.publishEvent(new PaymentCompletedEvent(payment));
```

The service is responsible for announcing that the payment has completed.

It does not need to know what happens after the event is published.

---

## Listening for the Custom Event

`PaymentNotificationService` listens for `PaymentCompletedEvent`:

```java
@EventListener
public void handlePaymentCompleted(PaymentCompletedEvent event) {

    lastNotifiedPaymentId = event.getPayment().getId();

    System.out.println("Sending payment notification for: " + event.getPayment().getId());
}
```

The event type in the method parameter tells Spring which event the listener is interested in:

```java
PaymentCompletedEvent event
```

When a matching event is published, Spring invokes the method.

---

## Event Flow

The complete flow is:

```text
1. PaymentService.completePayment()
             ↓
2. PaymentCompletedEvent is created
             ↓
3. ApplicationEventPublisher.publishEvent()
             ↓
4. Spring receives the event
             ↓
5. Spring finds matching @EventListener methods
             ↓
6. PaymentNotificationService.handlePaymentCompleted()
```

The publisher and listener remain loosely coupled.

---

## Why Use Custom Events?

Imagine a payment system where several components need to react when a payment completes.

Without events:

```text
PaymentService
     |
     ├── PaymentNotificationService
     ├── AuditService
     ├── AnalyticsService
     └── ReceiptService
```

`PaymentService` would need direct dependencies on every service.

With a custom event:

```text
                 PaymentCompletedEvent
                         |
             ┌───────────┼───────────┐
             ↓           ↓           ↓
       Notification    Audit      Analytics
        Listener       Listener    Listener
             |
             ↓
        Receipt Listener
```

`PaymentService` only publishes the event.

New listeners can be added without modifying the publisher.

---

## Events Represent Facts

A useful way to think about application events is that they represent something that **already happened**.

For example:

```text
PaymentCompletedEvent
```

communicates:

> A payment has completed.

The event is not telling another service what it must do.

Instead, it provides information about what happened so interested components can decide how to react.

This makes events particularly useful for decoupling application components.

---

## Events Do Not Have to Extend ApplicationEvent

Older Spring examples often define events by extending `ApplicationEvent`:

```java

public class PaymentCompletedEvent extends ApplicationEvent { }

```

Modern Spring also supports arbitrary objects as application events.

This example therefore uses a plain Java object:

```java

public class PaymentCompletedEvent { }

```

Spring can publish it directly:

```java

eventPublisher.publishEvent(new PaymentCompletedEvent(payment));

```
This keeps the event independent from Spring’s `ApplicationEvent` class.

---

## Custom Event vs Spring Event Infrastructure

It is useful to separate these concepts.

### Application-provided infrastructure

Spring provides:

```java
ApplicationEventPublisher
```

and

```java
@EventListener
```

### Application-defined event

The application defines:

```java
PaymentCompletedEvent
```

Spring doesn’t need to understand the business meaning of the event.

It simply provides the infrastructure for delivering it to interested listeners.

---

## Multiple Listeners

A custom event can have multiple listeners.

For example:

```text
                 PaymentCompletedEvent
                         |
          ┌──────────────┼──────────────┐
          ↓              ↓              ↓
 Notification        Audit          Analytics
  Listener           Listener         Listener
```

Each listener can perform a different task.

The PaymentService remains unchanged.

---

## Key Takeaways

* Custom events represent application-specific occurrences.
* `ApplicationEventPublisher` is used to publish custom events.
* `@EventListener` is used to react to custom events.
* Events can contain domain information needed by listeners.
* The publisher does not need to know about its listeners.
* Multiple listeners can react to the same event.
* Modern Spring events do not have to extend `ApplicationEvent`.
* Events can help reduce direct dependencies between application components.

---

## Running the Example

Run the `Main` class from your IDE.

You should see output similar to:

```text
Payment completed: 1001
Sending payment notification for: 1001
```

---

## Running the Tests

Run:

```bash
mvn test
```

The tests verify:

1. A `PaymentCompletedEvent` is handled by the listener.
2. The listener does not execute before a payment event is published.

---

## Next step

The next example will introduce **Transactional Events** and explore how event handling can be tied to a transaction’s lifecycle.
