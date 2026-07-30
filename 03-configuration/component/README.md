# `@Component`

The `@Component` annotation marks a class as a **Spring-managed component**. During application startup, Spring can automatically discover classes annotated with `@Component` and register them as beans in the Spring IoC container.

Unlike the previous example, where beans were explicitly registered using `@Bean`, this example introduces **automatic bean discovery**.

> **Note:** This example uses `@ComponentScan` to enable component discovery. The `@ComponentScan` annotation will be explored in detail in a later module.

---

## Learning Objectives

By the end of this module, you will be able to:

- Understand the purpose of the `@Component` annotation.
- Learn how Spring automatically discovers components.
- Understand the difference between `@Component` and `@Bean`.
- Retrieve automatically discovered beans from the `ApplicationContext`.
- Know when to use `@Component`.

---

## What Is `@Component`?

`@Component` is a stereotype annotation that tells Spring a class should be managed by the IoC container.

```java
@Component
public class GreetingService {

    public String greet() {
        return "Hello from GreetingService!";
    }

}
```

When Spring scans the application, it discovers this class, creates an instance, and registers it as a bean.

---

## Enabling Component Discovery

For Spring to discover components, component scanning must be enabled.

```java
@Configuration
@ComponentScan("com.springbyexample.component")
public class AppConfig {

}
```

In this example, `@ComponentScan` instructs Spring to scan the specified package for classes annotated with `@Component`.

> We'll explore `@ComponentScan` in depth in a dedicated module.

---

## Using the Component

Once the application context has been created, the component can be retrieved just like any other Spring bean.

```java
GreetingService greetingService = context.getBean(GreetingService.class);

System.out.println(greetingService.greet());
```

Expected output:

```text
Hello from GreetingService!
```

---

## `@Component` vs `@Bean`

| `@Component` | `@Bean` |
|---------------|---------|
| Applied to a class | Applied to a method |
| Bean is discovered automatically | Bean is registered explicitly |
| Requires component scanning | Requires a configuration class |
| Best for your own application classes | Best for third-party or external classes |

Both approaches register beans with the Spring IoC container, but they are designed for different use cases.

---

## When Should You Use `@Component`?

Use `@Component` when:

- The class belongs to your application.
- Spring should automatically manage the class.
- The class does not require custom construction logic.
- You want to reduce configuration boilerplate.

Use `@Bean` when:

- Registering third-party classes.
- Registering legacy classes.
- Creating objects that require custom initialization.

---

## How It Works Internally

When the application starts, Spring performs the following steps:

```text
Application Starts
        │
Create ApplicationContext
        │
Read @ComponentScan
        │
Scan Packages
        │
Find @Component Classes
        │
Create Bean Instances
        │
Register Beans
        │
Application Ready
```

Spring automatically creates and registers every discovered component.

---

## Best Practices

- Use `@Component` for general-purpose application components.
- Keep components focused on a single responsibility.
- Use specialized stereotype annotations (`@Service`, `@Repository`, and `@Controller`) when appropriate.
- Organize components into meaningful packages.

---

## Key Takeaways

- `@Component` marks a class as a Spring-managed bean.
- Spring discovers components through component scanning.
- Components are automatically registered in the IoC container.
- `@Component` reduces the need for explicit `@Bean` methods.
- Use `@Component` for application classes and `@Bean` for external or custom-created objects.

---

## What's Next?

The next example explores the `@Service` annotation.

You'll learn:

- How `@Service` specializes `@Component`.
- Why Spring provides stereotype annotations.
- When to use `@Service` for business logic.
- How stereotype annotations improve code readability.