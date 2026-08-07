# Map Injection

Spring allows multiple beans of the same type to be injected into a `Map`.

In the previous examples, we learned how Spring handles multiple implementations using:

- `@Primary` — selects a default bean.
- `@Qualifier` — selects a specific bean.
- Collection Injection — injects all matching beans.
- `@Order` — controls the order of injected beans.

However, sometimes an application needs access to multiple implementations while also being able to identify them by name.

This example demonstrates how Spring injects multiple beans into a `Map<String, BeanType>` using bean names as keys.

---

## Learning Objectives

By the end of this module, you will be able to:

- Understand how Spring injects beans into a `Map`.
- Learn how bean names are used as map keys.
- Understand the difference between `List` injection and `Map` injection.
- Learn when Map Injection is useful.
- Build a simple strategy selector using injected beans.

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

	String getName();

	void send(String message);

}
```

With Collection Injection, Spring can provide all implementations:

```java

List<NotificationService> services;

```

Spring creates:

```text
[
 EmailNotificationService,
 SmsNotificationService,
 PushNotificationService
]
```

However, the application still needs a way to identify and select a specific implementation dynamically.

For example:

```text
"email"  → EmailNotificationService
"sms"    → SmsNotificationService
"push"   → PushNotificationService
```

---

# The Solution

Spring can inject beans into a `Map`.

```java

private final Map<String, NotificationService> notificationServices;

```

Spring uses the bean names as keys.

Example:

```text
{
 "emailNotificationService" → EmailNotificationService,
 "smsNotificationService"   → SmsNotificationService,
 "pushNotificationService"  → PushNotificationService
}
```

---

# How Map Injection Works

Given these components:

```java
@Component
public class EmailNotificationService implements NotificationService {

}
```

```java
@Component
public class SmsNotificationService implements NotificationService {

}
```

```java
@Component
public class PushNotificationService implements NotificationService {

}
```

Spring automatically creates:

```java

Map<String, NotificationService>

```

with:

```text
Key                         Value

emailNotificationService    EmailNotificationService
smsNotificationService      SmsNotificationService
pushNotificationService     PushNotificationService
```

---

# NotificationManager

The consuming class receives all notification services:

```java
@Component
public class NotificationManager {

	private final Map<String, NotificationService> notificationServices;

	public NotificationManager(Map<String, NotificationService> notificationServices) {

		this.notificationServices = notificationServices;
	}
}
```

The application can now select a service dynamically:

```java

NotificationService service = notificationServices.get("emailNotificationService");

```

---

# Running the Example

```java
try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

	NotificationManager manager = context.getBean(NotificationManager.class);

	manager.notify("emailNotificationService","Hello Spring");
}
```

Output:

```text
Sending email: Hello Spring
```

---

# Collection Injection vs Map Injection

Both approaches inject multiple beans, but they solve different problems.

| Collection Injection | Map Injection |
|---|---|
| Injects all beans | Injects all beans |
| Access by position | Access by bean name |
| Uses `List<T>` or `Set<T>` | Uses `Map<String,T>` |
| Useful when processing all beans | Useful when selecting dynamically |

---

## Collection Injection

```java

List<NotificationService> services;

```

Example:

```text
[
 EmailNotificationService,
 SmsNotificationService,
 PushNotificationService
]
```

---

## Map Injection

```java

Map<String, NotificationService> services;

```

Example:

```text
{
 emailNotificationService → EmailNotificationService,
 smsNotificationService   → SmsNotificationService,
 pushNotificationService  → PushNotificationService
}
```

---

# Real World Examples

## Payment Processing

```java

Map<String, PaymentProcessor> processors;

```

Example:

```text
{
 "stripe" → StripeProcessor,
 "paypal" → PaypalProcessor
}
```

The application can select the processor based on user choice.

---

## File Exporters

```java

Map<String, Exporter> exporters;

```

Example:

```text
{
 "pdf"  → PdfExporter,
 "csv"  → CsvExporter,
 "json" → JsonExporter
}
```

---

## Plugin Systems

```java

Map<String, Plugin> plugins;

```

Plugins can be discovered and selected dynamically.

---

# Custom Bean Names

By default, Spring uses the class name converted to camel case.

Example:

```java
@Component
public class EmailNotificationService {

}
```

Bean name:

```text
emailNotificationService
```

A custom name can be provided:

```java
@Component("email")
public class EmailNotificationService {

}
```

The map becomes:

```text
{
 "email" → EmailNotificationService
}
```

---

# Best Practices

- Use Map Injection when beans need to be selected dynamically.
- Use Collection Injection when all beans should be processed.
- Use `@Qualifier` when a dependency is fixed.
- Use `@Primary` when one implementation is the default.
- Prefer meaningful bean names when using maps.

---

# Key Takeaways

- Spring can inject multiple beans into a `Map`.
- Bean names become map keys.
- Map Injection allows runtime selection of implementations.
- Collection Injection provides all beans without names.
- `@Qualifier` and `@Primary` solve different problems.
- Map Injection is useful for strategies, plugins, and processors.

---

# What's Next?

The next example explores **ObjectProvider**, which allows lazy and optional access to beans from the Spring container.