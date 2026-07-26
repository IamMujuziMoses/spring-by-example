# Bean Scopes Overview

## Goal

Learn what bean scopes are, why they exist, and how Spring uses them to manage the lifecycle and number of bean instances within the IoC container.

By the end of this example, you will understand:

- What a bean scope is
- Why bean scopes are important
- The bean scopes provided by Spring
- The default bean scope
- When different scopes are useful

---

## Prerequisites

Before continuing, complete:

- Hello Bean
- Dependency Injection
- Constructor Injection
- Setter Injection
- Field Injection
- Choosing an Injection Strategy

---

## What Is a Bean Scope?

A **bean scope** defines **how many instances** of a bean Spring creates and **how long those instances live** within the Spring IoC container.

Every bean managed by Spring has a scope.

The scope determines whether Spring should:

- Create a single shared instance
- Create a new instance every time one is requested
- Create one instance per HTTP request
- Create one instance per user session
- Or use another lifecycle depending on the application type

---

## Why Do Bean Scopes Matter?

Imagine an application with an `EmailService`.

Should Spring create:

- One `EmailService` for the entire application?

or

- A brand new `EmailService` every time another bean needs one?

The correct answer depends on how the bean is used.

Bean scopes allow Spring to manage these different scenarios automatically.

---

## Spring Bean Scopes

Spring provides several built-in bean scopes.

| Scope | Description |
|--------|-------------|
| Singleton | One shared bean instance per Spring container |
| Prototype | A new bean instance each time it is requested |
| Request | One bean instance per HTTP request |
| Session | One bean instance per HTTP session |
| Application | One bean instance per web application |

This module introduces these scopes at a high level. The following examples explore each one in more detail.

---

## The Default Scope

Unless you specify otherwise, Spring creates beans using the **Singleton** scope.

For example:

```java
@Bean
public EmailService emailService() {
    return new EmailService();
}
```

Even though no scope is specified, Spring treats the bean as if it were configured like this:

```java
@Bean
@Scope("singleton")
public EmailService emailService() {
    return new EmailService();
}
```

Since Singleton is the default scope, the `@Scope` annotation is often omitted.

---

## How Bean Scopes Work

Consider the following bean.

```java
@Bean
public NotificationService notificationService(EmailService emailService) {
    return new NotificationService(emailService);
}
```

Spring first determines the bean's scope.

Based on that scope, Spring decides:

- Whether to reuse an existing bean instance
- Whether to create a new instance
- How long the bean should remain alive inside the container

---

## Visualizing Bean Scopes

### Singleton

```text
ApplicationContext
        ▼
 EmailService
        ▲
Every call to getBean()
returns the same object
```

---

### Prototype

```text
ApplicationContext

getBean()
   ▼
EmailService #1

getBean()
   ▼
EmailService #2

getBean()
   ▼
EmailService #3
```

Unlike Singleton, each request receives a new object.

---

## When Should You Use Different Scopes?

Choosing the correct scope depends on how the bean is used.

For example:

- Service classes are typically Singleton.
- Stateful objects often benefit from Prototype scope.
- Web applications commonly use Request and Session scopes.

As you continue through this section, you'll learn when each scope is appropriate.

---

## Common Misconceptions

### Bean scope is the same as a Java object's lifetime

Not exactly.

A bean's scope determines how Spring manages the object, not how Java's garbage collector works.

---

### Every bean should be Prototype

Most Spring applications primarily use Singleton beans.

Prototype beans are intended for specific situations where a new object is required each time.

---

### Singleton means one object for the entire JVM

A Singleton bean is shared **within a single Spring IoC container**, not necessarily across an entire JVM.

Multiple `ApplicationContext` instances each maintain their own Singleton beans.

---

## Best Practices

- Understand the default Singleton scope.
- Choose bean scopes based on the responsibilities of the bean.
- Avoid changing scopes without a clear reason.
- Keep service beans stateless whenever possible.
- Learn each scope individually before using it in production applications.

---

## Key Takeaways

- Every Spring bean has a scope.
- Bean scopes determine how many bean instances Spring creates.
- Bean scopes also determine how long those instances live.
- Singleton is the default scope.
- Spring provides additional scopes for different application scenarios.

---

## What's Next?

The next example explores **Singleton Scope**, the default bean scope used by the Spring IoC container.

You'll learn:

- How Singleton beans are created
- How Spring reuses bean instances
- Why Singleton is the default scope
- When Singleton is the right choice