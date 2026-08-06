# @Primary
The `@Primary` annotation tells Spring which bean should be preferred when multiple beans of the same type are available for dependency injection.
Without a primary bean, Spring cannot determine which implementation should be injected and the application fails to start.
This example demonstrates how `@Primary` resolves bean ambiguity by defining a default candidate.
---
## Learning Objectives
By the end of this module, you will be able to:
- Understand why multiple beans of the same type create ambiguity.
- Learn how Spring selects beans during dependency injection.
- Use `@Primary` to define a default bean.
- Understand the relationship between `@Primary` and `@Qualifier`.
- Recognize when `@Primary` is the appropriate solution.
---
## The Problem
Consider an application with two implementations of the same interface.
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
When Spring creates `NotificationManager`, it discovers two beans that satisfy the dependency.
Since both beans are valid candidates, Spring cannot determine which implementation should be injected.
Application startup fails with a `NoUniqueBeanDefinitionException`.
A typical error looks similar to:
```text
Parameter 0 of constructor in NotificationManager required a single bean, but 2 were found:
- emailNotificationService
- smsNotificationService
```
---
## The Solution
Mark one implementation as the default using `@Primary`.
```java
@Primary
@Component
public class EmailNotificationService implements NotificationService {
    
    @Override
    public String send() {
        return "Sending email notification";
    }
    
}
```

Now, whenever Spring needs a `NotificationService` and no `@Qualifier` is specified, it automatically injects the primary bean.

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
Sending email notification
```

Although two `NotificationService` beans exist, Spring injects the one marked with `@Primary`.

---
## How Spring Resolves Dependencies

```text
NotificationService
        ▲
        │
 ┌──────┴────────┐
 │               │
Email (@Primary)  SMS
        │
        ▼
NotificationManager
```

During dependency injection, Spring detects multiple candidates and selects the bean marked with `@Primary`.

---
## Looking Up Beans
`@Primary` affects type-based dependency resolution.
```java

NotificationService service = context.getBean(NotificationService.class);

```
Spring returns the primary bean.
However, all implementations remain registered in the application context.

```java

Map<String, NotificationService> services = context.getBeansOfType(NotificationService.class);

```
Both `EmailNotificationService` and `SmsNotificationService` are present.
`@Primary` does **not** remove or replace other beans—it simply defines the default candidate.
---
## @Primary vs @Qualifier
| `@Primary` | `@Qualifier` |
|------------|--------------|
| Defines the default bean | Selects a specific bean |
| Applied to the bean definition | Applied at the injection point |
| Used when one implementation is usually preferred | Used when a specific implementation is required |

These annotations are complementary and are often used together in larger applications.

---

## Best Practices
- Use `@Primary` when one implementation is the sensible default.
- Use `@Qualifier` when different injection points require different implementations.
- Avoid having multiple `@Primary` beans of the same type.
- Keep bean responsibilities clear and focused.


---

## In Practice
Applications commonly provide multiple implementations of the same interface.
Examples include:
- Email and SMS notification services
- Different payment providers
- Multiple cache implementations
- Alternative storage providers
  `@Primary` allows one implementation to act as the default while preserving access to the others.


---

## Key Takeaways
- Multiple beans of the same type create dependency ambiguity.
- Spring throws a `NoUniqueBeanDefinitionException` when no default can be determined.
- `@Primary` designates the default bean for dependency injection.
- Other beans remain available in the application context.
- `@Qualifier` can override the default selection when necessary.


---

## What's Next?
The next example explores **`@Qualifier`**, which allows you to explicitly select a specific bean when multiple implementations are available.
