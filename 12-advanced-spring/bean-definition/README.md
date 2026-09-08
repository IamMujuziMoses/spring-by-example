# BeanDefinition

This example demonstrates Spring's **`BeanDefinition`** mechanism and how Spring represents the metadata needed to create and manage a bean.

A `BeanDefinition` does not represent the actual bean instance. Instead, it describes how Spring should create and configure the bean.

The example uses `RootBeanDefinition` with `DefaultListableBeanFactory` to register metadata for a `GreetingService` and then lets Spring create the actual bean from that metadata.

---

## Learning Objectives

By completing this example, you will understand:

- What `BeanDefinition` is
- The difference between a `BeanDefinition` and a bean instance
- How Spring uses bean metadata during bean creation
- How to create a `RootBeanDefinition`
- How to specify a bean class
- How to configure constructor arguments in a `BeanDefinition`
- How to register a `BeanDefinition` with a bean factory
- How Spring creates a bean from its definition

---

## What Is BeanDefinition?

A `BeanDefinition` is metadata that describes a bean managed by the Spring container.

It can contain information such as:

- The bean's class
- Constructor arguments
- Property values
- Scope
- Lazy initialization
- Autowiring information
- Dependencies
- Initialization and destruction methods

A simplified representation is:

```text
BeanDefinition
      │
      ├── Bean class
      ├── Constructor arguments
      ├── Properties
      ├── Scope
      ├── Dependencies
      └── Lifecycle configuration
```

The important distinction is:

```text
BeanDefinition
    =
Metadata describing a bean

Bean
    =
Actual object created from that metadata
```

---

## Why Does BeanDefinition Exist?

Spring needs a way to describe beans before creating their actual instances.

For example, instead of immediately creating:

```java
new GreetingService("Hello!");
```

Spring can first store metadata describing what should be created:

```text
Bean name:
    greetingService

Bean class:
    GreetingService

Constructor argument:
    "Hello!"
```

That metadata becomes a `BeanDefinition`.

Later, the Spring container uses the definition to create the actual object.

```text
                BeanDefinition
                      │
                      │ describes
                      ▼
               GreetingService
                      │
                      │ Spring creates
                      ▼
              Bean instance
```

This separation between **metadata** and **instances** is fundamental to Spring's IoC container.

---

## RootBeanDefinition

`RootBeanDefinition` is a concrete `BeanDefinition` implementation commonly used when programmatically describing a bean.

For example:

```java
RootBeanDefinition beanDefinition = new RootBeanDefinition(GreetingService.class);
```

This tells Spring that the bean should be created from:

```text
GreetingService.class
```

The definition itself is not the `GreetingService`.

It is the instructions Spring uses to create one.

---

## Defining the Bean Class

The simplest definition specifies the bean's class:

```java
RootBeanDefinition beanDefinition = new RootBeanDefinition(GreetingService.class);
```

Conceptually:

```text
RootBeanDefinition
        │
        └── beanClass
              │
              ▼
       GreetingService.class
```

Spring can use this information when it eventually needs to instantiate the bean.

---

## Constructor Arguments

A `BeanDefinition` can also describe constructor arguments.

The `GreetingService` in this example has a constructor:

```java
public GreetingService(String message) {
    this.message = message;
}
```

The constructor argument can be added to the definition:

```java
beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0,"Hello from BeanDefinition!");
```

The definition now contains:

```text
BeanDefinition
      │
      ├── Bean class
      │      └── GreetingService
      │
      └── Constructor argument
             └── "Hello from BeanDefinition!"
```

Spring uses this metadata when creating the bean.

---

## Registering the Definition

The definition must be registered with a Spring bean factory before it can be used.

```java
beanFactory.registerBeanDefinition("greetingService",beanDefinition);
```

The relationship is:

```text
DefaultListableBeanFactory
          │
          │ registers
          ▼
    BeanDefinition
          │
          │ describes
          ▼
    GreetingService
```

The bean factory now knows that the bean named `greetingService` should be created according to the supplied definition.

---

## Retrieving the Bean

Once the definition has been registered, the bean can be retrieved:

```java
GreetingService greetingService = beanFactory.getBean("greetingService", GreetingService.class);
```

At this point, Spring uses the metadata from the `BeanDefinition` to create and return the actual bean instance.

```text
registerBeanDefinition()
          │
          ▼
   BeanDefinition
          │
          │ getBean()
          ▼
   Bean creation
          │
          ▼
GreetingService instance
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

### BeanDefinitionApplication

```java
public class BeanDefinitionApplication {

    public static void main(String[] args) {

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        RootBeanDefinition beanDefinition = new RootBeanDefinition(GreetingService.class);

        beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, "Hello from BeanDefinition!");
        beanFactory.registerBeanDefinition("greetingService", beanDefinition);

        GreetingService greetingService = beanFactory.getBean("greetingService", GreetingService.class);

        System.out.println(greetingService.greet());
    }
}
```

The application produces:

```text
Hello from BeanDefinition!
```

---

## BeanDefinition vs Bean Instance

It is important not to confuse the definition with the object it describes.

Consider:

```java
RootBeanDefinition beanDefinition = new RootBeanDefinition(GreetingService.class);
```

At this point:

```text
beanDefinition
      │
      ▼
Metadata
```

There is not yet a `GreetingService` reference in the application.

When:

```java
GreetingService greetingService = beanFactory.getBean(GreetingService.class);
```

is called, Spring uses the definition to obtain the actual object:

```text
BeanDefinition
      │
      │ used by container
      ▼
Bean creation
      │
      ▼
GreetingService instance
```

This distinction becomes increasingly important when working with Spring's internal container infrastructure.

---

## BeanDefinition and BeanFactory

The previous example introduced `BeanFactory`.

The two concepts have different responsibilities:

```text
BeanFactory
    │
    ├── Stores/manages bean definitions
    ├── Creates beans
    └── Returns bean instances
```

while:

```text
BeanDefinition
    │
    └── Describes how a bean should be created
```

A simplified relationship is:

```text
             BeanFactory
                  │
                  │ uses
                  ▼
            BeanDefinition
                  │
                  │ describes
                  ▼
             Bean instance
```

The `BeanFactory` is the container.

The `BeanDefinition` is metadata used by the container.

---

## BeanDefinition Lifecycle

A simplified view of the process is:

```text
Create BeanDefinition
        │
        ▼
Configure metadata
        │
        ▼
Register definition
        │
        ▼
BeanFactory stores definition
        │
        ▼
Application requests bean
        │
        ▼
Spring reads definition
        │
        ▼
Spring creates bean
        │
        ▼
Bean instance returned
```

This is one of the core mechanisms behind Spring's IoC container.

Higher-level configuration mechanisms such as `@Bean`, component scanning, and XML configuration ultimately result in bean metadata that the container can use to create and manage objects.

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
mvn -pl 12-advanced-spring/bean-definition clean test
```

---

## Key Takeaways

- `BeanDefinition` is metadata describing a Spring-managed bean.
- A `BeanDefinition` is not the actual bean instance.
- A definition can describe the bean class, constructor arguments, scope, properties, and other configuration.
- `RootBeanDefinition` is a concrete way to create bean metadata programmatically.
- `DefaultListableBeanFactory` can register and use bean definitions.
- Spring uses the definition when it needs to create the actual bean.
- Bean definitions allow Spring to separate bean configuration from bean instantiation.
- Higher-level Spring configuration mechanisms ultimately contribute bean metadata to the container.

---

## Next

The next example focuses specifically on **BeanDefinitionRegistry**, the registry responsible for storing and managing `BeanDefinition` objects.