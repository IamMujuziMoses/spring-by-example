# Async Events

This example demonstrates how to process Spring application events asynchronously using `@Async`.

It builds on the previous **Publishing Events**, **Listening for Events**, **Custom Events**, and **Transactional Events** examples.

The key idea is that an event listener does not always need to execute on the same thread as the code that publishes the event.

With `@Async`, Spring can execute the event listener asynchronously, allowing the publisher to continue without waiting for the listener to finish.

---

## Overview

A normal event listener executes synchronously by default:

```text
OrderService
      |
      | publishEvent()
      v
OrderCreatedEvent
      |
      v
@EventListener
      |
      v
Notification
      |
      v
OrderService continues
```

With an asynchronous listener

```text
OrderService
      |
      | publishEvent()
      v
OrderCreatedEvent
      |
      +----------------------+
      |                      |
      v                      v
@Async @EventListener        OrderService continues
      |
      v
Notification
```

The publisher does not need to know whether the listener is synchronous or asynchronous.

---

## Learning Objectives

By completing this example, you will learn:

* What asynchronous event handling means in Spring.
* How to use `@Async` with `@EventListener`.
* How `@EnableAsync` enables asynchronous method execution.
* How asynchronous event listeners differ from regular event listeners.
* Why the publisher should remain independent of the listener’s execution strategy.
* Why asynchronous code requires different testing strategies.
* How `CountDownLatch` can be used to synchronize asynchronous tests.

---

## What Is Asynchronous Event Handling?

When an event is published normally, Spring invokes matching event listeners synchronously.

For example:

```java

eventPublisher.publishEvent(new OrderCreatedEvent(orderId));

```

With a regular listener:

```java
@EventListener
public void handleOrderCreated(OrderCreatedEvent event) {
    // Handle event.
}
```

the publishing thread waits for the listener method to complete.

An asynchronous listener changes this behavior:

```java
@Async
@EventListener
public void handleOrderCreated(OrderCreatedEvent event) {
    // Handle event asynchronously.
}
```

Spring schedules the listener for asynchronous execution, allowing the publishing thread to continue.

### The OrderCreatedEvent

The example uses a simple custom event:

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

The event represents the fact that an order has been created.

The event contains the order ID so that listeners can identify the order.

### The OrderService

The OrderService publishes the event:

```java
@Service
public class OrderService {

    private final ApplicationEventPublisher eventPublisher;

    public OrderService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void createOrder(Long orderId) {
        System.out.println("Creating order: " + orderId);

        eventPublisher.publishEvent(new OrderCreatedEvent(orderId));

        System.out.println("Order creation completed.");
    }
}
```

Notice that `OrderService` does not contain any asynchronous logic.

It simply publishes the event.

This is intentional.

The publisher should not need to know whether a listener processes the event synchronously or asynchronously.

### The Asynchronous Listener

The notification service listens for the event:

```java
@Component
public class OrderNotificationService {

    private Long lastNotifiedOrderId;

    @Async
    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        System.out.println("Sending notification for order: " + event.getOrderId());

        lastNotifiedOrderId = event.getOrderId();
    }

    public Long getLastNotifiedOrderId() {
        return lastNotifiedOrderId;
    }
}
```

Two annotations are important here:

```java
@Async
@EventListener
```

They have different responsibilities.

#### @EventListener

```java
@EventListener
```

tells Spring that the method should receive matching application events.

Because the method accepts:

```java

OrderCreatedEvent

```

Spring knows that this method should handle OrderCreatedEvent instances.

#### @Async

```java

@Async

```

tells Spring to execute the method asynchronously.

The event publisher therefore does not need to wait for the listener method to complete.

---

## Enabling Asynchronous Processing

Spring’s asynchronous method execution must be enabled.

Add `@EnableAsync` to the configuration class:

```java
@Configuration
@EnableAsync
@ComponentScan
public class AppConfig {
}
```

`@EnableAsync` enables Spring’s support for asynchronous method execution.

Without it, adding `@Async` to the listener does not provide the expected asynchronous behavior.

### Complete Configuration

The complete configuration is:

```java
@Configuration
@EnableAsync
@ComponentScan
public class AppConfig {
}
```

For this simple example, Spring’s default asynchronous executor is sufficient.

A later example could introduce a custom `TaskExecutor` to demonstrate thread-pool configuration and executor management.

---

## Synchronous vs Asynchronous Events

Consider a synchronous listener:

```java
@EventListener
public void handleOrderCreated(OrderCreatedEvent event) {
    // Process event.
}
```

The flow is:

```text
Publisher
    |
    v
publishEvent()
    |
    v
Listener
    |
    v
Listener finishes
    |
    v
Publisher continues
```

with `@Async`:

```java
@Async
@EventListener
public void handleOrderCreated(OrderCreatedEvent event) {
    // Process event asynchronously.
}
```

the flow becomes:

```text
Publisher
    |
    v
publishEvent()
    |
    +----------------------+
    |                      |
    v                      v
Async listener         Publisher continues
    |
    v
Listener finishes
```

The publisher and listener therefore have different execution timelines.

---

## Why Use Async Events?

`Asynchronous` events can be useful when the listener performs work that does not need to block the publisher.

### Examples include:

* Sending notifications.
* Sending emails.
* Processing analytics.
* Writing non-critical audit information.
* Triggering background processing.
* Performing other independent follow-up work.

For example:

```text
Order Created
     |
     +---- Update order
     |
     +---- Send email asynchronously
     |
     +---- Update analytics asynchronously
     |
     +---- Send notification asynchronously
```

The order creation operation does not necessarily need to wait for every secondary operation to finish.

---

## Publisher and Listener Remain Decoupled

One of the important benefits of using events is that the publisher does not need to know how listeners are implemented.

The `OrderService` only knows that it publishes:

```java

OrderCreatedEvent

```

It does not need to know:

* How many listeners exist.
* What each listener does.
* Whether listeners are synchronous.
* Whether listeners are asynchronous.
* Which thread executes the listener.

This keeps the event publisher loosely coupled to its consumers.

### Important: **@Async** Does Not Make the Event Itself Asynchronous

It is useful to distinguish between the event and the listener.

The event is still published using:

```java
eventPublisher.publishEvent(event);
```

The asynchronous behavior comes from the listener:

```java
@Async
@EventListener
public void handleOrderCreated(OrderCreatedEvent event) {
    // ...
}
```

In other words:

```text
Event publishing
        |
        v
ApplicationEventPublisher
        |
        v
Event listener
        |
        v
@Async controls listener execution
```

---

## Main

The example can be run with:

```java
public class Main {

    public static void main(String[] args) throws InterruptedException {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            var orderService = context.getBean(OrderService.class);

            orderService.createOrder(1001L);

            // Give the asynchronous listener time to complete.
            Thread.sleep(500);
        }
    }
}
```

The output may look similar to:

```text
Creating order: 1001
Order creation completed.
Sending notification for order: 1001
```

However, the exact ordering of the messages is not guaranteed.

The asynchronous listener may execute before or after:

```text
Order creation completed.
```

depending on thread scheduling.

Why `Thread.sleep()` Is Used Here

The `Thread.sleep()` in Main is only there to make the asynchronous behavior observable in this small standalone example.

Without it, the application context could close before the asynchronous listener has finished executing.

This is **not** a recommended production synchronization technique.

Production applications should use appropriate lifecycle management, task coordination, or executor management instead.

---

## Testing Asynchronous Events

Testing asynchronous code requires some additional consideration.

This can be unreliable:

```java
orderService.createOrder(1001L);

assertEquals(1001L,notificationService.getLastNotifiedOrderId());
```

The assertion could execute before the asynchronous listener has completed.

Instead, the test can explicitly wait for the asynchronous operation.

A `CountDownLatch` is useful for this purpose.

The listener can signal the test when processing has completed:

```java
@Component
public class OrderNotificationService {

    private final CountDownLatch notificationLatch = new CountDownLatch(1);

    private Long lastNotifiedOrderId;

    @Async
    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        lastNotifiedOrderId = event.getOrderId();

        notificationLatch.countDown();
    }

    public boolean awaitNotification() throws InterruptedException {
        return notificationLatch.await(2, TimeUnit.SECONDS);
    }

    public Long getLastNotifiedOrderId() {
        return lastNotifiedOrderId;
    }
}
```

The test can then wait for the listener;

```java
@Test
void shouldHandleOrderCreatedEventAsynchronously() throws InterruptedException {

    try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

        var orderService = context.getBean(OrderService.class);
        var notificationService = context.getBean(OrderNotificationService.class);

        orderService.createOrder(1001L);

        assertTrue(notificationService.awaitNotification());
        assertEquals(1001L, notificationService.getLastNotifiedOrderId());
    }
}
```

The timeout prevents the test from waiting indefinitely if the asynchronous listener never executes.

### Why CountDownLatch?

`CountDownLatch` allows the test to wait for a specific asynchronous operation.

The listener starts with:

```java
new CountDownLatch(1);
```

When the listener finishes:

```java
notificationLatch.countDown();
```

The test waits using:

```java
notificationLatch.await(2, TimeUnit.SECONDS);
```

This makes the synchronization explicit;

```text
Test
 |
 | createOrder()
 v
OrderService
 |
 | publishEvent()
 v
Async listener
 |
 | countDown()
 v
Test continues
 |
 v
Assertions
```

This is preferable to adding an arbitrary delay to the test.

---

## Async Events vs Transactional Events

The previous example introduced `@TransactionalEventListener`.

It is important to understand that transactional event handling and asynchronous event handling solve different problems.

### Transactional Events

`@TransactionalEventListener` answers:

> When should the event be handled relative to the transaction?

For example:

```java
@TransactionalEventListener
public void handleOrderCreated(OrderCreatedEvent event) {
    // Runs after the transaction commits by default.
}
```

### Async Events

`@Async` answers:

> Should the listener execute asynchronously?

For example:

```java
@Async
@EventListener
public void handleOrderCreated(OrderCreatedEvent event) {
    // Runs asynchronously.
}
```

These are separate concerns:

```text
@TransactionalEventListener
        |
        v
Transaction timing


@Async
        |
        v
Execution model
```

They can also  be combined when an application requires both behaviors:

```java
@Async
@TransactionalEventListener
public void handleOrderCreated(OrderCreatedEvent event) {
    // ...
}
```
However, combining them is intentionally left outside this introductory example so that each concept remains clear.

---

## Key Takeaways

* `@EventListener` identifies methods that handle application events.
* `@Async` allows an event listener to execute asynchronously.
* `@EnableAsync` enables Spring’s asynchronous method execution support.
* The event publisher does not need to know whether a listener is synchronous or asynchronous.
* Asynchronous listeners allow the publisher to continue without waiting for listener processing.
* The exact execution order of asynchronous operations is not guaranteed.
* Asynchronous code requires appropriate testing strategies.
* `CountDownLatch` can be useful when testing asynchronous operations.
* `@Async` and `@TransactionalEventListener` solve different problems.
* `Transactional` events control when an event is handled relative to a transaction.
* Async events control how the listener is executed.

---

## Running the Example

Run the `Main` class from your IDE.

You should see output similar to:

```text
Creating order: 1001
Order creation completed.
Sending notification for order: 1001
```

The exact order of the last two messages may vary because the notification listener runs asynchronously.

---

## Running the Tests

Run:

```bash
mvn test 
```

The tests verify that:

1. The `OrderCreatedEvent` is published.
2. The asynchronous listener processes the event.
3. The notification contains the correct order ID.

---

## Module 6 Complete

With Async Events, we have completed **Module 6 — Events.**

The module progressed from basic event publishing to asynchronous event processing:

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

The examples demonstrate several important aspects of Spring’s event system:

* Publishing application events.
* Listening for specific event types.
* Creating application-specific events.
* Connecting event processing to transaction lifecycles.
* Processing event listeners asynchronously.

Together, these concepts provide a foundation for building loosely coupled Spring applications.

---

## Next Module

The next module will introduce the next major concept in the Spring learning path.
