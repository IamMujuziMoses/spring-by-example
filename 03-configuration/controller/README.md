# `@Controller`

The `@Controller` annotation is a specialization of `@Component` that identifies a class as part of the **presentation layer** of an application.

From Spring's perspective, a class annotated with `@Controller` is registered as a Spring bean just like a class annotated with `@Component`. The primary difference is **intent**—`@Controller` communicates that the class is responsible for handling incoming requests and coordinating application flow.

In Spring MVC applications, controllers receive client requests, delegate business logic to services, and return responses.

> **Note:** This example uses `@ComponentScan` to enable component discovery. Component scanning is covered in detail in a later module. This example focuses on the `@Controller` stereotype rather than Spring MVC request mapping.

---

## Learning Objectives

By the end of this module, you will be able to:

- Understand the purpose of the `@Controller` annotation.
- Learn how `@Controller` relates to `@Component`.
- Understand the role of the presentation layer.
- Learn how controllers interact with services.
- Know when to use `@Controller`.

---

## What Is `@Controller`?

`@Controller` marks a class as a **presentation-layer component**.

```java
@Controller
public class GreetingController {

    private final GreetingService greetingService;

    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    public String greet() {
        return greetingService.getGreeting();
    }

}
```

During component scanning, Spring discovers the class, creates an instance, resolves its dependencies, and registers it as a managed bean.

---

## `@Controller` Is a Specialized `@Component`

The `@Controller` annotation is itself annotated with `@Component`.

Conceptually, it looks like this:

```java
@Component
public @interface Controller {

}
```

This means every class annotated with `@Controller` is also treated as a Spring component.

The benefit is not different bean registration behavior, but clearer communication of the class's responsibility within the application.

---

## The Presentation Layer

In a typical layered application, controllers act as the entry point into the application.

```text
Client
   │
Controller
   │
Service
   │
Repository
   │
Database
```

A controller receives a request, delegates business logic to a service, and returns the appropriate response.

---

## Using the Controller

After the application context is created, the controller can be retrieved like any other Spring bean.

```java
GreetingController controller = context.getBean(GreetingController.class);

System.out.println(controller.greet());
```

Expected output:

```text
Hello from GreetingService!
```

---

## `@Component` vs `@Controller`

| `@Component` | `@Controller` |
|--------------|---------------|
| Generic Spring component | Presentation-layer component |
| General-purpose stereotype | Web/presentation stereotype |
| Used for any managed class | Used for controllers |
| Automatically discovered | Automatically discovered |

From Spring's perspective, both annotations register beans through component scanning. The difference is semantic—it helps developers understand the responsibility of the class.

---

## Why Use `@Controller`?

Using `@Controller` makes your application's architecture easier to understand.

When another developer sees a class annotated with `@Controller`, they immediately know that it belongs to the presentation layer and is responsible for handling client interactions.

Controllers should remain lightweight by delegating business logic to services rather than implementing it directly.

---

## How It Works Internally

When the application starts, Spring performs the following steps:

```text
@Controller
      │
@Controller is meta-annotated with @Component
      │
Component scanning discovers the class
      │
Spring resolves constructor dependencies
      │
Spring creates the bean
      │
Registers it in the ApplicationContext
```

---

## Best Practices

- Use `@Controller` for presentation-layer components.
- Keep controllers lightweight.
- Delegate business logic to services.
- Avoid accessing repositories directly from controllers.
- Prefer constructor injection for dependencies.

---

## Key Takeaways

- `@Controller` is a specialization of `@Component`.
- It identifies classes that belong to the presentation layer.
- Spring automatically discovers and registers controllers during component scanning.
- Controllers should coordinate requests rather than implement business logic.
- Constructor injection is the preferred way to inject dependencies into controllers.

---

## What's Next?

So far, you've used `@Component`, `@Service`, `@Repository`, and `@Controller`, and Spring has automatically discovered each of them.

But how does Spring know where to find these classes?

The next example explores `@ComponentScan`, the annotation responsible for discovering and registering Spring-managed components.