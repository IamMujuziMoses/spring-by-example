# Circular Dependencies

A circular dependency occurs when two or more Spring beans depend on each other, creating a dependency cycle that the container cannot resolve.

This example demonstrates a circular dependency between two services using constructor injection.

---

## Learning Objectives

By the end of this example, you will be able to:

- Understand what a circular dependency is.
- Identify circular dependencies in a Spring application.
- Understand how constructor injection exposes circular dependencies.
- Understand why Spring cannot create beans involved in a constructor dependency cycle.
- Recognize `BeanCurrentlyInCreationException`.
- Understand why circular dependencies should generally be avoided.

---

# What Is a Circular Dependency?

A circular dependency occurs when one bean depends on another bean, which in turn depends on the first bean.

For example:

```text
ServiceA
    ↓
ServiceB
    ↓
ServiceA
```

`ServiceA` requires `ServiceB`, but `ServiceB` requires `ServiceA`.

Spring therefore has no way to create either bean first.

---

# The Example

## ServiceA

```java
@Service
public class ServiceA {

    private final ServiceB serviceB;

    public ServiceA(ServiceB serviceB) {
        this.serviceB = serviceB;
    }
}
```

`ServiceA` requires `ServiceB` through constructor injection.

---

## ServiceB

```java
@Service
public class ServiceB {

    private final ServiceA serviceA;

    public ServiceB(ServiceA serviceA) {
        this.serviceA = serviceA;
    }
}
```

`ServiceB` requires `ServiceA` through constructor injection.

The resulting dependency graph is:

```text
ServiceA
   |
   ↓
ServiceB
   |
   ↓
ServiceA
```

This is a circular dependency.

---

# Application Configuration

```java
@Configuration
@ComponentScan
public class AppConfig {

}
```

The component scan discovers both services and asks Spring to register them as beans.

---

# What Happens When Spring Starts?

When the application context starts, Spring attempts to create `ServiceA`.

```text
Create ServiceA
      ↓
ServiceA requires ServiceB
      ↓
Create ServiceB
      ↓
ServiceB requires ServiceA
      ↓
ServiceA is already being created
      ↓
Circular dependency detected
```

Spring cannot complete the bean creation process.

The application context therefore fails to start.

---

# The Test

The test verifies that Spring fails when attempting to create the application context.

```java
@Test
void shouldFailToCreateContextBecauseOfCircularDependency() {

    Exception exception = assertThrows(BeanCreationException.class, () -> new AnnotationConfigApplicationContext(AppConfig.class));

    assertTrue(exception.getMessage().contains("serviceA") || exception.getMessage().contains("serviceB"));
}
```

The important part is:

```java
new AnnotationConfigApplicationContext(AppConfig.class)
```

Creating the application context causes Spring to instantiate the discovered beans.

Because `ServiceA` and `ServiceB` depend on each other, bean creation fails.

---

# Exception

The underlying problem is represented by:

```text
BeanCurrentlyInCreationException
```

A typical error contains information similar to:

```text
Requested bean is currently in creation:
Is there an unresolvable circular reference?
```

This indicates that Spring attempted to create a bean that is already in the process of being created.

---

# Why Constructor Injection Exposes the Problem

Consider:

```

public ServiceA(ServiceB serviceB)

```

Spring cannot construct `ServiceA` without first providing a `ServiceB`.

But:

```java

public ServiceB(ServiceA serviceA)

```

means Spring cannot construct `ServiceB` without first providing a `ServiceA`.

Therefore:

```text
ServiceA cannot be created
        ↓
needs ServiceB
        ↓
ServiceB cannot be created
        ↓
needs ServiceA
        ↓
ServiceA is still being created
```

There is no valid starting point.

This is one reason constructor injection is useful: dependencies are explicit and circular dependencies become immediately visible.

---

# Why Circular Dependencies Should Be Avoided

A circular dependency often indicates that the responsibilities of the involved classes are too tightly coupled.

For example:

```text
ServiceA → ServiceB
   ↑          ↓
   └──────────┘
```

This creates a strong coupling between the two components.

Instead of trying to hide the cycle, it is generally better to reconsider the design and identify whether:

- Responsibilities can be separated.
- Shared behavior can be extracted into another component.
- One service can depend on an abstraction instead.
- The dependency direction can be changed.

The goal should generally be to create a dependency graph that flows in one direction.

---

# What About `@Lazy`?

Spring provides `@Lazy`, which can defer the initialization of a dependency.

For example:

```java

public ServiceA(@Lazy ServiceB serviceB) {
    this.serviceB = serviceB;
}
```

This can allow Spring to defer creation of `ServiceB` and break certain circular dependency scenarios.

However, `@Lazy` should not automatically be considered the solution to every circular dependency.

If two services fundamentally depend on each other, the underlying design may still be unnecessarily coupled.

It is better to understand why the circular dependency exists before using `@Lazy` to defer it.

---

# Key Takeaways

- A circular dependency occurs when beans depend on each other directly or indirectly.
- Constructor injection makes the dependency relationship explicit.
- Spring cannot resolve a direct constructor dependency cycle.
- The application context fails during bean creation.
- `BeanCurrentlyInCreationException` indicates that Spring attempted to create a bean that is already being created.
- Circular dependencies often indicate that the dependency design should be reconsidered.
- `@Lazy` can defer dependency creation in some situations, but should not be used simply to hide poor dependency design.

---

# What's Next?

The next example explores **Bean Aliases**, demonstrating how a single Spring bean can be registered under multiple names.