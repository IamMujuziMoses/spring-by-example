# SpringApplication

## Description

This example demonstrates how `SpringApplication` is used to bootstrap a Spring Boot application.

Unlike the previous Spring examples, there is no separate `Main` class or `AppConfig` class. Spring Boot uses a single application class annotated with `@SpringBootApplication` as the application's entry point and configuration source.

## Learning Objectives

- Understand what `SpringApplication` is.
- Learn how `SpringApplication.run()` bootstraps a Spring Boot application.
- Understand the role of `@SpringBootApplication`.
- Understand why a separate `Main` class is not required.
- Understand why a separate `@Configuration` class is not required for basic Bootstrapping.
- Learn how Spring Boot creates and manages the application context.
- Understand how Spring Boot discovers Spring-managed components.
- Compare Spring's traditional `AnnotationConfigApplicationContext` approach with Spring Boot's `SpringApplication`.

---

## Implementation

### Application

`Application` is the entry point of the Spring Boot application.

```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

The important part is:

```java
SpringApplication.run(Application.class, args);
```

This starts the Spring application and creates the application context.

### `@SpringBootApplication`

`@SpringBootApplication` is a convenience annotation that combines several commonly used Spring annotations:

```java
@Configuration
@EnableAutoConfiguration
@ComponentScan
```

Conceptually, the application class provides the following:

```text
@SpringBootApplication
        │
        ├── @Configuration
        │      └── Provides application configuration
        │
        ├── @EnableAutoConfiguration
        │      └── Enables Spring Boot auto-configuration
        │
        └── @ComponentScan
               └── Discovers Spring components
```

This allows a basic Spring Boot application to be bootstrapped with very little configuration.

### GreetingService

A simple Spring-managed service is included to demonstrate that the application context is created successfully and that component scanning discovers application beans.

```java
@Service
public class GreetingService {

    public String greet(String name) {
        return "Hello, " + name + "!";
    }
}
```

Because `GreetingService` is annotated with `@Service`, Spring Boot discovers it through component scanning.

---

## SpringApplication

`SpringApplication` is responsible for bootstrapping the Spring application.

The following call starts the application:

```java
SpringApplication.run(Application.class, args);
```

At a high level, Spring Boot:

1. Creates the application context.
2. Registers the application configuration.
3. Performs component scanning.
4. Applies auto-configuration.
5. Creates and initializes Spring-managed beans.
6. Starts the application.

For a simple application, this replaces a significant amount of manual bootstrapping code.

---

## Comparing Traditional Spring and Spring Boot

Earlier examples in this project used `AnnotationConfigApplicationContext` directly:

```java
try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

    GreetingService greetingService = context.getBean(GreetingService.class);
}
```

The Spring Boot equivalent starts the application with:

```java
SpringApplication.run(Application.class, args);
```

The difference can be summarized as:

```text
Traditional Spring

Main
 │
 └── AnnotationConfigApplicationContext
          │
          └── AppConfig
                 │
                 └── Spring beans


Spring Boot

Application
 │
 └── SpringApplication.run()
          │
          └── @SpringBootApplication
                 │
                 ├── Configuration
                 ├── Component Scanning
                 └── Auto-Configuration
```

The goal is not that Spring Boot eliminates the Spring application context. Instead, it provides a convenient way to configure and bootstrap it.

---

## Why There Is No Separate `Main.java`

This example intentionally does not have a separate `Main.java`.

The `main()` method is placed inside `Application.java`:

```java
public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
}
```

Therefore, `Application.java` acts as the application's entry point.

This is the conventional structure for a Spring Boot application.

There is also no separate `AppConfig.java` because `@SpringBootApplication` provides the primary configuration entry point for this simple application.

---

## Running the Example

From the project root:

```bash
mvn clean install
```

To run only this module:

```bash
mvn -pl 09-spring-boot/spring-application spring-boot:run
```

You can also run `Application.main()` directly from your IDE.

When the application starts successfully, Spring Boot will print a startup message similar to:

```text
Started Application in 0.XXX seconds
```

---

## Key Takeaways

- `SpringApplication` is used to bootstrap Spring Boot applications.
- `SpringApplication.run()` creates and starts the application context.
- `@SpringBootApplication` provides the main configuration entry point.
- `@SpringBootApplication` combines `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan`.
- A separate `Main.java` class is not necessary.
- A separate `AppConfig.java` class is not necessary for this basic example.
- `@Service` beans are discovered through component scanning.
- Spring Boot simplifies application startup while still relying on the Spring application context.

---

## What This Example Does Not Cover

This example intentionally focuses only on application bootstrapping.

The following topics are covered separately in this module:

- Auto Configuration
- Starter Dependencies
- Configuration Properties
- Profiles
- `CommandLineRunner`
- Actuator

Keeping these topics separate makes it easier to understand what `SpringApplication` itself does before introducing the additional features provided by Spring Boot.

---

## Related Concepts

- `SpringApplication`
- `@SpringBootApplication`
- `ApplicationContext`
- `@Configuration`
- `@ComponentScan`
- `@EnableAutoConfiguration`

## Next Example

The next example explores Spring Boot **Auto Configuration** and demonstrates how Spring Boot can automatically configure application infrastructure based on the dependencies available on the classpath.