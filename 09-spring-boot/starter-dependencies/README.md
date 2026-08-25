# Spring Boot Starter Dependencies

Demonstrates how Spring Boot starter dependencies provide a convenient way to add a group of related dependencies to an application.

---

## Overview

Spring Boot starters are dependency descriptors that provide a convenient way to include the dependencies commonly required for a particular type of application.

Instead of manually adding several related dependencies, an application can depend on a single starter.

For example:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

The `spring-boot-starter-web` starter provides the dependencies needed for building a Spring web application, including Spring Web MVC and an embedded web server.

This example demonstrates the effect of adding a web starter to a Spring Boot application.

---

## Learning Objectives

- Understand what Spring Boot starter dependencies are.
- Learn why Spring Boot provides starter dependencies.
- Understand how starters simplify dependency management.
- Learn the difference between a starter dependency and auto-configuration.
- Understand how `spring-boot-starter-web` brings web-related dependencies into an application.
- Observe how adding a web starter causes Spring Boot to configure an embedded Tomcat server.
- Learn how to inspect starter dependencies using Maven's dependency tree.

---

## Implementation

### Application

The application uses `@SpringBootApplication` and `SpringApplication.run()` to start the Spring Boot application.

```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### Starter Dependency

The important dependency in this example is:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

Instead of manually declaring the individual dependencies required for a web application, the starter provides them as a convenient dependency set.

Conceptually:

```text
spring-boot-starter-web
        │
        ├── Spring Web
        ├── Spring MVC
        ├── JSON support
        ├── Embedded Tomcat
        └── other web-related dependencies
```

The exact dependency tree is managed by the Spring Boot release.

---

## Starter Dependencies vs Auto-Configuration

Starter dependencies and auto-configuration are related, but they solve different problems.

### Starter Dependencies

A starter makes it easier to add a group of related dependencies:

```text
Starter
   │
   ▼
Related dependencies
```

For example:

```text
spring-boot-starter-web
        │
        ├── Spring Web
        ├── Spring MVC
        ├── Jackson
        └── Embedded Tomcat
```

### Auto-Configuration

Auto-configuration uses the dependencies available on the classpath to configure appropriate Spring beans automatically:

```text
Dependencies
      │
      ▼
Spring Boot detects them
      │
      ▼
Auto-configuration
      │
      ▼
Configured application
```

Together, the concepts work like this:

```text
spring-boot-starter-web
        │
        ▼
Web dependencies become available
        │
        ▼
Spring Boot detects those dependencies
        │
        ▼
Web auto-configuration is applied
        │
        ▼
Embedded Tomcat starts
```

---

## Observing the Difference

The `spring-application` example uses the basic Spring Boot starter and does not start a web server.

Its output ends after the application context starts:

```text
Started Application in 1.095 seconds
```

The application can then exit because there is no server keeping the JVM alive.

After adding:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

the application starts an embedded Tomcat server:

```text
Tomcat initialized with port 8080 (http)
```

and:

```text
Tomcat started on port 8080 (http)
```

The application remains running because Tomcat is listening for HTTP requests.

This demonstrates how adding a starter can change the capabilities and runtime behavior of a Spring Boot application.

---

## Inspecting Starter Dependencies

Maven can be used to inspect the dependency tree:

```bash
mvn dependency:tree
```

Look for the dependencies introduced by:

```text
spring-boot-starter-web
```

This is a useful way to understand what a starter actually brings into the application.

You can also compare the dependency tree with the `spring-application` example to see the additional web-related dependencies.

---

## Why Use Starters?

Without starters, an application would need to explicitly declare many related dependencies.

For example, a web application might need dependencies for:

- Spring Web
- Spring MVC
- JSON processing
- Embedded servlet container
- Other web infrastructure

A starter provides a convenient dependency entry point instead.

This makes the project's `pom.xml` easier to understand and reduces the amount of dependency configuration developers need to maintain manually.

---

## Running the Example

From the module directory:

```bash
mvn spring-boot:run
```

Or run the `Application` class directly from your IDE.

The application should start an embedded Tomcat server on port `8080`.

You should see output similar to:

```text
Tomcat initialized with port 8080 (http)
Tomcat started on port 8080 (http)
Started Application in ...
```

Because the web server keeps the application running, stop the application manually when finished.

---

## Testing

Run the tests with:

```bash
mvn test
```

The tests verify that:

- The Spring Boot application context starts successfully.
- The application can be initialized with the web starter.

---

## Key Takeaways

- A Spring Boot starter is a convenient way to declare a group of related dependencies.
- `spring-boot-starter-web` provides the dependencies commonly needed for web applications.
- Starters simplify Maven dependency management.
- Starters and auto-configuration are different concepts.
- Starters make dependencies available on the classpath.
- Auto-configuration detects those dependencies and configures the application accordingly.
- Adding `spring-boot-starter-web` causes Spring Boot to configure an embedded web server.
- Maven's `dependency:tree` command can be used to inspect what a starter brings into the application.

---

## Next Step

The next example explores **Configuration Properties**, demonstrating how Spring Boot applications can externalize and bind configuration values.