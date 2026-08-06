# @Qualifier

The `@Qualifier` annotation tells Spring exactly which bean should be injected when multiple beans of the same type are available.

In the previous example, `@Primary` resolved dependency ambiguity by selecting a default bean. While this works well when one implementation is usually preferred, there are situations where a component requires a specific implementation instead.

This example demonstrates how `@Qualifier` allows you to explicitly select the bean to inject.

---

## Learning Objectives

By the end of this module, you will be able to:

- Understand why multiple beans of the same type create ambiguity.
- Learn how `@Qualifier` selects a specific bean.
- Understand the relationship between `@Qualifier` and `@Primary`.
- Learn how Spring resolves beans by name.
- Recognize when `@Qualifier` is the appropriate solution.

---

## The Problem

Suppose an application contains multiple implementations of the same interface.

```text
NotificationService
        ▲
        │
 ┌──────┴────────┐
 │               │
Email        SMS
```

Another component depends on `NotificationService`.

```java
@Component
public class NotificationManager {

    private final NotificationService notificationService;

    public NotificationManager(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

}
```

Since Spring finds two matching beans, it cannot determine which implementation should be injected.

Application startup fails with a `NoUniqueBeanDefinitionException`.

One solution is to designate a default implementation using `@Primary`.

However, what if this particular component should always use the SMS implementation instead of the default?

---

## The Solution

`@Qualifier` allows you to explicitly identify which bean should be injected.

```java
public NotificationManager(@Qualifier("smsNotificationService") NotificationService notificationService) {

    this.notificationService = notificationService;
}
```

Instead of relying on Spring's default selection, the dependency is resolved using the specified bean name.

---

## Running the Application

The application context is created using component scanning.

```java
try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

    NotificationManager manager = context.getBean(NotificationManager.class);

    manager.notifyUser();
}
```

Expected output:

```text
Sending SMS notification
```

Although multiple implementations exist, Spring injects the bean identified by `@Qualifier`.

---

## How Spring Resolves Dependencies

```text
NotificationService
        ▲
        │
 ┌──────┴────────┐
 │               │
Email          SMS
                 ▲
                 │
@Qualifier("smsNotificationService")
                 │
                 ▼
      NotificationManager
```

Spring matches the qualifier value with the bean name and injects the corresponding implementation.

---

## Looking Up Beans

The value supplied to `@Qualifier` corresponds to the Spring bean name.

```java

NotificationService service = context.getBean("smsNotificationService", NotificationService.class);

```

This retrieves the same bean referenced by the qualifier.

Both implementations remain registered in the application context.

```java

Map<String, NotificationService> services = context.getBeansOfType(NotificationService.class);

```

`@Qualifier` affects dependency resolution, not bean registration.

---

## @Primary vs @Qualifier

| `@Primary` | `@Qualifier` |
|------------|--------------|
| Defines the default bean | Selects a specific bean |
| Applied to the bean definition | Applied at the injection point |
| Used when one implementation is usually preferred | Used when a component requires a particular implementation |
| Can be overridden | Overrides the default selection |

These annotations complement each other and are frequently used together in Spring applications.

---

## Best Practices

- Use `@Primary` when one implementation should be the default.
- Use `@Qualifier` when a component requires a specific implementation.
- Choose descriptive bean names to make qualifiers easy to understand.
- Avoid unnecessary qualifiers when only one bean of a given type exists.

---

## In Practice

Applications often provide multiple implementations of the same service.

Examples include:

- Email, SMS, and Push notification services
- Multiple payment gateways
- Different storage providers
- Multiple cache implementations

`@Qualifier` allows each component to receive the implementation it specifically requires.

---

## Key Takeaways

- Multiple beans of the same type create dependency ambiguity.
- `@Qualifier` explicitly selects the bean to inject.
- Qualifier values correspond to Spring bean names.
- `@Qualifier` overrides the default bean selection.
- `@Primary` and `@Qualifier` work together to provide flexible dependency injection.

---

## What's Next?

The next example explores **Collection Injection**, where Spring injects all beans of the same type into a collection, allowing applications to work with multiple implementations simultaneously.