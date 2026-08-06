# Collection Injection

Spring allows multiple beans of the same type to be injected into a collection such as `List<T>`, `Set<T>`, or an array.

In previous examples, we learned how Spring resolves multiple beans using:

- `@Primary` — defines the default bean.
- `@Qualifier` — selects a specific bean.

However, there are situations where an application does not want to choose only one implementation. Instead, it needs **all available implementations**.

This example demonstrates how Spring automatically discovers multiple beans of the same type and injects them into a collection.

---

## Learning Objectives

By the end of this module, you will be able to:

- Understand how Spring injects multiple beans of the same type.
- Learn how to inject beans using `List<T>`.
- Understand the difference between single bean injection and collection injection.
- Learn how Spring handles multiple implementations of an interface.
- Understand when to use Collection Injection instead of `@Primary` or `@Qualifier`.

---

# The Problem

Imagine an application that supports multiple notification channels.

```text
NotificationService
        ▲
        │
 ┌──────┼──────────────┐
 │      │              │
Email   SMS       Push Notification
```

Each implementation provides the same contract:

```java
public interface NotificationService {

    String send();

}
```

When a component requires a single `NotificationService`:

```java

private final NotificationService notificationService;

```

Spring must choose one bean.

However, if multiple implementations exist:

```text
EmailNotificationService
SmsNotificationService
PushNotificationService
```

Spring cannot determine which bean should be injected.

This results in:

```text

NoUniqueBeanDefinitionException

```

---

# The Solution

Instead of selecting one bean, we can ask Spring to inject all matching beans.

```java
@Component
public class NotificationManager {

    private final List<NotificationService> notificationServices;

    public NotificationManager(List<NotificationService> notificationServices) {

        this.notificationServices = notificationServices;
    }
}
```

Spring searches the application context for every bean implementing `NotificationService` and injects them into the collection.

---

# Running the Application

The application context is created using component scanning:

```java
try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

    NotificationManager manager = context.getBean(NotificationManager.class);

    manager.notifyUsers();
}
```

Expected output:

```text
Sending email notification
Sending SMS notification
Sending push notification
```

All registered `NotificationService` beans are available in the collection.

---

# How Collection Injection Works

## Single Bean Injection

When injecting a single bean:

```java

private final NotificationService notificationService;

```

Spring expects exactly one matching bean.

If multiple candidates exist:

```text
EmailNotificationService
SmsNotificationService
PushNotificationService
```

Spring cannot determine which implementation should be used.

---

## Collection Injection

When injecting a collection:

```java

private final List<NotificationService> notificationServices;

```

Spring injects all matching beans:

```text
[
 EmailNotificationService,
 SmsNotificationService,
 PushNotificationService
]
```

The consuming class can work with all implementations without knowing their concrete classes.

---

# Supported Collection Types

Spring supports several collection types.

## List

```java

List<NotificationService> services;

```

The most common choice.

Useful when working with multiple implementations.

---

## Set

```java

Set<NotificationService> services;

```

Useful when uniqueness matters.

---

## Array

```java

NotificationService[] services;

```

Spring also supports array injection.

---

# Collection Injection vs @Qualifier

Both features help solve problems involving multiple beans, but they serve different purposes.

| Collection Injection | `@Qualifier` |
|---|---|
| Injects all matching beans | Injects one specific bean |
| Used when multiple implementations are required | Used when one implementation is required |
| Works with `List`, `Set`, and arrays | Works with a single dependency |

---

# Empty Collections

If no matching beans exist:

```java

List<UnknownService> services;

```

Spring injects an empty collection.

It does not throw:

```text
NoSuchBeanDefinitionException
```

This makes collection injection useful when a group of implementations may or may not exist.

---

# Best Practices

- Use Collection Injection when all implementations should be available.
- Use `@Qualifier` when one specific implementation is required.
- Use `@Primary` when one implementation should be the default choice.
- Prefer interfaces when designing multiple implementations.
- Keep the consuming class independent from concrete implementations.

---

# Key Takeaways

- Spring can inject multiple beans of the same type.
- Collection Injection allows working with all implementations.
- `List<T>` is the most common collection injection approach.
- `@Primary` chooses one default bean.
- `@Qualifier` chooses one specific bean.
- Collection Injection is useful for strategies, plugins, processors, and validators.

---

# What's Next?

The next example explores **`@Order`**, where we will learn how Spring controls bean priority when multiple implementations are injected into collections.