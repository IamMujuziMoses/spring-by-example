# Bean Aliases

A Spring bean can have more than one name.

Bean aliases allow multiple names to refer to the same bean instance. This can be useful when maintaining backwards compatibility with an existing bean name or when providing alternative names for a bean.

This example demonstrates how to define multiple names for a bean and retrieve the same bean using each name.

---

## Learning Objectives

By the end of this example, you will be able to:

- Understand what a Spring bean alias is.
- Define multiple names for a Spring bean.
- Retrieve a bean using different names.
- Understand that aliases refer to the same bean instance.
- Understand a practical use case for bean aliases.

---

# What Is a Bean Alias?

A bean alias is an additional name that can be used to refer to an existing Spring bean.

For example:

```text
notificationService
        │
        ├──────────────┐
        │              │
        ↓              ↓
NotificationService
        ↑              ↑
        │              │
emailNotificationService
```

There is only **one bean instance**, but it has multiple names.

---

# Defining Bean Aliases

Spring allows multiple bean names to be specified using the `name` attribute of `@Bean`.

```java
@Configuration
public class AppConfig {

    @Bean(name = {"notificationService", "emailNotificationService"})
    public NotificationService notificationService() {
        return new NotificationService();
    }
}
```

This registers the bean using:

```text
notificationService
emailNotificationService
```

Both names refer to the same `NotificationService` bean.

---

# Example

## NotificationService

```java
public class NotificationService {

    public void sendNotification() {
        System.out.println("Sending notification.");
    }
}
```

The service itself does not need to know anything about its aliases.

The aliases are part of the Spring bean configuration.

---

## AppConfig

```java
@Configuration
public class AppConfig {

    @Bean(name = {"notificationService", "emailNotificationService"})
    public NotificationService notificationService() {
        return new NotificationService();
    }
}
```

Spring creates one `NotificationService` instance and registers two names for it.

---

# Retrieving the Bean by Name

The bean can be retrieved using either name.

```java
NotificationService service = context.getBean("notificationService", NotificationService.class);

NotificationService emailService = context.getBean("emailNotificationService", NotificationService.class);
```

Both references point to the same Spring bean.

---

# Bean Identity

The most important concept in this example is that aliases do **not** create additional bean instances.

The following:

```java

context.getBean("notificationService");

```

and:

```java

context.getBean("emailNotificationService");

```

return the same object.

Conceptually:

```text
                    NotificationService
                           ▲
                          / \
                         /   \
                        /     \
       notificationService   emailNotificationService
```

---

# Why Use Bean Aliases?

Bean aliases can be useful when an application needs to support multiple names for the same bean.

One common example is maintaining backwards compatibility.

Suppose an existing application uses:

```text
emailNotificationService
```

as the bean name.

Later, the application decides that:

```text
notificationService
```

is a better name.

Instead of immediately removing the old name, both names can point to the same bean:

```text
emailNotificationService ──┐
                           ├──→ NotificationService
notificationService ───────┘
```

Existing code can continue using the old name while new code uses the new name.

---

# Bean Alias vs Multiple Beans

Bean aliases do not create multiple beans.

### Bean aliases

```text
name1 ──┐
        ├──→ One bean instance
name2 ──┘
```

### Multiple beans

```text
name1 ──→ Bean instance #1

name2 ──→ Bean instance #2
```

The important difference is that aliases are simply additional names for the same bean definition.

---

# Bean Aliases and Bean Scope

Bean aliases do not change the scope of a bean.

For example, if the bean is a singleton:

```java
@Bean(name = {"notificationService", "emailNotificationService"})
public NotificationService notificationService() {
    return new NotificationService();
}
```

both names resolve to the same singleton instance.

The aliases do not create separate singleton instances.

---

# Relationship to `@Qualifier`

Bean aliases and `@Qualifier` solve different problems.

`@Qualifier` helps select a particular bean when multiple beans of the same type are available.

Bean aliases provide additional names for an existing bean.

For example:

```text
@Qualifier
    ↓
Which bean should be injected?

Bean Alias
    ↓
What other name can be used to refer to this bean?
```

The two concepts can work together, but they should not be confused.

---

# Key Takeaways

- A Spring bean can have multiple names.
- Additional names are called bean aliases.
- Bean aliases can be declared using the `name` attribute of `@Bean`.
- Multiple aliases still refer to the same bean instance.
- Aliases can be useful for backwards compatibility.
- Bean aliases do not create additional bean instances.
- Bean aliases are different from `@Qualifier`.

---

# What's Next?

This completes **Module 4 — Dependency Injection**.

The next module explores the **Spring Bean Lifecycle**, including how Spring creates, initializes, and destroys beans.