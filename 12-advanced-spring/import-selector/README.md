# ImportSelector

This example demonstrates Spring's `ImportSelector`, which allows configuration classes to be selected programmatically during configuration processing.

Rather than directly importing a configuration class with `@Import`, an `ImportSelector` can determine which configuration classes Spring should import.

---

## Learning Objectives

- Understand what `ImportSelector` is
- Understand how `ImportSelector` works with `@Import`
- Implement a custom `ImportSelector`
- Understand the role of `AnnotationMetadata`
- Programmatically select configuration classes
- Understand how selected configuration classes become part of the Spring application context
- Distinguish `ImportSelector` from directly importing configuration classes

---

## What Is ImportSelector?

`ImportSelector` is a Spring interface that allows an application to **programmatically select configuration classes to import**.

It is part of Spring's configuration infrastructure and is commonly used when the configuration that should be imported depends on some condition, metadata, or other programmatic decision.

The interface provides the following method:

```java
String[] selectImports(AnnotationMetadata importingClassMetadata);
```

The returned array contains the fully qualified class names of configuration classes that Spring should import.

For example:

```java
public class GreetingImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[] {
                GreetingConfiguration.class.getName()
        };
    }
}
```

Spring takes the returned class names and processes those classes as imported configuration classes.

---

## Why Does ImportSelector Exist?

Spring applications often need to include different configuration depending on some programmatic decision.

Without an `ImportSelector`, configuration can be imported directly:

```java
@Configuration
@Import(GreetingConfiguration.class)
public class ApplicationConfig {
}
```

This works well when the configuration is known ahead of time.

However, sometimes the configuration that should be imported needs to be determined programmatically.

`ImportSelector` provides a mechanism for doing this:

```text
Application Configuration
        │
        ▼
ImportSelector
        │
        │ decides what to import
        ▼
Selected Configuration
        │
        ▼
Spring ApplicationContext
```

This makes `ImportSelector` useful for building flexible configuration systems and reusable Spring infrastructure.

---

## How ImportSelector Works

The basic process is:

```text
1. Spring processes ApplicationConfig
            │
            ▼
2. Spring encounters @Import(GreetingImportSelector.class)
            │
            ▼
3. Spring processes the ImportSelector
            │
            ▼
4. Spring calls selectImports(...)
            │
            ▼
5. ImportSelector returns configuration class names
            │
            ▼
6. Spring imports those configuration classes
            │
            ▼
7. Beans declared by those configurations are registered
```

In this example:

```text
ApplicationConfig
       │
       │ @Import
       ▼
GreetingImportSelector
       │
       │ selectImports()
       ▼
GreetingConfiguration
       │
       │ @Bean
       ▼
GreetingService
```

---

## ImportSelector Interface

The example implements:

```java
public interface ImportSelector {

    String[] selectImports(
            AnnotationMetadata importingClassMetadata
    );
}
```

The important method is `selectImports()`.

It receives `AnnotationMetadata`, which provides metadata about the configuration class that triggered the import.

The method returns an array of class names.

For example:

```java
@Override
public String[] selectImports(AnnotationMetadata importingClassMetadata) {
    return new String[] {
            GreetingConfiguration.class.getName()
    };
}
```

This tells Spring to import `GreetingConfiguration` into the application context.

---

## Example

### GreetingService

The example contains a simple service:

```java
public class GreetingService {

    public String greet() {
        return "Hello from ImportSelector!";
    }
}
```

The service itself does not contain any Spring annotations.

Instead, it will be created by a configuration class selected through an `ImportSelector`.

### GreetingConfiguration

The configuration declares the service as a Spring bean:

```java
@Configuration
public class GreetingConfiguration {

    @Bean
    public GreetingService greetingService() {
        return new GreetingService();
    }
}
```

Normally, this configuration could be imported directly.

However, the example intentionally selects it through an `ImportSelector`.

### GreetingImportSelector

The custom selector implements `ImportSelector`:

```java
public class GreetingImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[] {
                GreetingConfiguration.class.getName()
        };
    }
}
```

The selector returns the name of `GreetingConfiguration`.

Spring then imports and processes that configuration.

### ApplicationConfig

The selector is registered using `@Import`:

```java
@Configuration
@Import(GreetingImportSelector.class)
public class ApplicationConfig {
}
```

Notice that `ApplicationConfig` does **not** directly import `GreetingConfiguration`.

Instead, it imports the selector:

```text
ApplicationConfig
      │
      │ @Import
      ▼
GreetingImportSelector
      │
      │ returns
      ▼
GreetingConfiguration
```

This is the central concept demonstrated by the example.

---

## Creating the ApplicationContext

The application starts an `AnnotationConfigApplicationContext` using `ApplicationConfig`:

```java
try (AnnotationConfigApplicationContext applicationContext =
             new AnnotationConfigApplicationContext(ApplicationConfig.class)) {

    GreetingService greetingService = applicationContext.getBean(GreetingService.class);

    System.out.println(greetingService.greet());
}
```

When the context processes `ApplicationConfig`, Spring encounters:

```java
@Import(GreetingImportSelector.class)
```

Spring invokes the selector.

The selector returns:

```java
GreetingConfiguration.class.getName()
```

Spring then processes `GreetingConfiguration` and registers its `GreetingService` bean.

The application can subsequently retrieve the bean normally:

```java
GreetingService greetingService =
        applicationContext.getBean(GreetingService.class);
```

---

## The Role of AnnotationMetadata

The `selectImports()` method receives:

```java
AnnotationMetadata importingClassMetadata
```

This represents metadata about the configuration class from which the selector was imported.

For example, a selector can inspect annotations on the importing configuration class and make its selection based on that metadata.

The current example does not need to inspect the metadata because it always selects the same configuration class.

However, the parameter is what allows more advanced selectors to make configuration decisions dynamically.

Conceptually:

```text
Importing Configuration
        │
        │ metadata
        ▼
AnnotationMetadata
        │
        ▼
ImportSelector
        │
        │ decision
        ▼
Selected Configuration Classes
```

---

## Direct Import vs ImportSelector

A direct import looks like this:

```java
@Configuration
@Import(GreetingConfiguration.class)
public class ApplicationConfig {
}
```

The configuration class is explicitly specified.

With `ImportSelector`:

```java
@Configuration
@Import(GreetingImportSelector.class)
public class ApplicationConfig {
}
```

The selector determines which configuration classes should be imported.

| Approach | How configuration is selected |
|---|---|
| Direct `@Import` | Configuration class is explicitly specified |
| `ImportSelector` | Configuration class is selected programmatically |

Use direct `@Import` when the configuration is known and does not require a selection mechanism.

Use `ImportSelector` when configuration selection needs to be determined programmatically.

---

## ImportSelector vs BeanDefinitionRegistry

`ImportSelector` and `BeanDefinitionRegistry` operate at different levels.

```text
ImportSelector
    │
    └── Selects configuration classes to import


BeanDefinitionRegistry
    │
    └── Stores and manages BeanDefinition metadata
```

An `ImportSelector` answers:

> Which configuration classes should Spring import?

A `BeanDefinitionRegistry` answers:

> Which bean definitions are registered in this registry?

They are therefore separate mechanisms and are covered by separate examples in this module.

---

## ImportSelector vs DeferredImportSelector

`ImportSelector` performs its selection during configuration processing.

`DeferredImportSelector`, which is covered in a separate example, defers the selection until a later phase of configuration processing.

The distinction can be summarized as:

| Interface | Selection timing |
|---|---|
| `ImportSelector` | During configuration processing |
| `DeferredImportSelector` | Deferred until a later configuration-processing phase |

This example intentionally focuses only on `ImportSelector`.

---

## Class Responsibilities

| Class | Responsibility |
|---|---|
| `GreetingService` | Provides the example service |
| `GreetingConfiguration` | Declares `GreetingService` as a Spring bean |
| `GreetingImportSelector` | Selects `GreetingConfiguration` for import |
| `ApplicationConfig` | Entry-point configuration that imports the selector |
| `ImportSelectorApplication` | Starts the application context and retrieves the service |
| `ImportSelectorApplicationTest` | Verifies the selected configuration and bean |

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

`spring-context` provides:

- `@Configuration`
- `@Bean`
- `@Import`
- `AnnotationConfigApplicationContext`
- `ImportSelector`
- `AnnotationMetadata`

---

## Running the Example

From the project root, run:

```bash
mvn clean install
```

To run only this module:

```bash
mvn -pl 12-advanced-spring/import-selector clean install
```

---

## Key Takeaways

- `ImportSelector` allows configuration classes to be selected programmatically.
- It is used through `@Import`.
- Spring calls `selectImports()` during configuration processing.
- `selectImports()` returns the fully qualified names of configuration classes to import.
- The returned configuration classes are processed by Spring like other configuration classes.
- `AnnotationMetadata` provides metadata about the configuration class that triggered the import.
- `ImportSelector` selects configuration classes; it does not directly create bean instances.
- The selected configuration classes can define normal Spring beans using `@Bean`.
- `ImportSelector` is different from `BeanDefinitionRegistry`, which manages bean definition metadata.
- `ImportSelector` is also different from `DeferredImportSelector`, which defers the selection to a later phase.

---

## Next

The next example covers **DeferredImportSelector**, demonstrating how Spring can defer import selection until a later stage of configuration processing.