# @PostConstruct

Spring provides several ways to execute initialization logic after a bean has been created and configured.

This example demonstrates the `@PostConstruct` annotation and how Spring invokes the annotated method during bean initialization.

The example builds on the previous `InitializingBean` example and introduces an annotation-based alternative for defining initialization logic.

---

## Learning Objectives

By the end of this example, you will understand:

- What `@PostConstruct` is.
- Why `@PostConstruct` is used.
- When a `@PostConstruct` method is invoked.
- How `@PostConstruct` fits into the Spring bean lifecycle.
- How to use `jakarta.annotation.PostConstruct`.
- How `@PostConstruct` compares with `InitializingBean`.
- How Spring detects and invokes `@PostConstruct` methods.
- Why application code should not manually invoke `@PostConstruct` methods.

---

# What Is `@PostConstruct`?

`@PostConstruct` is an annotation that marks a method to be executed after a Spring bean has been created and its dependencies have been populated.

For example:

```java
public class ReportService {

    @PostConstruct
    public void initialize() {
        // Initialization logic
    }
}
```

When Spring creates and initializes the bean, it detects the `@PostConstruct` annotation and invokes the annotated method.

The application does not normally call the method itself.

Conceptually:

```text
Bean created
     ↓
Dependencies populated
     ↓
@PostConstruct method invoked
     ↓
Bean ready for use
```

---

# Why Does `@PostConstruct` Exist?

A bean may need to perform some initialization after Spring has configured it.

For example, a bean might need to:

- Validate configuration.
- Prepare internal state.
- Build a cache.
- Initialize a resource.
- Calculate derived values.
- Perform setup that depends on injected dependencies.

The constructor is not always the appropriate place for this work because Spring may not have completed dependency injection yet.

`@PostConstruct` provides a convenient way to tell Spring:

> "After this bean has been constructed and configured, invoke this method."

---

# `@PostConstruct` and Constructors

It is important to understand the difference between constructor execution and `@PostConstruct`.

Consider:

```java
public class ReportService {

    public ReportService() {
        // Constructor
    }

    @PostConstruct
    public void initialize() {
        // Initialization
    }
}
```

The lifecycle is conceptually:

```text
Constructor
    ↓
Object created
    ↓
Dependencies populated
    ↓
@PostConstruct
    ↓
Bean ready
```

The constructor runs when Java creates the object.

The `@PostConstruct` method runs later as part of Spring's bean initialization process.

This distinction becomes especially important when initialization depends on values supplied by Spring.

---

# Example

The example uses a simple `ReportService`.

The service records whether its initialization method has been executed.

## ReportService

```java
import jakarta.annotation.PostConstruct;

public class ReportService {

    private boolean initialized;

    @PostConstruct
    public void initialize() {
        initialized = true;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
```

The field starts with its default value:

```text
initialized = false
```

When Spring invokes:

```java

initialize();

```

the value becomes:

```text
initialized = true
```

This gives us an observable result that we can verify in our tests.

---

# Why `jakarta.annotation.PostConstruct`?

With Spring 6, the Jakarta namespace is used instead of the older `javax` namespace.

The correct import is:

```java

import jakarta.annotation.PostConstruct;

```

Do not use:

```java

import javax.annotation.PostConstruct;

```

for this Spring 6 example.

The `@PostConstruct` annotation comes from the Jakarta Annotations API rather than Spring itself.

---

# Maven Dependency

If the project does not already provide the Jakarta Annotations API, add the following dependency:

```xml
<dependency>
    <groupId>jakarta.annotation</groupId>
    <artifactId>jakarta.annotation-api</artifactId>
    <version>3.0.0</version>
</dependency>
```

This dependency provides:

```java

jakarta.annotation.PostConstruct

```

Spring then processes the annotation during bean initialization.

The relationship is:

```text
jakarta.annotation-api
        ↓
@PostConstruct
        ↓
Spring bean processing
        ↓
Initialization method invoked
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

Spring owns the resulting `ReportService` instance and therefore manages its lifecycle.

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

The important point is that the application never explicitly calls:

```java

reportService.initialize();

```

Spring invokes the method automatically.

---

# What Happens When the Context Starts?

When the following code executes:

```java

new AnnotationConfigApplicationContext(AppConfig.class);

```

Spring creates the application context and begins managing the beans defined by the configuration.

For `ReportService`, the simplified process is:

```text
1. Spring discovers the ReportService bean definition
                ↓
2. Spring creates a ReportService instance
                ↓
3. Spring populates dependencies and properties
                ↓
4. Spring processes the bean
                ↓
5. Spring detects @PostConstruct
                ↓
6. Spring invokes initialize()
                ↓
7. ReportService is ready for use
```

Therefore, when this executes:

```java

context.getBean(ReportService.class);

```

the `@PostConstruct` method has already been invoked.

---

# What If We Call the Method Manually?

It is technically possible to call:

```java

reportService.initialize();

```

but this is not how `@PostConstruct` is intended to be used.

The purpose of the annotation is to allow Spring to manage when the method executes.

The preferred flow is:

```text
Application code
       ↓
ApplicationContext
       ↓
Spring creates and initializes bean
       ↓
Spring invokes @PostConstruct
       ↓
Bean ready
```

rather than:

```text
Application code
       ↓
initialize()
```

This distinction is important because Spring should remain responsible for managing the bean lifecycle.

---

# `@PostConstruct` vs `InitializingBean`

The previous example introduced `InitializingBean`.

There are two different ways to express initialization behavior:

## InitializingBean

```java
public class ReportService implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
        // Initialization logic
    }
}
```

## @PostConstruct

```java
public class ReportService {

    @PostConstruct
    public void initialize() {
        // Initialization logic
    }
}
```

Both participate in the initialization phase of the Spring bean lifecycle.

Conceptually:

```text
                 Bean created
                      ↓
              Dependencies set
                      ↓
               Initialization
                  ↙       ↘
                 ↓         ↓
      InitializingBean   @PostConstruct
      afterPropertiesSet initialize()
                 ↘       ↙
                      ↓
                  Bean ready
```

The key difference is how the lifecycle behavior is expressed.

`InitializingBean` requires the class to implement a Spring interface:

```java

implements InitializingBean

```

`@PostConstruct` uses an annotation:

```java

@PostConstruct

```

---

# Why Use `@PostConstruct`?

One advantage of `@PostConstruct` is that the class does not need to implement a Spring lifecycle interface.

With `InitializingBean`:

```java

public class ReportService implements InitializingBean { }

```

the class is explicitly coupled to that Spring interface.

With `@PostConstruct`:

```java
public class ReportService {

    @PostConstruct
    public void initialize() {
    }
}
```

the lifecycle behavior is expressed declaratively.

This can make the intent easier to see:

```text
@PostConstruct
    ↓
"This method should run after bean initialization."
```

However, both approaches are valid Spring lifecycle mechanisms, and understanding both is more important than simply choosing one.

---

# How Does Spring Know About `@PostConstruct`?

`@PostConstruct` is not magic.

Spring detects and processes the annotation through its bean post-processing infrastructure.

One of the components involved is:

```text
CommonAnnotationBeanPostProcessor
```

Conceptually:

```text
Spring creates bean
       ↓
BeanPostProcessor processing
       ↓
CommonAnnotationBeanPostProcessor
       ↓
Find @PostConstruct
       ↓
Invoke initialize()
       ↓
Continue bean initialization
```

This is an important connection because we will explore `BeanPostProcessor` in detail later in this module.

For now, the important idea is:

> `@PostConstruct` is implemented through Spring's bean processing infrastructure.

---

# `@PostConstruct` and BeanPostProcessor

This example introduces an important concept that will become clearer later.

Spring's `BeanPostProcessor` infrastructure can participate in bean initialization.

`@PostConstruct` is processed through this infrastructure.

This means the lifecycle isn't simply:

```text
Create object
    ↓
Call @PostConstruct
```

There is framework processing involved between bean creation and the final usable bean.

Later, when we implement our own `BeanPostProcessor`, we will be able to see this mechanism more directly.

---

# When Should You Use `@PostConstruct`?

`@PostConstruct` is useful when a bean needs initialization logic that should execute after Spring has configured it.

Examples include:

- Validating injected configuration.
- Preparing internal state.
- Building caches.
- Initializing resources.
- Performing setup that depends on injected dependencies.

It is particularly useful when you want to express lifecycle behavior without implementing a Spring-specific lifecycle interface.

---

# Important Considerations

## The Method Is Not Called During Construction

This:

```java

public ReportService() { }

```

runs during object construction.

This:

```java
@PostConstruct
public void initialize() { }
```

runs later during Spring's initialization process.

Do not assume that `@PostConstruct` is equivalent to a constructor.

---

## The Bean Must Be Managed by Spring

Spring only processes `@PostConstruct` for objects that it manages.

For example:

```java
@Bean
public ReportService reportService() {
    return new ReportService();
}
```

is managed by Spring.

But:

```java

ReportService reportService = new ReportService();

```

creates a normal Java object outside Spring's bean lifecycle.

Spring will not automatically process its `@PostConstruct` method.

This distinction is fundamental:

```text
Spring-managed object
        ↓
Spring lifecycle
        ↓
@PostConstruct processed
```

versus:

```text
new ReportService()
        ↓
Normal Java object
        ↓
No Spring lifecycle
```

---

# Key Takeaways

- `@PostConstruct` marks a method to be invoked during Spring bean initialization.
- The annotation comes from `jakarta.annotation.PostConstruct`.
- Spring 6 uses the Jakarta namespace.
- The method runs after the bean has been created and configured.
- Application code normally does not call the method manually.
- `@PostConstruct` provides an annotation-based alternative to `InitializingBean`.
- `InitializingBean` uses `afterPropertiesSet()`.
- `@PostConstruct` uses an annotated method.
- Spring processes `@PostConstruct` through its bean post-processing infrastructure.
- The bean must be managed by Spring for `@PostConstruct` to be processed.
- `@PostConstruct` is part of initialization, not construction.

---

# What's Next?

Next, we'll explore the destruction counterpart to `@PostConstruct` which is `@PreDestroy`:

```text
@PreDestroy
       ↓
cleanup()
```

This will allow us to compare:

```text
InitializingBean    ↔    DisposableBean
@PostConstruct      ↔    @PreDestroy
```

and complete our understanding of the common initialization and destruction callback mechanisms.