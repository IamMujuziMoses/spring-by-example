# MockBean

This example demonstrates how to replace a Spring-managed bean with a Mockito mock during testing.

> **Note:** Spring Boot 4 uses `@MockitoBean` instead of the older `@MockBean` annotation. This example uses `@MockitoBean` because the project is built with Spring Boot 4.1.

---

## Overview

When testing a Spring application, a component may depend on another component that is difficult or unnecessary to use during a test.

For example, a service may depend on a repository that normally communicates with a database.

Instead of using the real dependency, Spring's testing support can replace it with a Mockito mock.

This allows the test to control the dependency's behavior and verify how the component interacts with it.

---

## Example

The example contains a `GreetingService` that depends on a `GreetingRepository`.

```java
@Service
public class GreetingService {

    private final GreetingRepository greetingRepository;

    public GreetingService(GreetingRepository greetingRepository) {
        this.greetingRepository = greetingRepository;
    }

    public String greet(Long id) {
        String name = greetingRepository.findNameById(id);
        return "Hello, " + name + "!";
    }
}
```

The repository is a Spring-managed bean:

```java
@Repository
public class GreetingRepository {

    public String findNameById(Long id) {
        return "Spring";
    }
}
```

During the test, the real repository is replaced with a Mockito mock:

```java
@SpringBootTest
class GreetingServiceTest {

    @Autowired
    private GreetingService greetingService;

    @MockitoBean
    private GreetingRepository greetingRepository;

    @Test
    void shouldCreateGreetingUsingMockedRepository() {
        when(greetingRepository.findNameById(1L))
                .thenReturn("Spring");

        String result = greetingService.greet(1L);

        assertEquals("Hello, Spring!", result);

        verify(greetingRepository).findNameById(1L);
    }
}
```

---

## `@MockitoBean`

```java
@MockitoBean
private GreetingRepository greetingRepository;
```

`@MockitoBean` creates a Mockito mock and adds it to the Spring test application context.

If a bean of the same type already exists, the mock replaces that bean for the test.

This means that when `GreetingService` is created by Spring, its `GreetingRepository` dependency is the mock instead of the real repository.

The resulting relationship is:

```text
@SpringBootTest
       │
       ▼
ApplicationContext
       │
       ├── GreetingService
       │       │
       │       ▼
       │   Mock GreetingRepository
       │
       └── Other beans
```

---

## Configuring Mock Behavior

Mockito allows the test to define what the mock should return:

```java
when(greetingRepository.findNameById(1L))
        .thenReturn("Spring");
```

When the service calls:

```java
greetingRepository.findNameById(1L);
```

the mock returns `"Spring"`.

This gives the test complete control over the dependency's behavior.

---

## Verifying Interactions

The test can also verify that the dependency was called correctly:

```java
verify(greetingRepository).findNameById(1L);
```

This verifies that `GreetingService` interacted with the repository as expected.

---

## Why Use Mocking?

Consider a repository that communicates with a database.

Using the real repository during every service test could require:

- Starting a database
- Preparing test data
- Executing database operations
- Cleaning up test data

A mock avoids these dependencies.

Instead, the test can define exactly how the repository behaves:

```text
GreetingService
      │
      ▼
Mock GreetingRepository
      │
      └── returns controlled test data
```

This allows the test to focus on the behavior of `GreetingService`.

---

## `@MockitoBean` vs `@Mock`

A regular Mockito mock can be created using:

```java
@Mock
private GreetingRepository greetingRepository;
```

However, `@Mock` creates a Mockito mock without automatically replacing the corresponding bean in the Spring application context.

`@MockitoBean` integrates the mock with Spring's test context:

```java
@MockitoBean
private GreetingRepository greetingRepository;
```

This allows Spring to inject the mock into other Spring-managed beans.

---

## What Happened to `@MockBean`?

Older versions of Spring Boot provided:

```java
@MockBean
private GreetingRepository greetingRepository;
```

Spring Boot 4 introduced `@MockitoBean` as the replacement:

```java
@MockitoBean
private GreetingRepository greetingRepository;
```

Because this project uses Spring Boot 4.1, the example uses `@MockitoBean`.

The roadmap refers to this concept as **MockBean**, but the implementation uses the current Spring Boot API.

---

## Testing

Run the tests from this module:

```bash
mvn test
```

Or from the project root:

```bash
mvn -pl 11-spring-testing/mock-bean test
```

---

## Key Takeaways

- `@MockitoBean` replaces a Spring-managed bean with a Mockito mock during testing.
- Mocking allows tests to control the behavior of dependencies.
- Mockito's `when()` method can define mock behavior.
- Mockito's `verify()` method can verify interactions.
- `@MockitoBean` integrates Mockito mocks with Spring's application context.
- `@Mock` creates a Mockito mock but does not automatically replace a Spring bean.
- Spring Boot 4 uses `@MockitoBean` instead of the older `@MockBean`.

---

## Next

The next example will demonstrate **`@TestConfiguration`** and how to provide test-specific Spring configuration and beans.