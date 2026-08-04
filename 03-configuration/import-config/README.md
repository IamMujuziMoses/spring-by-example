# `@Import`

The `@Import` annotation allows one Spring configuration class to import one or more other configuration classes.

As applications grow, placing every bean definition in a single configuration class becomes difficult to manage. `@Import` helps organize configuration into smaller, focused modules while combining them into a single `ApplicationContext`.

In this example, you'll learn how multiple configuration classes work together using `@Import`.

---

## Learning Objectives

By the end of this module, you will be able to:

- Understand the purpose of `@Import`.
- Learn how to split configuration across multiple classes.
- Understand how imported configuration classes contribute beans to the same `ApplicationContext`.
- Learn when to use `@Import`.
- Organize Spring configuration more effectively.

---

## What Is `@Import`?

`@Import` allows one configuration class to include one or more other configuration classes.

```java
@Configuration
@Import({
        GreetingConfig.class,
        TimeConfig.class
})
public class AppConfig {

}
```

When Spring processes `AppConfig`, it also processes each imported configuration class as though its bean definitions were declared directly.

---

## Why Use `@Import`?

Small applications may only require a single configuration class.

As an application grows, however, keeping all bean definitions in one place becomes difficult to maintain.

Instead, configuration can be organized by feature or responsibility.

For example:

- `GreetingConfig` registers greeting-related beans.
- `TimeConfig` registers time-related beans.
- `AppConfig` combines both configurations.

This approach keeps configuration modular and easier to understand.

---

## Imported Configurations

Each imported configuration defines its own beans.

```java
@Configuration
public class GreetingConfig {

    @Bean
    GreetingService greetingService() {
        return new GreetingService();
    }

}
```

```java
@Configuration
public class TimeConfig {

    @Bean
    TimeService timeService() {
        return new TimeService();
    }

}
```

Although the beans are declared in different configuration classes, they all become part of the same application context.

---

## Running the Application

After creating the application context, beans from both imported configurations are available.

```java
GreetingService greeting = context.getBean(GreetingService.class);

TimeService time = context.getBean(TimeService.class);

System.out.println(greeting.greet());
System.out.println(time.getTimeZone());
```

Expected output:

```text
Hello from GreetingService!
UTC
```

---

## How `@Import` Works

```text
             AppConfig
                 │
        ┌────────┴────────┐
GreetingConfig      TimeConfig
        │                 │
GreetingService    TimeService
        │                 │
        └────────┬────────┘
        ApplicationContext
```

Spring processes each imported configuration and registers all discovered beans in the same `ApplicationContext`.

---

## Benefits of `@Import`

Using `@Import` provides several advantages:

- Organizes configuration into smaller classes.
- Improves readability.
- Encourages modular application design.
- Makes configuration easier to maintain.
- Allows configuration to grow without becoming difficult to manage.

---

## Best Practices

- Group related beans into dedicated configuration classes.
- Keep configuration classes focused on a single responsibility.
- Use `@Import` to compose larger applications.
- Avoid placing every bean definition into one configuration class.
- Choose descriptive names that reflect each configuration's purpose.

---

## How It Works Internally

When the application starts, Spring performs the following steps:

```text
Application starts
        │
AppConfig discovered
        │
@Import processed
        │
GreetingConfig loaded
TimeConfig loaded
        │
@Bean methods processed
        │
BeanDefinitions created
        │
Beans instantiated
        │
ApplicationContext ready
```

From Spring's perspective, imported configuration classes are processed exactly like the primary configuration class.

---

## Key Takeaways

- `@Import` allows configuration classes to be composed together.
- Imported configurations contribute beans to the same `ApplicationContext`.
- Splitting configuration improves organization and maintainability.
- `@Import` is commonly used in larger Spring applications.
- Modular configuration makes applications easier to extend.

---

## What's Next?

So far, you've learned how to organize Java-based configuration using `@Import`.

The next example explores `@ImportResource`, which allows Spring to import traditional XML configuration into a Java-based application, making it easier to integrate legacy Spring applications with modern configuration styles.