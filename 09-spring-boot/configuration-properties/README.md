# Configuration Properties

This example demonstrates how Spring Boot binds external configuration values from `application.properties` to a Java object using `@ConfigurationProperties`.

---

## What This Example Demonstrates

- `application.properties`
- `@ConfigurationProperties`
- Configuration property prefixes
- Type-safe configuration binding
- `@ConfigurationPropertiesScan`
- Injecting configuration properties into a Spring bean

---

## How It Works

Configuration values are defined in `application.properties`:

```properties
app.name=Spring by Example
app.description=Learning Spring Boot through small runnable examples
app.version=1.0
```

The `AppProperties` class maps these values using the `app` prefix:

```java
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String name;
    private String description;
    private String version;

    // getters and setters
}
```

Spring Boot discovers the configuration properties class through:

```java
@ConfigurationPropertiesScan
```

This makes `AppProperties` available as a Spring bean that can be injected into other components.

For example:

```java
@Service
public class GreetingService {

    private final AppProperties properties;

    public GreetingService(AppProperties properties) {
        this.properties = properties;
    }
}
```

---

## Running the Example

From the project root:

```bash
mvn -pl 09-spring-boot/configuration-properties spring-boot:run
```

Or run `Application.main()` directly from your IDE.

The application prints the values loaded from `application.properties`.

Example output:

```text
Application: Spring by Example
Description: Learning Spring Boot through small runnable examples
Version: 1.0
```

---

## `@ConfigurationProperties` vs `@Value`

Spring provides more than one way to access configuration values.

For example, individual values can be injected using `@Value`:

```java
@Value("${app.name}")
private String name;
```

For a small number of unrelated values, this can be convenient.

`@ConfigurationProperties` is better suited to groups of related configuration:

```java
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String name;
    private String description;
    private String version;
}
```

This keeps configuration in one dedicated object and makes it easier to work with larger configuration structures.

---

## Key Takeaway

`@ConfigurationProperties` provides a type-safe way to bind external configuration to a Java object.

The basic flow is:

```text
application.properties
        │
        ▼
   app.name
   app.description
   app.version
        │
        ▼
@ConfigurationProperties(prefix = "app")
        │
        ▼
    AppProperties
        │
        ▼
    Spring Bean
        │
        ▼
   Application Code
```

This allows application configuration to remain outside the Java source code while still being easy to consume from Spring-managed components.

---

## Related Concepts

- Spring Boot
- Externalized Configuration
- `application.properties`
- `@ConfigurationProperties`
- `@ConfigurationPropertiesScan`
- Type-safe configuration

---

## Next

**Next up:** Profiles

The next example will demonstrate how Spring Boot profiles allow different configuration and application behavior to be selected for different environments, such as development, testing, and production.
