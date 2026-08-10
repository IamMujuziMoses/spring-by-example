# DisposableBean

Spring provides several ways to execute cleanup logic when a bean is being destroyed.

This example demonstrates the `DisposableBean` interface and its `destroy()` callback.

The example builds on the previous `InitializingBean` example by exploring the other end of the bean lifecycle: **destruction**.

---

## Learning Objectives

By the end of this example, you will understand:

- What `DisposableBean` is.
- Why Spring provides destruction callbacks.
- When `destroy()` is invoked.
- How `DisposableBean` fits into the Spring bean lifecycle.
- How closing an `ApplicationContext` triggers bean destruction.
- How to test destruction behavior.
- Why application code should allow Spring to manage bean destruction.
- The relationship between `InitializingBean` and `DisposableBean`.

---

# What Is `DisposableBean`?

`DisposableBean` is a Spring interface that provides a callback for bean destruction.

A bean implements the interface by defining:

```java

destroy()

```

Spring invokes this method when the bean is being destroyed.

For example:

```java
public class ReportService implements DisposableBean {

    @Override
    public void destroy() {
        // Cleanup logic
    }
}
```

The important point is that application code normally does not call `destroy()` directly.

The Spring container manages the bean lifecycle and invokes the callback when the bean is destroyed.

---

# Why Does `DisposableBean` Exist?

Some beans need to perform cleanup when they are no longer needed.

For example, a bean might:

- Close a resource.
- Stop a background process.
- Release application-managed resources.
- Clear internal state.
- Perform other cleanup operations.

Spring needs a way to notify these beans that they are being destroyed.

`DisposableBean` provides one such mechanism.

The simplified lifecycle is:

```text
Bean created
     ↓
Bean initialized
     ↓
Bean used
     ↓
ApplicationContext closes
     ↓
Bean destroyed
     ↓
destroy() invoked
```

---

# The Spring Bean Lifecycle

A simplified Spring bean lifecycle looks like this:

```text
Bean definition discovered
        ↓
Bean instance created
        ↓
Dependencies populated
        ↓
Initialization processing
        ↓
Initialization callbacks
        ↓
BeanPostProcessor
        ↓
Bean ready for use
        ↓
Application context remains active
        ↓
Application context closes
        ↓
Destruction callbacks
        ↓
Bean destroyed
```

`DisposableBean` participates in the **destruction** portion of this lifecycle.

The callback provided by the interface is:

```java

destroy()

```

---

# Example

The example uses a simple `ReportService`.

The service maintains an `active` state so that we can observe what happens when Spring destroys the bean.

## ReportService

```java
public class ReportService implements DisposableBean {

    private boolean active = true;

    public boolean isActive() {
        return active;
    }

    @Override
    public void destroy() {
        active = false;
    }
}
```

When the bean is created:

```text
active = true
```

When Spring invokes:

```java

destroy()

```

the value changes to:

```text
active = false
```

The boolean is intentionally simple. In a real application, the destruction callback could perform actual cleanup work.

---

# Configuration

The bean is registered using Java-based configuration.

## AppConfig

```java
@Configuration
public class AppConfig {

    @Bean
    public ReportService reportService() {
        return new ReportService();
    }
}
```

No special destruction configuration is required.

Because Spring owns this bean, the container can manage its lifecycle.

---

# Running the Example

## Main

```java
public class Main {

    public static void main(String[] args) {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            ReportService reportService = context.getBean(ReportService.class);

            System.out.println("Active: " + reportService.isActive());
        }
    }
}
```

The expected output is:

```text
Active: true
```

The interesting part is not the output itself, but what happens when the try-with-resources block ends.

---

# Why Does `try-with-resources` Matter?

The application context implements `AutoCloseable`.

Therefore, this:

```java

try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {}

```

automatically closes the context when the block finishes.

Conceptually:

```text
try block starts
        ↓
ApplicationContext created
        ↓
ReportService created
        ↓
ReportService retrieved
        ↓
try block ends
        ↓
ApplicationContext.close()
        ↓
Spring destroys managed beans
        ↓
ReportService.destroy()
```

Without closing the application context, we would not have an explicit lifecycle event that demonstrates destruction.

---

# What Happens When the Context Closes?

Consider:

```java

context.close();

```

This tells Spring that the application context is shutting down.

Spring then begins destroying the beans it manages.

For our `ReportService`:

```text
context.close()
       ↓
Spring begins bean destruction
       ↓
destroy() invoked
       ↓
active = false
```

This is why the destruction callback belongs to the Spring container rather than normal application logic.

---

# Why Don't We Call `destroy()` Directly?

It might be tempting to write:

```java

reportService.destroy();

```

but this defeats the purpose of the lifecycle callback.

The purpose of `DisposableBean` is to allow **Spring to control when destruction occurs**.

The preferred flow is:

```text
Application code
       ↓
context.close()
       ↓
Spring container
       ↓
destroy()
```

rather than:

```text
Application code
       ↓
destroy()
```

The Spring container knows which beans it manages and can invoke their destruction callbacks as part of the application context shutdown process.

---

# Singleton Beans and Destruction

This example uses the default Spring bean scope:

```java
@Bean
public ReportService reportService() {
    return new ReportService();
}
```

The default scope is singleton.

This is important because Spring's application context manages the complete lifecycle of its singleton beans, including their destruction callbacks.

This example intentionally does not introduce prototype scope because the goal here is to focus on the destruction phase of the Spring bean lifecycle.

---

# InitializingBean vs DisposableBean

The previous example introduced `InitializingBean`.

Now we can see the relationship between the two interfaces.

| Interface | Callback | Lifecycle phase |
|---|---|---|
| `InitializingBean` | `afterPropertiesSet()` | Initialization |
| `DisposableBean` | `destroy()` | Destruction |

The simplified lifecycle becomes:

```text
                 Bean Lifecycle

                      Creation
                         ↓
                  Initialization
                         ↓
              afterPropertiesSet()
                         ↓
                    Bean ready
                         ↓
                     Bean used
                         ↓
                Context closes
                         ↓
                    Destruction
                         ↓
                      destroy()
```

This gives us two important lifecycle extension points:

```text
InitializingBean
       ↓
"Prepare the bean."

DisposableBean
       ↓
"Clean up the bean."
```

---

# `DisposableBean` vs `@PreDestroy`

`DisposableBean` is not the only way to define destruction logic.

Spring also supports:

```java
@PreDestroy
public void cleanup() {
    // Cleanup logic
}
```

The `@PreDestroy` mechanism will be explored in a dedicated example later in this module.

For now, the important distinction is that `DisposableBean` requires the class to implement a Spring-specific interface:

```java

public class ReportService implements DisposableBean

```

whereas `@PreDestroy` uses an annotation:

```java
@PreDestroy
public void cleanup() {
}
```

Keeping these examples separate allows us to understand each mechanism before comparing them.

---

# When Should You Use `DisposableBean`?

`DisposableBean` can be useful when a Spring-managed bean needs to perform cleanup during application shutdown.

Examples include:

- Closing resources managed by the bean.
- Stopping background processing.
- Releasing resources.
- Cleaning up internal state.
- Performing shutdown-related operations.

However, `DisposableBean` is Spring-specific.

That means the class becomes directly coupled to Spring:

```java

import org.springframework.beans.factory.DisposableBean;

```

This is one reason Spring provides other lifecycle mechanisms such as `@PreDestroy`.

The goal of this example is to understand how Spring manages destruction, rather than suggesting that `DisposableBean` is always the preferred mechanism.

---

# Important Lifecycle Concept

There is an important distinction between **destroying a Java object** and **Spring destroying a managed bean**.

Java's garbage collector determines when an object is no longer reachable and can be reclaimed.

Spring's destruction lifecycle is different.

Spring knows that a bean belongs to its application context and can perform lifecycle callbacks when that context shuts down.

Conceptually:

```text
Java
 ↓
Garbage collection
 ↓
Memory reclamation


Spring
 ↓
ApplicationContext closes
 ↓
Bean destruction callbacks
 ↓
destroy()
```

`DisposableBean` is therefore about **Spring lifecycle management**, not Java garbage collection.

---

# Key Takeaways

- `DisposableBean` is a Spring lifecycle interface.
- It provides the `destroy()` callback.
- Spring invokes `destroy()` when the managed bean is being destroyed.
- Closing the `ApplicationContext` triggers the destruction phase.
- Application code should normally close the context rather than call `destroy()` directly.
- The example uses a singleton bean so Spring can manage its complete lifecycle.
- `DisposableBean` represents the destruction side of the lifecycle.
- `InitializingBean` and `DisposableBean` provide complementary lifecycle callbacks.
- `@PreDestroy` provides another way to define destruction logic and will be explored separately.
- Spring bean destruction is different from Java garbage collection.

---

# What's Next?

The next example introduces:

**`@PostConstruct`**

This will give us an opportunity to compare interface-based lifecycle callbacks with annotation-based callbacks.