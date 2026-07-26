# Choosing an Injection Strategy

## Goal

Learn how to choose the most appropriate dependency injection strategy in Spring based on your application's requirements and understand the trade-offs between constructor, setter, and field injection.

By the end of this example, you will understand:

- When to use constructor injection
- When setter injection is appropriate
- Why field injection is generally discouraged
- Spring's recommended approach
- Best practices for dependency injection

---

## Prerequisites

Before continuing, complete:

- [Hello Bean](../hello-bean)
- [Dependency Injection](../dependency-injection)
- [Constructor Injection](../constructor-injection)
- [Setter Injection](../setter-injection)
- [Field Injection](../field-injection)

---

## Why Are There Multiple Injection Strategies?

Spring provides multiple ways to inject dependencies because different situations have different requirements.

- **Constructor Injection** is ideal for required dependencies.
- **Setter Injection** is useful for optional dependencies.
- **Field Injection** offers concise code but hides dependencies and makes testing more difficult.

Modern Spring applications generally prefer **constructor injection** because it produces more maintainable and testable code.

---

## Example

This example uses **constructor injection**, the recommended approach for most Spring applications.

`EmailService` is responsible for sending email notifications.

```java
public class EmailService {

    public void send(String message) {
        System.out.println("Sending email: " + message);
    }

}
```

`NotificationService` depends on `EmailService`.

```java
public class NotificationService {

    private final EmailService emailService;

    public NotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void notify(String message) {
        emailService.send(message);
    }

}
```

Notice that:

- The dependency is required.
- The field is immutable (`final`).
- The dependency is clearly visible in the constructor.

---

## Java Configuration

```java
@Configuration
public class AppConfig {

    @Bean
    public EmailService emailService() {
        return new EmailService();
    }

    @Bean
    public NotificationService notificationService(EmailService emailService) {
        return new NotificationService(emailService);
    }

}
```

Spring resolves the dependency automatically and passes it to the constructor when creating the bean.

---

## Running the Example

Run the `Main` class.

Expected output:

```text
Sending email: Choosing injection strategy!
```

---

# Comparing Injection Strategies

## Constructor Injection

### Advantages

- Required dependencies are explicit.
- Supports immutable (`final`) fields.
- Easy to unit test.
- Prevents partially initialized objects.
- Recommended by the Spring team.

### Best for

- Required dependencies
- Service classes
- Production applications

---

## Setter Injection

### Advantages

- Supports optional dependencies.
- Allows dependencies to be changed after object creation.
- Flexible configuration.

### Best for

- Optional dependencies
- Configurable components

---

## Field Injection

### Advantages

- Less boilerplate.
- Very concise.

### Disadvantages

- Hidden dependencies.
- Harder to unit test.
- Uses reflection.
- Does not naturally support immutable dependencies.

### Best for

- Legacy applications.
- Small demonstration projects.

---

## Comparison Table

| Feature | Constructor | Setter | Field |
|----------|------------|--------|-------|
| Required dependencies | ✅ | ❌ | ❌ |
| Optional dependencies | ⚠️ | ✅ | ⚠️ |
| Immutable fields | ✅ | ❌ | ❌ |
| Easy unit testing | ✅ | ✅ | ❌ |
| Explicit dependencies | ✅ | ⚠️ | ❌ |
| Uses reflection | ❌ | ❌ | ✅ |
| Recommended for new applications | ✅ | Sometimes | Rarely |

---

## Spring's Recommendation

Modern Spring applications generally favor **constructor injection**.

It makes dependencies explicit, encourages immutable objects, simplifies unit testing, and ensures that required dependencies are available when an object is created.

Setter injection remains useful for optional dependencies, while field injection is most commonly encountered in existing codebases.

---

## Migrating from Field Injection

Field injection:

```java
@Autowired
private EmailService emailService;
```

Constructor injection:

```java
private final EmailService emailService;

public NotificationService(EmailService emailService) {
    this.emailService = emailService;
}
```

This simple refactoring makes dependencies explicit and improves testability.

---

## Common Misconceptions

### Constructor injection creates too much boilerplate

Although it requires a constructor, the benefits of explicit dependencies and easier testing generally outweigh the additional code.

---

### Field injection is faster

There is no meaningful performance advantage. The choice should be based on maintainability rather than speed.

---

### Setter injection is outdated

Setter injection is still valuable when a dependency is optional or needs to be changed after object creation.

---

## Best Practices

- Prefer constructor injection for required dependencies.
- Use `final` fields whenever possible.
- Use setter injection for optional dependencies.
- Avoid field injection in new production code.
- Keep dependencies explicit.

---

## Key Takeaways

- Constructor injection is the recommended approach for most Spring applications.
- Setter injection is appropriate for optional dependencies.
- Field injection is convenient but has several design drawbacks.
- Choose the injection strategy that best communicates the intent of your class.
- Good dependency injection improves maintainability, readability, and testability.

---

## What's Next?

The next example explores **Bean Scopes**, where you'll learn how Spring manages the lifecycle and visibility of beans within the `ApplicationContext`.

Continue with:

- Bean Scopes
- Singleton vs Prototype
- Bean Lifecycle
- Lazy Initialization