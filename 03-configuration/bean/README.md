# @Bean

The `@Bean` annotation tells Spring that the object returned by a method should be **managed as a bean** within the Spring IoC container.

While `@Configuration` identifies a class as a source of bean definitions, `@Bean` identifies the individual methods that create those bean definitions.

This module demonstrates how Spring invokes `@Bean` methods during application startup and registers the returned objects as managed beans.

---

## Learning Objectives

By the end of this module, you will be able to:

- Understand the purpose of the `@Bean` annotation.
- Learn how Spring registers beans using Java configuration.
- Understand how bean names are determined.
- Register multiple beans within a configuration class.
- Know when to use `@Bean` instead of component scanning.

---

## What Is `@Bean`?

The `@Bean` annotation marks a method whose return value should be registered as a Spring bean.

```java
@Configuration
public class AppConfig {

    @Bean
    public GreetingService greetingService() {
        return new GreetingService();
    }

}
```

When Spring processes the configuration class, it invokes the `greetingService()` method and registers the returned `GreetingService` object in the IoC container.

---

## Registering Multiple Beans

A configuration class can define multiple beans.

```java
@Configuration
public class AppConfig {

    @Bean
    public GreetingService greetingService() {
        return new GreetingService();
    }

    @Bean
    public TimeService timeService() {
        return new TimeService();
    }

}
```

Each method annotated with `@Bean` contributes one bean definition to the application context.

---

## Bean Names

Unless explicitly specified, Spring uses the method name as the bean name.

```java
@Bean
public GreetingService greetingService() {
    return new GreetingService();
}
```

The bean is registered with the name:

```text
greetingService
```

Similarly,

```java
@Bean
public TimeService timeService() {
    return new TimeService();
}
```

is registered as:

```text
timeService
```

These names can be used to retrieve beans from the `ApplicationContext`.

---

## Loading the Beans

The beans can be retrieved by type.

```java
GreetingService greetingService =
        context.getBean(GreetingService.class);

TimeService timeService =
        context.getBean(TimeService.class);
```

Because these objects are managed by Spring, the IoC container controls their lifecycle.

---

## Why Does `@Bean` Exist?

One of the biggest advantages of `@Bean` is that it allows you to register classes that you cannot modify.

For example:

- Classes from third-party libraries.
- Legacy classes.
- External framework classes.
- Classes without Spring annotations.

Since these classes cannot be annotated with `@Component`, `@Service`, or similar annotations, `@Bean` provides a way to register them with the Spring IoC container.

---

## `@Bean` vs `@Component`

| `@Bean` | `@Component` |
|----------|--------------|
| Explicit bean registration | Automatic bean discovery |
| Declared inside a `@Configuration` class | Declared directly on the class |
| Ideal for third-party or external classes | Ideal for application classes |
| Gives full control over object creation | Simpler for most application components |

Both approaches register Spring beans, but they solve different problems.

---

## Running the Example

Run the `Main` class.

Expected output:

```text
Hello from GreetingService!
Current time: 10:15:42
```

(The displayed time will vary.)

This demonstrates that both `GreetingService` and `TimeService` have been registered successfully by Spring.

---

## How It Works Internally

When the application starts, Spring performs the following steps:

```text
Application Starts
        │
Create ApplicationContext
        │
Detect @Configuration
        │
Find @Bean methods
        │
Invoke each @Bean method
        │
Create bean instances
        │
Register beans in the IoC container
        │
Application Ready
```

Spring processes every `@Bean` method and stores the returned objects as managed beans.

---

## Best Practices

- Group related bean definitions in the same configuration class.
- Use descriptive method names since they become bean names by default.
- Prefer `@Bean` when registering third-party or external classes.
- Keep `@Bean` methods focused on creating and configuring a single object.

---

## Key Takeaways

- `@Bean` registers objects with the Spring IoC container.
- Every `@Bean` method contributes one bean definition.
- By default, the method name becomes the bean name.
- `@Bean` is ideal for classes that cannot be annotated.
- Spring manages the lifecycle of objects returned by `@Bean` methods.

---

## What's Next?

The next example explores the `@Component` annotation.

You'll learn:

- How Spring automatically discovers beans.
- What component scanning is.
- How `@Component` differs from `@Bean`.
- When to use `@Component` instead of explicit bean registration.