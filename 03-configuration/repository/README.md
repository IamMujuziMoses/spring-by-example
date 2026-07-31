# `@Repository`

The `@Repository` annotation is a specialization of `@Component` that identifies a class as part of the **repository (data access) layer** of an application.

From Spring's perspective, a class annotated with `@Repository` is registered as a Spring bean just like a class annotated with `@Component`. The primary difference is **intent**—`@Repository` communicates that the class is responsible for interacting with a data source.

In addition to improving readability, Spring can apply persistence-related functionality, such as exception translation, to classes annotated with `@Repository`.

> **Note:** This example uses `@ComponentScan` to enable component discovery. Component scanning is covered in detail in a later module.

---

## Learning Objectives

By the end of this module, you will be able to:

- Understand the purpose of the `@Repository` annotation.
- Learn how `@Repository` relates to `@Component`.
- Understand the role of the repository layer.
- Learn why Spring provides stereotype annotations.
- Know when to use `@Repository`.

---

## What Is `@Repository`?

`@Repository` marks a class as a **repository component**.

```java
@Repository
public class GreetingRepository {

    public String findGreeting() {
        return "Hello from GreetingRepository!";
    }

}
```

During component scanning, Spring discovers the class, creates an instance, and registers it as a managed bean.

---

## `@Repository` Is a Specialized `@Component`

The `@Repository` annotation is itself annotated with `@Component`.

Conceptually, it looks like this:

```java
@Component
public @interface Repository {

}
```

This means every class annotated with `@Repository` is also treated as a Spring component.

The benefit is not different bean registration behavior, but clearer communication of the class's role within the application.

---

## The Repository Layer

In a typical layered application, the repository layer is responsible for interacting with the data source.

```text
Presentation Layer
        │
Service Layer
        │
Repository Layer
        │
Database
```

Repositories encapsulate data access operations, allowing the service layer to focus on business logic instead of persistence details.

---

## Using the Repository

After the application context is created, the repository can be retrieved like any other Spring bean.

```java
GreetingRepository repository = context.getBean(GreetingRepository.class);

System.out.println(repository.findGreeting());
```

Expected output:

```text
Hello from GreetingRepository!
```

---

## `@Component` vs `@Repository`

| `@Component` | `@Repository` |
|--------------|---------------|
| Generic Spring component | Data access component |
| General-purpose stereotype | Persistence stereotype |
| Used for any managed class | Used for repositories and DAOs |
| Automatically discovered | Automatically discovered |

From Spring's perspective, both annotations register beans through component scanning. The difference is semantic—it helps developers understand the responsibility of the class.

---

## Spring Exception Translation

One advantage of using `@Repository` is that Spring can automatically translate technology-specific persistence exceptions into Spring's consistent `DataAccessException` hierarchy.

This provides a common exception model regardless of the underlying persistence technology.

> Exception translation is an advanced topic and will be explored in later modules.

---

## Why Use `@Repository`?

Using `@Repository` makes your codebase easier to understand.

When another developer sees a class annotated with `@Repository`, they immediately know that the class is responsible for data access rather than business logic or request handling.

Specialized stereotype annotations also help organize an application's architecture and improve maintainability.

---

## How It Works Internally

When the application starts, Spring performs the following steps:

```text
@Repository
       │
@Repository is meta-annotated with @Component
       │
Component scanning discovers the class
       │
Spring creates the bean
       │
Registers it in the ApplicationContext
```

For supported persistence technologies, Spring can also apply persistence exception translation.

---

## Best Practices

- Use `@Repository` for classes that interact with a data source.
- Keep repositories focused on persistence operations.
- Avoid placing business logic inside repositories.
- Let services coordinate business operations while repositories handle data access.

---

## Key Takeaways

- `@Repository` is a specialization of `@Component`.
- It identifies classes that belong to the repository layer.
- Spring automatically discovers and registers repositories during component scanning.
- `@Repository` improves readability and communicates architectural intent.
- Spring can provide additional persistence support, including exception translation.

---

## What's Next?

The next example explores the `@Controller` annotation.

You'll learn:

- How `@Controller` specializes `@Component`.
- The role of controllers in Spring MVC.
- How controllers handle incoming requests.
- How the presentation layer interacts with the service layer.