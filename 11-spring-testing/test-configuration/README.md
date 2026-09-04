# TestConfiguration

## Overview

This example demonstrates how to use **`@TestConfiguration`** in Spring Boot to provide test-specific beans and configuration.

`@TestConfiguration` is useful when a test needs additional or alternative beans without adding that configuration to the normal application context.

In this example, the application has a production `GreetingProvider`, while the test imports a separate `TestGreetingConfiguration` that provides a test-specific implementation.

---

## Concepts Covered

- `@TestConfiguration`
- `@Bean`
- `@Import`
- `@Primary`
- Test-specific Spring beans
- Customizing the Spring application context for tests

---

## How It Works

The application defines a `GreetingProvider` interface:

```java
public interface GreetingProvider {

    String getGreeting();
}
```

The normal application uses `DefaultGreetingProvider`:

```java
@Component
public class DefaultGreetingProvider implements GreetingProvider {

    @Override
    public String getGreeting() {
        return "Hello from production!";
    }
}
```

`GreetingService` depends on the `GreetingProvider`:

```java
@Service
public class GreetingService {

    private final GreetingProvider greetingProvider;

    public GreetingService(GreetingProvider greetingProvider) {
        this.greetingProvider = greetingProvider;
    }

    public String greet() {
        return greetingProvider.getGreeting();
    }
}
```

### Test Configuration

The test defines an alternative `GreetingProvider` using `@TestConfiguration`:

```java
@TestConfiguration
public class TestGreetingConfiguration {

    @Bean
    @Primary
    GreetingProvider greetingProvider() {
        return () -> "Hello from the test!";
    }
}
```

The test configuration is explicitly imported:

```java
@SpringBootTest
@Import(TestGreetingConfiguration.class)
class GreetingServiceTest {
    // ...
}
```

Because the test provider is marked with `@Primary`, Spring prefers it over the production `DefaultGreetingProvider` when injecting a `GreetingProvider`.

As a result, the test receives:

```text
Hello from the test!
```

instead of:

```text
Hello from production!
```

---

## `@TestConfiguration`

`@TestConfiguration` is a specialized form of Spring configuration intended for tests.

It allows tests to define beans that should be available in the test application context without making them part of the normal production configuration.

For example:

```java
@TestConfiguration
public class TestGreetingConfiguration {

    @Bean
    GreetingProvider greetingProvider() {
        return () -> "Hello from the test!";
    }
}
```

The configuration can then be included in a test with:

```java
@Import(TestGreetingConfiguration.class)
```

---

## Why Use `@TestConfiguration`?

`@TestConfiguration` is useful when:

- A test needs additional Spring beans.
- A test needs an alternative implementation of a dependency.
- Production configuration should remain unchanged.
- Different tests require different application configurations.
- You want to customize the Spring context specifically for testing.

---

## `@TestConfiguration` vs `@Configuration`

A regular configuration class is part of the application's normal configuration:

```java
@Configuration
public class ApplicationConfiguration {
}
```

A test configuration is specifically intended for tests:

```java
@TestConfiguration
public class TestConfiguration {
}
```

This separation prevents test-specific configuration from accidentally becoming part of the application's production configuration.

---

## `@Primary`

This example contains two `GreetingProvider` beans:

```text
DefaultGreetingProvider
TestGreetingConfiguration
└── GreetingProvider
```

Both implement the same interface.

The test provider is marked with `@Primary`:

```java
@Bean
@Primary
GreetingProvider greetingProvider() {
    return () -> "Hello from the test!";
}
```

`@Primary` tells Spring to prefer this bean when multiple candidates are available for dependency injection.

---

## Testing

Run the tests from the module directory:

```bash
mvn test
```

Or from the project root:

```bash
mvn -pl 11-spring-testing/test-configuration test
```

The test verifies that the `GreetingService` uses the test-specific `GreetingProvider`.

---

## Key Takeaways

- `@TestConfiguration` defines Spring configuration specifically for tests.
- Test configurations can provide additional or alternative beans.
- `@Import` can explicitly include a test configuration in the test context.
- `@Primary` can be used when the test context contains multiple beans of the same type.
- Test-specific configuration allows production code to remain unchanged.

---

## Next

The next example will demonstrate **Integration Testing** and how to test multiple parts of a Spring application together using the application context.