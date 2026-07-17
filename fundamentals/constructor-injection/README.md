# Constructor Injection

## Goal

Learn how Spring uses **constructor injection** to provide dependencies to objects and why it is the recommended approach for dependency injection.

By the end of this example, you will understand:

- What constructor injection is
- Why constructor injection is preferred
- How Spring resolves constructor dependencies
- How constructor injection improves testability and maintainability

---

## Prerequisites

Before continuing, complete:

- [Hello Bean](../hello-bean)
- [Dependency Injection](../dependency-injection)

The previous examples introduced:

- Spring Beans
- `ApplicationContext`
- `@Configuration`
- `@Bean`
- Dependency Injection concepts

---

## The Problem

A common approach when creating objects is to let a class create its own dependencies.

Example:

```java
public class NotificationService {

    private final EmailService emailService = new EmailService();

    public void notify(String message) {
        emailService.send(message);
    }
}
```

Although this works, it creates a strong dependency between `NotificationService` and `EmailService`.

This makes the code harder to:

- Test
- Maintain
- Extend
- Replace dependencies

---

## What is Constructor Injection?

Constructor injection is a technique where an object's required dependencies are provided through its constructor.

Instead of creating the dependency:

```text
NotificationService
        |
new EmailService()
```

The dependency is provided externally:

```text
ApplicationContext
        |
        +----------------+
        |                |
        v                v
 EmailService   NotificationService
                         ^
                         |
              Injected through constructor
```

Spring creates the required objects and passes dependencies into the constructor.

---

## Constructor Injection Example

`EmailService` represents a service responsible for sending emails.

```java
public class EmailService {

    public void send(String message) {
        System.out.println("Sending email: " + message);
    }
}
```

`NotificationService` requires an `EmailService`.

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

The dependency is:

- Explicit
- Required
- Immutable

because the field can be declared as `final`.

---

## Java Configuration

Spring creates and connects the beans using `@Configuration` and `@Bean`.

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

When Spring creates `NotificationService`, it automatically finds the `EmailService` bean and passes it into the constructor.

---

## Running the Example

Run the `Main` class.

Expected output:

```text
Sending email: Welcome to Spring!
```

---

## Why Constructor Injection is Preferred

Constructor injection has several advantages:

### 1. Dependencies are explicit

The constructor clearly shows what the object requires.

```java
public NotificationService(EmailService emailService)
```

Anyone reading the class immediately understands its dependencies.

---

### 2. Supports immutable objects

Dependencies can be declared as `final`.

```java
private final EmailService emailService;
```

Once initialized, the dependency cannot be changed.

---

### 3. Easier testing

Dependencies can easily be provided without starting Spring.

Example:

```java
EmailService emailService = new EmailService();

NotificationService service = new NotificationService(emailService);
```

---

### 4. Prevents incomplete objects

A `NotificationService` cannot be created without an `EmailService`.

The required dependency must exist before the object is created.

---

## Behind the Scenes

When the `ApplicationContext` starts, Spring processes the configuration class.

For this bean:

```java
@Bean
public NotificationService notificationService(EmailService emailService) {
    return new NotificationService(emailService);
}
```

Spring performs these steps:

```text
Application starts
        |
Create ApplicationContext
        |
Read AppConfig
        |
Create EmailService bean
        |
Create NotificationService bean
        |
Resolve EmailService dependency
        |
Call constructor
        |
Store NotificationService in context
```

The object returned from:

```java
context.getBean(NotificationService.class);
```

is already fully initialized with its dependencies.

---

## Common Mistakes

### Creating dependencies manually

Avoid:

```java
new EmailService();
```

inside classes managed by Spring.

This bypasses the Spring container.

---

### Too many constructor dependencies

A class requiring many dependencies may indicate that it has too many responsibilities.

Example:

```java
public Service(
    Database database,
    EmailService email,
    Logger logger,
    Cache cache,
    SecurityService security)
```

This may be a sign that the class needs to be redesigned.

---

### Using field injection as the default

Field injection:

```java
@Autowired
private EmailService emailService;
```

works, but constructor injection is usually preferred because dependencies remain visible and testable.

---

## Try It Yourself

1. Create a `SmsService`.
2. Create a `MessageService`.
3. Inject both `EmailService` and `SmsService` using the constructor.
4. Send a message using both channels.

---

## What's Next?

Continue with:

- Setter Injection
- Field Injection
- Bean Scopes
- Singleton vs Prototype