# Mixed XML and Java Configuration

Spring allows XML configuration and Java configuration to coexist within the same application.

This makes it possible to modernize applications gradually by introducing Java-based configuration while continuing to use existing XML configuration where appropriate.

In this example, you'll build a Spring application that combines both approaches, demonstrating how Spring merges bean definitions from multiple configuration sources into a single `ApplicationContext`.

---

## Learning Objectives

By the end of this module, you will be able to:

- Understand how XML and Java configuration work together.
- Learn how Spring combines bean definitions from multiple sources.
- Configure beans using both XML and Java.
- Understand when a hybrid configuration approach is useful.
- Recognize how legacy Spring applications can be modernized incrementally.

---

## Why Mix XML and Java Configuration?

Many enterprise applications were originally built using XML configuration.

As Spring evolved, Java Configuration became the preferred approach because it is type-safe, easier to maintain, and better supported by modern IDEs.

Rather than requiring an entire application to be rewritten, Spring allows both configuration styles to work together.

This enables teams to migrate applications incrementally while continuing to support existing XML-based modules.

---

## Java Configuration

`AppConfig` contributes beans using `@Bean` methods and imports the XML configuration.

```java
@ImportResource("classpath:applicationContext.xml")
public class AppConfig {

    @Bean
    TimeService timeService() {
        return new TimeService();
    }

}
```

---

## XML Configuration

The XML file contributes additional bean definitions.

```xml
<beans>

    <bean id="greetingService" class="com.springbyexample.mixedconfiguration.GreetingService"/>

</beans>
```

Both configuration sources are processed during application startup.

---

## Running the Application

Beans defined in XML and Java are available from the same application context.

```java
try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

    GreetingService greeting = context.getBean(GreetingService.class);

    TimeService time = context.getBean(TimeService.class);

    System.out.println(greeting.greet());
    System.out.println(time.timezone());
}
```

Expected output:

```text
Hello
UTC
```

The application does not distinguish between beans defined in XML and beans defined in Java. Both are managed identically by Spring.

---

## How Mixed Configuration Works

```text
               AppConfig
                   │
      ┌────────────┴────────────┐
@Bean methods            @ImportResource
      │                         │
 TimeService          applicationContext.xml
                                │
                        GreetingService
      └────────────┬────────────┘
          ApplicationContext
```

Spring processes both configuration sources, merges the resulting bean definitions, and creates a single `ApplicationContext`.

---

## Configuration Comparison

| Java Configuration | XML Configuration |
|--------------------|-------------------|
| Uses `@Configuration` and `@Bean` | Uses `<beans>` and `<bean>` |
| Type-safe | XML-based |
| Compiled with the application | Stored as external configuration |
| Preferred for new applications | Common in legacy applications |

Regardless of how a bean is defined, it becomes a Spring-managed bean within the same IoC container.

---

## Why Use Mixed Configuration?

A hybrid configuration approach is particularly useful when:

- Migrating a large XML-based application to Java Configuration.
- Integrating legacy modules into a modern Spring application.
- Adopting Java Configuration incrementally.
- Maintaining existing XML configuration while developing new features in Java.

This approach minimizes risk by allowing migration to occur in manageable stages.

---

## Best Practices

- Prefer Java Configuration for new development.
- Keep XML configuration limited to legacy or externalized components.
- Use `@ImportResource` to integrate XML configuration into Java applications.
- Migrate XML gradually rather than rewriting everything at once.
- Keep configuration organized by feature or responsibility.

---

## How It Works Internally

When the application starts, Spring performs the following steps:

```text
Application starts
        │
@Configuration processed
        │
        ├───────────────┐
@Bean methods    @ImportResource
        │               │
BeanDefinitions merged
        │
Dependencies resolved
        │
Beans instantiated
        │
ApplicationContext ready
```

Spring merges bean definitions from both Java and XML before creating the application context.

---

## In Practice

Many mature Spring applications use both XML and Java configuration.

For example, projects such as **OpenMRS** have gradually introduced Java-based configuration while continuing to support existing XML configuration. This incremental approach allows applications to modernize without requiring a complete rewrite.

Understanding mixed configuration is valuable when maintaining or evolving long-lived Spring applications.

---

## Key Takeaways

- Spring supports XML and Java configuration within the same application.
- `@ImportResource` bridges Java configuration and XML configuration.
- Bean definitions from multiple sources are merged into one `ApplicationContext`.
- XML-defined and Java-defined beans are managed identically.
- Mixed configuration enables incremental migration from XML to Java.

---

## Configuration Journey

Throughout this module, you've explored the major configuration styles supported by Spring:

```text
@Configuration
        ↓
@Bean
        ↓
@ComponentScan
        ↓
@Import
        ↓
@ImportResource
        ↓
XML Configuration
        ↓
Java Configuration
        ↓
Mixed XML and Java Configuration ✓
```

---

## What's Next?

Congratulations! You've completed **Module 3 — Configuration**.

Next, you'll begin **Module 4 — Dependency Injection**, where you'll explore advanced dependency injection techniques such as `@Primary`, `@Qualifier`, optional dependencies, collection injection, and more complex bean resolution scenarios.