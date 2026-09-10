# Environment

This example demonstrates Spring's **`Environment`** abstraction and how it provides access to environment-related information such as active and default profiles.

The example uses `AnnotationConfigApplicationContext` to access the `Environment` associated with a Spring application context.

---

## Learning Objectives

By completing this example, you will understand:

- What `Environment` is
- How an `ApplicationContext` exposes its `Environment`
- The difference between `Environment` and `ConfigurableEnvironment`
- What active profiles are
- What default profiles are
- How to configure active profiles programmatically
- How Spring's environment abstraction fits into the application context

---

## What Is Environment?

`Environment` is a Spring abstraction that represents the environment in which an application is running.

It provides access to environment-related information such as:

```text
Environment
    │
    ├── Active Profiles
    ├── Default Profiles
    ├── Properties
    └── Property Sources
```

The `Environment` is available from an `ApplicationContext`:

```java
Environment environment = applicationContext.getEnvironment();
```

The `Environment` itself provides access to environment information. Configuration operations are available through its `ConfigurableEnvironment` subtype.

---

## Why Does Environment Exist?

Spring applications often need to adapt their configuration based on the environment in which they are running.

For example, an application might have different configurations for:

```text
development
testing
production
```

Spring's `Environment` abstraction provides a central API for accessing this information.

Conceptually:

```text
ApplicationContext
        │
        ▼
   Environment
        │
        ├── Active Profiles
        │
        ├── Default Profiles
        │
        ├── Properties
        │
        └── Property Sources
```

This allows Spring's configuration infrastructure to work with environment-specific information without requiring application components to manage it themselves.

---

## Environment and ApplicationContext

An `ApplicationContext` exposes its `Environment` through:

```java
applicationContext.getEnvironment();
```

For example:

```java
try (AnnotationConfigApplicationContext applicationContext =
             new AnnotationConfigApplicationContext()) {

    Environment environment = applicationContext.getEnvironment();

    System.out.println(environment.getActiveProfiles());
}
```

The application context owns and exposes the environment used by the container.

---

## Environment vs ConfigurableEnvironment

`Environment` is primarily used for reading environment information.

```java
Environment environment = applicationContext.getEnvironment();
```

`ConfigurableEnvironment` extends `Environment` and adds configuration operations.

```java
ConfigurableEnvironment environment = applicationContext.getEnvironment();

environment.setActiveProfiles("development");
```

The relationship is:

```text
Environment
    │
    ├── getActiveProfiles()
    ├── getDefaultProfiles()
    └── Other environment access
          ▲
          │ extends
          │
ConfigurableEnvironment
    │
    └── Configuration operations
            └── setActiveProfiles()
```

This distinction is useful because application code may only need to read environment information, while configuration infrastructure may need to modify it.

---

## Active Profiles

Active profiles identify the profiles currently enabled in the environment.

They can be configured programmatically using `ConfigurableEnvironment`:

```java
ConfigurableEnvironment environment = applicationContext.getEnvironment();

environment.setActiveProfiles("development","testing");
```

The active profiles can then be retrieved through `Environment`:

```java
String[] activeProfiles = environment.getActiveProfiles();
```

For example:

```java
ConfigurableEnvironment environment = applicationContext.getEnvironment();

environment.setActiveProfiles("development","testing");

System.out.println(String.join(", ", environment.getActiveProfiles()));
```

The output is:

```text
development, testing
```

---

## Default Profiles

Spring also provides default profiles.

If no active profile is configured, Spring uses the default profile.

The default profile can be retrieved with:

```java
String[] defaultProfiles = environment.getDefaultProfiles();
```

By default, Spring uses:

```text
default
```

For example:

```java
Environment environment = applicationContext.getEnvironment();

System.out.println(String.join(", ", environment.getDefaultProfiles()));
```

The output is:

```text
default
```

---

## Active Profiles vs Default Profiles

Active and default profiles serve different purposes.

| Profile Type | Purpose |
|---|---|
| Active profile | Explicitly enabled profile |
| Default profile | Used when no active profile is configured |

For example:

```text
No active profiles
        │
        ▼
Default profile
    "default"
```

When active profiles are configured:

```text
Active profiles
    │
    ├── development
    └── testing

Default profile
    │
    └── default
```

---

## Configuring Active Profiles

Active profiles should be configured through `ConfigurableEnvironment`.

For example:

```java
ConfigurableEnvironment environment = applicationContext.getEnvironment();

environment.setActiveProfiles("development");
```

Multiple profiles can also be configured:

```java
environment.setActiveProfiles("development","testing");
```

They can then be retrieved using:

```java
environment.getActiveProfiles();
```

---

## Complete Example

### EnvironmentApplication

```java
public class EnvironmentApplication {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext()) {

            Environment environment = applicationContext.getEnvironment();

            System.out.println("Active profiles: " + String.join(", ", environment.getActiveProfiles()));
            System.out.println("Default profiles: " + String.join(", ", environment.getDefaultProfiles()));
        }
    }
}
```

The application accesses the `Environment` through the application context and retrieves its active and default profiles.

---

## Accessing the Environment

The environment can be retrieved directly from the application context:

```java
Environment environment = applicationContext.getEnvironment();
```

This provides read access to environment information.

For example:

```java
String[] activeProfiles = environment.getActiveProfiles();
String[] defaultProfiles = environment.getDefaultProfiles();
```

---

## Configuring the Environment

When configuration is required, the environment can be accessed as a `ConfigurableEnvironment`:

```java
ConfigurableEnvironment environment = applicationContext.getEnvironment();

environment.setActiveProfiles("development");
```

This demonstrates the difference between using the environment as a read-only abstraction and using its configurable subtype.

---

## Environment and Property Sources

The `Environment` also provides access to properties from configured property sources.

For example:

```java
String value = environment.getProperty("application.name");
```

Property access and property source management are covered in more detail in the next example.

This example focuses on the `Environment` abstraction itself and its profile-related functionality.

---

## Environment in Spring's Configuration Infrastructure

The `Environment` is one part of Spring's broader configuration infrastructure.

```text
ApplicationContext
        │
        ├── BeanFactory
        │
        ├── Environment
        │       │
        │       ├── Active Profiles
        │       ├── Default Profiles
        │       ├── Properties
        │       └── Property Sources
        │
        └── Other ApplicationContext Services
```

The `Environment` provides a common abstraction for accessing environment-specific information.

---

## Key Operations

The main operations demonstrated in this example are:

| Operation | Purpose |
|---|---|
| `getEnvironment()` | Retrieves the environment from an application context |
| `getActiveProfiles()` | Returns the currently active profiles |
| `getDefaultProfiles()` | Returns the default profiles |
| `setActiveProfiles()` | Configures active profiles through `ConfigurableEnvironment` |

---

## Dependencies

This example requires Spring Context and JUnit.

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

To run the tests for this module:

```bash
mvn -pl 12-advanced-spring/environment clean test
```

---

## Key Takeaways

- `Environment` represents the environment in which a Spring application runs.
- An `ApplicationContext` exposes its `Environment` through `getEnvironment()`.
- `Environment` provides access to active and default profiles.
- `ConfigurableEnvironment` extends `Environment` with configuration operations.
- Active profiles can be configured programmatically using `setActiveProfiles()`.
- Spring provides the `default` profile when no active profile is configured.
- The `Environment` is also used as part of Spring's property and configuration infrastructure.
- Property sources and property lookup are separate concerns that build on the `Environment` abstraction.

---

## Next

The next example focuses specifically on **Property Sources** and demonstrates how Spring's `Environment` works with different sources of configuration properties.