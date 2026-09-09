# DeferredImportSelector

This example demonstrates Spring's `DeferredImportSelector`, which allows configuration classes to be selected for import at a **later phase of configuration processing**.

`DeferredImportSelector` builds on the same basic idea as `ImportSelector`, but differs in **when the import selection is processed**.

---

## Learning Objectives

- Understand what `DeferredImportSelector` is
- Understand how `DeferredImportSelector` works with `@Import`
- Implement a custom `DeferredImportSelector`
- Understand deferred import processing
- Understand the role of `AnnotationMetadata`
- Understand how the selected configuration becomes part of the Spring application context
- Distinguish `DeferredImportSelector` from `ImportSelector`

---

## What Is DeferredImportSelector?

`DeferredImportSelector` is a Spring interface that extends `ImportSelector` and allows configuration classes to be imported during a **deferred phase of configuration processing**.

Like `ImportSelector`, it provides the `selectImports()` method:

```java
String[] selectImports(AnnotationMetadata importingClassMetadata);
```

For example:

```java
public class GreetingDeferredImportSelector implements DeferredImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[] {
                GreetingConfiguration.class.getName()
        };
    }
}
```

The important difference is not what the method returns, but **when Spring processes the selector**.

---

## Why Does DeferredImportSelector Exist?

Spring configuration can involve many configuration classes and import selectors.

Sometimes a configuration import should not be processed immediately with the other regular imports. Instead, it should be considered after the initial configuration processing has progressed.

`DeferredImportSelector` provides this deferred processing mechanism.

Conceptually:

```text
Regular Configuration Processing
          │
          ▼
Regular Imports
          │
          ▼
Deferred Import Phase
          │
          ▼
DeferredImportSelector
          │
          ▼
Selected Configuration
```

This allows Spring to postpone certain configuration imports until its deferred-import processing phase.

---

## How DeferredImportSelector Works

The basic process is:

```text
1. Spring processes ApplicationConfig
            │
            ▼
2. Spring encounters @Import(GreetingDeferredImportSelector.class)
            │
            ▼
3. Spring recognizes the DeferredImportSelector
            │
            ▼
4. Import selection is deferred
            │
            ▼
5. Spring reaches the deferred import phase
            │
            ▼
6. selectImports(...) is processed
            │
            ▼
7. GreetingConfiguration is selected
            │
            ▼
8. Spring processes GreetingConfiguration
            │
            ▼
9. GreetingService becomes a Spring bean
```

The important concept is the **deferred phase**.

---

## ImportSelector vs DeferredImportSelector

Both interfaces are used to programmatically select configuration classes.

The main difference is when the selection is processed.

| Interface | Purpose | Processing |
|---|---|---|
| `ImportSelector` | Select configuration classes for import | Regular configuration processing |
| `DeferredImportSelector` | Select configuration classes for import later | Deferred import phase |

An `ImportSelector` can be thought of as:

```text
"Select these imports as part of normal configuration processing."
```

A `DeferredImportSelector` can be thought of as:

```text
"Select these imports during the deferred import phase."
```

The two interfaces are related:

```text
ImportSelector
      ▲
      │ extends
      │
DeferredImportSelector
```

`DeferredImportSelector` therefore has the same `selectImports()` mechanism while adding deferred processing semantics.

---

## Example

### GreetingService

The example contains a simple service:

```java
public class GreetingService {

    public String greet() {
        return "Hello from DeferredImportSelector!";
    }
}
```

The service is deliberately simple so that the example can focus on the configuration import mechanism.

### GreetingConfiguration

The service is declared as a Spring bean through a configuration class:

```java
@Configuration
public class GreetingConfiguration {

    @Bean
    public GreetingService greetingService() {
        return new GreetingService();
    }
}
```

This configuration class is not directly imported by `ApplicationConfig`.

Instead, it will be selected by the `DeferredImportSelector`.

### GreetingDeferredImportSelector

The custom selector implements `DeferredImportSelector`:

```java
public class GreetingDeferredImportSelector implements DeferredImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[] {
                GreetingConfiguration.class.getName()
        };
    }
}
```

The selector returns the fully qualified name of `GreetingConfiguration`.

Spring processes this import during its deferred-import phase.

### ApplicationConfig

The selector is registered using `@Import`:

```java
@Configuration
@Import(GreetingDeferredImportSelector.class)
public class ApplicationConfig {
}
```

Notice that `ApplicationConfig` does not directly import `GreetingConfiguration`.

Instead:

```text
ApplicationConfig
       │
       │ @Import
       ▼
GreetingDeferredImportSelector
       │
       │ deferred selection
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

When Spring processes the application configuration, it discovers the `DeferredImportSelector`.

The selector's import is deferred until the appropriate deferred-import phase.

The selector then returns:

```java
GreetingConfiguration.class.getName()
```

Spring processes that configuration and registers the `GreetingService` bean.

The application can then retrieve the bean normally:

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

This represents metadata about the configuration class that triggered the import.

For example:

```java
@Override
public String[] selectImports(
        AnnotationMetadata importingClassMetadata) {

    // Use importingClassMetadata to inspect
    // the importing configuration if needed.

    return new String[] {
            GreetingConfiguration.class.getName()
    };
}
```

The current example does not inspect the metadata because the selector always imports the same configuration class.

However, `AnnotationMetadata` allows more advanced selectors to make their import decisions based on the configuration that triggered them.

Conceptually:

```text
Importing Configuration
        │
        │ metadata
        ▼
AnnotationMetadata
        │
        ▼
DeferredImportSelector
        │
        │ deferred decision
        ▼
Selected Configuration Classes
```

---

## Deferred Import Processing

The defining characteristic of `DeferredImportSelector` is that its imports are processed later than regular imports.

Conceptually, Spring configuration processing can be viewed as:

```text
Configuration Classes
        │
        ▼
Process Configuration
        │
        ├── Regular Imports
        │
        └── Deferred Imports
                 │
                 ▼
        DeferredImportSelector
                 │
                 ▼
        Selected Configurations
```

This deferred mechanism is particularly important in Spring's own configuration infrastructure, where the ordering of configuration processing can affect which configuration should ultimately be imported.

---

## Why Not Just Use ImportSelector?

If the configuration can be selected during normal configuration processing, an `ImportSelector` may be sufficient.

`DeferredImportSelector` is useful when the import decision needs to participate in Spring's **deferred import phase**.

The choice can be summarized as:

```text
Need normal programmatic imports?
        │
        └──► ImportSelector

Need imports processed during the deferred phase?
        │
        └──► DeferredImportSelector
```

The important distinction is therefore **processing timing**, not the basic mechanism of returning configuration class names.

---

## Class Responsibilities

| Class | Responsibility |
|---|---|
| `GreetingService` | Provides the example service |
| `GreetingConfiguration` | Declares `GreetingService` as a Spring bean |
| `GreetingDeferredImportSelector` | Selects `GreetingConfiguration` during deferred import processing |
| `ApplicationConfig` | Entry-point configuration that imports the selector |
| `DeferredImportSelectorApplication` | Starts the application context and retrieves the service |
| `DeferredImportSelectorApplicationTest` | Verifies the deferred import and resulting bean |

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
- `ImportSelector`
- `DeferredImportSelector`
- `AnnotationMetadata`

---

## Running the Example

From the project root, run:

```bash
mvn clean install
```

To run only this module:

```bash
mvn -pl 12-advanced-spring/deferred-import-selector clean install
```

---

## Key Takeaways

- `DeferredImportSelector` allows configuration classes to be selected programmatically.
- It extends the `ImportSelector` concept.
- It is registered through `@Import`.
- It provides the same `selectImports()` mechanism as `ImportSelector`.
- Its defining characteristic is that its imports are processed during Spring's deferred import phase.
- `AnnotationMetadata` provides metadata about the configuration class that triggered the import.
- The selector selects configuration classes; it does not directly create bean instances.
- The selected configuration can declare normal Spring beans using `@Bean`.
- `ImportSelector` and `DeferredImportSelector` use similar selection mechanisms but differ in processing timing.
- `DeferredImportSelector` is useful when configuration imports need to participate in Spring's deferred configuration processing.

---

## Next

The next example covers **ImportBeanDefinitionRegistrar**, demonstrating how bean definitions can be programmatically registered with Spring's `BeanDefinitionRegistry`.