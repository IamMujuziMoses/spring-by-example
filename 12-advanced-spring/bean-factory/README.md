# BeanFactory

This example demonstrates Spring's `BeanFactory`, the foundational interface of the Spring IoC container.

The goal is to understand how Spring can manage and create beans at the basic container level, without using Spring Boot, component scanning, or `ApplicationContext`.

The example uses `DefaultListableBeanFactory` to manually register a bean definition and retrieve the resulting bean from the container.

---

## Learning Objectives

By completing this example, you should understand:

- What `BeanFactory` is.
- The role of `BeanFactory` in Spring's IoC container.
- How beans can be registered programmatically.
- What a `BeanDefinition` represents.
- How `BeanFactory` creates and manages beans.
- How to retrieve beans using `getBean()`.
- Spring's default singleton behavior.

---

## What Is BeanFactory?

`BeanFactory` is one of the core interfaces of the Spring Framework's IoC container.

It provides the basic functionality for managing and retrieving Spring beans.

At its simplest, a `BeanFactory` allows application code to ask the container for a bean:

```java
GreetingService greetingService = beanFactory.getBean(GreetingService.class);
```

Instead of creating the object directly:

```java
GreetingService greetingService = new GreetingService();
```

The container becomes responsible for creating and managing the object.

Conceptually:

```text
Without Spring

Application
    │
    └── new GreetingService()
            │
            ▼
      GreetingService


With BeanFactory

Application
    │
    │ getBean()
    ▼
BeanFactory
    │
    ▼
GreetingService
```

The important idea is that the application does not need to control the creation of the object itself. The container manages it.

---

## Why Does BeanFactory Exist?

One of Spring's core purposes is to provide an IoC container that manages application objects and their dependencies.

`BeanFactory` provides the fundamental container functionality needed to:

- Register bean definitions.
- Create beans.
- Manage bean instances.
- Resolve dependencies.
- Retrieve beans from the container.
- Apply Spring's bean lifecycle behavior.

It represents the basic foundation of Spring's bean management infrastructure.

---

## DefaultListableBeanFactory

`DefaultListableBeanFactory` is a concrete implementation of Spring's bean factory infrastructure.

It provides functionality for registering bean definitions and retrieving beans.

For this example, it allows us to see the basic container process explicitly:

```java
DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
```

At this point, we have an empty bean factory.

We can then register a bean definition:

```java
beanFactory.registerBeanDefinition("greetingService",new RootBeanDefinition(GreetingService.class));
```

The container now knows about a bean named `greetingService`.

We can retrieve it using:

```java
GreetingService greetingService = beanFactory.getBean(GreetingService.class);
```

The complete flow is:

```text
DefaultListableBeanFactory
          │
          │ registerBeanDefinition()
          ▼
   BeanDefinition
          │
          │ getBean()
          ▼
   GreetingService
```

---

## Creating a Bean

The example contains a simple `GreetingService`:

```java
public class GreetingService {

    public String greet() {
        return "Hello from BeanFactory!";
    }
}
```

There are no Spring annotations on this class.

This is intentional.

The example focuses on the container itself rather than component scanning or annotation-based configuration.

---

## Registering a Bean Definition

Before the `BeanFactory` can create the bean, it needs information describing how the bean should be created.

This information is represented by a `BeanDefinition`.

The example registers the definition like this:

```java
beanFactory.registerBeanDefinition("greetingService",new RootBeanDefinition(GreetingService.class));
```

The definition tells Spring that:

```text
Bean name:  greetingService
Bean class: GreetingService
```

The `BeanFactory` can then use this metadata when the application requests the bean.

---

## Retrieving a Bean

Once the definition has been registered, the bean can be retrieved:

```java
GreetingService greetingService = beanFactory.getBean(GreetingService.class);
```

The container is responsible for creating and returning the object.

It can also be retrieved by its bean name:

```java
GreetingService greetingService = beanFactory.getBean("greetingService", GreetingService.class);
```

Both approaches retrieve the Spring-managed bean.

---

## Bean Creation

One important characteristic of `BeanFactory` is that bean creation can be lazy.

Registering a bean definition does not necessarily mean that the actual object has already been created.

The container has the metadata describing how the bean should be created.

When the application requests the bean:

```java
beanFactory.getBean(GreetingService.class);
```

the factory can create the instance and return it.

Conceptually:

```text
registerBeanDefinition()
          │
          ▼
Bean definition stored
          │
          │
          │ Object may not exist yet
          │
          ▼
     getBean()
          │
          ▼
BeanFactory creates bean
          │
          ▼
GreetingService instance
```

This separation between a **bean definition** and a **bean instance** is an important concept when learning how the Spring container works internally.

---

## Singleton Beans

Spring beans are singleton-scoped by default.

For example:

```java
GreetingService first =
        beanFactory.getBean(GreetingService.class);

GreetingService second =
        beanFactory.getBean(GreetingService.class);
```

Both references point to the same bean instance:

```text
        BeanFactory
             │
       ┌─────┴─────┐
       │           │
    getBean()   getBean()
       │           │
       ▼           ▼
       └─────┬─────┘
             │
             ▼
      GreetingService
        (same instance)
```

The test verifies this behavior:

```java
assertEquals(first, second);
```

This is the default singleton behavior of the Spring bean container.

---

## Complete Example

The main example is intentionally small:

```java
public class BeanFactoryApplication {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        beanFactory.registerBeanDefinition("greetingService", new RootBeanDefinition(GreetingService.class));

        GreetingService greetingService = beanFactory.getBean(GreetingService.class);

        System.out.println(greetingService.greet());
    }
}
```

Running the application produces:

```text
Hello from BeanFactory!
```

---

## BeanFactory and Manual Object Creation

Without a container:

```java
GreetingService greetingService = new GreetingService();
```

The application directly controls object creation.

With `BeanFactory`:

```java
GreetingService greetingService = beanFactory.getBean(GreetingService.class);
```

The container controls the creation and management of the object.

| Manual Creation | BeanFactory |
|---|---|
| Application creates the object | Container creates the object |
| Uses `new` directly | Uses `getBean()` |
| No Spring container involved | Spring manages the bean |
| Dependencies must be managed manually | Container can manage dependencies |
| Object management is the application's responsibility | Object management is delegated to Spring |

---

## Dependencies

This example only requires the Spring Beans module and JUnit for testing.

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-beans</artifactId>
</dependency>

<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
```

There is no Spring Boot dependency because this example is intended to demonstrate the core bean factory directly.

---

## Running the Tests

From the `bean-factory` directory, run:

```bash
mvn clean install
```

To run only the tests:

```bash
mvn test
```

To run the application, run `BeanFactoryApplication` from your IDE or configured Maven execution environment.

---

## Key Takeaways

- `BeanFactory` is a foundational Spring IoC container interface.
- It provides the basic functionality for managing and retrieving Spring beans.
- Beans can be registered programmatically using bean definitions.
- A `BeanDefinition` describes metadata about how a bean should be created.
- `getBean()` retrieves a managed bean from the container.
- Bean creation can be lazy.
- Spring beans are singleton-scoped by default.
- `DefaultListableBeanFactory` provides concrete functionality for registering and managing bean definitions.
- A `BeanFactory` can be used without Spring Boot, component scanning, or `ApplicationContext`.

The main idea is:

```text
Bean Definition
       │
       ▼
   BeanFactory
       │
       │ getBean()
       ▼
 Managed Bean
```

This example focuses specifically on the fundamental bean factory concept. Other Spring container capabilities are explored in separate examples.

---

## Next

The next example explores `ApplicationContext` as a separate concept and examines the additional capabilities provided by the application context.