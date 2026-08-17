# What is AOP?

This example introduces **Aspect-Oriented Programming (AOP)** and the problem it is designed to solve.

Rather than starting with Spring AOP annotations, this example first looks at a common problem in application design: **cross-cutting concerns**.

The goal is to understand why AOP exists before exploring how Spring implements it.

---

## Learning Objectives

By completing this example, you will learn:

- What Aspect-Oriented Programming is.
- What cross-cutting concerns are.
- Why cross-cutting concerns can lead to duplicated code.
- How logging can become scattered across application services.
- How AOP can separate cross-cutting concerns from business logic.
- The basic terminology used in AOP.

---

## What Is AOP?

**Aspect-Oriented Programming** is a programming technique for separating cross-cutting concerns from the application's core business logic.

A cross-cutting concern is functionality that applies to multiple, otherwise unrelated parts of an application.

Common examples include:

- Logging
- Security
- Transactions
- Performance monitoring
- Auditing
- Caching

For example, an application may need logging around many different services:

```text
OrderService
    └── Logging

PaymentService
    └── Logging

InventoryService
    └── Logging

UserService
    └── Logging
```

Without a mechanism for separating this concern, logging code can become scattered throughout the application.

---

## The Problem

Consider an order service:

```java
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
```

The service is responsible for creating the order, but it is also explicitly responsible for logging.

As the application grows, similar logging code could appear in many different services.

This creates several problems:

* Business logic becomes mixed with infrastructure concerns.
* The same logging code may be repeated.
* Changing the logging behavior requires modifying multiple classes.
* Services become more tightly coupled to logging infrastructure.

---

## Cross-Cutting Concerns

Logging is a good example of a cross-cutting concern.

The concern is not specific to one particular business operation. It can apply across many different services.

For example:
```text
                    ┌──────────────┐
                    │    Logging   │
                    │    Concern   │
                    └──────┬───────┘
                           │
             ┌─────────────┼─────────────┐
             ↓             ↓             ↓
       OrderService   PaymentService  UserService
```

The same idea applies to other concerns such as security, transactions, and auditing.

---

## AOP’s Goal

AOP provides a way to separate these cross-cutting concerns from the classes that contain the application’s core business logic.

Instead of:

```java
public void createOrder(Long orderId) {
    loggingService.log("Creating order");

    // Business logic

    loggingService.log("Finished creating order");
}
```

we want  the service to focus on it's actual responsibility:

```java
public void createOrder(Long orderId) {
    // Business logic
}
```

The logging concern can then be handled separately.

Conceptually:

```text
                    Logging
                       │
                       ↓
                 OrderService
                       │
                       ↓
                Business Logic
```

This separation is one of the main reasons AOP exists.

---

## Example Application

This example uses a simple order service and logging service.

#### OrderService

```java
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
```

#### LoggingService

```java
public class LoggingService {

    public void log(String message) {
        System.out.println("[LOG] " + message);
    }
}
```

The logging service provides the cross-cutting functionality, while the order service represents application business logic.

This example intentionally keeps the two concerns connected so that the problem is visible.

Later examples will show how Spring AOP can separate them.

#### Configuration

The services are registered with the Spring container:

```java
@Configuration
@ComponentScan
public class AppConfig {

    @Bean
    public LoggingService loggingService() {
        return new LoggingService();
    }

    @Bean
    public OrderService orderService(LoggingService loggingService) {
        return new OrderService(loggingService);
    }
}
```

---

## Running the Example

The application can be run with:

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

The output will be similar to:

```text
[LOG] Creating order: 1001
Order created: 1001
[LOG] Finished creating order: 1001
```

---

## AOP Terminology

Before exploring Spring AOP, it is useful to understand a few basic terms.

#### Aspect

An aspect represents a cross-cutting concern that should be applied to one or more parts of an application.

Examples include:

* Logging aspect
* Security aspect
* Transaction aspect

In later examples, we will create actual Spring aspects.

#### Advice

Advice is the action performed by an aspect.

For example:

```text
Before a method → log something
After a method  → log something
Around a method → measure execution time
```

Spring provides several types of advice, including:

* `@Before`
* `@After`
* `@Around`

#### Join Point

A join point represents a point during program execution where additional behavior can be applied.

Spring AOP primarily works with method execution join points.

#### Pointcut

A pointcut determines which join points an aspect should apply to.

For example, a pointcut could match:

```text
All methods in OrderService
```

Pointcuts will be explored in more detail later in this module.


---

## How Spring AOP Fits Together

At a high level, Spring AOP connects these concepts:

```text
Aspect
  |
  +-- Advice
  |
  +-- Pointcut
          |
          ↓
     Target Method
```

For example:

```text
Logging Aspect
      |
      ↓
Pointcut: OrderService methods
      |
      ↓
Before Advice
      |
      ↓
OrderService.createOrder()
```

The following examples in this module will show how Spring actually implements this behavior.

---

## Why Not Just Use Regular Java Methods?

The example intentionally uses a regular LoggingService to demonstrate the problem.

Explicitly calling:

```java
loggingService.log()
```

works, but the business service must know about the logging concern.

With AOP, the goal is to move that responsibility outside the service.

This allows the service to focus on:

```text
Business Logic
```

while the aspect handles:

```text
Cross-Cutting Logic
```

---

## AOP in Spring

Spring AOP provides a proxy-based implementation of AOP.

At a high level, Spring can create a proxy around a Spring-managed bean:

```text
Caller
   |
   v
Proxy
   |
   +---- Aspect logic
   |
   v
Target Bean
   |
   v
Business method
```

The proxy can intercept method calls and apply additional behavior before, after, or around the target method.

The next examples will explore how these proxies work.

---

## Key Takeaways

* AOP is used to separate cross-cutting concerns from business logic.
* Cross-cutting concerns can affect many unrelated parts of an application.
* Logging is a common example of a cross-cutting concern.
* Without AOP, cross-cutting concerns can become scattered throughout application code.
* An aspect represents a cross-cutting concern.
* Advice defines what an aspect does.
* A pointcut determines where an aspect applies.
* Spring AOP uses proxies to apply aspects to Spring-managed beans.
* Spring AOP primarily works with method execution join points.

---

## What’s Next?

The next examples will explore how Spring implements AOP using proxies.

The progression will be:

```text
What is AOP?
      ↓
JDK Dynamic Proxies
      ↓
CGLIB Proxies
      ↓
Creating an Aspect
      ↓
Before Advice
      ↓
After Advice
      ↓
Around Advice
      ↓
Pointcuts
      ↓
Advice Ordering
```

The next example will focus on **JDK Dynamic Proxies** and how Spring can use interface-based proxies to apply additional behavior around a target bean.
