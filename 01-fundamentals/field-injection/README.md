# Field Injection

## Goal

Learn how Spring injects dependencies directly into class fields using the `@Autowired` annotation and understand why field injection is generally discouraged in modern Spring applications.

By the end of this example, you will understand:

- What field injection is
- How Spring performs field injection
- The advantages and disadvantages of field injection
- Why constructor injection is the preferred approach
- When you might still encounter field injection

---

## Prerequisites

Before continuing, complete:

- [Hello Bean](../hello-bean)
- [Dependency Injection](../dependency-injection)
- [Constructor Injection](../constructor-injection)
- [Setter Injection](../setter-injection)

---

## The Problem

Sometimes developers want the simplest way to inject dependencies.

Instead of writing constructors or setter methods, Spring allows dependencies to be injected directly into class fields.

This approach reduces boilerplate code but comes with important trade-offs.

---

## What is Field Injection?

Field injection is a dependency injection technique where Spring injects dependencies directly into a class field.

The field is annotated with `@Autowired`, allowing Spring to automatically locate and inject the appropriate bean.

```text
ApplicationContext
        |
        +----------------+
        |                |
 EmailService    NotificationService
                         |
               @Autowired field
```

---

## Example

`EmailService` sends email notifications.

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

    @Autowired
    private EmailService emailService;

    public void notify(String message) {
        emailService.send(message);
    }

}
```

Notice that there is no constructor or setter method.

Spring injects the dependency directly into the private field.

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
    public NotificationService notificationService() {
        return new NotificationService();
    }

}
```

Unlike constructor and setter injection, the configuration does not explicitly connect the beans.

Spring performs the injection automatically.

---

## Running the Example

Run the `Main` class.

Expected output:

```text
Sending email: Welcome to Spring!
```

---

## Advantages of Field Injection

### Less boilerplate

No constructors or setter methods are required.

---

### Concise code

Classes remain small and easy to read in simple examples.

---

### Common in older applications

Many existing Spring applications use field injection, so understanding it is useful when maintaining legacy code.

---

## Disadvantages of Field Injection

### Hidden dependencies

Dependencies are not visible through the constructor.

Readers must inspect the class fields to understand what the class requires.

---

### Harder unit testing

Without Spring, dependencies remain `null`.

Testing often requires reflection or a Spring container.

---

### No immutable dependencies

Since Spring injects dependencies after object creation, fields cannot easily be declared `final`.

---

### Uses reflection

Spring injects the dependency directly into the field using reflection.

This hides part of the object's initialization process.

---

## Constructor vs Setter vs Field Injection

| Feature | Constructor | Setter | Field |
|----------|------------|--------|-------|
| Required dependencies | ✅ | ❌ | ❌ |
| Immutable dependencies | ✅ | ❌ | ❌ |
| Easy unit testing | ✅ | ✅ | ❌ |
| Explicit dependencies | ✅ | ⚠️ | ❌ |
| Uses reflection | ❌ | ❌ | ✅ |
| Recommended by Spring | ✅ | Optional dependencies | Rarely |

---

## Behind the Scenes

When the `ApplicationContext` starts, Spring:

```text
Create EmailService
        |
Create NotificationService
        |
Inspect fields
        |
Find @Autowired
        |
Locate EmailService bean
        |
Inject dependency using reflection
        |
Store NotificationService in the ApplicationContext
```

Unlike constructor injection, Spring does not call a constructor with dependencies.

Instead, it sets the field directly after the object has been created.

---

## Common Mistakes

### Forgetting `@Autowired`

Without the annotation, Spring does not inject the dependency.

---

### Instantiating the class manually

```java
NotificationService service = new NotificationService();
```

Since Spring is not managing the object, the dependency remains `null`.

---

### Overusing field injection

Although convenient, constructor injection is generally preferred because it makes dependencies explicit and improves testability.

---

## Try It Yourself

As an exercise:

1. Add an `SmsService`.
2. Inject it using field injection.
3. Send both an email and an SMS notification.
4. Compare this implementation with the constructor injection example.

---

## Key Takeaways

- Field injection injects dependencies directly into class fields.
- Spring performs field injection using reflection.
- It reduces boilerplate but hides dependencies.
- Constructor injection is generally preferred in modern Spring applications.
- Understanding field injection is important because it is common in older Spring codebases.

---

## What's Next?

The next example compares constructor, setter, and field injection, helping you choose the most appropriate dependency injection strategy for your applications.

Continue with:

- Choosing an Injection Strategy
- Bean Scopes
- Singleton vs Prototype
- Bean Lifecycle