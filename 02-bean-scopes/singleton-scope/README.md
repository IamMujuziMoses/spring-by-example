# Singleton Scope

The **singleton** scope is Spring's default bean scope. When a bean is defined as a singleton, the Spring IoC container creates exactly **one instance** of that bean per `ApplicationContext` and returns the same instance every time it is requested.

This module demonstrates how Spring manages singleton beans and why singleton is the default scope for most Spring applications.

---

## Learning Objectives

By the end of this module, you will be able to:

- Understand what the singleton bean scope is.
- Explain why singleton is Spring's default scope.
- Verify that multiple bean lookups return the same object.
- Understand how singleton beans share state.
- Identify common use cases and best practices for singleton beans.

---

## Defining a Singleton Bean

In Spring, **singleton is the default bean scope**. This means you don't need to explicitly specify it.

```java
@Bean
public Counter counter() {
    return new Counter();
}
```

The above bean is automatically registered as a singleton.

You can also declare the scope explicitly:

```java
@Bean
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public Counter counter() {
    return new Counter();
}
```

Both configurations produce the same result.

---

## Example

```java
Counter counter1 = context.getBean(Counter.class);
Counter counter2 = context.getBean(Counter.class);

System.out.println(counter1 == counter2);
```

Output:

```text
true
```

Both variables reference the exact same object.

---

## Demonstrating Shared State

The `Counter` bean contains mutable state.

```java
counter1.increment();

System.out.println(counter1.getCount());
System.out.println(counter2.getCount());
```

Output:

```text
1
1
```

Since both variables reference the same singleton bean, updating one reference is immediately visible through the other.

---

## Demonstrating Singleton Services

The example also demonstrates that `CounterService` is a singleton by default.

```java
CounterService service1 = context.getBean(CounterService.class);
CounterService service2 = context.getBean(CounterService.class);

System.out.println(service1 == service2);
```

Output:

```text
true
```

Like `Counter`, only one `CounterService` instance exists within the `ApplicationContext`.

---

## Why Singleton Is the Default

Creating objects can be expensive.

For services, repositories, configuration classes, and many infrastructure components, only one instance is usually required.

Using singleton scope:

- Reduces object creation.
- Improves performance.
- Reduces memory usage.
- Allows Spring to efficiently manage shared components.

---

## Common Use Cases

Singleton scope is appropriate for:

- Service classes
- Repository classes
- Configuration classes
- Utility classes
- Validators
- Business logic components

Most Spring applications use singleton beans for the majority of their components.

---

## Advantages

- Only one instance is created.
- Better performance.
- Lower memory consumption.
- Ideal for stateless services.
- Simplifies dependency management.

---

## Considerations

Singleton beans are shared across the entire `ApplicationContext`.

If a singleton bean contains mutable state, that state is shared by all consumers. Care should be taken to avoid unintended side effects, especially in multi-threaded applications.

Whenever possible, design singleton beans to be **stateless**.

---

## Running the Example

Run the `Main` class.

Expected output:

```text
Service 1 count: 1
Service 2 count: 1

Same CounterService instance? true
Same Counter instance? true
```

This demonstrates that both `CounterService` and `Counter` are singleton beans managed by the Spring IoC container.

---

## Key Takeaways

- Singleton is Spring's default bean scope.
- Only one bean instance exists per `ApplicationContext`.
- Every bean lookup returns the same object.
- Singleton beans share state.
- Singleton scope is ideal for stateless services and infrastructure components.
- Stateful singleton beans should be designed carefully.

## What's Next?

The next example explores **Prototype Scope**, a bean scope where the Spring IoC container creates a **new instance** every time the bean is requested.

You'll learn:

- How Prototype beans differ from Singleton beans
- Why each bean lookup returns a new instance
- How Prototype beans maintain independent state
- When Prototype scope is the right choice