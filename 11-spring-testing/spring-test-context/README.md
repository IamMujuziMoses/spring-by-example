# Spring TestContext

This example demonstrates how to use Spring's **TestContext Framework** to load a Spring `ApplicationContext` and test Spring-managed components with JUnit.

---

## Overview

Spring's TestContext Framework provides support for testing applications that use the Spring Framework.

Unlike a plain unit test, the Spring TestContext example loads a Spring `ApplicationContext`. This allows Spring to create and manage beans during the test and makes features such as dependency injection available.

This example uses:

- JUnit Jupiter
- `SpringExtension`
- `@ContextConfiguration`
- `@Autowired`
- Spring `ApplicationContext`

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

A configuration class enables component scanning:

```java
@Configuration
@ComponentScan
public class TestContextApplication {
}
```

The test loads this configuration using Spring's TestContext Framework:

```java
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestContextApplication.class)
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

## `@ExtendWith`

```java
@ExtendWith(SpringExtension.class)
```

`SpringExtension` integrates Spring's testing support with JUnit Jupiter.

It allows Spring to participate in the JUnit test lifecycle and manage the test's application context.

---

## `@ContextConfiguration`

```java
@ContextConfiguration(classes = TestContextApplication.class)
```

`@ContextConfiguration` tells Spring which configuration to use when creating the `ApplicationContext`.

In this example, Spring uses `TestContextApplication` to discover and create the `GreetingService` bean.

---

## Dependency Injection in Tests

Because the test is running with Spring's TestContext Framework, Spring can inject the `GreetingService` bean:

```java
@Autowired
private GreetingService greetingService;
```

The test does not create the service itself.

Instead, Spring creates and manages the bean as part of the application context.

---

## `@SpringJUnitConfig`

Spring also provides `@SpringJUnitConfig` as a convenient alternative to combining `@ExtendWith` and `@ContextConfiguration`.

Instead of:

```java
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestContextApplication.class)
```

you can use:

```java
@SpringJUnitConfig(TestContextApplication.class)
```

The explicit annotations are used in this example to make the TestContext setup easier to understand.

---

## Unit Testing vs Spring TestContext

The previous Unit Testing example creates the object directly:

```java
Calculator calculator = new Calculator();
```

No Spring application context is involved.

The Spring TestContext example allows Spring to create and manage the object:

```java
@Autowired
private GreetingService greetingService;
```

The difference can be represented as:

```text
Unit Test

Test
 │
 └── new Calculator()
       │
       ▼
   Calculator
```

```text
Spring TestContext

Test
 │
 ▼
SpringExtension
 │
 ▼
ApplicationContext
 │
 ▼
GreetingService
```

---

## Why Use Spring TestContext?

Spring TestContext is useful when the behavior being tested depends on Spring itself.

For example, it allows tests to verify:

- Spring bean creation
- Dependency injection
- Component scanning
- Application configuration
- Interactions between Spring-managed components

It provides more realistic testing than a plain unit test while still allowing the test to load only the configuration it needs.

---

## Testing

Run the tests from this module:

```bash
mvn test
```

Or from the project root:

```bash
mvn -pl 11-spring-testing/spring-test-context test
```

---

## Key Takeaways

- Spring TestContext provides Spring integration for tests.
- `SpringExtension` connects Spring's testing support with JUnit Jupiter.
- `@ContextConfiguration` specifies the configuration used to create the test `ApplicationContext`.
- Spring can inject beans into tests using `@Autowired`.
- Spring TestContext tests are different from plain unit tests because they involve the Spring container.
- `@SpringJUnitConfig` provides a convenient alternative to explicitly declaring `SpringExtension` and `ContextConfiguration`.
- A `@SpringBootApplication` is not required for this example.

---

## Next

The next example will demonstrate **`@SpringBootTest`** and how Spring Boot loads the application context for testing.