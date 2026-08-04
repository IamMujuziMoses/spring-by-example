# `@ImportResource`

The `@ImportResource` annotation allows a Java-based Spring configuration class to import bean definitions from an XML configuration file.

Although modern Spring applications typically use Java configuration, many existing applications still rely on XML. `@ImportResource` provides a bridge between these approaches, allowing Java and XML configuration to coexist within the same application.

In this example, you'll learn how Spring imports bean definitions from an XML file into the `ApplicationContext`.

---

## Learning Objectives

By the end of this module, you will be able to:

- Understand the purpose of `@ImportResource`.
- Learn how Java configuration can import XML configuration.
- Understand how Spring processes XML bean definitions.
- Learn when `@ImportResource` is appropriate.
- Understand how Java and XML configuration can work together.

---

## What Is `@ImportResource`?

`@ImportResource` imports one or more XML configuration files into a Java-based Spring application.

```java
@ImportResource("classpath:applicationContext.xml")
public class AppConfig {

}
```

When Spring creates the application context, it loads the XML file, processes its bean definitions, and registers those beans alongside any Java-configured beans.

> **Note:** This example intentionally omits `@Configuration`. Since the class does not declare any `@Bean` methods, the annotation is not required. The focus is solely on demonstrating how `@ImportResource` imports XML configuration.

---

## Does `@ImportResource` Require `@Configuration`?

A common question is whether `@ImportResource` must be used together with `@Configuration`.

The short answer is **no**.

```java
@ImportResource("classpath:applicationContext.xml")
public class AppConfig {

}
```

This works because the class is explicitly registered when creating the application context.

```java
var context =
        new AnnotationConfigApplicationContext(AppConfig.class);
```

Spring inspects the annotations on the registered class, processes `@ImportResource`, loads the XML configuration, and registers the imported bean definitions.

In many real-world Spring applications, however, you'll often see `@ImportResource` used together with `@Configuration`.

```java
@Configuration
@ImportResource("classpath:applicationContext.xml")
public class AppConfig {

}
```

This is the conventional approach because it clearly identifies the class as a configuration class and allows `@Bean` methods to be added later if needed.

For this example, `@Configuration` is intentionally omitted to demonstrate that `@ImportResource` alone is sufficient when the class only imports XML configuration.

---

## XML Configuration

The imported XML file defines a regular Spring bean.

```xml
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
           http://www.springframework.org/schema/beans
           https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="greetingService" class="com.springbyexample.importresource.GreetingService"/>

</beans>
```

Notice that `GreetingService` contains no Spring annotations. The bean is registered entirely through XML.

---

## Running the Application

After creating the application context, the XML-defined bean can be retrieved like any other Spring bean.

```java
GreetingService service = context.getBean(GreetingService.class);

System.out.println(service.greet());
```

Expected output:

```text
Hello from XML configuration!
```

Although the bean is defined in XML, it behaves exactly like any other Spring-managed bean.

---

## How `@ImportResource` Works

```text
                AppConfig
                    │
@ImportResource("applicationContext.xml")
                    │
         applicationContext.xml
                    │
             <bean ... />
                    │
         ApplicationContext
```

Spring processes the XML configuration and registers its bean definitions in the same `ApplicationContext` used by the Java configuration.

---

## Why Use `@ImportResource`?

Many long-lived Spring applications were originally built using XML configuration.

Rather than requiring an entire application to be migrated at once, Spring allows Java configuration to import existing XML files, making gradual migration possible.

This helps teams modernize applications incrementally while continuing to use existing XML-based configuration where appropriate.

---

## Best Practices

- Prefer Java configuration for new applications.
- Use `@ImportResource` when integrating legacy XML configuration.
- Migrate XML configuration gradually instead of rewriting everything at once.
- Keep imported XML configuration focused and well organized.

---

## How It Works Internally

When the application starts, Spring performs the following steps:

```text
Application starts
        │
AppConfig discovered
        │
@ImportResource processed
        │
applicationContext.xml loaded
        │
XML bean definitions parsed
        │
BeanDefinitions created
        │
Beans instantiated
        │
ApplicationContext ready
```

---

## In Practice

Many enterprise Spring applications still use XML configuration alongside Java configuration.

Projects such as **OpenMRS** use this approach extensively, allowing new Java configuration to coexist with existing XML while applications are modernized over time.

Understanding `@ImportResource` makes it easier to work with and gradually migrate these codebases.

---

## Key Takeaways

- `@ImportResource` imports XML configuration into a Java-based application.
- XML-defined beans become part of the same `ApplicationContext`.
- `@Configuration` is not required when the class only imports XML configuration.
- `@ImportResource` is particularly useful when integrating or migrating legacy Spring applications.
- Java and XML configuration can coexist seamlessly.

---

## What's Next?

You've now seen how Spring supports both Java-based and XML-based configuration.

The next example explores **XML Configuration**, where the entire application is configured using XML before comparing it with modern Java-based configuration.