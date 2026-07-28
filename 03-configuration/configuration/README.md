# @Configuration

The `@Configuration` annotation marks a class as a **source of bean definitions** for the Spring IoC container.

Instead of defining beans in an XML file, Spring allows you to use Java classes to configure your application. Classes annotated with `@Configuration` are processed during application startup, and the beans they define are registered with the Spring IoC container.

This module introduces Java-based configuration and demonstrates how Spring uses configuration classes to build the application context.

---

## Learning Objectives

By the end of this module, you will be able to:

- Understand the purpose of the `@Configuration` annotation.
- Learn how Spring processes configuration classes.
- Create and load a Java-based configuration class.
- Understand the relationship between `ApplicationContext` and `@Configuration`.
- Compare Java-based configuration with traditional XML configuration.

---

## What Is `@Configuration`?

The `@Configuration` annotation tells Spring that a class contains bean definitions that should be processed during application startup.

```java
@Configuration
public class AppConfig {

    @Bean
    public GreetingService greetingService() {
        return new GreetingService();
    }

}
```

When the `ApplicationContext` is created, Spring detects the `@Configuration` annotation, processes the class, and registers the beans defined within it.

---

## Why Use `@Configuration`?

Before Java-based configuration, Spring applications commonly defined beans in XML files.

Java configuration provides several advantages:

- Type-safe configuration.
- Refactoring support from IDEs.
- Compile-time checking.
- Better readability.
- Easier navigation and maintenance.

Today, Java configuration is the preferred approach for most Spring applications.

---

## Loading a Configuration Class

A configuration class is supplied to the `ApplicationContext` during startup.

```java
var context = new AnnotationConfigApplicationContext(AppConfig.class);
```

Spring reads the configuration class, processes its bean definitions, and initializes the application context.

---

## Running the Example

Run the `Main` class.

Expected output:

```text
Hello from GreetingService!
```

This demonstrates that Spring successfully loaded the configuration class and created the `GreetingService` bean.

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
Process Bean Definitions
        │
Register Beans
        │
Application Ready
```

The `@Configuration` annotation acts as metadata that instructs Spring where to find bean definitions.

---

## Java Configuration vs XML Configuration

| Java Configuration | XML Configuration |
|--------------------|-------------------|
| Uses Java classes | Uses XML files |
| Type-safe | String-based configuration |
| IDE refactoring support | Limited refactoring support |
| Compile-time checking | Errors often appear at runtime |
| Preferred for modern Spring applications | Common in legacy applications |

---

## Best Practices

- Keep configuration classes focused on a single responsibility.
- Organize related bean definitions together.
- Use meaningful class names such as `AppConfig` or `DatabaseConfig`.
- Prefer Java configuration for new applications.

---

## Key Takeaways

- `@Configuration` marks a class as a source of bean definitions.
- Spring processes configuration classes during application startup.
- Java configuration replaces most XML configuration in modern Spring applications.
- Configuration classes are loaded by the `ApplicationContext`.
- Java configuration is type-safe, maintainable, and IDE-friendly.

---

## What's Next?

The next example explores the `@Bean` annotation.

You'll learn:

- How beans are registered with the Spring IoC container.
- How `@Bean` methods work.
- How Spring determines bean names.
- When to use `@Bean` instead of component scanning.