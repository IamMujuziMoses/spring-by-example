# OpenmrsBeanRegistrar

This example demonstrates **programmatic Spring bean registration** using an OpenMRS-style bean registrar.

Instead of relying on annotations such as `@Component` or `@Service`, the example creates a `BeanDefinition` programmatically and registers it with Spring's `BeanDefinitionRegistry`.

This connects the OpenMRS integration topics in Module 14 with the Spring internals explored in Module 13, particularly `BeanDefinition`, `BeanDefinitionRegistry`, and `DefaultListableBeanFactory`.

> **Note:** `OpenmrsBeanRegistrar` in this example is a simplified learning abstraction. It demonstrates the underlying Spring bean-registration mechanism rather than reproducing a specific current OpenMRS core class.

---

## Learning Objectives

By completing this example, you will learn:

- How Spring beans can be registered programmatically.
- What a `BeanDefinitionRegistry` does.
- How `RootBeanDefinition` describes a bean.
- How `registerBeanDefinition()` adds a bean to the container.
- How `DefaultListableBeanFactory` stores and creates registered beans.
- How programmatic registration differs from annotation-based registration.
- How these mechanisms relate to OpenMRS-style Spring integration.

---

## What Is Programmatic Bean Registration?

Spring does not require every bean to be declared using annotations or XML.

A bean can also be registered directly with Spring's `BeanDefinitionRegistry`.

The general process is:

```text
Bean Class
    │
    ▼
RootBeanDefinition
    │
    ▼
BeanDefinitionRegistry
    │
    ▼
registerBeanDefinition()
    │
    ▼
DefaultListableBeanFactory
    │
    ▼
getBean()
    │
    ▼
Managed Object
```

This is useful when an application needs to dynamically or programmatically add components to the Spring container.

---

## The Service Interface

The example begins with a simple service interface:

```java
public interface GreetingService {

    String greet();
}
```

The interface represents the service that will eventually be managed by Spring.

---

## The Service Implementation

The implementation does not use `@Service` or `@Component`.

```java
public class GreetingServiceImpl implements GreetingService {

    @Override
    public String greet() {
        return "Hello from an OpenMRS registered component!";
    }
}
```

This is intentional.

The purpose of this example is to demonstrate how the class can become a Spring-managed bean **without annotation-based component scanning**.

---

## Creating a BeanDefinition

Spring needs metadata describing how a bean should be created.

A `RootBeanDefinition` can be used for this:

```java
BeanDefinition beanDefinition = new RootBeanDefinition(GreetingServiceImpl.class);
```

The definition tells Spring:

> "There is a bean whose implementation class is `GreetingServiceImpl`."

At this point, the object has **not necessarily been created yet**.

The `BeanDefinition` is metadata describing the bean.

---

## Registering the Bean

The definition can then be registered with a `BeanDefinitionRegistry`:

```java
registry.registerBeanDefinition("greetingService",beanDefinition);
```

The first argument is the bean name:

```text
greetingService
```

The second argument contains the metadata describing how Spring should create the bean.

After registration, the Spring container knows about the bean.

---

## OpenmrsBeanRegistrar

The example wraps this registration process in a small registrar class:

```java
public class OpenmrsBeanRegistrar {

    public void registerBeans(BeanDefinitionRegistry registry) {
        BeanDefinition beanDefinition = new RootBeanDefinition(GreetingServiceImpl.class);

        registry.registerBeanDefinition("greetingService", beanDefinition);
    }
}
```

The registrar receives a `BeanDefinitionRegistry` rather than creating a specific `DefaultListableBeanFactory` itself.

This keeps the registrar focused on one responsibility:

> Register the required bean definitions.

---

## Creating the Spring BeanFactory

The example uses `DefaultListableBeanFactory` as the container:

```java
DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
```

`DefaultListableBeanFactory` implements `BeanDefinitionRegistry`, so it can accept bean definitions directly.

The registrar can therefore register the service:

```java
OpenmrsBeanRegistrar registrar = new OpenmrsBeanRegistrar();
registrar.registerBeans(beanFactory);
```

---

## Retrieving the Registered Bean

Once the bean definition has been registered, the bean can be retrieved:

```java
GreetingService greetingService = beanFactory.getBean("greetingService", GreetingService.class);
```

Spring creates the object according to the registered definition and returns it.

The service can then be used normally:

```java
System.out.println(greetingService.greet());
```

Output:

```text
Hello from an OpenMRS registered component!
```

---

## Complete Example

The application brings the pieces together:

```java
public class OpenmrsBeanRegistrarApplication {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        OpenmrsBeanRegistrar registrar = new OpenmrsBeanRegistrar();
        registrar.registerBeans(beanFactory);

        GreetingService greetingService = beanFactory.getBean("greetingService", GreetingService.class);

        System.out.println(greetingService.greet());
    }
}
```

The important sequence is:

```text
Create BeanFactory
       │
       ▼
Create Registrar
       │
       ▼
Create BeanDefinition
       │
       ▼
Register BeanDefinition
       │
       ▼
Request Bean
       │
       ▼
Spring Creates Bean
       │
       ▼
Use Bean
```

---

## Bean Registration vs Bean Creation

One of the most important concepts demonstrated by this example is that **registration and creation are separate operations**.

When this executes:

```java
registry.registerBeanDefinition("greetingService",beanDefinition);
```

Spring receives the bean definition.

The definition contains metadata such as:

- Bean name
- Bean class
- Scope
- Dependencies
- Lifecycle information

The actual object can then be created when requested from the container.

```java
beanFactory.getBean("greetingService");
```

This distinction is important when understanding Spring's internal architecture.

---

## Why Use a Registrar?

A registrar can centralize the process of adding multiple related beans.

For example, a registrar could eventually register:

```text
Service A
Service B
Service C
Repository A
Repository B
```

Instead of scattering registration logic throughout an application, the registration process can be encapsulated in one component.

This is particularly useful in systems where components need to be added dynamically or where framework infrastructure needs to integrate application-specific components into the Spring container.

---

## Comparison with Other Registration Mechanisms

### `@Component`

With component scanning:

```java
@Component
public class GreetingServiceImpl {
}
```

Spring discovers the class during scanning and registers it.

### `@Bean`

With Java configuration:

```java
@Configuration
public class AppConfig {

    @Bean
    public GreetingService greetingService() {
        return new GreetingServiceImpl();
    }
}
```

Spring processes the configuration class and registers the resulting bean.

### XML

With XML:

```xml
<bean id="greetingService" class="com.springbyexample.openmrsbeanregistrar.GreetingServiceImpl"/>
```

Spring reads the XML configuration and registers the bean.

### Programmatic Registration

With this example:

```java
BeanDefinition definition = new RootBeanDefinition(GreetingServiceImpl.class);

registry.registerBeanDefinition("greetingService",definition);
```

The application directly interacts with Spring's bean-definition infrastructure.

---

## Relationship to OpenMRS

OpenMRS uses Spring as part of its application infrastructure, including Spring-managed services and components.

The OpenMRS `ServiceContext` provides access to registered services/components and ultimately works with the Spring `ApplicationContext` to retrieve registered components.

This example focuses on a lower-level mechanism underneath that broader integration:

```text
OpenMRS Integration
       │
       ▼
Spring ApplicationContext
       │
       ▼
Bean Definitions
       │
       ▼
BeanDefinitionRegistry
       │
       ▼
Spring-managed Beans
```

The goal is not to reproduce the complete OpenMRS runtime, but to make the underlying Spring registration mechanism understandable.

---

## Dependencies

The example requires:

- Spring Beans
- JUnit Jupiter

The Spring dependency provides:

- `BeanDefinition`
- `RootBeanDefinition`
- `BeanDefinitionRegistry`
- `DefaultListableBeanFactory`

---

## Running the Example

From the project root:

```bash
mvn -pl 14-openmrs-examples/openmrs-bean-registrar clean install
```

Run the application from your IDE or execute the compiled application using your preferred Java configuration.

Expected output:

```text
Hello from an OpenMRS registered component!
```

---

## Key Takeaways

- Spring beans can be registered without annotations or XML.
- `BeanDefinition` describes how a bean should be created.
- `RootBeanDefinition` provides a concrete bean definition.
- `BeanDefinitionRegistry` stores bean definitions.
- `DefaultListableBeanFactory` provides both bean-definition registration and bean retrieval.
- Registering a bean definition and creating the bean are separate concepts.
- A registrar can encapsulate programmatic bean registration.
- This example connects the Spring internals from Module 13 with OpenMRS-style Spring integration.

---

## Next

The next example explores: _ServiceContext_

The **ServiceContext** example will demonstrate how OpenMRS provides access to Spring-managed services and how that service layer interacts with the underlying Spring `ApplicationContext`.