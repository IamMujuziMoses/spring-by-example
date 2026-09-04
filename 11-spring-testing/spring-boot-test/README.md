# @SpringBootTest

This example demonstrates how to use **`@SpringBootTest`** to load the Spring Boot application context and test Spring-managed components.

---

## Overview

`@SpringBootTest` provides support for testing Spring Boot applications by loading the application context used by the application.

Unlike a plain unit test, the test starts the Spring Boot context and allows Spring to create and manage application beans.

This example uses:

- `@SpringBootTest`
- `@SpringBootApplication`
- `@Autowired`
- Spring Boot's application context
- JUnit Jupiter

---

## Example

The example contains a simple `GreetingService` managed by Spring:

```java
@Service
public class GreetingService {

    public String greet(String name) {
        return "Hello, " + name + "!";
    }
}
```

The application uses `@SpringBootApplication`:

```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

The test uses `@SpringBootTest`:

```java
@SpringBootTest
class GreetingServiceTest {

    @Autowired
    private GreetingService greetingService;

    @Test
    void shouldCreateGreeting() {
        String result = greetingService.greet("Spring");

        assertEquals("Hello, Spring!", result);
    }
}
```

---

## `@SpringBootTest`

```java
@SpringBootTest
```

`@SpringBootTest` tells Spring Boot to create the application context for the test.

Spring Boot searches for the application's `@SpringBootConfiguration`, which is normally provided by `@SpringBootApplication`.

The application context is then created and the application's Spring-managed beans become available to the test.

---

## Application Context

The test follows this general flow:

```text
@SpringBootTest
      │
      ▼
Spring Boot Application
      │
      ▼
ApplicationContext
      │
      ├── GreetingService
      └── Other application beans
      │
      ▼
GreetingServiceTest
```

Because `GreetingService` is managed by Spring, it can be injected into the test:

```java
@Autowired
private GreetingService greetingService;
```

---

## Testing the Application Context

`@SpringBootTest` can also be used to verify that the application context starts successfully:

```java
@SpringBootTest
class ApplicationTest {

    @Test
    void contextLoads() {
    }
}
```

This test does not contain any assertions. If the application context cannot start, the test fails.

This can help detect problems with:

- Bean creation
- Application configuration
- Dependency injection
- Missing dependencies
- Spring Boot configuration

---

## Spring TestContext vs `@SpringBootTest`

The previous Spring TestContext example explicitly specified the configuration:

```java
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestContextApplication.class)
```

With `@SpringBootTest`, Spring Boot discovers the application's configuration automatically:

```java
@SpringBootTest
class GreetingServiceTest {
}
```

The difference can be represented as:

```text
Spring TestContext

@ContextConfiguration(...)
        │
        ▼
Specific Spring configuration
        │
        ▼
ApplicationContext
```

```text
@SpringBootTest

@SpringBootTest
        │
        ▼
Spring Boot application configuration
        │
        ▼
ApplicationContext
```

`@SpringBootTest` is useful when you want to test the application using its actual Spring Boot configuration.

---

## Unit Testing vs `@SpringBootTest`

A unit test creates the object directly:

```java
Calculator calculator = new Calculator();
```

No Spring context is involved.

With `@SpringBootTest`, Spring creates and manages the application's beans:

```java
@Autowired
private GreetingService greetingService;
```

This makes `@SpringBootTest` more suitable for tests that need to verify Spring configuration and interactions between Spring-managed components.

---

## Testing

Run the tests from this module:

```bash
mvn test
```

Or from the project root:

```bash
mvn -pl 11-spring-testing/spring-boot-test test
```

---

## Key Takeaways

- `@SpringBootTest` loads the Spring Boot application context for testing.
- Spring Boot discovers the application's configuration through `@SpringBootApplication`.
- Spring-managed beans can be injected into the test.
- `@SpringBootTest` can verify that the application context starts successfully.
- Tests using `@SpringBootTest` involve the Spring container and are therefore different from plain unit tests.
- `@SpringBootTest` is useful when testing application configuration and Spring-managed components.

---

## Related Concepts

The next examples will build on Spring Boot testing with:

- `@MockBean`
- `@TestConfiguration`
- Integration Testing