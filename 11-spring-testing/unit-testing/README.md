# Unit Testing

This example demonstrates how to write **unit tests** for Java classes using JUnit without starting the Spring application context.

---

## Overview

Unit testing focuses on testing a single class or piece of functionality in isolation.

Unlike Spring integration tests, unit tests do not require:

- A Spring `ApplicationContext`
- `@SpringBootTest`
- `@Autowired`
- Spring configuration
- A running application

This makes unit tests fast, focused, and independent of the Spring container.

---

## Example

The example uses a simple `Calculator` class containing basic arithmetic operations.

```java
public class Calculator {

    public int add(int first, int second) {
        return first + second;
    }

    public int subtract(int first, int second) {
        return first - second;
    }

    public int multiply(int first, int second) {
        return first * second;
    }

    public int divide(int first, int second) {
        return first / second;
    }
}
```

The class is tested directly using JUnit:

```java
class CalculatorTest {

    private final Calculator calculator = new Calculator();

    @Test
    void shouldAddTwoNumbers() {
        assertEquals(5, calculator.add(2, 3));
    }
}
```

The `Calculator` instance is created directly with `new Calculator()`. No Spring container is involved.

---

## Why Unit Testing?

Unit tests help verify that individual pieces of application logic behave as expected.

They are useful because they are:

- **Fast** — no application context needs to start.
- **Focused** — each test targets a specific unit of functionality.
- **Independent** — tests do not depend on other application components.
- **Easy to run** — they can be executed without starting the application.

---

## Unit Testing vs Spring Testing

A unit test tests a class in isolation:

```text
CalculatorTest
      │
      ▼
Calculator
```

Spring-based tests involve the Spring container:

```text
Test
  │
  ▼
Spring ApplicationContext
  │
  ├── Service
  ├── Repository
  └── Other Beans
```

This distinction is important when deciding which type of test to write.

If the behavior can be tested without Spring, a unit test is often the simplest and fastest option.

---

## Testing

Run the tests from this module:

```bash
mvn test
```

Or from the project root:

```bash
mvn -pl 11-spring-testing/unit-testing test
```

---

## Key Takeaways

- Unit tests focus on testing individual classes in isolation.
- Unit tests do not require the Spring application context.
- JUnit provides the framework for writing and running the tests.
- Dependencies can be created directly when testing a class in isolation.
- Unit tests are generally faster than tests that start the Spring context.
- Not every test in a Spring application needs to involve Spring.

---

## Related Concepts

This example is the starting point for Spring testing.

The following examples will introduce Spring's testing infrastructure, including:

- Spring TestContext
- `@SpringBootTest`
- `@MockBean`
- `@TestConfiguration`
- Integration Testing