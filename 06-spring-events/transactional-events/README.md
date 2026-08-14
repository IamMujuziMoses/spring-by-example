# Transactional Events

This example demonstrates how to use Spring's `@TransactionalEventListener` to handle application events in relation to a transaction's lifecycle.

It builds on the previous **Publishing Events**, **Listening for Events**, and **Custom Events** examples.

Instead of simply reacting to an event when it is published, this example shows how an event listener can be associated with a transaction and execute after the transaction successfully commits.

---

## Overview

A regular `@EventListener` handles an event when Spring publishes it.

A `@TransactionalEventListener`, however, can associate the event listener with a transaction phase.

By default, `@TransactionalEventListener` runs during the `AFTER_COMMIT` phase.

The example uses an order creation scenario:

```text
OrderService
      |
      | @Transactional
      v
Create Order
      |
      | publishEvent()
      v
OrderCreatedEvent
      |
      v
Transaction commits
      |
      v
@TransactionalEventListener
      |
      v
OrderNotificationService
```

---

## Learning Objectives

By completing this example, you will learn:

* What transactional events are.
* The difference between `@EventListener` and `@TransactionalEventListener`.
* How to publish an event from inside a transactional method.
* How `@TransactionalEventListener` associates event handling with a transaction.
* What the `AFTER_COMMIT` phase means.
* Why transaction boundaries matter when handling application events.
* How Spring uses a `PlatformTransactionManager` to manage transactions.
* Why a real transaction resource is useful when learning transactional events.

---

## Dependencies

This example uses Spring’s transaction and `JDBC` support together with an embedded `H2 database`.

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-tx</artifactId>
    <version>${spring.version}</version>
</dependency>

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
    <version>${spring.version}</version>
</dependency>

<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>2.4.240</version>
</dependency>
```

`spring-tx` provides Spring’s transaction abstraction and the `@Transactional` and `@TransactionalEventListener` annotations.

`spring-jdbc` provides `JDBC` transaction support, including `DataSourceTransactionManager`.

`H2` provides an embedded database that can be used as the transaction resource without requiring an external database server.

---

## What Is a Transactional Event?

A transactional event is an application event whose listener is associated with the lifecycle of a transaction.

For example, consider an order creation operation:

```text
Create Order
     |
     v
Publish OrderCreatedEvent
     |
     v
Transaction
     |
     +---- COMMIT ----> Handle Event
     |
     +---- ROLLBACK --> Do Not Handle AFTER_COMMIT Event
```

This is useful when an event should only be processed after an operation has successfully committed.

For example, an application might want to send an email only after an order has successfully been saved.

---

## @EventListener vs @TransactionalEventListener

A regular event listener can be declared using:

```java
@EventListener
public void handleOrderCreated(OrderCreatedEvent event) {
    // Handle event.
}
```

The listener reacts when the event is published.

A transactional event listener is declared using:

```java
@TransactionalEventListener
public void handleOrderCreated(OrderCreatedEvent event) {
    // Handle event according to the transaction phase.
}
```

The second approach associates event handling with the surrounding transaction.

This distinction becomes important when the operation that produced the event can either commit or roll back.

---

## The OrderCreatedEvent

The example defines a simple custom event:

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

The event represents the fact that an order was created.

The event carries the order ID so that listeners can identify which order was created.

---

## The OrderService

OrderService publishes the event from a transactional method:

```java
@Service
public class OrderService {

    private final ApplicationEventPublisher eventPublisher;

    public OrderService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void createOrder(Long orderId) {
        System.out.println("Creating order: " + orderId);

        eventPublisher.publishEvent(new OrderCreatedEvent(orderId));
    }
}
```

The `@Transactional` annotation tells Spring to execute `createOrder()` within a transaction.

The event is published while that transaction is active.

---

## The OrderNotificationService

The listener uses `@TransactionalEventListener`:

```java
@Component
public class OrderNotificationService {

    private Long lastNotifiedOrderId;

    @TransactionalEventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        lastNotifiedOrderId = event.getOrderId();

        System.out.println("Sending notification for order: " + event.getOrderId());
    }

    public Long getLastNotifiedOrderId() {
        return lastNotifiedOrderId;
    }
}
```

The important difference from the previous event examples is the use of:

```java
@TransactionalEventListener
```

By default, this listener is associated with the **AFTER_COMMIT** phase.

Therefore, when the transaction successfully commits, Spring invokes the listener.

---

## Transaction Phases

@TransactionalEventListener supports several transaction phases.

### `AFTER_COMMIT`

```java
@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
```

The listener executes after the transaction successfully commits.

This is also the default phase.

It is useful for actions that should only happen after the transaction succeeds.

Examples include:

* Sending notifications.
* Publishing integration messages.
* Updating external systems.
* Triggering non-critical follow-up work.

### BEFORE_COMMIT

```java
@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
```

The listener executes before the transaction commits.

This can be useful when event processing needs to happen as part of the transaction before the commit occurs.

### AFTER_ROLLBACK

```java
@TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
```

The listener executes after the transaction rolls back.

This can be useful for handling actions related specifically to failed transactions.

### AFTER_COMPLETION

```java
@TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
```

The listener executes after the transaction completes, regardless of whether it committed or rolled back.

---

## The Default Phase

When no phase is specified:

```java
@TransactionalEventListener
```

Spring uses:

```java
TransactionPhase.AFTER_COMMIT
```

Therefore:

```java
@TransactionalEventListener
public void handleOrderCreated(OrderCreatedEvent event) {
    // Runs after a successful commit.
}
```

is equivalent to:

```java
@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
public void handleOrderCreated(OrderCreatedEvent event) {
    // Runs after a successful commit.
}
```

Using the default in this example keeps the first introduction to transactional events simple.

---

## Transaction Manager

The example uses a `JDBC` transaction manager:


```java
@Bean
public PlatformTransactionManager transactionManager(DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
}
```

The transaction manager needs a `DataSource`, so the example uses an embedded `H2` database:

```java
@Bean
public DataSource dataSource() {
    return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
}
```

The complete configuration is:

```java
@Configuration
@EnableTransactionManagement
@ComponentScan
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {

        return new DataSourceTransactionManager(dataSource);
    }
}
```

`@EnableTransactionManagement` enables Spring’s annotation-driven transaction management.

---

## Why Use H2?

The example does not need a real application database.

`H2` provides an embedded database that allows Spring to create a real `JDBC` transaction without requiring an external database server.

The purpose of `H2` here is to provide a transaction resource for the example.

The focus remains on Spring’s transaction and event infrastructure.

---

## Complete Flow

When `createOrder()` is called:

```text
1. OrderService.createOrder()
                |
                v
2. Spring starts a transaction
                |
                v
3. Order is created
                |
                v
4. OrderCreatedEvent is published
                |
                v
5. Transaction commits
                |
                v
6. @TransactionalEventListener executes
                |
                v
7. Notification is sent
```

The important part is that publishing the event and handling the event are separate steps.

The event is published during the transaction, but the default transactional listener waits for the transaction to successfully commit.

---

## Why Does This Matter?

Consider an application that creates an order and then sends a notification.

If the order creation fails:

```text
Create Order
     |
     v
Transaction fails
     |
     v
ROLLBACK
```

You generally do not want to send an **“Order Created”** notification for an order that was never successfully committed.

A transactional event listener can help prevent this:

```text
Order Creation
      |
      v
Publish Event
      |
      v
Transaction
   /       \
  /         \
COMMIT     ROLLBACK
  |           |
  v           v
Notify      No AFTER_COMMIT
```

This makes `@TransactionalEventListener` useful for actions that depend on a successful transaction.

---

## Running the Example

Run the `Main` class

```java
public class Main {

    public static void main(String[] args) {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            var orderService = context.getBean(OrderService.class);

            orderService.createOrder(1001L);
        }
    }
}
```

The output should be similar to:

```text
Creating order: 1001
Sending notification for order: 1001
```

The `H2` database may also log a shutdown message when the application context is closed.

That is expected because the embedded database is automatically shut down with the application context.

---

## Running the Tests

Run:

```bash
mvn test
```

The test should pass successfully.

---

## Key Takeaways

* `@TransactionalEventListener` connects event handling to a transaction lifecycle.
* The default transaction phase is **AFTER_COMMIT**.
* Events can be published while a transaction is active.
* A transactional listener can wait until the transaction successfully commits.
* Transactional events are useful when event processing should depend on transaction success.
* `@EventListener` and `@TransactionalEventListener` have different semantics.
* `@Transactional` requires transaction infrastructure.
* `PlatformTransactionManager` is responsible for managing transactions.
* `DataSourceTransactionManager` can manage `JDBC` transactions.
* An embedded `H2` database provides a convenient transaction resource for this example.
* Services using `@Transactional` should be obtained from the Spring container so Spring’s transactional proxy can be applied.

---

##  Next Step

The next example will introduce **Async Events** and demonstrate how Spring can process application events asynchronously.
