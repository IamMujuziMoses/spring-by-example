# Optional Dependencies

Not every dependency is required for an application to function.

Sometimes a component can provide additional functionality when another bean is available, but should continue working when that bean is not registered.

Spring supports optional dependencies using several approaches.

This example focuses on using `Optional<T>` with constructor injection to represent a dependency that may or may not be available.

---

## Learning Objectives

By the end of this example, you will be able to:

- Understand what an optional dependency is.
- Use `Optional<T>` with constructor injection.
- Understand how Spring handles an available optional dependency.
- Understand how Spring handles a missing optional dependency.
- Distinguish optional dependencies from required dependencies.
- Compare `Optional<T>` with `ObjectProvider`.
- Understand when optional dependencies are useful.

---

# What Is an Optional Dependency?

A required dependency is one that a bean cannot operate without.

For example:

```java
public PaymentService(PaymentRepository repository) {
    this.repository = repository;
}
```

If `PaymentRepository` is not available, Spring cannot create `PaymentService`.

An optional dependency is different.

The application can operate without the dependency, but can provide additional functionality when it is available.

For example:

```text
PaymentService
      |
      └── PaymentNotificationService
                |
                ├── Available → send notification
                |
                └── Missing   → continue without notification
```

---

# The Problem

Consider a payment service.

After processing a payment, the application may want to send a notification.

However, notification support should not be required for the payment service to function.

Without optional dependency support:

```text
PaymentService
      |
      └── PaymentNotificationService
                    |
                    └── Required
```

If `PaymentNotificationService` is not registered, Spring cannot create `PaymentService`.

This creates an unnecessary requirement.

---

# The Solution

Java's `Optional<T>` can be used to represent an optional dependency.

```java
public PaymentService(Optional<PaymentNotificationService> notificationService) {

    this.notificationService = notificationService;
}
```

Spring provides:

```text
Bean exists
    ↓
Optional.of(bean)
```

or:

```text
Bean does not exist
    ↓
Optional.empty()
```

The application can then decide what to do.

---

# Example

## PaymentNotificationService

```java
public interface PaymentNotificationService {

    void notifyPayment();

}
```

---

## EmailPaymentNotificationService

```java
@Component
public class EmailPaymentNotificationService implements PaymentNotificationService {

    @Override
    public void notifyPayment() {
        System.out.println("Sending payment notification.");
    }
}
```

---

## PaymentService

```java
@Service
public class PaymentService {

    private final Optional<PaymentNotificationService> notificationService;

    public PaymentService(Optional<PaymentNotificationService> notificationService) {

        this.notificationService = notificationService;
    }

    public void processPayment() {

        System.out.println("Payment processed successfully.");

        notificationService.ifPresent(PaymentNotificationService::notifyPayment);
    }

    public Optional<PaymentNotificationService> getNotificationService() {
        return notificationService;
    }
}
```

The important part is:

```java

Optional<PaymentNotificationService>

```

The dependency is explicitly represented as optional.

---

# When the Dependency Exists

Because `EmailPaymentNotificationService` is registered as a Spring bean:

```java
@Component
public class EmailPaymentNotificationService implements PaymentNotificationService {
}
```

Spring injects:

```text
Optional<PaymentNotificationService>
        |
        └── EmailPaymentNotificationService
```

Running:

```java

paymentService.processPayment();

```

produces:

```text
Payment processed successfully.
Sending payment notification.
```

---

# When the Dependency Does Not Exist

The payment service can also be created without a notification bean.

Spring provides:

```text
Optional.empty()
```

The application can safely check the dependency:

```java

notificationService.ifPresent(PaymentNotificationService::notifyPayment);

```

If the dependency is absent, nothing happens.

The payment can still be processed:

```text
Payment processed successfully.
```

---

# Testing the Missing Dependency

A separate configuration can deliberately omit the notification bean:

```java
@Configuration
public class PaymentServiceOnlyConfig {

    @Bean
    PaymentService paymentService(Optional<PaymentNotificationService> notificationService) {

        return new PaymentService(notificationService);
    }
}
```

The test can then verify:

```java
@Test
void shouldInjectEmptyOptionalWhenBeanIsMissing() {

    try (var context = new AnnotationConfigApplicationContext(PaymentServiceOnlyConfig.class)) {

        PaymentService paymentService = context.getBean(PaymentService.class);

        assertTrue(paymentService.getNotificationService().isEmpty());
    }
}
```

This demonstrates that Spring can create the `PaymentService` even though the optional dependency is not registered.

---

# Optional Dependencies vs Required Dependencies

## Required Dependency

```java
public PaymentService(PaymentRepository repository) {

    this.repository = repository;
}
```

If the repository is missing:

```text
ApplicationContext
        ↓
Cannot resolve dependency
        ↓
Bean creation fails
```

---

## Optional Dependency

```java
public PaymentService(Optional<PaymentNotificationService> notificationService) {

    this.notificationService = notificationService;
}
```

If the notification service is missing:

```text
ApplicationContext
        ↓
Optional.empty()
        ↓
PaymentService is created successfully
```

---

# Optional Dependencies vs ObjectProvider

Both can be used when a dependency may not exist, but they serve different purposes.

| `Optional<T>` | `ObjectProvider<T>` |
|---|---|
| Represents an optional dependency | Provides programmatic access to the container |
| Usually used with constructor injection | Supports lazy bean retrieval |
| Dependency is represented as a value | Dependency is represented as a provider |
| `Optional.empty()` when missing | `getIfAvailable()` can return `null` |
| Good for required-or-optional dependency semantics | Good for lazy or dynamic access |

Example:

```java
Optional<PaymentNotificationService>
```

means:

> This dependency may or may not be available.

Whereas:

```java
ObjectProvider<PaymentNotificationService>
```

means:

> Give me a way to retrieve this dependency when I need it.

---

# Other Ways to Define Optional Dependencies

Spring also supports other approaches.

## `@Autowired(required = false)`

```java
@Autowired(required = false)
private PaymentNotificationService notificationService;
```

This tells Spring that the dependency is not required.

However, constructor injection with `Optional<T>` makes the dependency explicit in the class's API and avoids field injection.

---

# When to Use Optional Dependencies

Optional dependencies are useful when a feature is genuinely optional.

Examples include:

### Notifications

```text
PaymentService
      |
      └── Optional<NotificationService>
```

### Monitoring

An application can continue running when an optional monitoring component is not installed.

### Integrations

An application may optionally integrate with:

- External payment providers
- Analytics services
- Notification providers
- External messaging systems

### Feature Extensions

A core component can operate independently while taking advantage of additional functionality when available.

---

# Best Practices

- Prefer constructor injection for optional dependencies.
- Use `Optional<T>` when the dependency is conceptually optional.
- Avoid using `Optional` merely to hide a dependency that should actually be required.
- Use `ObjectProvider` when lazy or programmatic bean retrieval is needed.
- Keep the optional behavior explicit and easy to understand.

---

# Key Takeaways

- An optional dependency is a dependency that may not be available.
- `Optional<T>` allows Spring to represent an absent dependency without failing bean creation.
- When the bean exists, Spring provides `Optional.of(bean)`.
- When the bean does not exist, Spring provides `Optional.empty()`.
- Constructor injection makes optional dependencies explicit.
- `Optional<T>` and `ObjectProvider<T>` solve related but different problems.

---

# What's Next?

The next example explores **Circular Dependencies** and demonstrates what happens when two Spring beans depend on each other.