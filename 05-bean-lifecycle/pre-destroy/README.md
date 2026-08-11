# @PreDestroy

Spring provides several mechanisms for executing cleanup logic when a bean is being destroyed.

This example demonstrates the `@PreDestroy` annotation and how Spring invokes the annotated method before a managed bean is destroyed.

It builds on the previous `DisposableBean` example and introduces the annotation-based counterpart to `DisposableBean`.

---

## Learning Objectives

By the end of this example, you will understand:

- What `@PreDestroy` is.
- Why `@PreDestroy` is used.
- When a `@PreDestroy` method is invoked.
- How `@PreDestroy` fits into the Spring bean lifecycle.
- How to use `jakarta.annotation.PreDestroy`.
- How `@PreDestroy` compares with `DisposableBean`.
- Why the application context must be closed for destruction callbacks to run.
- Why application code should not manually invoke `@PreDestroy` methods.
- The difference between Spring bean destruction and Java garbage collection.
- How `@PreDestroy` relates to Spring's bean lifecycle processing.

---

# What Is `@PreDestroy`?

`@PreDestroy` is an annotation that marks a method to be executed before a Spring-managed bean is destroyed.

For example:

```java
public class ReportService {

    @PreDestroy
    public void cleanup() {
        // Cleanup logic
    }
}
```

When the Spring application context is closed, Spring detects the `@PreDestroy` method and invokes it as part of the bean destruction lifecycle.

Conceptually:

```text
Bean created
     ↓
Bean initialized
     ↓
Bean ready for use
     ↓
ApplicationContext closes
     ↓
@PreDestroy method invoked
     ↓
Bean destroyed
```

---

# Why Does `@PreDestroy` Exist?

Some beans need to perform cleanup when the application is shutting down.

For example, a bean might need to:

- Close a resource.
- Stop background processing.
- Release application-managed resources.
- Clear internal state.
- Perform shutdown-related operations.

`@PreDestroy` provides a declarative way to tell Spring:

> "Before this bean is destroyed, invoke this method."

The application does not normally need to call the method itself.

---

# `@PreDestroy` and the Bean Lifecycle

`@PreDestroy` belongs to the destruction phase of the Spring bean lifecycle.

A simplified lifecycle looks like this:

```text
Bean created
     ↓
Dependencies populated
     ↓
Initialization callbacks
     ↓
Bean ready
     ↓
Bean used
     ↓
ApplicationContext closes
     ↓
Destruction callbacks
     ↓
Bean destroyed
```

`@PreDestroy` participates near the end:

```text
ApplicationContext closes
        ↓
Spring begins bean destruction
        ↓
@PreDestroy invoked
        ↓
Cleanup performed
        ↓
Bean destruction completes
```

---

# Example

The example uses a simple `ReportService`.

The service starts in an active state and changes that state when its cleanup method is invoked.

## ReportService

```java
import jakarta.annotation.PreDestroy;

public class ReportService {

    private boolean active = true;

    @PreDestroy
    public void cleanup() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }
}
```

When the bean is created:

```text
active = true
```

When Spring invokes:

```java

cleanup();

```

the state changes to:

```text
active = false
```

The boolean is intentionally simple so that the destruction lifecycle can be observed and tested easily.

In a real application, `cleanup()` could perform actual cleanup operations.

---

# Why `jakarta.annotation.PreDestroy`?

With Spring 6, the Jakarta namespace is used instead of the older `javax` namespace.

The correct import is:

```java

import jakarta.annotation.PreDestroy;

```

Do not use:

```java

import javax.annotation.PreDestroy;

```

for this Spring 6 example.

`@PreDestroy` is provided by the Jakarta Annotations API rather than by Spring itself.

---

# Maven Dependency

The Jakarta Annotations API provides both:

```java

jakarta.annotation.PostConstruct

```

and:

```java

jakarta.annotation.PreDestroy

```

If the project does not already provide the API, add:

```xml
<dependency>
    <groupId>jakarta.annotation</groupId>
    <artifactId>jakarta.annotation-api</artifactId>
    <version>3.0.0</version>
</dependency>
```

The dependency relationship is:

```text
jakarta.annotation-api
        ↓
@PostConstruct
@PreDestroy
        ↓
Spring processes the annotations
        ↓
Lifecycle callbacks are invoked
```

---

# Configuration

The bean is registered using Java-based Spring configuration.

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

Spring owns the resulting `ReportService` instance.

Because Spring manages the bean, Spring also manages its lifecycle.

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

The interesting part happens when the try-with-resources block finishes.

---

# Why Does `try-with-resources` Matter?

`AnnotationConfigApplicationContext` implements `AutoCloseable`.

Therefore:

```java

try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) { }

```

automatically closes the application context when the block finishes.

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
Spring begins bean destruction
        ↓
@PreDestroy invoked
        ↓
ReportService.cleanup()
```

This makes the example useful for demonstrating the destruction lifecycle explicitly.

---

# What Happens When the Context Closes?

Consider:

```java

context.close();

```

Closing the application context tells Spring that the context is shutting down.

Spring then begins destroying the beans it manages.

For our `ReportService`:

```text
context.close()
       ↓
Spring begins destruction
       ↓
@PreDestroy detected
       ↓
cleanup() invoked
       ↓
active = false
       ↓
Bean destruction completes
```

This is why the test explicitly closes the application context.

---

# Why Don't We Call `cleanup()` Directly?

It is technically possible to write:

```java

reportService.cleanup();

```

but that defeats the purpose of `@PreDestroy`.

The purpose of the annotation is to allow Spring to manage when the cleanup method executes.

The preferred flow is:

```text
Application code
       ↓
context.close()
       ↓
Spring container
       ↓
@PreDestroy
       ↓
cleanup()
```

rather than:

```text
Application code
       ↓
cleanup()
```

This allows Spring to coordinate the destruction of the beans it manages.

---

# `@PreDestroy` vs `DisposableBean`

The previous example introduced `DisposableBean`.

There are two different ways to define destruction logic.

## DisposableBean

```java
public class ReportService implements DisposableBean {

    @Override
    public void destroy() {
        // Cleanup logic
    }
}
```

The class implements a Spring interface and provides the `destroy()` callback.

## @PreDestroy

```java
public class ReportService {

    @PreDestroy
    public void cleanup() {
        // Cleanup logic
    }
}
```

The class does not need to implement `DisposableBean`.

Instead, Spring detects the annotation and invokes the method during bean destruction.

---

# Comparing the Two Approaches

| Mechanism | Callback | Style |
|---|---|---|
| `DisposableBean` | `destroy()` | Interface-based |
| `@PreDestroy` | Annotated method | Annotation-based |

Both participate in the same general lifecycle phase:

```text
ApplicationContext closes
        ↓
Bean destruction
        ↓
Cleanup callback
```

The difference is how the callback is expressed.

---

# Initialization vs Destruction

At this point, we have covered both interface-based and annotation-based lifecycle callbacks.

| Lifecycle phase | Interface | Annotation |
|---|---|---|
| Initialization | `InitializingBean` | `@PostConstruct` |
| Destruction | `DisposableBean` | `@PreDestroy` |

The lifecycle can therefore be represented as:

```text
                 Spring Bean Lifecycle

                      Creation
                         ↓
                 Dependencies set
                         ↓
              ┌──────────┴──────────┐
              ↓                     ↓
     InitializingBean        @PostConstruct
     afterPropertiesSet()    initialize()
              └──────────┬──────────┘
                         ↓
                    Bean ready
                         ↓
                      Bean used
                         ↓
                 Context closes
                         ↓
              ┌──────────┴──────────┐
              ↓                     ↓
       DisposableBean          @PreDestroy
          destroy()               cleanup()
              └──────────┬──────────┘
                         ↓
                 Bean destruction
```

This is an important mental model for understanding Spring's bean lifecycle.

---

# `@PostConstruct` vs `@PreDestroy`

The two annotations form a natural pair.

### `@PostConstruct`

```java
@PostConstruct
public void initialize() {
    // Initialization
}
```

Runs after the bean has been created and configured.

### `@PreDestroy`

```java
@PreDestroy
public void cleanup() {
    // Cleanup
}
```

Runs before the managed bean is destroyed.

Conceptually:

```text
@PostConstruct
       ↓
"Prepare the bean."


       Bean lifetime


       ↓
@PreDestroy
       ↓
"Clean up the bean."
```

---

# Spring-Managed Beans

`@PreDestroy` is processed as part of the Spring bean lifecycle.

Therefore, the object needs to be managed by Spring.

This works:

```java
@Bean
public ReportService reportService() {
    return new ReportService();
}
```

because Spring creates and manages the bean.

This does not automatically participate in the Spring lifecycle:

```java

ReportService reportService = new ReportService();

```

because the object was created directly by application code.

The distinction is:

```text
Spring-managed object
        ↓
Spring lifecycle
        ↓
@PreDestroy processed
```

versus:

```text
new ReportService()
        ↓
Normal Java object
        ↓
No Spring-managed lifecycle
```

---

# Singleton Beans and Destruction

This example uses the default singleton scope:

```java
@Bean
public ReportService reportService() {
    return new ReportService();
}
```

The default scope is singleton.

Spring's application context manages the lifecycle of its singleton beans, including destruction callbacks.

This is why:

```java

context.close();

```

causes Spring to invoke the `@PreDestroy` method.

Prototype-scoped beans have different destruction semantics and are intentionally outside the scope of this example.

---

# `@PreDestroy` and BeanPostProcessor

`@PreDestroy` is another example of Spring providing lifecycle behavior through its bean processing infrastructure.

At a high level:

```text
ApplicationContext closes
        ↓
Spring begins destroying singleton beans
        ↓
Destruction callbacks are processed
        ↓
@PreDestroy method detected
        ↓
cleanup() invoked
        ↓
Bean destruction completes
```

This is an important connection to the upcoming `BeanPostProcessor` example.

We've already seen that Spring can process annotations during the bean lifecycle.

The next step is to understand how we can participate in that processing ourselves.

---

# `@PreDestroy` Is Not Garbage Collection

An important distinction is that Spring bean destruction is not the same thing as Java garbage collection.

When Spring invokes:

```java

cleanup();

```

the Java object has not necessarily been removed from memory.

Instead:

```text
Spring
 ↓
Bean lifecycle ends
 ↓
@PreDestroy executes
 ↓
Cleanup logic runs
```

Later, when the object is no longer reachable, the JVM may reclaim its memory through garbage collection.

Therefore:

```text
@PreDestroy
    ≠
Garbage collection
```

`@PreDestroy` is about **Spring lifecycle management**, not memory reclamation.

---

# When Should You Use `@PreDestroy`?

`@PreDestroy` is useful when a Spring-managed bean needs to perform cleanup before the application context shuts down.

Examples include:

- Closing resources.
- Stopping background processing.
- Releasing application-managed resources.
- Clearing internal state.
- Performing shutdown-related operations.

It is particularly useful when you want to express destruction behavior without making the class implement Spring's `DisposableBean` interface.

---

# Important Considerations

## The Context Must Be Closed

For this example:

```java

context.close();

```

is what triggers the destruction lifecycle.

Using try-with-resources is another convenient way to ensure that the context is closed:

```java
try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
    // Use beans
}
```

When the block exits, the context is closed automatically.

---

## Don't Use `@PreDestroy` as a General Cleanup Mechanism for Arbitrary Objects

`@PreDestroy` is part of the Spring-managed lifecycle.

If an object is created manually:

```java

new ReportService();

```

Spring does not automatically manage its lifecycle.

Therefore, its `@PreDestroy` method will not automatically be invoked by Spring.

---

# Key Takeaways

- `@PreDestroy` marks a method that Spring invokes before a managed bean is destroyed.
- It belongs to the destruction phase of the Spring bean lifecycle.
- The annotation comes from `jakarta.annotation.PreDestroy`.
- Spring 6 uses the Jakarta namespace.
- Closing the `ApplicationContext` triggers the destruction lifecycle.
- Application code should normally close the context rather than call the cleanup method directly.
- `@PreDestroy` is the annotation-based counterpart to `DisposableBean`.
- `DisposableBean` uses `destroy()`.
- `@PreDestroy` allows any appropriately annotated method to act as the destruction callback.
- `@PostConstruct` and `@PreDestroy` form a natural annotation-based pair.
- `@PreDestroy` applies to Spring-managed beans.
- Spring bean destruction is different from Java garbage collection.
- Understanding these callbacks provides a foundation for understanding `BeanPostProcessor`.

---

# What's Next?

Next, we'll move to a different level of Spring's lifecycle processing, **`BeanPostProcessor`**

Instead of asking:

> "How can a bean execute code during its own lifecycle?"

we'll ask:

> "How can we intercept and process beans as Spring creates and initializes them?"

That is the role of `BeanPostProcessor`.