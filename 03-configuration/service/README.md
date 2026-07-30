# `@Service`

The `@Service` annotation is a specialization of `@Component` that identifies a class as part of the **service layer** of an application.

From Spring's perspective, a class annotated with `@Service` is registered as a Spring bean just like a class annotated with `@Component`. The primary difference is **intent**—`@Service` communicates that the class contains business logic.

This module demonstrates how `@Service` helps organize an application's architecture while continuing to leverage Spring's component scanning mechanism.

> **Note:** This example uses `@ComponentScan` to enable component discovery. Component scanning is covered in detail in a later module.

---

## Learning Objectives

By the end of this module, you will be able to:

- Understand the purpose of the `@Service` annotation.
- Learn how `@Service` relates to `@Component`.
- Understand the role of the service layer.
- Learn why Spring provides stereotype annotations.
- Know when to use `@Service` instead of `@Component`.

---

## What Is `@Service`?

`@Service` marks a class as a **service component**.

```java
@Service
public class GreetingService {

    public String greet() {
        return "Hello from GreetingService!";
    }

}
```

During component scanning, Spring discovers the class, creates an instance, and registers it as a managed bean.

---

## `@Service` Is a Specialized `@Component`

The `@Service` annotation is itself annotated with `@Component`.

Conceptually, it looks like this:

```java
@Component
public @interface Service {

}
```

This means every class annotated with `@Service` is also treated as a Spring component.

The benefit is not different bean registration behavior, but clearer communication of the class's role within the application.

---

## The Service Layer

In a typical layered application, the service layer contains the application's business logic.

```text
Presentation Layer
        │
Service Layer
        │
Repository Layer
        │
Database
```

Services coordinate application behavior, perform business operations, and often interact with one or more repositories.

---

## Using the Service

After the application context is created, the service can be retrieved like any other Spring bean.

```java
GreetingService greetingService = context.getBean(GreetingService.class);

System.out.println(greetingService.greet());
```

Expected output:

```text
Hello from GreetingService!
```

---

## `@Component` vs `@Service`

| `@Component` | `@Service` |
|---------------|------------|
| Generic Spring component | Business/service layer component |
| General-purpose stereotype | Specialized stereotype |
| Used for any managed class | Used for business logic |
| Automatically discovered | Automatically discovered |

From Spring's perspective, both annotations register beans through component scanning. The difference is semantic—it helps developers understand the responsibility of the class.

---

## Why Use `@Service`?

Using `@Service` makes your codebase easier to understand.

When another developer sees a class annotated with `@Service`, they immediately know that the class is responsible for business logic rather than configuration, persistence, or web requests.

Specialized stereotype annotations also make the application's architecture more consistent and self-documenting.

---

## How It Works Internally

When the application starts, Spring performs the following steps:

```text
@Service
      │
@Service is meta-annotated with @Component
      │
Component scanning discovers the class
      │
Spring creates the bean
      │
Registers it in the ApplicationContext
```

Although `@Service` is a specialized annotation, the bean registration process is the same as for `@Component`.

---

## Best Practices

- Use `@Service` for classes that contain business logic.
- Keep services focused on a single responsibility.
- Avoid placing persistence logic directly in services.
- Use meaningful service names that describe their responsibilities.

---

## Key Takeaways

- `@Service` is a specialization of `@Component`.
- It identifies classes that belong to the service layer.
- Spring automatically discovers and registers services during component scanning.
- `@Service` improves readability and communicates architectural intent.
- From a bean registration perspective, `@Service` behaves like `@Component`.

---

## What's Next?

The next example explores the `@Repository` annotation.

You'll learn:

- How `@Repository` specializes `@Component`.
- The role of the repository layer.
- How Spring enhances persistence components.
- When to use `@Repository` instead of `@Service`.