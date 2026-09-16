# How `@Autowired` Works

`@Autowired` is one of Spring's most recognizable dependency injection annotations. It allows Spring to identify fields, constructors, or methods that require dependencies and resolve those dependencies from the application context.

However, `@Autowired` does not perform dependency injection by itself.

Spring uses `AutowiredAnnotationBeanPostProcessor` to detect `@Autowired` injection points during the bean lifecycle. The processor then uses the bean factory's dependency resolution infrastructure to find the required dependency and inject it into the bean.

This example demonstrates that process using Spring's lower-level infrastructure instead of hiding it behind a fully configured application context.

## Learning Objectives

By completing this example, you will understand:

- What `@Autowired` does
- Why `@Autowired` does not perform injection by itself
- The role of `AutowiredAnnotationBeanPostProcessor`
- How Spring discovers `@Autowired` injection points
- The relationship between `@Autowired` and `DependencyDescriptor`
- How `AutowiredAnnotationBeanPostProcessor` uses the bean factory
- How dependency resolution leads to the actual injection
- Why Spring-managed beans are different from objects created with `new`
- How `@Autowired` builds on Spring's dependency resolution infrastructure

---

## What Is `@Autowired`?

`@Autowired` is an annotation used to tell Spring that a dependency should be resolved and injected by the Spring container.

For example:

```java
@Component
public class GreetingController {

    @Autowired
    private GreetingService greetingService;
}
```

The annotation identifies:

```java
@Autowired
private GreetingService greetingService;
```

as an injection point.

However, the annotation itself does not locate `GreetingService` or assign it to the field.

That work is performed by Spring infrastructure, specifically:

```text
AutowiredAnnotationBeanPostProcessor
```

---

## `@Autowired` Does Not Inject Anything by Itself

Consider this ordinary Java code:

```java
GreetingController controller = new GreetingController();
```

Because the object was created directly using `new`, Spring has not been given an opportunity to process it.

The following field therefore remains `null`:

```java
@Autowired
private GreetingService greetingService;
```

The `@Autowired` annotation is metadata. Something has to inspect that metadata and act on it.

Spring's:

```java
AutowiredAnnotationBeanPostProcessor
```

is responsible for processing the annotation.

---

## What Is `AutowiredAnnotationBeanPostProcessor`?

`AutowiredAnnotationBeanPostProcessor` is a Spring `BeanPostProcessor` responsible for processing annotations such as `@Autowired`.

It participates in the bean lifecycle and looks for injection points on Spring-managed beans.

Conceptually:

```text
Bean created
     │
     ▼
BeanPostProcessor
     │
     ▼
AutowiredAnnotationBeanPostProcessor
     │
     ▼
Find @Autowired injection points
     │
     ▼
Resolve dependencies
     │
     ▼
Inject dependencies
```

This is why `@Autowired` works automatically in a normal Spring application.

Spring registers and manages the processor as part of its application infrastructure.

In this example, we register it manually so that its role is visible.

---

## Registering the Bean Definitions

The example starts with a `DefaultListableBeanFactory`:

```java
DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
```

We then register the two beans:

```java
beanFactory.registerBeanDefinition("greetingService",new RootBeanDefinition(GreetingService.class));

beanFactory.registerBeanDefinition("greetingController",new RootBeanDefinition(GreetingController.class));
```

At this point, Spring knows about both bean definitions.

Conceptually:

```text
DefaultListableBeanFactory
        │
        ├── greetingService
        │      └── GreetingService
        │
        └── greetingController
               └── GreetingController
```

The definitions tell Spring how the beans can be created.

The `GreetingController` definition also contains the class that has the `@Autowired` field.

---

## Registering the `AutowiredAnnotationBeanPostProcessor`

We explicitly create the processor:

```java
AutowiredAnnotationBeanPostProcessor postProcessor = new AutowiredAnnotationBeanPostProcessor();
```

Then we give it access to the bean factory:

```java
postProcessor.setBeanFactory(beanFactory);
```

Finally, we register it with the bean factory:

```java
beanFactory.addBeanPostProcessor(postProcessor);
```

Now the bean factory knows that the processor should participate in bean creation.

The setup looks like:

```text
DefaultListableBeanFactory
        │
        ├── Bean definitions
        │
        └── BeanPostProcessors
                │
                └── AutowiredAnnotationBeanPostProcessor
```

---

## Creating the Bean

When we request the controller:

```java
GreetingController controller = beanFactory.getBean("greetingController", GreetingController.class);
```

Spring begins creating the bean.

The important part is that bean creation happens through the bean factory rather than directly through:

```java
new GreetingController()
```

This allows the registered `BeanPostProcessor`s to participate in the lifecycle.

---

## The `@Autowired` Processing Flow

Conceptually, the process looks like this:

```text
getBean("greetingController")
             │
             ▼
Create GreetingController
             │
             ▼
Populate bean
             │
             ▼
AutowiredAnnotationBeanPostProcessor
             │
             ▼
Find @Autowired field
             │
             ▼
Create dependency metadata
             │
             ▼
Resolve dependency
             │
             ▼
Find GreetingService
             │
             ▼
Inject GreetingService
             │
             ▼
GreetingController ready
```

This is the central flow demonstrated by the example.

---

## Discovering the Injection Point

The processor examines the bean's class and searches for supported autowiring annotations.

For our controller:

```java
@Component
public class GreetingController {

    @Autowired
    private GreetingService greetingService;

    public String greet() {
        return greetingService.greet();
    }
}
```

The processor identifies:

```java
@Autowired
private GreetingService greetingService;
```

as an injection point.

Conceptually:

```text
GreetingController
        │
        ▼
@Autowired GreetingService greetingService
        │
        ▼
Injection point
```

The processor then has enough information to ask the bean factory to resolve the dependency.

---

## Dependency Resolution

The dependency-resolution mechanism was demonstrated in the previous **How Dependency Injection Works** example.

There, we explicitly created a `DependencyDescriptor` and called:

```java
beanFactory.resolveDependency(...)
```

With `@Autowired`, the process is triggered automatically by `AutowiredAnnotationBeanPostProcessor`.

Conceptually:

```text
@Autowired field
       │
       ▼
DependencyDescriptor
       │
       ▼
BeanFactory
       │
       ▼
resolveDependency()
       │
       ▼
GreetingService
```

This demonstrates an important relationship between the two examples:

> `@Autowired` provides the injection metadata, while Spring's dependency-resolution infrastructure determines which bean satisfies the dependency.

---

## Why Does the Bean Factory Matter?

The processor needs access to a bean factory because the factory is responsible for resolving dependencies.

This is why the example contains:

```java
postProcessor.setBeanFactory(beanFactory);
```

The processor can then work with the bean factory when resolving:

```java
GreetingService
```

The relationship can be visualized as:

```text
AutowiredAnnotationBeanPostProcessor
                │
                │ uses
                ▼
      DefaultListableBeanFactory
                │
                │ resolves
                ▼
         GreetingService
```

---

## Why `new` Does Not Trigger `@Autowired`

This distinction is particularly important when learning Spring internals.

This code:

```java
GreetingController controller = new GreetingController();
```

is ordinary Java.

Spring does not automatically intercept every object created by the JVM.

Therefore, Spring does not process the `@Autowired` annotation on that object.

By contrast:

```java
GreetingController controller = beanFactory.getBean(GreetingController.class);
```

requests the bean from Spring.

The bean factory can therefore run the configured bean lifecycle and invoke its registered post-processors.

Conceptually:

```text
new GreetingController()
        │
        ▼
Plain Java object
        │
        └── No Spring processing


beanFactory.getBean(...)
        │
        ▼
Spring-managed bean
        │
        ▼
Bean lifecycle
        │
        ▼
AutowiredAnnotationBeanPostProcessor
        │
        ▼
Dependency injection
```

---

## Field Injection in This Example

This example uses field injection:

```java
@Autowired
private GreetingService greetingService;
```

Field injection makes the processor's role easy to demonstrate because the processor needs to locate the field and populate it after the object has been created.

Spring also supports other injection styles, including constructor and method injection.

For example, constructor injection:

```java
@Autowired
public GreetingController(GreetingService greetingService) {
    this.greetingService = greetingService;
}
```

And method injection:

```java
@Autowired
public void setGreetingService(GreetingService greetingService) {
    this.greetingService = greetingService;
}
```

The exact processing path differs depending on the injection point, but the underlying concept remains the same:

```text
Injection metadata
        │
        ▼
AutowiredAnnotationBeanPostProcessor
        │
        ▼
Dependency resolution
        │
        ▼
Injection
```

The example focuses on field injection to keep the internal flow straightforward.

---

## Complete Example

The complete application is:

```java
public class AutowiredApplication {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        beanFactory.registerBeanDefinition("greetingService", new RootBeanDefinition(GreetingService.class));
        beanFactory.registerBeanDefinition("greetingController", new RootBeanDefinition(GreetingController.class));

        AutowiredAnnotationBeanPostProcessor postProcessor = new AutowiredAnnotationBeanPostProcessor();

        postProcessor.setBeanFactory(beanFactory);
        beanFactory.addBeanPostProcessor(postProcessor);

        GreetingController controller = beanFactory.getBean("greetingController", GreetingController.class);

        System.out.println(controller.greet());
    }
}
```

The application demonstrates:

1. Creating a bean factory
2. Registering bean definitions
3. Creating `AutowiredAnnotationBeanPostProcessor`
4. Giving the processor access to the bean factory
5. Registering the processor
6. Requesting a Spring-managed bean
7. Detecting the `@Autowired` field
8. Resolving `GreetingService`
9. Injecting the dependency
10. Using the resulting bean

---

## Complete Injection Flow

The entire process can be summarized as:

```text
GreetingController
        │
        │ contains
        ▼
@Autowired GreetingService
        │
        ▼
AutowiredAnnotationBeanPostProcessor
        │
        │ identifies injection point
        ▼
Dependency metadata
        │
        ▼
DefaultListableBeanFactory
        │
        │ resolves dependency
        ▼
GreetingService bean
        │
        ▼
Injected into GreetingController
```

This is the relationship between the main Spring internals involved in this example.

---

## Relationship to Previous Examples

This example builds directly on the previous Module 13 examples.

### How Beans Are Registered

We learned that Spring represents bean metadata using `BeanDefinition`:

```text
BeanDefinition
      │
      ▼
BeanDefinitionRegistry
      │
      ▼
DefaultListableBeanFactory
```

### How Dependency Injection Works

We then looked at dependency resolution:

```text
DependencyDescriptor
      │
      ▼
DefaultListableBeanFactory
      │
      ▼
resolveDependency()
```

### How `@Autowired` Works

Now we add the annotation-processing layer:

```text
@Autowired
      │
      ▼
AutowiredAnnotationBeanPostProcessor
      │
      ▼
Dependency resolution
      │
      ▼
Injection
```

Putting the examples together:

```text
BeanDefinition
      │
      ▼
DefaultListableBeanFactory
      │
      ├──────────────────────────┐
      │                          │
      ▼                          ▼
Dependency Resolution      BeanPostProcessor
      │                          │
      │                  AutowiredAnnotation-
      │                  BeanPostProcessor
      │                          │
      └──────────────┬───────────┘
                     ▼
                 Injection
```

This illustrates how several seemingly independent Spring features are actually built on top of the same underlying infrastructure.

---

## Dependencies

The example uses:

- **Spring Context** — provides `AutowiredAnnotationBeanPostProcessor` and annotation-based bean infrastructure
- **Spring Beans** — provides `DefaultListableBeanFactory` and bean definition infrastructure
- **JUnit Jupiter** — provides the test framework

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

From the `autowired` directory:

```bash
mvn clean install
```

You can also run the main application from your IDE.

Expected output:

```text
Hello from GreetingService!
```

---

## Key Takeaways

1. `@Autowired` identifies an injection point.
2. `@Autowired` does not perform dependency injection by itself.
3. `AutowiredAnnotationBeanPostProcessor` detects and processes `@Autowired`.
4. The processor participates in the Spring bean lifecycle.
5. The processor uses the bean factory to resolve dependencies.
6. Dependency resolution ultimately relies on Spring's bean factory infrastructure.
7. Spring must manage the bean for the normal `@Autowired` processing lifecycle to occur.
8. Objects created directly with `new` are not automatically processed by Spring.
9. Component scanning, bean definitions, dependency resolution, and `@Autowired` processing are interconnected.
10. `@Autowired` is a higher-level feature built on top of Spring's lower-level dependency-resolution and bean-lifecycle infrastructure.

The central idea is:

```text
@Autowired
    │
    ▼
AutowiredAnnotationBeanPostProcessor
    │
    ▼
Injection Point
    │
    ▼
Dependency Resolution
    │
    ▼
BeanFactory
    │
    ▼
Dependency
    │
    ▼
Injected Bean
```

---

## Next

**How `@Transactional` Works**

The next example will examine how Spring implements declarative transaction management using `@Transactional`, transaction attributes, `TransactionInterceptor`, `TransactionAttributeSource`, and `PlatformTransactionManager`.