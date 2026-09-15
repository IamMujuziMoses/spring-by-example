# How Beans Are Registered

This example demonstrates how Spring registers beans internally using a `BeanDefinition` and `DefaultListableBeanFactory`.

Rather than using annotations such as `@Component` or `@Bean`, the example explicitly creates a bean definition, registers it with the bean factory, and then retrieves the resulting bean.

The goal is to understand the distinction between **registering bean metadata** and **creating the actual bean instance**.

## Learning Objectives

- Understand how Spring represents bean metadata using `BeanDefinition`.
- Understand how bean definitions are registered with the container.
- Understand the role of `DefaultListableBeanFactory`.
- Understand the difference between bean registration and bean creation.
- Understand how a registered definition is used to create a bean.
- Understand the basic path from a bean definition to a bean instance.

---

## What Is a Bean Definition?

A `BeanDefinition` is metadata that describes how Spring should manage a bean.

It can contain information such as:

- The bean's class
- Constructor arguments
- Property values
- Scope
- Lazy initialization
- Autowiring information
- Initialization and destruction methods

For example:

```java
BeanDefinition beanDefinition = new RootBeanDefinition(GreetingService.class);
```

This creates metadata describing a `GreetingService` bean.

At this point, the `GreetingService` object has not necessarily been created.

The definition describes the bean that the container should manage.

---

## Registering a Bean Definition

Spring provides `BeanDefinitionRegistry` for registering bean definitions.

`DefaultListableBeanFactory` implements this interface and can therefore be used to register definitions directly:

```java
DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

RootBeanDefinition beanDefinition = new RootBeanDefinition(GreetingService.class);

beanFactory.registerBeanDefinition("greetingService",beanDefinition);
```

The name:

```text
greetingService
```

becomes the bean's name within the container.

The registry now knows that a bean with this name exists and has the supplied definition.

---

## Registration vs Bean Creation

An important concept in Spring is that **registering a bean definition and creating a bean are separate operations**.

The process can be simplified as:

```text
Bean class
    │
    ▼
BeanDefinition
    │
    ▼
registerBeanDefinition(...)
    │
    ▼
BeanDefinitionRegistry
    │
    ▼
DefaultListableBeanFactory
    │
    ▼
getBean(...)
    │
    ▼
Bean instance
```

The `BeanDefinition` provides the metadata Spring needs to create and manage the bean.

When the bean is requested, the bean factory uses that metadata to create and return the instance.

---

## Checking Registration

The bean factory can determine whether a bean definition has been registered:

```java
boolean registered = beanFactory.containsBeanDefinition("greetingService");
```

This checks the registry rather than retrieving the bean itself.

For example:

```java
assertTrue(beanFactory.containsBeanDefinition("greetingService"));
```

This demonstrates that the bean definition exists in the container.

---

## Retrieving the Bean

Once the definition has been registered, the bean can be retrieved:

```java
GreetingService greetingService = beanFactory.getBean("greetingService", GreetingService.class);
```

The bean factory uses the registered definition to create and return the `GreetingService` instance.

The resulting object can then be used normally:

```java
System.out.println(greetingService.greet());
```

---

## Complete Example

```java
DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

RootBeanDefinition beanDefinition = new RootBeanDefinition(GreetingService.class);

beanFactory.registerBeanDefinition("greetingService",beanDefinition);

GreetingService greetingService = beanFactory.getBean("greetingService", GreetingService.class);

System.out.println(greetingService.greet());
```

This small example demonstrates the fundamental flow used by the Spring container:

```text
1. Create BeanDefinition
2. Register BeanDefinition
3. BeanFactory stores the definition
4. Request the bean
5. BeanFactory creates the bean
6. Return the bean instance
```

---

## Why This Matters

Understanding bean registration is important because many higher-level Spring features eventually result in bean definitions being registered with the container.

For example, Spring can discover beans through:

- `@Component` scanning
- `@Bean` methods
- `@Import`
- `ImportSelector`
- `ImportBeanDefinitionRegistrar`
- XML configuration
- Programmatic registration

Although these mechanisms look different, they ultimately contribute bean definitions to the Spring container.

Understanding `BeanDefinition` and bean definition registration therefore provides a foundation for understanding many of Spring's higher-level configuration mechanisms.

---

## Dependencies

The example uses Spring Beans:

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

From the module directory:

```bash
mvn test
```

To build the example:

```bash
mvn clean install
```

---

## Key Takeaways

- A Spring bean is represented by a `BeanDefinition` before the actual bean instance is created.
- `BeanDefinition` contains metadata describing how Spring should manage the bean.
- `BeanDefinitionRegistry` is responsible for storing bean definitions.
- `DefaultListableBeanFactory` provides the core registry and bean factory functionality.
- `registerBeanDefinition()` registers bean metadata with the container.
- `containsBeanDefinition()` can be used to verify registration.
- `getBean()` retrieves the managed bean instance.
- Bean registration and bean creation are separate stages.
- Many higher-level Spring configuration mechanisms eventually result in bean definitions being registered.

---

## Next

Continue with **How Dependency Injection Works** to explore how Spring resolves dependencies between registered beans.