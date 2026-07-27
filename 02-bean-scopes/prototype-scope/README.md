# Prototype Scope

The **prototype** scope instructs the Spring IoC container to create a **new instance of a bean every time it is requested**.

Unlike the singleton scope, where Spring creates one shared instance per `ApplicationContext`, prototype-scoped beans are independent objects with their own state.

This module demonstrates how Spring creates multiple instances of the same bean and how each instance maintains independent data.

---

## Learning Objectives

By the end of this module, you will be able to:

- Understand what the prototype bean scope is.
- Explain how prototype differs from singleton scope.
- Verify that each bean lookup returns a new instance.
- Understand how prototype beans maintain independent state.
- Understand the lifecycle behavior of prototype beans.

---


## What Is Prototype Scope?

A prototype bean is a bean where Spring creates a **new instance every time the bean is requested**.

For example:

```java
Counter counter1 = context.getBean(Counter.class);
Counter counter2 = context.getBean(Counter.class);
```

Spring creates two different objects:

```
ApplicationContext

getBean()
    |
    └── Counter instance #1


getBean()
    |
    └── Counter instance #2
```

---

## Defining a Prototype Bean

Unlike singleton scope, prototype scope must be explicitly configured.

```java
@Bean
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public Counter counter() {
    return new Counter();
}
```

The `@Scope` annotation tells Spring to create a new instance whenever the bean is requested.

---

## Example

```java
Counter counter1 = context.getBean(Counter.class);
Counter counter2 = context.getBean(Counter.class);

System.out.println(counter1 == counter2);
```

Output:

```text
false
```

The two references point to different objects.

---

## Demonstrating Independent State

Each prototype instance maintains its own state.

```java
counter1.increment();

System.out.println(counter1.getCount());
System.out.println(counter2.getCount());
```

Output:

```text
1
0
```

Incrementing `counter1` does not affect `counter2` because they are separate objects.

---

## Prototype vs Singleton

| Singleton | Prototype |
|-----------|-----------|
| One instance per `ApplicationContext` | New instance for every lookup |
| Shared state | Independent state |
| Default Spring scope | Must be explicitly configured |
| Spring manages the complete lifecycle | Spring manages creation only |

---

## When to Use Prototype Scope

Prototype scope is useful when each consumer requires its own independent object.

Common examples include:

- Stateful objects
- Builders
- Temporary processing objects
- Objects created for a specific task
- Objects that should not share data

---

## Lifecycle Considerations

Spring manages the creation of prototype beans but does **not manage their complete lifecycle**.

For singleton beans, Spring manages:

- Creation
- Initialization callbacks
- Destruction callbacks

For prototype beans, Spring only manages:

- Creation
- Dependency injection
- Initialization callbacks

After returning a prototype bean, Spring no longer tracks that object.

This means destruction callbacks such as `@PreDestroy` are not automatically called.

---

## Running the Example

Run the `Main` class.

Expected output:

```text
Counter 1: 1
Counter 2: 0

Same instance? false
```

This demonstrates that each lookup returns a different `Counter` instance.

---

## Key Takeaways

- Prototype beans create a new object every time they are requested.
- Prototype beans do not share state.
- Prototype scope must be explicitly configured.
- Spring manages creation but not destruction of prototype beans.
- Prototype scope is useful for independent, short-lived objects.

---

## What's Next?

The next example explores **Request Scope**, a bean scope commonly used in Spring web applications.

You'll learn:

- How Request scoped beans are created
- How Spring manages one bean instance per HTTP request
- How Request Scope differs from Singleton and Prototype
- When Request Scope is the right choice