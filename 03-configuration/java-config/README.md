# Java Configuration

Java Configuration is the modern, type-safe approach to configuring Spring applications.

Instead of declaring beans in XML, Java Configuration uses `@Configuration` classes and `@Bean` methods to define and wire application components.

In this example, you'll build the same application from the previous XML Configuration example, but using Java instead of XML.

---

## Learning Objectives

By the end of this module, you will be able to:

- Understand Java-based configuration in Spring.
- Learn how `@Configuration` and `@Bean` work together.
- Configure constructor injection using Java.
- Compare Java Configuration with XML Configuration.
- Understand why Java Configuration is the preferred approach for modern Spring applications.

---

## What Is Java Configuration?

Java Configuration uses Java classes to define and configure Spring beans.

A class annotated with `@Configuration` contains one or more `@Bean` methods that create the objects managed by the Spring IoC container.

```java
@Configuration
public class AppConfig {

    @Bean
    GreetingRepository greetingRepository() {
        return new GreetingRepository();
    }

}
```

When Spring creates the application context, it processes the configuration class, invokes each `@Bean` method, and registers the returned objects as Spring beans.

---

## Configuring Dependencies

Dependencies are expressed directly in Java.

```java
@Bean
GreetingService greetingService() {
    return new GreetingService(greetingRepository());
}
```

Rather than using XML elements such as `<constructor-arg>`, dependencies are wired using ordinary constructor calls.

This approach is fully type-safe and benefits from IDE support such as refactoring, navigation, and compile-time checking.

---

## Complete Application Configuration

The entire application is configured within `AppConfig`.

```text
AppConfig
    │
    ├── greetingRepository()
    ├── greetingService()
    └── greetingController()
```

Each `@Bean` method contributes a bean to the `ApplicationContext`.

---

## Running the Application

The application context is created from the configuration class.

```java
try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

    GreetingController controller = context.getBean(GreetingController.class);

    System.out.println(controller.greet());
}
```

Expected output:

```text
Hello from Java configuration!
```

Although the application is configured entirely in Java, the IoC container manages the beans exactly as it does for XML configuration.

---

## How Java Configuration Works

When the application starts, Spring performs the following steps:

```text
@Configuration
        │
@Bean methods discovered
        │
BeanDefinitions created
        │
Constructor dependencies resolved
        │
Beans instantiated
        │
ApplicationContext ready
```

---

## Java Configuration vs XML Configuration

| Java Configuration | XML Configuration |
|--------------------|-------------------|
| `@Configuration` class | `applicationContext.xml` |
| `@Bean` methods | `<bean>` elements |
| Constructor calls | `<constructor-arg>` |
| `AnnotationConfigApplicationContext` | `ClassPathXmlApplicationContext` |
| Type-safe | String-based configuration |
| IDE refactoring support | Limited IDE support |

Both approaches configure the same Spring IoC container and ultimately produce the same result: Spring-managed beans.

---

## Why Use Java Configuration?

Java Configuration has become the preferred approach because it offers several advantages.

- Type-safe configuration.
- Better IDE support.
- Easier refactoring.
- Compile-time validation.
- Reduced XML boilerplate.
- Improved readability for many applications.

These benefits make Java Configuration the recommended choice for new Spring projects.

---

## Best Practices

- Keep configuration classes focused on a specific responsibility.
- Prefer constructor injection when creating beans.
- Organize large applications into multiple configuration classes.
- Use `@Import` to compose related configurations.
- Separate configuration from application logic.

---

## In Practice

Most modern Spring Framework and Spring Boot applications use Java Configuration as their primary configuration style.

Large enterprise applications may still combine Java and XML configuration, especially when integrating legacy systems or migrating existing codebases.

Understanding Java Configuration is essential for developing contemporary Spring applications.

---

## Key Takeaways

- Java Configuration replaces XML with ordinary Java classes.
- `@Configuration` identifies configuration classes.
- `@Bean` methods define Spring-managed beans.
- Constructor injection is expressed directly in Java.
- Java Configuration is the preferred approach for modern Spring applications.

---

## What's Next?

You've now configured the same application using both XML and Java.

The next example explores **Mixing XML and Java Configuration**, demonstrating how both approaches can coexist within the same Spring application.