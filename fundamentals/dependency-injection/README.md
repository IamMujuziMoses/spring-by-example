# Dependency Injection

## Goal

Learn what **Dependency Injection (DI)** is, why it exists, and how Spring uses it to create and wire objects together.

By the end of this example, you will understand:

- What a dependency is
- The drawbacks of manually creating dependencies
- How Spring manages object creation
- Constructor injection
- Why constructor injection is the recommended approach

---

## Prerequisites

Before continuing, complete the previous example:

- **Hello Bean**

It introduces:

- Spring Beans
- `ApplicationContext`
- `@Configuration`
- `@Bean`

---

## The Problem

Imagine a `HelloService` needs a `GreetingService`.

A common approach is to create the dependency directly.

```java
public class HelloService {

    private final GreetingService greetingService = new GreetingService();

    public void sayHello() {
        System.out.println(greetingService.getGreeting());
    }
}
```

This works, but it tightly couples `HelloService` to `GreetingService`.

As applications grow, manually creating every dependency becomes difficult to maintain and test.

---

## What is Dependency Injection?

Dependency Injection is a design pattern where an object's dependencies are provided from the outside instead of being created by the object itself.

Instead of this:

```text
HelloService
      |
      └── new GreetingService()
```

Spring does this:

```text
ApplicationContext
        |
        +----------------------+
        |                      |
        v                      v
GreetingService         HelloService
                               ^
                               |
                     Injected by Spring
```

The Spring container is responsible for creating objects and connecting them together.

This principle is also known as **Inversion of Control (IoC)**.

---

## Constructor Injection

In this example, `HelloService` receives its dependency through its constructor.

```java
public HelloService(GreetingService greetingService) {
    this.greetingService = greetingService;
}
```

Notice that `HelloService` never creates a `GreetingService`.

Spring provides it automatically.

---

## Java Configuration

Spring creates both beans inside the configuration class.

```java
@Bean
public GreetingService greetingService() {
    return new GreetingService();
}

@Bean
public HelloService helloService(GreetingService greetingService) {
    return new HelloService(greetingService);
}
```

The `GreetingService` parameter is automatically resolved from the `ApplicationContext`.

---

## Running the Example

Execute the `Main` class.

Expected output:

```text
Hello from GreetingService!
```

---

## Key Takeaways

- A dependency is an object another object needs.
- Objects should not be responsible for creating their own dependencies.
- Spring manages object creation and wiring.
- Constructor injection is simple, explicit, and easy to test.
- Dependency Injection reduces coupling and improves maintainability.

---

## Behind the Scenes

When the application starts, the `ApplicationContext` scans the configuration class and discovers the `@Bean` methods.

```java
@Bean
public GreetingService greetingService() {
    return new GreetingService();
}

@Bean
public HelloService helloService(GreetingService greetingService) {
    return new HelloService(greetingService);
}
```

Spring first creates the `GreetingService` bean because `HelloService` depends on it.

When Spring prepares to call:

```java
helloService(GreetingService greetingService);
```

it notices that the method requires a `GreetingService`.

Instead of expecting you to provide one, Spring searches its container for a bean of that type.

It finds the `GreetingService` bean that was created earlier and automatically passes it to the method.

Internally, the process looks something like this:

```text
Application starts
        │
        ▼
Create ApplicationContext
        │
        ▼
Read @Configuration class
        │
        ▼
Find @Bean methods
        │
        ▼
Create GreetingService bean
        │
        ▼
Need HelloService bean
        │
        ▼
Find GreetingService dependency
        │
        ▼
Inject GreetingService
        │
        ▼
Create HelloService bean
        │
        ▼
Store both beans in the ApplicationContext
```

Later, when your code executes:

```java
HelloService service = context.getBean(HelloService.class);
```

Spring does **not** create a new `HelloService`.

Instead, it returns the existing bean that it already created during application startup.

This behavior is the default because Spring beans are **singleton** scoped unless configured otherwise.

---

## Did You Know?

The method parameter name is **not** what Spring uses to inject the dependency.

For example, all of these methods work exactly the same:

```java
@Bean
public HelloService helloService(GreetingService greetingService) {
    return new HelloService(greetingService);
}
```

```java
@Bean
public HelloService helloService(GreetingService service) {
    return new HelloService(service);
}
```

```java
@Bean
public HelloService helloService(GreetingService anythingYouWant) {
    return new HelloService(anythingYouWant);
}
```

Spring resolves the dependency by **type**, not by the parameter name.

Only when multiple beans of the same type exist do annotations like `@Qualifier` or `@Primary` become necessary.

---

## Common Mistakes

### Creating dependencies manually

```java
private final GreetingService greetingService = new GreetingService();
```

Doing this bypasses the Spring container.

### Forgetting to register a bean

Every dependency must be registered with Spring.

Either:

- using `@Bean`
- or (later) using component scanning.

### Using field injection everywhere

Field injection works, but constructor injection is generally preferred because it:

- makes dependencies explicit
- supports immutable fields
- improves testability

---

## Try It Yourself

As an exercise:

1. Create a `TimeService`.
2. Inject it into `GreetingService`.
3. Update the greeting to include the current time.

---

## What's Next?

Continue with:

- Constructor Injection
- Setter Injection
- Field Injection
- Bean Scopes