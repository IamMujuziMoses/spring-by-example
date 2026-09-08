# BeanDefinitionRegistry

This example demonstrates Spring's **`BeanDefinitionRegistry`**, the registry responsible for storing and managing `BeanDefinition` objects.

It builds on the previous `BeanDefinition` example. While a `BeanDefinition` describes how a bean should be created, a `BeanDefinitionRegistry` provides operations for registering, inspecting, and removing those definitions.

The example uses `DefaultListableBeanFactory`, which implements `BeanDefinitionRegistry`, to demonstrate programmatic bean definition management.

---

## Learning Objectives

By completing this example, you will understand:

- What `BeanDefinitionRegistry` is
- The relationship between `BeanDefinition` and `BeanDefinitionRegistry`
- How to register a `BeanDefinition`
- How to check whether a definition exists
- How to retrieve a registered `BeanDefinition`
- How to retrieve registered bean definition names
- How to remove a `BeanDefinition`
- How a registry fits into Spring's IoC infrastructure

---

## What Is BeanDefinitionRegistry?

`BeanDefinitionRegistry` is a Spring interface that defines operations for registering and managing bean definitions.

A simplified view is:

```text
BeanDefinitionRegistry
        │
        ├── Register definitions
        ├── Check definitions
        ├── Retrieve definitions
        ├── List definition names
        └── Remove definitions
```

The registry manages **bean definitions**, not the actual bean instances.

This distinction is important:

```text
BeanDefinition
    ↓
Describes a bean

BeanDefinitionRegistry
    ↓
Stores and manages the definition

BeanFactory
    ↓
Uses definitions to create and retrieve beans
```

---

## Why Does BeanDefinitionRegistry Exist?

Spring needs a mechanism for storing bean definitions before the container creates the corresponding objects.

For example, an application may define:

```text
Bean name:
    greetingService

Bean class:
    GreetingService

Constructor argument:
    "Hello!"
```

That information can be represented as a `BeanDefinition` and registered with a `BeanDefinitionRegistry`.

```text
              BeanDefinition
                    │
                    │ register
                    ▼
        BeanDefinitionRegistry
                    │
                    │ stores
                    ▼
          Registered definition
                    │
                    │ used by BeanFactory
                    ▼
             Bean instance
```

This separation allows Spring to build and manipulate its bean metadata before creating the actual objects.

---

## BeanDefinitionRegistry and BeanDefinition

The previous example focused on `BeanDefinition`.

A `BeanDefinition` answers:

> **How should this bean be created?**

A `BeanDefinitionRegistry` answers:

> **Where is this bean definition registered and managed?**

For example:

```java
RootBeanDefinition beanDefinition = new RootBeanDefinition(GreetingService.class);
```

creates the metadata.

Then:

```java
registry.registerBeanDefinition("greetingService",beanDefinition);
```

registers that metadata.

The relationship is:

```text
RootBeanDefinition
        │
        │ describes
        ▼
GreetingService
        ▲
        │
        │ registered in
        │
BeanDefinitionRegistry
```

---

## DefaultListableBeanFactory

`DefaultListableBeanFactory` is a commonly used concrete Spring container implementation.

It provides both bean factory functionality and bean definition registry functionality.

Conceptually:

```text
DefaultListableBeanFactory
        │
        ├── BeanFactory
        │      └── Create and retrieve beans
        │
        └── BeanDefinitionRegistry
               └── Register and manage definitions
```

This makes it useful for demonstrating how the two responsibilities work together.

In this example, the object is deliberately referenced through the `BeanDefinitionRegistry` interface:

```java
BeanDefinitionRegistry registry = beanFactory;
```

This makes the registry's specific responsibility explicit.

---

## Registering a BeanDefinition

A definition can be registered using:

```java
registry.registerBeanDefinition("greetingService",beanDefinition);
```

The first argument is the bean name.

The second argument is the `BeanDefinition`.

For example:

```java
RootBeanDefinition beanDefinition = new RootBeanDefinition(GreetingService.class);

registry.registerBeanDefinition("greetingService",beanDefinition);
```

After registration:

```text
BeanDefinitionRegistry
        │
        └── "greetingService"
                │
                ▼
        RootBeanDefinition
                │
                ▼
        GreetingService.class
```

---

## Checking for a BeanDefinition

The registry can determine whether a definition has been registered:

```java
boolean exists = registry.containsBeanDefinition("greetingService");
```

For example:

```java
assertTrue(registry.containsBeanDefinition("greetingService"));
```

This checks the registry's metadata rather than attempting to retrieve the actual bean.

---

## Retrieving a BeanDefinition

A registered definition can be retrieved by name:

```java
BeanDefinition definition = registry.getBeanDefinition("greetingService");
```

This returns the metadata stored under that bean name.

For example:

```java
assertSame(beanDefinition, registry.getBeanDefinition("greetingService"));
```

This demonstrates that the registry stores the actual `BeanDefinition` object that was registered.

---

## Getting BeanDefinition Names

The registry can also provide the names of registered definitions:

```java
String[] names = registry.getBeanDefinitionNames();
```

For example:

```java
assertArrayEquals(new String[]{"greetingService"},registry.getBeanDefinitionNames());
```

This is useful when inspecting or processing the bean metadata held by a container.

---

## Removing a BeanDefinition

A definition can be removed from the registry:

```java
registry.removeBeanDefinition("greetingService");
```

After removal:

```java
assertFalse(registry.containsBeanDefinition("greetingService"));
```

The operation removes the **bean definition** from the registry.

Conceptually:

```text
Before:

BeanDefinitionRegistry
        │
        └── greetingService
                │
                ▼
          BeanDefinition


removeBeanDefinition("greetingService")


After:

BeanDefinitionRegistry
        │
        └── no greetingService definition
```

---

## Complete Example

### GreetingService

```java
public class GreetingService {

    private final String message;

    public GreetingService(String message) {
        this.message = message;
    }

    public String greet() {
        return message;
    }
}
```

### BeanDefinitionRegistryApplication

```java
public class BeanDefinitionRegistryApplication {

    public static void main(String[] args) {

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        RootBeanDefinition beanDefinition = new RootBeanDefinition(GreetingService.class);

        beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, "Hello from BeanDefinitionRegistry!");
        ((BeanDefinitionRegistry) beanFactory).registerBeanDefinition("greetingService", beanDefinition);

        GreetingService greetingService = beanFactory.getBean("greetingService", GreetingService.class);

        System.out.println(greetingService.greet());
    }
}
```

The application produces:

```text
Hello from BeanDefinitionRegistry!
```

---

## Registry and Bean Creation

The registry itself does not represent the actual bean instance.

The complete process involves multiple responsibilities:

```text
              BeanDefinition
                    │
                    │ register
                    ▼
        BeanDefinitionRegistry
                    │
                    │ stores
                    ▼
          Registered definition
                    │
                    │ used by
                    ▼
               BeanFactory
                    │
                    │ getBean()
                    ▼
             Bean instance
```

This separation is fundamental to Spring's container infrastructure.

The registry manages the metadata.

The bean factory uses that metadata to create and manage objects.

---

## BeanDefinitionRegistry Operations

The main operations demonstrated in this example are:

| Operation | Purpose |
|---|---|
| `registerBeanDefinition()` | Registers a bean definition |
| `containsBeanDefinition()` | Checks whether a definition exists |
| `getBeanDefinition()` | Retrieves a registered definition |
| `getBeanDefinitionNames()` | Returns registered definition names |
| `removeBeanDefinition()` | Removes a definition |

These operations operate on **bean definitions**, not directly on bean instances.

---

## Dependencies

This example requires Spring Beans and JUnit.

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

---

## Running the Example

From the project root:

```bash
mvn clean install
```

To run the tests for this module:

```bash
mvn -pl 12-advanced-spring/bean-definition-registry clean test
```

---

## Key Takeaways

- `BeanDefinitionRegistry` manages Spring bean definitions.
- A `BeanDefinition` describes how a bean should be created.
- The registry stores those definitions under bean names.
- `registerBeanDefinition()` adds a definition to the registry.
- `containsBeanDefinition()` checks whether a definition exists.
- `getBeanDefinition()` retrieves registered metadata.
- `getBeanDefinitionNames()` lists registered definitions.
- `removeBeanDefinition()` removes a definition.
- `DefaultListableBeanFactory` provides both bean factory and bean definition registry functionality.
- The registry manages metadata; the `BeanFactory` uses that metadata to create and retrieve actual bean instances.

---

## Next

The next example focuses specifically on **ImportSelector**, demonstrating how Spring can programmatically determine which configuration classes should be imported into an application context.