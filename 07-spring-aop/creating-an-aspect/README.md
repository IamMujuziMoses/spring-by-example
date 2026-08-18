# Creating an Aspect

Spring AOP provides a way to separate cross-cutting concerns from application business logic.

This example demonstrates how to create a simple Spring AOP aspect and have Spring automatically apply it to a Spring-managed bean.

It builds on the previous JDK Dynamic Proxy and CGLIB Proxy examples by moving from manually created proxies to Spring-managed proxy-based AOP.

---

## Learning Objectives

By the end of this example, you will understand:

- What a Spring AOP aspect is.
- Why aspects are useful.
- How to create an aspect using `@Aspect`.
- How to register an aspect as a Spring bean.
- How `@EnableAspectJAutoProxy` enables proxy-based AOP.
- How Spring discovers and applies an aspect.
- How an aspect can intercept a target method.
- How `@Before` can be used to execute advice before a method.
- How Spring AOP builds on JDK dynamic proxies and CGLIB proxies.
- Why the target class does not need to know about the aspect.

---

# What Is an Aspect?

An **aspect** is a modular way of defining behavior that applies across multiple parts of an application.

These behaviors are often called **cross-cutting concerns**.

Examples include:

- Logging.
- Security.
- Transactions.
- Performance monitoring.
- Auditing.
- Caching.

Instead of putting this behavior directly into every service:

```java
public void createOrder(Long orderId) {

    System.out.println("Logging...");

    // Business logic
}
```

Spring AOP allows the concern to be separated:

```text
OrderService
     ↓
Business logic

OrderLoggingAspect
     ↓
Logging concern
```

Spring then connects the two through a proxy.

---

# Why Do Aspects Exist?

Cross-cutting concerns often appear in many different classes.

For example:

```text
OrderService
    ├── Logging
    ├── Security
    └── Transactions

PaymentService
    ├── Logging
    ├── Security
    └── Transactions

CustomerService
    ├── Logging
    ├── Security
    └── Transactions
```

Without AOP, the same code can become duplicated throughout the application.

AOP allows these concerns to be separated:

```text
             Cross-cutting concerns
              /        |        \
             /         |         \
        Logging     Security   Transactions
             \         |         /
              \        |        /
               Spring AOP
                    ↓
             Application beans
```

The goal is to keep business logic focused on the business problem while allowing common behavior to be applied separately.

---

# Aspect vs Advice

An important distinction is that an **aspect** is the class that groups cross-cutting behavior, while **advice** is the behavior that executes at a particular point in the method invocation.

For example:

```java
@Aspect
@Component
public class OrderLoggingAspect {

    @Before("execution(* com.springbyexample.creatinganaspect.OrderService.createOrder(..))")
    public void logBeforeCreateOrder() {
        System.out.println("About to create order");
    }
}
```

Here:

```text
OrderLoggingAspect
        ↓
      Aspect
        ↓
logBeforeCreateOrder()
        ↓
      Advice
```

The `@Before` annotation specifies when the advice should execute.

The next examples in this module will explore the different advice types in more detail.

---

# Example

The example consists of three main components:

```text
AppConfig
    ↓
Spring configuration

OrderService
    ↓
Target bean

OrderLoggingAspect
    ↓
Cross-cutting behavior
```

Spring connects them through its AOP infrastructure.

---

# OrderService

The target class contains the business logic.

## `OrderService`

```java
@Service
public class OrderService {

    public void createOrder(Long orderId) {
        System.out.println("Creating order: " + orderId);
    }
}
```

Notice that `OrderService` does not contain any logging-related code.

It only knows about its own responsibility:

```java
public void createOrder(Long orderId) {
    System.out.println("Creating order: " + orderId);
}
```

The logging concern is handled by the aspect.

---

# Creating the Aspect

Spring identifies an aspect using the `@Aspect` annotation.

## `OrderLoggingAspect`

```java
@Aspect
@Component
public class OrderLoggingAspect {

    private boolean invoked;

    @Before("execution(* com.springbyexample.creatinganaspect.OrderService.createOrder(..))")
    public void logBeforeCreateOrder() {
        invoked = true;

        System.out.println("About to create order");
    }

    public boolean wasInvoked() {
        return invoked;
    }
}
```

There are two important annotations here:

```java
@Aspect
```

and:

```java
@Component
```

`@Aspect` tells Spring that the class contains AOP definitions.

`@Component` makes the aspect a Spring-managed bean.

---

# What Does `@Aspect` Do?

The:

```java
@Aspect
```

annotation identifies the class as an AspectJ-style aspect that Spring AOP can process.

For example:

```java
@Aspect
public class OrderLoggingAspect {
}
```

The class can then contain advice methods such as:

```java
@Before(...)
public void logBeforeCreateOrder() {
}
```

Conceptually:

```text
@Aspect
   ↓
Spring identifies aspect
   ↓
Spring reads its advice
   ↓
Spring creates appropriate proxy
   ↓
Proxy applies advice to matching beans
```

---

# Why Is `@Component` Needed?

The aspect also uses:

```java
@Component
```

This registers it as a Spring bean.

```java
@Aspect
@Component
public class OrderLoggingAspect {
}
```

The two annotations have different responsibilities:

| Annotation | Purpose |
|---|---|
| `@Aspect` | Identifies the class as an aspect |
| `@Component` | Registers the class as a Spring bean |

Spring needs to discover the aspect as part of the application context before it can apply it.

---

# The Pointcut Expression

The example uses:

```java
@Before("execution(* com.springbyexample.creatingaspect.OrderService.createOrder(..))")
```

The expression identifies which method should be intercepted.

For this example, it targets:

```java
OrderService.createOrder(...)
```

The simplified structure is:

```text
execution(return-type, package.Class.method(arguments))
```

The expression:

```text
execution(* com.springbyexample.creatingaspect.OrderService.createOrder(..))
```

means:

```text
execution
   ↓
any return type
   ↓
com.springbyexample.creatinganaspect.OrderService
   ↓
createOrder
   ↓
any arguments
```

Pointcuts will be explored in greater detail in the dedicated **Pointcuts** example later in this module.

---

# Configuration

Spring needs to be configured to discover the beans and enable proxy-based AOP.

## `AppConfig`

```java
@Configuration
@ComponentScan
@EnableAspectJAutoProxy
public class AppConfig {
}
```

There are three important annotations:

```java
@Configuration
@ComponentScan
@EnableAspectJAutoProxy
```

---

# `@Configuration`

```java
@Configuration
```

marks `AppConfig` as a source of Spring configuration.

It allows the class to be used to create the application context:

```java
new AnnotationConfigApplicationContext(AppConfig.class);
```

---

# `@ComponentScan`

```java
@ComponentScan
```

tells Spring to scan the package containing `AppConfig` and discover component classes.

This allows Spring to discover:

```java
@Service
public class OrderService {
}
```

and:

```java
@Component
@Aspect
public class OrderLoggingAspect {
}
```

The resulting application context contains both beans.

---

# `@EnableAspectJAutoProxy`

The most important configuration for this example is:

```java
@EnableAspectJAutoProxy
```

It enables Spring's annotation-driven AOP proxy infrastructure.

Conceptually:

```text
@EnableAspectJAutoProxy
          ↓
Spring AOP enabled
          ↓
Spring finds aspects
          ↓
Spring finds matching beans
          ↓
Spring creates proxies
          ↓
Method calls can be intercepted
```

Without this configuration, Spring will not automatically create the AOP proxies required for this example.

---

# How Spring Connects Everything

When the application context starts, Spring discovers:

```text
OrderService
OrderLoggingAspect
```

It then uses the aspect definition to determine whether `OrderService` needs to be proxied.

The resulting structure is conceptually:

```text
              ApplicationContext
                      ↓
             OrderLoggingAspect
                      ↓
                 Spring AOP
                      ↓
                Proxy object
                      ↓
                OrderService
```

The application receives the proxy rather than directly interacting with the raw target object.

---

# Running the Example

## `Main`

```java
public class Main {

    public static void main(String[] args) {

        try (var context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {

            var orderService = context.getBean(OrderService.class);

            orderService.createOrder(1001L);
        }
    }
}
```

The application retrieves the service from the Spring context:

```java
var orderService = context.getBean(OrderService.class);
```

It then invokes:

```java
orderService.createOrder(1001L);
```

Spring's proxy intercepts the invocation and applies the matching advice.

---

# Expected Output

Running the example produces:

```text
About to create order
Creating order: 1001
```

The output demonstrates that the aspect executes before the target method.

The target method itself only prints:

```text
Creating order: 1001
```

The additional:

```text
About to create order
```

comes from the aspect.

---

# Understanding the Method Call

The application code appears to call:

```java
orderService.createOrder(1001L);
```

But conceptually, the call goes through a Spring AOP proxy:

```text
orderService.createOrder(1001L)
              ↓
        Spring AOP Proxy
              ↓
        Matching pointcut
              ↓
          @Before advice
              ↓
   "About to create order"
              ↓
       OrderService
              ↓
       createOrder()
              ↓
   "Creating order: 1001"
```

This is the fundamental proxy-based model behind this example.

---


# Why Use a Spring Application Context in the Test?

A Spring AOP aspect is applied by Spring's proxy infrastructure.

Therefore, this would not test the actual Spring AOP behavior:

```java
var orderService = new OrderService();
```

The object would simply be a normal Java object.

Instead, the test creates:

```java
new AnnotationConfigApplicationContext(AppConfig.class);
```

This allows Spring to:

1. Discover the target bean.
2. Discover the aspect.
3. Process the aspect.
4. Create the appropriate proxy.
5. Apply the advice when the method is invoked.

The flow is:

```text
ApplicationContext
        ↓
Component scanning
        ↓
OrderService discovered
        ↓
OrderLoggingAspect discovered
        ↓
@EnableAspectJAutoProxy
        ↓
Spring creates proxy
        ↓
Test retrieves proxied OrderService
        ↓
Method invocation
        ↓
Aspect executes
```

---

# Why Doesn't `OrderService` Know About the Aspect?

One of the main benefits of AOP is separation of concerns.

`OrderService` contains:

```java
public void createOrder(Long orderId) {
    System.out.println("Creating order: " + orderId);
}
```

It does not contain:

```java
System.out.println("About to create order");
```

The logging behavior is located in:

```java
OrderLoggingAspect
```

This keeps the business logic independent from the cross-cutting concern.

Conceptually:

```text
OrderService
    ↓
Business concern

OrderLoggingAspect
    ↓
Logging concern

Spring AOP
    ↓
Connects the two
```

---

# Aspect-Based vs Direct Implementation

Without AOP:

```java
public void createOrder(Long orderId) {

    System.out.println("About to create order");

    System.out.println("Creating order: " + orderId);
}
```

With AOP:

```text
OrderService
    ↓
Creating order

OrderLoggingAspect
    ↓
About to create order
```

Spring combines them through the proxy:

```text
Caller
  ↓
Proxy
  ↓
Aspect
  ↓
Target
```

This becomes particularly valuable when the same concern applies to many different methods or classes.

---

# How This Relates to JDK Dynamic Proxies

The previous example demonstrated JDK Dynamic Proxies.

The proxy structure was conceptually:

```text
Caller
   ↓
JDK Dynamic Proxy
   ↓
InvocationHandler
   ↓
Target
```

The proxy had to implement an interface.

---

# How This Relates to CGLIB

The previous CGLIB example demonstrated class-based proxying:

```text
Caller
   ↓
CGLIB Proxy
   ↓
MethodInterceptor
   ↓
Target
```

The proxy was generated as a subclass of the target class.

---

# Spring AOP Builds on These Concepts

This example takes the proxy concept one step further.

Instead of manually creating:

```java
Enhancer
```

or:

```java
Proxy.newProxyInstance(...)
```

Spring manages the process.

Conceptually:

```text
JDK Dynamic Proxy
        ↓
Manual proxy creation

CGLIB Proxy
        ↓
Manual proxy creation

Spring AOP
        ↓
Spring creates and manages proxies
        ↓
Aspects determine additional behavior
```

This is why understanding the previous examples makes Spring AOP easier to understand.

---

# Aspect, Proxy, and Target

The three concepts can be viewed together:

```text
             Aspect
                ↓
        Defines cross-cutting
             behavior
                ↓
          Spring AOP
                ↓
             Proxy
                ↓
        Intercepts invocation
                ↓
             Target
                ↓
        Executes business logic
```

For this example:

```text
OrderLoggingAspect
        ↓
       Proxy
        ↓
   OrderService
```

---

# What Is Being Introduced Here?

This example intentionally introduces only the basic concepts required to create an aspect.

We have:

```java
@Aspect
```

to identify the aspect.

We have:

```java
@Component
```

to register it as a Spring bean.

We have:

```java
@EnableAspectJAutoProxy
```

to enable proxy-based AOP.

And we have:

```java
@Before(...)
```

to define advice that executes before the matching method.

The following examples will explore each of these concepts in greater depth.

---

# Dependency

This example requires Spring AOP and AspectJ Weaver.

Add the following dependencies if they are not already available:

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aop</artifactId>
    <version>${spring.version}</version>
</dependency>

<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
    <version>1.9.24</version>
</dependency>
```

`spring-aop` provides Spring's AOP infrastructure.

`aspectjweaver` provides the AspectJ expression and weaving infrastructure used by Spring's annotation-driven AOP support.

---

# Important Considerations

## Spring AOP Uses Proxies

Spring AOP is proxy-based.

Conceptually:

```text
Caller
   ↓
Spring AOP Proxy
   ↓
Advice
   ↓
Target method
```

This is different from full AspectJ compile-time or load-time weaving.

---

## The Bean Must Be Managed by Spring

The aspect is applied to Spring-managed beans.

This works:

```java
var orderService = context.getBean(OrderService.class);
```

because Spring manages the bean.

Creating the object manually:

```java
var orderService = new OrderService();
```

does not cause Spring AOP advice to be automatically applied.

---

## The Aspect Must Be Discoverable

The aspect needs to be registered as a Spring bean.

For this example:

```java
@Component
@Aspect
public class OrderLoggingAspect {
}
```

and:

```java
@ComponentScan
```

allows Spring to discover it.

---

# Key Takeaways

- An aspect encapsulates cross-cutting behavior.
- Cross-cutting concerns include logging, security, transactions, caching, and monitoring.
- `@Aspect` identifies a class as an aspect.
- `@Component` registers the aspect as a Spring bean.
- `@EnableAspectJAutoProxy` enables Spring's proxy-based AOP infrastructure.
- Advice defines behavior that executes at a particular point during method invocation.
- `@Before` can execute advice before a matching method.
- A pointcut determines which methods the advice applies to.
- Spring creates proxies around matching Spring-managed beans.
- The target class does not need to contain the cross-cutting behavior.
- Spring AOP builds on the proxy concepts introduced by JDK Dynamic Proxies and CGLIB.
- Spring manages the proxy creation instead of requiring application code to create proxies manually.
- This example provides the foundation for understanding different advice types and pointcuts.

---

# What's Next?

The next example introduces:

**Before Advice**

This example used `@Before` to demonstrate the basic structure of an aspect.

Next, we'll focus specifically on how **before advice** works and when it is appropriate.

---