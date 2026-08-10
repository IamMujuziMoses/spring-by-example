# InitializingBean

Spring provides several ways to execute initialization logic after a bean has been created and its dependencies have been populated.

This example demonstrates the `InitializingBean` interface and its `afterPropertiesSet()` callback.

---

## Learning Objectives

By the end of this example, you will understand:

- What `InitializingBean` is.
- Why Spring provides initialization callbacks.
- When `afterPropertiesSet()` is invoked.
- How `InitializingBean` fits into the Spring bean lifecycle.
- How to verify initialization behavior with a test.
- Why initialization logic should not be performed manually by application code.

---

## What Is `InitializingBean`?

`InitializingBean` is a Spring interface that provides a callback for bean initialization.

A bean implements the interface by defining the:

```java

afterPropertiesSet()

```

method.

Spring calls this method after it has created the bean and populated its properties.

For example:

```java
public class ReportService implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
        // Initialization logic
    }
}
```

The important point is that application code does not normally call `afterPropertiesSet()` itself.

Spring manages the invocation as part of the bean lifecycle.

---

# Why Does `InitializingBean` Exist?

Creating a Java object and making that object ready for use are not always the same thing.

Consider a service that requires some initialization after its dependencies have been configured.

For example, a service might need to:

- Validate its configuration.
- Prepare an internal data structure.
- Initialize a resource.
- Calculate some derived state.
- Perform setup that depends on injected properties.

We want this work to happen at the appropriate point in the Spring lifecycle.

`InitializingBean` provides one way to express that requirement:

```text
Bean created
     ↓
Dependencies populated
     ↓
Initialization callback
     ↓
Bean ready for use
```

---

# Spring Bean Lifecycle

At a simplified level, Spring's bean lifecycle looks like this:

```text
Bean definition discovered
        ↓
Bean instance created
        ↓
Dependencies populated
        ↓
Aware callbacks
        ↓
BeanPostProcessor
        ↓
Initialization callbacks
        ↓
BeanPostProcessor
        ↓
Bean ready for use
        ↓
Application context eventually closes
        ↓
Destruction callbacks
```

`InitializingBean` participates in the **initialization** part of this lifecycle.

The `afterPropertiesSet()` method is called after the bean's properties have been set.

This is why the method is named `afterPropertiesSet()`.

---

# Example

The example uses a simple `ReportService`.

The service records whether its initialization callback has been executed.

## ReportService

```java
public class ReportService implements InitializingBean {

    private boolean initialized;

    @Override
    public void afterPropertiesSet() {
        initialized = true;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
```

The `initialized` field starts as:

```text
false
```

When Spring invokes:

```java

afterPropertiesSet()

```

the value becomes:

```text
true
```

This gives us something observable that we can verify in our tests.

---

# Configuration

The bean is registered using Java configuration.

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

When the application context starts, Spring discovers the `ReportService` bean definition.

Spring then manages the creation and initialization of the bean.

---

# Running the Example

## Main

```java
public class Main {

    public static void main(String[] args) {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            ReportService reportService = context.getBean(ReportService.class);

            System.out.println("Initialized: " + reportService.isInitialized());
        }
    }
}
```

The expected output is:

```text
Initialized: true
```

The application never calls:

```java

reportService.afterPropertiesSet();

```

Spring calls it automatically during bean initialization.

---

# What Happens When the Context Starts?

When this code executes:

```java

var context = new AnnotationConfigApplicationContext(AppConfig.class);

```

Spring creates the application context and begins creating the beans defined by `AppConfig`.

For `ReportService`, the simplified sequence is:

```text
1. Spring reads the ReportService bean definition
                ↓
2. Spring creates a ReportService instance
                ↓
3. Spring populates its properties/dependencies
                ↓
4. Spring performs initialization processing
                ↓
5. Spring invokes afterPropertiesSet()
                ↓
6. ReportService becomes ready for use
```

Therefore, by the time this executes:

```java

ReportService reportService = context.getBean(ReportService.class);

```

the initialization callback has already been executed.

---

# What If We Call It Manually?

It is technically possible to call the method directly:

```java

reportService.afterPropertiesSet();

```

However, this is not how the lifecycle callback is intended to be used.

The purpose of implementing `InitializingBean` is to allow the Spring container to manage when the initialization callback is executed.

Application code should generally obtain the bean and allow Spring to manage its lifecycle.

---

# `InitializingBean` vs Constructor Initialization

It is important to distinguish constructor logic from lifecycle initialization.

A constructor runs when the Java object is created:

```java

public ReportService() {
    // Constructor logic
}
```

`afterPropertiesSet()` runs later as part of Spring's lifecycle:

```java
@Override
public void afterPropertiesSet() {
    // Initialization logic
}
```

Conceptually:

```text
new ReportService()
        ↓
Constructor executes
        ↓
Bean exists
        ↓
Dependencies/properties populated
        ↓
afterPropertiesSet()
        ↓
Bean initialized
```

This distinction becomes particularly important when initialization depends on values that Spring injects into the bean.

---

# When Should You Use `InitializingBean`?

`InitializingBean` can be useful when a bean needs to perform initialization after Spring has configured it.

Examples include:

- Validating injected configuration.
- Preparing internal state.
- Initializing resources.
- Building caches.
- Performing setup that depends on injected properties.

However, it is not the only way to define initialization behavior in Spring.

Spring also provides mechanisms such as:

- `@PostConstruct`
- `@Bean(initMethod = "...")`

These alternatives will be explored in later examples.

---

# `InitializingBean` and `@PostConstruct`

Both `InitializingBean` and `@PostConstruct` can be used to execute initialization logic.

With `InitializingBean`:

```java
public class ReportService implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
        // Initialization logic
    }
}
```

With `@PostConstruct`:

```java
public class ReportService {

    @PostConstruct
    public void initialize() {
        // Initialization logic
    }
}
```

They solve a similar problem, but they use different mechanisms.

This project will explore `@PostConstruct` separately so that the differences can be understood clearly rather than introducing both mechanisms in the same example.

---

# Important Consideration

`InitializingBean` is a Spring-specific interface.

That means the application class becomes directly coupled to the Spring framework:

```java

import org.springframework.beans.factory.InitializingBean;

```

This is one reason annotation-based approaches such as `@PostConstruct` are also useful to understand.

The goal of this example is not to claim that `InitializingBean` is always the best choice.

Instead, the goal is to understand:

1. What the interface does.
2. When Spring invokes it.
3. Where it fits into the lifecycle.
4. What alternatives exist.

---

# Key Takeaways

- `InitializingBean` is a Spring lifecycle interface.
- A bean implements `InitializingBean` to receive an initialization callback.
- The callback method is `afterPropertiesSet()`.
- Spring invokes `afterPropertiesSet()` after the bean's properties have been populated.
- Application code normally does not call `afterPropertiesSet()` manually.
- The callback allows a bean to perform initialization before it is used.
- `InitializingBean` is one of several initialization mechanisms provided by Spring.
- Later examples will explore `@PostConstruct` and other lifecycle mechanisms.

---

# What's Next?

The next example introduces:

**`DisposableBean`**

If `InitializingBean` answers:

> "How can a bean perform initialization after Spring has configured it?"

then `DisposableBean` answers the other side of the lifecycle:

> "How can a bean perform cleanup when Spring is destroying it?"

This will allow us to explore the **destruction phase** of the Spring bean lifecycle.