# ImportBeanDefinitionRegistrar

This example demonstrates Spring's `ImportBeanDefinitionRegistrar`, which allows bean definitions to be registered programmatically with Spring's `BeanDefinitionRegistry`.

Unlike `ImportSelector`, which selects configuration classes for import, `ImportBeanDefinitionRegistrar` works directly with the bean definition registry and gives application code the ability to register `BeanDefinition` objects programmatically.

---

## Learning Objectives

- Understand what `ImportBeanDefinitionRegistrar` is
- Understand how it works with `@Import`
- Implement a custom `ImportBeanDefinitionRegistrar`
- Understand the role of `BeanDefinitionRegistry`
- Programmatically create and register a `BeanDefinition`
- Understand the difference between registering a bean definition and creating a bean instance
- Understand how Spring makes the registered bean available from the `ApplicationContext`
- Distinguish `ImportBeanDefinitionRegistrar` from `ImportSelector`

---

## What Is ImportBeanDefinitionRegistrar?

`ImportBeanDefinitionRegistrar` is a Spring interface that allows additional bean definitions to be registered programmatically during configuration processing.

It provides the following method:

```java
void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry);
```

The registrar receives:

- `AnnotationMetadata` describing the configuration class that triggered the import
- `BeanDefinitionRegistry` used to register bean definitions

A simple implementation looks like this:

```java
public class GreetingRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        registry.registerBeanDefinition("greetingService",
                BeanDefinitionBuilder.rootBeanDefinition(GreetingService.class).getBeanDefinition());
    }
}
```

The important part is that the registrar does not directly create the `GreetingService`.

Instead, it creates a `BeanDefinition` describing the bean and registers that definition with Spring.

---

## Why Does ImportBeanDefinitionRegistrar Exist?

Spring normally allows beans to be declared through mechanisms such as `@Bean`:

```java
@Configuration
public class GreetingConfiguration {

    @Bean
    public GreetingService greetingService() {
        return new GreetingService();
    }
}
```

However, Spring's configuration infrastructure sometimes needs to register beans programmatically.

`ImportBeanDefinitionRegistrar` provides a mechanism for doing exactly that.

The registrar can construct bean definitions and add them directly to Spring's `BeanDefinitionRegistry`.

Conceptually:

```text
Configuration
      │
      │ @Import
      ▼
ImportBeanDefinitionRegistrar
      │
      │ creates BeanDefinition
      ▼
BeanDefinitionRegistry
      │
      │ stores definition
      ▼
ApplicationContext
      │
      │ creates bean when needed
      ▼
GreetingService
```

This gives framework and library code a way to participate directly in Spring's bean registration process.

---

## How ImportBeanDefinitionRegistrar Works

The basic process is:

```text
1. Spring processes ApplicationConfig
            │
            ▼
2. Spring encounters @Import(GreetingRegistrar.class)
            │
            ▼
3. Spring processes GreetingRegistrar
            │
            ▼
4. Spring calls registerBeanDefinitions(...)
            │
            ▼
5. Registrar creates a BeanDefinition
            │
            ▼
6. Registrar registers the definition
            │
            ▼
7. BeanDefinitionRegistry stores the definition
            │
            ▼
8. ApplicationContext can create and retrieve the bean
```

The central operation is:

```java
registry.registerBeanDefinition(...)
```

---

## BeanDefinitionRegistry

The registrar receives a `BeanDefinitionRegistry`:

```java
BeanDefinitionRegistry registry
```

This is the registry into which bean definitions can be registered.

The registry provides operations such as:

```java
registry.registerBeanDefinition(...);
registry.containsBeanDefinition(...);
registry.getBeanDefinition(...);
registry.getBeanDefinitionNames();
registry.removeBeanDefinition(...);
```

The `ImportBeanDefinitionRegistrar` example primarily uses:

```java
registry.registerBeanDefinition(...)
```

to add a new bean definition.

The important distinction is:

```text
BeanDefinition
    │
    └── Describes a bean

BeanDefinitionRegistry
    │
    └── Stores/manages bean definitions

BeanFactory / ApplicationContext
    │
    └── Uses definitions to create/retrieve beans
```

---

## Example

### GreetingService

The example contains a simple service:

```java
public class GreetingService {

    public String greet() {
        return "Hello from ImportBeanDefinitionRegistrar!";
    }
}
```

There are no Spring annotations on the class.

The bean will instead be registered programmatically by the registrar.

### GreetingRegistrar

The registrar implements `ImportBeanDefinitionRegistrar`:

```java
public class GreetingRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        registry.registerBeanDefinition("greetingService",
                BeanDefinitionBuilder.rootBeanDefinition(GreetingService.class).getBeanDefinition());
    }
}
```

The registrar creates a `RootBeanDefinition` through `BeanDefinitionBuilder`:

```java
BeanDefinitionBuilder.rootBeanDefinition(GreetingService.class).getBeanDefinition()
```

This produces a `BeanDefinition` describing a bean whose class is:

```java
GreetingService.class
```

The definition is then registered under the name:

```text
greetingService
```

using:

```java
registry.registerBeanDefinition(...)
```

---

## Registering the Bean Definition

The key operation is:

```java
registry.registerBeanDefinition("greetingService",
        BeanDefinitionBuilder.rootBeanDefinition(GreetingService.class).getBeanDefinition());
```

There are two important pieces here.

### Bean Name

The first argument is the bean name:

```java
"greetingService"
```

This is the name under which the definition is registered.

### Bean Definition

The second argument is the `BeanDefinition`:

```java
BeanDefinitionBuilder.rootBeanDefinition(GreetingService.class).getBeanDefinition()
```

It describes how Spring should create the bean.

At this point, the registrar has registered **metadata**, not directly created the application object.

---

## ApplicationConfig

The registrar is activated using `@Import`:

```java
@Configuration
@Import(GreetingRegistrar.class)
public class ApplicationConfig {
}
```

This tells Spring to process `GreetingRegistrar` as part of configuration processing.

The important distinction is that `ApplicationConfig` does not contain:

```java
@Bean
public GreetingService greetingService() {
    return new GreetingService();
}
```

Instead, it imports the registrar:

```java
@Import(GreetingRegistrar.class)
```

The registrar is then responsible for registering the bean definition.

---

## Creating the ApplicationContext

The application creates an `AnnotationConfigApplicationContext`:

```java
try (AnnotationConfigApplicationContext applicationContext =
             new AnnotationConfigApplicationContext(ApplicationConfig.class)) {

    GreetingService greetingService = applicationContext.getBean(GreetingService.class);

    System.out.println(greetingService.greet());
}
```

During configuration processing, Spring discovers the imported registrar.

The registrar registers the `GreetingService` bean definition.

The application context can then retrieve the resulting bean:

```java
GreetingService greetingService = applicationContext.getBean(GreetingService.class);
```

The output is:

```text
Hello from ImportBeanDefinitionRegistrar!
```

---

## Complete Registration Flow

The complete process can be visualized as:

```text
ApplicationConfig
       │
       │ @Import(GreetingRegistrar.class)
       ▼
GreetingRegistrar
       │
       │ registerBeanDefinitions(...)
       ▼
BeanDefinitionBuilder
       │
       │ creates BeanDefinition
       ▼
BeanDefinitionRegistry
       │
       │ registerBeanDefinition(...)
       ▼
greetingService BeanDefinition
       │
       ▼
ApplicationContext
       │
       │ getBean(...)
       ▼
GreetingService instance
```

This illustrates the distinction between **bean definition registration** and **bean creation**.

---

## ImportBeanDefinitionRegistrar vs ImportSelector

`ImportSelector` and `ImportBeanDefinitionRegistrar` are both configuration import mechanisms, but they operate differently.

### ImportSelector

An `ImportSelector` returns configuration class names:

```java
@Override
public String[] selectImports(AnnotationMetadata importingClassMetadata) {
    return new String[] {
            GreetingConfiguration.class.getName()
    };
}
```

Spring then processes the selected configuration classes.

```text
ImportSelector
      │
      │ selects
      ▼
Configuration Class
      │
      ▼
@Bean methods
      │
      ▼
Bean Definitions
```

### ImportBeanDefinitionRegistrar

An `ImportBeanDefinitionRegistrar` receives the registry directly:

```java
@Override
public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

    registry.registerBeanDefinition(...);
}
```

The registrar can directly add bean definitions.

```text
ImportBeanDefinitionRegistrar
      │
      │ registers
      ▼
BeanDefinitionRegistry
      │
      ▼
Bean Definition
      │
      ▼
ApplicationContext
```

The distinction can be summarized as:

| Mechanism | Main responsibility |
|---|---|
| `ImportSelector` | Selects configuration classes to import |
| `DeferredImportSelector` | Selects configuration classes during deferred processing |
| `ImportBeanDefinitionRegistrar` | Directly registers bean definitions |

---

## ImportBeanDefinitionRegistrar vs BeanDefinitionRegistry

These two concepts are closely related but have different responsibilities.

`ImportBeanDefinitionRegistrar` is the component performing the registration:

```text
ImportBeanDefinitionRegistrar
        │
        │ uses
        ▼
BeanDefinitionRegistry
```

The `BeanDefinitionRegistry` is the registry receiving the definitions:

```text
BeanDefinitionRegistry
        │
        ├── registerBeanDefinition()
        ├── getBeanDefinition()
        ├── containsBeanDefinition()
        ├── getBeanDefinitionNames()
        └── removeBeanDefinition()
```

The registrar therefore uses the registry rather than replacing it.

---

## The Role of AnnotationMetadata

The registrar also receives:

```java
AnnotationMetadata importingClassMetadata
```

This provides metadata about the configuration class that imported the registrar.

The current example does not use this metadata because it always registers the same bean.

However, it allows more advanced registrars to inspect the importing configuration and make registration decisions based on its annotations or other metadata.

Conceptually:

```text
Importing Configuration
        │
        │ metadata
        ▼
AnnotationMetadata
        │
        ▼
ImportBeanDefinitionRegistrar
        │
        │ registration decision
        ▼
BeanDefinitionRegistry
```

---

## Bean Definition vs Bean Instance

One of the most important concepts demonstrated by this example is the difference between a bean definition and a bean instance.

The registrar creates and registers:

```text
BeanDefinition
```

not:

```text
GreetingService instance
```

The relationship is:

```text
BeanDefinition
      │
      │ describes
      ▼
How Spring should create the bean
      │
      ▼
BeanFactory / ApplicationContext
      │
      │ creates
      ▼
GreetingService instance
```

This is why the registrar can participate in Spring's bean registration infrastructure without directly managing the lifecycle of the resulting object.

---

## Class Responsibilities

| Class | Responsibility |
|---|---|
| `GreetingService` | Provides the example service |
| `GreetingRegistrar` | Programmatically registers the `GreetingService` bean definition |
| `ApplicationConfig` | Entry-point configuration that imports the registrar |
| `ImportBeanDefinitionRegistrarApplication` | Starts the application context and retrieves the service |
| `ImportBeanDefinitionRegistrarApplicationTest` | Verifies the registered bean |

---

## Dependencies

The example requires Spring's context module and JUnit for testing.

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
</dependency>

<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
```

`spring-context` provides the configuration infrastructure used by the example, including:

- `@Configuration`
- `@Bean`
- `@Import`
- `AnnotationConfigApplicationContext`
- `ImportBeanDefinitionRegistrar`
- `BeanDefinitionRegistry`
- `BeanDefinitionBuilder`
- `AnnotationMetadata`

---

## Running the Example

From the project root, run:

```bash
mvn clean install
```

To run only this module:

```bash
mvn -pl 12-advanced-spring/import-bean-definition-registrar clean install
```

---

## Key Takeaways

- `ImportBeanDefinitionRegistrar` allows bean definitions to be registered programmatically.
- It is activated through `@Import`.
- Spring provides the registrar with an `AnnotationMetadata` object and a `BeanDefinitionRegistry`.
- The registrar uses the registry to register `BeanDefinition` objects.
- `BeanDefinitionBuilder` provides a convenient way to create bean definitions.
- Registering a `BeanDefinition` is different from directly creating a bean instance.
- The `ApplicationContext` uses the registered definition to create and provide the bean.
- `ImportSelector` selects configuration classes, while `ImportBeanDefinitionRegistrar` can directly register bean definitions.
- `BeanDefinitionRegistry` is responsible for storing and managing bean definitions.
- `ImportBeanDefinitionRegistrar` is useful when configuration needs direct programmatic control over bean registration.

---

## Next

The next example covers **Environment**, demonstrating how Spring exposes application environment information such as profiles and configuration properties.