# How Dependency Injection Works

This example demonstrates how Spring resolves dependencies internally using `DependencyDescriptor` and `DefaultListableBeanFactory`.

Instead of using higher-level annotations such as `@Autowired`, the example manually creates a `DependencyDescriptor` for a constructor parameter and passes it to the bean factory's `resolveDependency()` method.

The goal is to understand what happens behind the scenes when Spring needs to find and provide a dependency for a bean.

## Learning Objectives

- Understand how Spring resolves dependencies internally.
- Understand the role of `DependencyDescriptor`.
- Understand how constructor parameters represent injection points.
- Understand how `MethodParameter` is used to describe a constructor parameter.
- Understand how `DefaultListableBeanFactory` resolves dependencies.
- Understand the relationship between dependency resolution and bean lookup.
- Understand the basic flow behind constructor dependency injection.

---

## What Is Dependency Injection?

Dependency injection is the process of providing an object with the dependencies it needs instead of requiring the object to create those dependencies itself.

For example:

```java
public class GreetingController {

    private final GreetingService greetingService;

    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }
}
```

`GreetingController` depends on `GreetingService`.

Rather than creating the dependency itself:

```java
GreetingService greetingService = new GreetingService();
```

the dependency can be provided by the Spring container.

Spring is responsible for locating the appropriate `GreetingService` bean and supplying it to the constructor.

---

## What Is `DependencyDescriptor`?

`DependencyDescriptor` describes an injection point that Spring needs to resolve.

An injection point can be a:

- Constructor parameter
- Method parameter
- Field

In this example, the injection point is the `GreetingService` parameter of the `GreetingController` constructor.

The constructor parameter is represented using Spring's `MethodParameter`:

```java
Constructor<GreetingController> constructor =
        GreetingController.class.getConstructor(GreetingService.class);

MethodParameter methodParameter = new MethodParameter(constructor, 0);
DependencyDescriptor descriptor = new DependencyDescriptor(methodParameter, true);
```

The `0` indicates that the dependency is the first parameter of the constructor.

The `true` argument indicates that the dependency is required.

---

## Resolving a Dependency

Once Spring has a `DependencyDescriptor`, the bean factory can resolve the dependency:

```java
Object dependency = beanFactory.resolveDependency(descriptor, "greetingController");
```

`resolveDependency()` examines the descriptor and determines which bean should satisfy the dependency.

In this example, the required dependency type is:

```text
GreetingService
```

The bean factory finds the registered `GreetingService` bean and returns it.

---

## The Dependency Resolution Flow

The process can be simplified as:

```text
GreetingController constructor
            │
            ▼
       MethodParameter
            │
            ▼
    DependencyDescriptor
            │
            ▼
resolveDependency(...)
            │
            ▼
Find matching bean
            │
            ▼
GreetingService instance
```

This is an important part of understanding what happens behind the scenes when Spring performs dependency injection.

---

## Registering the Dependency

Before the dependency can be resolved, the bean factory needs to know about the `GreetingService`.

The example registers a bean definition:

```java
DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

beanFactory.registerBeanDefinition("greetingService",new RootBeanDefinition(GreetingService.class));
```

The bean factory now has a definition describing the `GreetingService` bean.

When `resolveDependency()` is called, Spring can use this definition to locate and create the dependency.

---

## Resolving and Injecting the Dependency

The dependency can then be resolved and passed to the dependent object:

```java
Object dependency = beanFactory.resolveDependency(descriptor, "greetingController");

GreetingController controller = new GreetingController((GreetingService) dependency);
```

The important distinction is that `resolveDependency()` **resolves** the dependency. The actual constructor invocation in this low-level example is performed explicitly:

```java
new GreetingController(greetingService);
```

Higher-level Spring bean creation builds on this dependency-resolution mechanism and performs the necessary injection as part of creating the bean.

---

## Complete Example

```java
public class DependencyInjectionApplication {

    public static void main(String[] args) throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        beanFactory.registerBeanDefinition("greetingService", new RootBeanDefinition(GreetingService.class));

        Constructor<GreetingController> constructor = GreetingController.class.getConstructor(GreetingService.class);

        MethodParameter methodParameter = new MethodParameter(constructor, 0);

        DependencyDescriptor descriptor = new DependencyDescriptor(methodParameter, true);

        Object dependency = beanFactory.resolveDependency(descriptor, "greetingController");

        GreetingController controller = new GreetingController((GreetingService) dependency);

        System.out.println(controller.greet());
    }
}
```

---

## Why `MethodParameter` Is Used

Java reflection provides `Constructor` and `Parameter` types, but Spring's dependency-resolution infrastructure uses its own `MethodParameter` abstraction.

For the constructor:

```java
public GreetingController(GreetingService greetingService)
```

the first parameter can be represented as:

```java
MethodParameter methodParameter = new MethodParameter(constructor, 0);
```

That Spring abstraction can then be wrapped in a `DependencyDescriptor`:

```java
DependencyDescriptor descriptor = new DependencyDescriptor(methodParameter, true);
```

This provides the bean factory with the information it needs to resolve the dependency.

---

## Dependencies

The example uses Spring Beans for `DefaultListableBeanFactory`, `DependencyDescriptor`, and related container infrastructure:

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-beans</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-core</artifactId>
</dependency>

<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
```

---

## Running the Example

From the example directory:

```bash
mvn test
```

To build the example:

```bash
mvn clean install
```

---

## Key Takeaways

- Dependency injection requires Spring to identify and resolve an injection point.
- `DependencyDescriptor` describes the dependency that needs to be resolved.
- `MethodParameter` represents the constructor parameter in this example.
- `DefaultListableBeanFactory` provides the dependency-resolution mechanism.
- `resolveDependency()` locates or creates a bean that satisfies the dependency.
- Dependency resolution and object construction are separate concepts.
- Higher-level Spring dependency injection builds on this underlying resolution mechanism.
- Understanding `DependencyDescriptor` and `resolveDependency()` provides a foundation for understanding how `@Autowired` works internally.

---

## Next

Continue with **How Component Scanning Works** to explore how Spring discovers components and turns them into bean definitions before dependency resolution takes place.