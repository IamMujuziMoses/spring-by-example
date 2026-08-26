# Profiles

This example demonstrates how Spring Boot profiles allow different configuration to be loaded depending on the active environment.

Profiles are useful when an application needs different settings for environments such as development, testing, and production.

---

## What This Example Demonstrates

- Spring Boot profiles
- `application.properties`
- Profile-specific configuration files
- `spring.profiles.active`
- Selecting profiles from the command line
- `@ActiveProfiles` in tests
- Environment-specific configuration

---

## How It Works

Spring Boot uses the following naming convention for profile-specific configuration:

```text
application-{profile}.properties
```

For example:

```text
application-dev.properties
application-prod.properties
```

The `dev` profile loads:

```text
application.properties
application-dev.properties
```

while the `prod` profile loads:

```text
application.properties
application-prod.properties
```

When the same property exists in both files, the profile-specific value takes precedence.

---

## Configuration

The common configuration is stored in `application.properties`:

```properties
app.environment=default
```

The development configuration is stored in `application-dev.properties`:

```properties
app.environment=development
```

The production configuration is stored in `application-prod.properties`:

```properties
app.environment=production
```

---

## Selecting a Profile

A profile can be selected when starting the application.

Using Maven:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

The application prints:

```text
Active environment: development
```

To use the production configuration:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

The output becomes:

```text
Active environment: production
```

The packaged application can also be started with:

```bash
java -jar target/profiles.jar --spring.profiles.active=prod
```

---

## Setting a Default Profile

A profile can also be configured directly in `application.properties`:

```properties
spring.profiles.active=dev
```

With this configuration, Spring Boot automatically activates the `dev` profile when the application starts.

For this example, leaving the profile unspecified and selecting it at runtime better demonstrates why profiles are useful: the same application can be started with different configurations without changing the source code.

---

## Application

The application reads the configured environment using `@Value`:

```java
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Value("${app.environment}")
    private String environment;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("Active environment: " + environment);
    }
}
```

The Java code does not need to change when switching between environments.

Only the active profile changes.

---

## `@ActiveProfiles` vs `spring.profiles.active`

These two mechanisms serve different purposes.

### `spring.profiles.active`

Used when running the application:

```bash
java -jar target/profiles.jar --spring.profiles.active=prod
```

or:

```properties
spring.profiles.active=prod
```

### `@ActiveProfiles`

Used primarily when running tests:

```java
@ActiveProfiles("dev")
```

This allows a test to explicitly control which Spring profile is active.

---

## Profile Configuration Flow

```text
                 Spring Boot Application
                           │
                           ▼
                    Active Profile
                           │
              ┌────────────┴────────────┐
              ▼                         ▼
     application-dev.properties   application-prod.properties
              │                         │
              ▼                         ▼
        development                production
```

The important idea is that the application code remains the same while configuration can change based on the environment.

---

## Why Profiles Matter

A typical application may need different configuration for:

- Local development
- Testing
- Staging
- Production

For example:

```text
Development
    └── Local database
    └── Debug logging
    └── Development services

Production
    └── Production database
    └── Reduced logging
    └── Production services
```

Profiles provide a way to separate these configurations without creating different versions of the application.

---

## Key Takeaway

Spring Boot profiles allow applications to load environment-specific configuration.

The basic pattern is:

```text
application.properties
        +
application-{profile}.properties
        │
        ▼
   Active profile
        │
        ▼
Environment-specific configuration
```

The same application can therefore run with different configuration simply by selecting a different profile.

---

## Next

**Next up:** CommandLineRunner

The next example will demonstrate how `CommandLineRunner` allows code to execute automatically after the Spring Boot application context has been initialized.