# ApplicationContext

This example demonstrates the **ApplicationContext**, Spring's higher-level application container.

It builds on the basic bean management concepts introduced with `BeanFactory` and shows how an `ApplicationContext` can create, configure, and manage beans declared through Java configuration.

The example uses `AnnotationConfigApplicationContext` together with `@Configuration` and `@Bean`.

---

## Learning Objectives

By completing this example, you will understand:

- What `ApplicationContext` is
- How `ApplicationContext` relates to `BeanFactory`
- How `AnnotationConfigApplicationContext` creates an application context
- How `@Configuration` defines configuration
- How `@Bean` registers a bean with the context
- How to retrieve beans using `getBean()`
- How Spring manages singleton beans
- How to close an application context properly

---

## What Is ApplicationContext?

`ApplicationContext` is Spring's central interface for an application-level IoC container.

It is responsible for managing Spring beans and provides functionality beyond the basic bean management provided by `BeanFactory`.

The interface extends `BeanFactory`:

```text
ApplicationContext
        │
        └── BeanFactory
```

This means an `ApplicationContext` can perform the basic operations provided by a `BeanFactory`, such as retrieving beans, while also providing additional application-oriented capabilities.

Some of those additional capabilities include:

- Application events
- Message resolution
- Resource loading
- Environment and property management
- Integration with Spring's annotation-based configuration
- Automatic detection and registration of certain infrastructure components

These features will be explored individually in later examples.

---

## Why Does ApplicationContext Exist?

`BeanFactory` provides the fundamental IoC container functionality required to create and retrieve beans.

However, real applications need more than just bean lookup.

An application may need to:

- Load configuration
- Resolve application properties
- Load resources
- Publish and consume application events
- Resolve messages
- Detect application components
- Manage application startup and shutdown

`ApplicationContext` provides a higher-level container designed for these application concerns.

A simplified relationship is:

```text
BeanFactory
    │
    ├── Create beans
    ├── Manage beans
    └── Retrieve beans
            │
            ▼
ApplicationContext
    │
    ├── BeanFactory capabilities
    ├── Configuration support
    ├── Events
    ├── Resources
    ├── Environment
    └── Messages
```

---

## AnnotationConfigApplicationContext

For Java-based configuration, Spring provides `AnnotationConfigApplicationContext`.

It creates an `ApplicationContext` from classes annotated with `@Configuration`.

For example:

```java
try (AnnotationConfigApplicationContext applicationContext =
             new AnnotationConfigApplicationContext(ApplicationContextConfig.class)) {

    GreetingService greetingService = applicationContext.getBean(GreetingService.class);
}
```

The configuration class tells Spring how the application's beans should be created.

---

## Defining Configuration

Spring's `@Configuration` annotation identifies a class as a source of bean definitions.

```java
@Configuration
public class ApplicationContextConfig {

    @Bean
    public GreetingService greetingService() {
        return new GreetingService();
    }
}
```

The `@Bean` method tells Spring:

> Create and manage the object returned by this method as a Spring bean.

The application does not need to manually instantiate `GreetingService` when retrieving it from the context.

---

## Registering a Bean

The following configuration defines a `GreetingService` bean:

```java
@Configuration
public class ApplicationContextConfig {

    @Bean
    public GreetingService greetingService() {
        return new GreetingService();
    }
}
```

When the application context starts, Spring processes the configuration class and registers the `GreetingService` bean.

Conceptually:

```text
ApplicationContext
        │
        ▼
ApplicationContextConfig
        │
        └── @Bean greetingService()
                    │
                    ▼
             GreetingService
                    │
                    ▼
             Managed by Spring
```

---

## Retrieving a Bean

Once the context has been created, beans can be retrieved using `getBean()`.

```java
GreetingService greetingService = applicationContext.getBean(GreetingService.class);
```

Spring returns the managed `GreetingService` instance.

The application therefore interacts with the container instead of manually creating the object.

---

## Complete Example

### GreetingService

```java
public class GreetingService {

    public String greet() {
        return "Hello from ApplicationContext!";
    }
}
```

### ApplicationContextConfig

```java
@Configuration
public class ApplicationContextConfig {

    @Bean
    public GreetingService greetingService() {
        return new GreetingService();
    }
}
```

### Application

```java
public class ApplicationContextApplication {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(ApplicationContextConfig.class)) {

            GreetingService greetingService = applicationContext.getBean(GreetingService.class);

            System.out.println(greetingService.greet());
        }
    }
}
```

Running the application produces:

```text
Hello from ApplicationContext!
```

---

## ApplicationContext Lifecycle

Creating an `AnnotationConfigApplicationContext` initializes the application context.

A simplified lifecycle is:

```text
new AnnotationConfigApplicationContext(...)
                │
                ▼
        Create the context
                │
                ▼
       Process configuration
                │
                ▼
        Register bean definitions
                │
                ▼
       Create/manage beans
                │
                ▼
          Context ready
                │
                ▼
           getBean()
                │
                ▼
         Use application
                │
                ▼
       Close the context
```

In this example, the context is used inside a try-with-resources block:

```java
try (AnnotationConfigApplicationContext applicationContext =
             new AnnotationConfigApplicationContext(ApplicationContextConfig.class)) {

    // Use the application context
}
```

`AnnotationConfigApplicationContext` implements `AutoCloseable`, allowing the context to be closed automatically when the block finishes.

---

## Singleton Beans

Spring beans are singleton-scoped by default.

This means that repeated lookups of the same bean return the same managed instance.

For example:

```java
GreetingService first = applicationContext.getBean(GreetingService.class);
GreetingService second = applicationContext.getBean(GreetingService.class);
```

The two references point to the same bean instance:

```text
                 ApplicationContext
                        │
                        ▼
               GreetingService
                  ┌─────┴─────┐
                  │           │
                  ▼           ▼
                first       second
                  │           │
                  └─────┬─────┘
                        │
                   same instance
```

This can be verified with `assertSame()`:

```java
assertSame(first, second);
```

---

## ApplicationContext vs BeanFactory

The previous example introduced `BeanFactory` using `DefaultListableBeanFactory`.

The two containers share the fundamental IoC concept of managing beans, but `ApplicationContext` provides a broader application-level abstraction.

| Feature | BeanFactory | ApplicationContext |
|---|---|---|
| Bean creation | Yes | Yes |
| Bean retrieval | Yes | Yes |
| Dependency injection | Yes | Yes |
| Bean scopes | Yes | Yes |
| Java configuration support | Through appropriate implementations | Yes |
| Application events | No | Yes |
| Message resolution | No | Yes |
| Resource loading | No | Yes |
| Environment abstraction | No | Yes |
| Application-level container | Basic | Yes |

A useful mental model is:

```text
BeanFactory
    ↓
Core IoC container functionality

ApplicationContext
    ↓
Core IoC functionality
+
Application-level services
```

`ApplicationContext` should therefore not be thought of as an entirely separate container from `BeanFactory`. It is a higher-level abstraction built on the core bean factory infrastructure.

---

## Dependencies

This example only requires the Spring Context module and JUnit for testing.

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

From the project root:

```bash
mvn clean install
```

To run only this module:

```bash
mvn -pl 12-advanced-spring/application-context clean test
```

---

## Key Takeaways

- `ApplicationContext` is Spring's higher-level IoC container abstraction.
- It extends the core `BeanFactory` contract.
- `AnnotationConfigApplicationContext` can create a context from Java configuration.
- `@Configuration` defines a configuration class.
- `@Bean` registers an object as a Spring-managed bean.
- `getBean()` retrieves a managed bean from the context.
- Beans are singleton-scoped by default.
- An application context provides capabilities beyond basic bean management, including events, resources, messages, and environment support.
- The context should be closed when the application is finished with it.

---

## Next

The next example focuses specifically on **FactoryBean** and how Spring's `FactoryBean` contract allows a bean definition to control the creation of another object.