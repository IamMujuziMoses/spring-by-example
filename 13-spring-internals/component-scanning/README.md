# How Component Scanning Works

Component scanning is one of the mechanisms Spring uses to automatically discover classes that should become Spring beans.

When Spring encounters a class annotated with `@Component`, `@Service`, `@Repository`, or `@Controller`, it can discover that class during classpath scanning, create a `BeanDefinition`, and register that definition with the bean factory.

This example demonstrates the component scanning process using Spring's lower-level `ClassPathBeanDefinitionScanner` rather than hiding the process behind a full application context.

## Learning Objectives

By completing this example, you will understand:

- What component scanning is
- How `@Component` identifies candidate components
- How Spring scans packages on the classpath
- The role of `ClassPathBeanDefinitionScanner`
- How candidate components are discovered
- How discovered components become `BeanDefinition` objects
- How bean definitions are registered with a `BeanDefinitionRegistry`
- The relationship between component scanning and bean creation
- Why component scanning is separate from dependency injection

---

## What Is Component Scanning?

Component scanning is the process Spring uses to search specified packages for classes that qualify as Spring components.

For example:

```java
@Component
public class GreetingService {

    public String greet() {
        return "Hello from a scanned component!";
    }
}
```

The `@Component` annotation tells Spring that this class is a candidate for registration as a Spring bean.

The annotation itself does not create the object.

Instead, it provides metadata that Spring's component scanning infrastructure can detect.

Spring also provides specialized component annotations such as:

```text
@Component
@Service
@Repository
@Controller
```

These annotations are recognized by Spring's component scanning infrastructure, with the specialized annotations providing additional semantic meaning.

---

## The Component Scanning Flow

At a high level, the process looks like this:

```text
@Component
    │
    ▼
Classpath scanning
    │
    ▼
Candidate component discovered
    │
    ▼
BeanDefinition created
    │
    ▼
BeanDefinitionRegistry
    │
    ▼
DefaultListableBeanFactory
    │
    ▼
Bean instance created
```

The example exposes this process using Spring's `ClassPathBeanDefinitionScanner`.

---

## `ClassPathBeanDefinitionScanner`

The main class used in this example is:

```java
ClassPathBeanDefinitionScanner
```

It is responsible for scanning the classpath for candidate components and registering their bean definitions.

We create it with a bean definition registry:

```java
DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanFactory);
```

`DefaultListableBeanFactory` implements `BeanDefinitionRegistry`, so the scanner can register discovered bean definitions directly with it.

---

## Starting the Scan

The scan is started by specifying the package to search:

```java
scanner.scan("com.springbyexample.componentscanning");
```

Spring searches the specified package and its subpackages for candidate components.

For this example, it discovers:

```java
@Component
public class GreetingService {
    ...
}
```

The scanner then creates a bean definition representing that component.

Conceptually:

```text
com.springbyexample.componentscanning.GreetingService
                         │
                         ▼
                  BeanDefinition
                         │
                         ▼
             "greetingService"
```

---

## From Component to BeanDefinition

This is one of the most important concepts in this example.

The scanner does not immediately need to create:

```java
new GreetingService()
```

Instead, it first creates metadata describing the bean.

That metadata is represented by a `BeanDefinition`.

The definition contains information Spring can use later to create and configure the bean.

Conceptually:

```text
GreetingService.class
        │
        ▼
   BeanDefinition
        │
        ├── Bean class
        ├── Scope
        ├── Lazy initialization
        ├── Dependencies
        └── Other bean metadata
```

This is the same `BeanDefinition` concept explored in the previous **How Beans Are Registered** example.

The difference is how the definition is obtained.

### Programmatic Registration

The previous example explicitly created a definition:

```java
RootBeanDefinition beanDefinition = new RootBeanDefinition(GreetingService.class);

beanFactory.registerBeanDefinition("greetingService",beanDefinition);
```

### Component Scanning

This example allows Spring's scanner to discover the class and create and register the definition:

```java
scanner.scan("com.springbyexample.componentscanning");
```

Both approaches ultimately lead to bean definitions being registered with the bean factory.

---

## Component Scanning and Bean Definition Registration

The scanner uses the supplied registry to register the discovered definitions.

In this example:

```java
DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
```

acts as both:

```text
BeanFactory
    +
BeanDefinitionRegistry
```

After scanning:

```java
scanner.scan("com.springbyexample.componentscanning");
```

the bean factory contains a bean definition for `GreetingService`.

We can verify this directly:

```java
beanFactory.containsBeanDefinition("greetingService");
```

This demonstrates that component scanning has resulted in a registered bean definition.

---

## Component Scanning Does Not Equal Bean Creation

It is important to separate these two operations.

### Scanning

Scanning discovers candidate classes and registers their definitions.

```text
@Component
    │
    ▼
Scanner
    │
    ▼
BeanDefinition
    │
    ▼
Registry
```

### Bean Creation

The bean factory can then use the registered definition to create the actual object.

```java
GreetingService greetingService = beanFactory.getBean(GreetingService.class);
```

Conceptually:

```text
BeanDefinition
      │
      ▼
BeanFactory
      │
      ▼
GreetingService instance
```

Therefore:

> Component scanning discovers and registers bean metadata; bean creation is a separate stage.

This distinction becomes particularly important when studying dependency injection and bean post-processors.

---

## Candidate Components

The scanner does not blindly turn every class in a package into a bean.

It looks for classes that satisfy its component-detection rules.

For example:

```java
@Component
public class GreetingService {
}
```

is a candidate component.

A normal class without component metadata:

```java
public class Utility {
}
```

is not automatically treated as a component by the default component scanner.

This distinction is important:

```text
Classes on classpath
        │
        ▼
Component scanning
        │
        ▼
Candidate components
        │
        ▼
Bean definitions
```

Only candidates that satisfy the scanner's rules proceed through the registration process.

---

## Component Scanning and `@ComponentScan`

In a typical Spring application, component scanning is often triggered using:

```java
@ComponentScan("com.springbyexample.componentscanning")
```

For example:

```java
@Configuration
@ComponentScan("com.springbyexample.componentscanning")
public class AppConfig {
}
```

The configuration-based approach is convenient because Spring manages the surrounding infrastructure.

This example intentionally uses:

```java
ClassPathBeanDefinitionScanner
```

directly so that the scanning mechanism itself is visible.

The higher-level configuration ultimately relies on the same general concept: scan packages, discover components, and register bean definitions.

---

## Complete Example

The complete application is:

```java
public class ComponentScanningApplication {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanFactory);

        scanner.scan("com.springbyexample.componentscanning");

        GreetingService greetingService = beanFactory.getBean(GreetingService.class);

        System.out.println(greetingService.greet());
    }
}
```

The important sequence is:

```text
Create DefaultListableBeanFactory
            │
            ▼
Create ClassPathBeanDefinitionScanner
            │
            ▼
Scan package
            │
            ▼
Discover @Component
            │
            ▼
Register BeanDefinition
            │
            ▼
Retrieve bean
            │
            ▼
Create/use GreetingService
```

---

## Why Does Spring Scan Packages?

Without component scanning, applications would need to explicitly register every component.

For example:

```java
@Bean
public GreetingService greetingService() {
    return new GreetingService();
}
```

With component scanning:

```java
@Component
public class GreetingService {
}
```

Spring can discover the class automatically.

This becomes especially useful in applications containing many components.

Instead of manually maintaining registrations for every class, developers can organize components into packages and allow Spring to discover them.

---

## Component Scanning vs Dependency Injection

Component scanning and dependency injection are related, but they are not the same operation.

### Component Scanning

Answers:

> Which classes should Spring register as beans?

```text
@Component
    │
    ▼
Scanner
    │
    ▼
BeanDefinition
```

### Dependency Injection

Answers:

> Which bean should be supplied to this dependency?

```text
DependencyDescriptor
    │
    ▼
BeanFactory
    │
    ▼
Resolved dependency
```

This distinction prepares us for the later examples in Module 13:

- How `@Autowired` Works
- How Bean Post Processors Work

---

## Relationship to the Bean Registration Example

The **How Beans Are Registered** example demonstrated explicit bean definition registration:

```text
Bean Class
    │
    ▼
BeanDefinition
    │
    ▼
registerBeanDefinition()
    │
    ▼
BeanDefinitionRegistry
```

Component scanning adds an automated discovery step:

```text
@Component
    │
    ▼
Classpath Scanner
    │
    ▼
BeanDefinition
    │
    ▼
BeanDefinitionRegistry
```

Both paths eventually lead to the same underlying bean definition infrastructure.

This is an important Spring internals concept:

> Many different configuration mechanisms ultimately contribute `BeanDefinition`s to the bean factory.

---

## Dependencies

The example uses:

- **Spring Context** — component scanning infrastructure
- **Spring Beans** — `DefaultListableBeanFactory` and bean definitions
- **JUnit Jupiter** — tests

Maven dependencies:

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

---

## Running the Example

From the `component-scanning` directory:

```bash
mvn clean install
```

You can also run the main application from your IDE.

Expected output:

```text
Hello from a scanned component!
```

---

## Key Takeaways

1. Component scanning allows Spring to discover components automatically.
2. `@Component` identifies a class as a candidate component.
3. `ClassPathBeanDefinitionScanner` performs classpath scanning.
4. The scanner discovers candidate components rather than instantiating every class.
5. Discovered components are represented as `BeanDefinition`s.
6. Those definitions are registered with a `BeanDefinitionRegistry`.
7. `DefaultListableBeanFactory` can act as both the registry and bean factory.
8. Component scanning and bean creation are separate stages.
9. Component scanning is different from dependency injection.
10. Higher-level mechanisms such as `@ComponentScan` build on this underlying infrastructure.
11. Different Spring configuration mechanisms ultimately contribute bean definitions to the bean factory.

The central idea is:

```text
@Component
    │
    ▼
Component Scanner
    │
    ▼
Candidate Component
    │
    ▼
BeanDefinition
    │
    ▼
BeanDefinitionRegistry
    │
    ▼
BeanFactory
    │
    ▼
Bean Instance
```

---

## Next

**How `@Autowired` Works**

The next example will go deeper into how Spring processes `@Autowired` annotations and how `AutowiredAnnotationBeanPostProcessor` identifies injection points and resolves dependencies.