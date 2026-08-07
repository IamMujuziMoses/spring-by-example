# @Order

Spring allows multiple beans of the same type to be injected into a collection.

In the previous example, we learned how **Collection Injection** allows Spring to inject multiple implementations of the same interface.

However, when multiple beans are injected into a collection, we may need to control the order in which those beans appear.

This example demonstrates how the `@Order` annotation controls the ordering of beans when they are injected into collections.

---

## Learning Objectives

By the end of this module, you will be able to:

- Understand the purpose of the `@Order` annotation.
- Learn how Spring sorts beans in collections.
- Understand how `@Order` works with Collection Injection.
- Learn the relationship between `@Order`, `@Primary`, and `@Qualifier`.
- Understand when controlling bean order is useful.

---

# The Problem

Imagine an application with multiple notification channels.

```text
NotificationService
        ▲
        │
 ┌──────┼──────────────┐
 │      │              │
Email   SMS       Push Notification
```

Each notification type implements the same interface:

```java
public interface NotificationService {

	String getName();

	void send(String message);
}
```

Spring can inject all implementations using Collection Injection:

```java

private final List<NotificationService> notificationServices;

```

However, without explicitly defining an order, the order of beans in the collection should not be relied upon.

Example:

```text
[
 EmailNotificationService,
 SmsNotificationService,
 PushNotificationService
]
```

The order may not represent the order your application expects.

---

# The Solution

Spring provides the `@Order` annotation to define the priority of beans.

Example:

```java
@Component
@Order(1)
public class SmsNotificationService implements NotificationService {

}
```

```java
@Component
@Order(2)
public class PushNotificationService implements NotificationService {

}
```

```java
@Component
@Order(3)
public class EmailNotificationService implements NotificationService {

}
```

Spring injects the beans in ascending order:

```text
[
 SmsNotificationService,
 PushNotificationService,
 EmailNotificationService
]
```

The lower the number, the higher the priority.

---

# How @Order Works

`@Order` does not select a bean.

It only controls the order of beans when Spring needs to sort multiple beans.

For example:

```java

List<NotificationService> services;

```

Spring creates:

```text
[
 SmsNotificationService,
 PushNotificationService,
 EmailNotificationService
]
```

based on their `@Order` values.

---

# Injecting Ordered Beans

The consuming class receives all beans:

```java
@Component
public class NotificationManager {

	private final List<NotificationService> notificationServices;

	public NotificationManager(
			List<NotificationService> notificationServices) {

		this.notificationServices = notificationServices;
	}
}
```

Spring applies the `@Order` values automatically.

---

# Running the Example

```java
try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

	NotificationManager manager = context.getBean(NotificationManager.class);

	manager.notifyUsers("Hello Spring");
}
```

Output:

```text
Sending SMS: Hello Spring
Sending Push notification: Hello Spring
Sending Email: Hello Spring
```

---

# @Order vs @Primary vs @Qualifier

These annotations solve different problems.

| Annotation | Purpose |
|---|---|
| `@Primary` | Selects one default bean |
| `@Qualifier` | Selects one specific bean |
| Collection Injection | Injects all matching beans |
| `@Order` | Controls the order of injected beans |

---

## @Primary

Use `@Primary` when Spring needs to choose one bean.

Example:

```java

private final NotificationService service;

```

Spring chooses:

```text
EmailNotificationService (@Primary)
```

---

## @Qualifier

Use `@Qualifier` when you need a specific bean.

Example:

```java

@Qualifier("smsNotificationService")
NotificationService service;

```

Spring injects:

```text
SmsNotificationService
```

---

## @Order

Use `@Order` when you need all beans but in a specific sequence.

Example:

```java

List<NotificationService> services;

```

Spring injects:

```text
[
 SmsNotificationService,
 PushNotificationService,
 EmailNotificationService
]
```

---

# Real World Examples

## Validation Pipeline

Multiple validation rules may need to run in a specific order.

```java

List<Validator> validators;

```

Example:

```text
1. RequiredFieldValidator
2. FormatValidator
3. BusinessRuleValidator
```

---

## Processing Chains

Multiple processors can handle data sequentially.

```java

List<DataProcessor> processors;

```

Example:

```text
1. NormalizeProcessor
2. ValidateProcessor
3. SaveProcessor
```

---

## Event Handlers

Multiple handlers can process events in a defined sequence.

```java

List<EventHandler> handlers;

```

---

# Best Practices

- Use `@Order` only when ordering is part of your application's behavior.
- Prefer explicit ordering over relying on default bean order.
- Use `@Primary` when you need one default bean.
- Use `@Qualifier` when you need one specific bean.
- Use Collection Injection when all implementations are required.

---

# Key Takeaways

- `@Order` controls the ordering of multiple beans.
- `@Order` is commonly used with Collection Injection.
- Lower order values have higher priority.
- `@Order` does not choose a single bean.
- `@Primary`, `@Qualifier`, Collection Injection, and `@Order` solve different dependency injection problems.

---

# What's Next?

The next example explores **Map Injection**, where Spring injects multiple beans into a `Map<String, BeanType>` using bean names as keys.