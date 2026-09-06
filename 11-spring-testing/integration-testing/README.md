# Integration Testing

Spring integration testing verifies that multiple parts of an application work together correctly.

Unlike a unit test, which normally tests a single component in isolation, an integration test can load the Spring application context, start the application, and exercise several real components together.

This example demonstrates integration testing with `@SpringBootTest`, `@AutoConfigureTestRestTemplate`, and `TestRestTemplate`.

The test sends a real HTTP request to a running Spring Boot application and verifies the response produced by the complete application flow.

---

## Learning Objectives

By the end of this example, you will understand:

- What integration testing is.
- How integration testing differs from unit testing.
- Why integration tests are useful.
- How `@SpringBootTest` loads the Spring Boot application context.
- How `RANDOM_PORT` starts an embedded web server for testing.
- What `TestRestTemplate` is.
- How `@AutoConfigureTestRestTemplate` configures `TestRestTemplate`.
- How to make HTTP requests from an integration test.
- How multiple Spring-managed components work together during an integration test.
- Why integration tests generally use real application components instead of mocks.
- How a request can be tested across the controller, service, and repository layers.

---

# What Is Integration Testing?

Integration testing verifies that multiple parts of an application work correctly together.

For example, instead of testing a service by itself:

```text
GreetingService
      ↓
Mock GreetingRepository
```

an integration test can use the real components:

```text
GreetingController
      ↓
GreetingService
      ↓
GreetingRepository
```

The goal is not only to verify that each individual component works, but also that the components are correctly connected and interact as expected.

For a web application, an integration test can go one step further and test the application through HTTP:

```text
HTTP Request
      ↓
GreetingController
      ↓
GreetingService
      ↓
GreetingRepository
      ↓
HTTP Response
```

This is what the example demonstrates.

---

# Why Do Integration Tests Exist?

Unit tests are excellent for testing individual pieces of application logic.

However, an application can have correctly implemented individual components while still having problems in the way those components are configured or connected.

For example:

```text
GreetingController
       ↓
   GreetingService
       ↓
GreetingRepository
```

A unit test might verify each component separately.

An integration test verifies that:

- Spring discovers the components.
- Spring creates the components.
- Dependencies are injected correctly.
- The controller is mapped to the expected URL.
- The service is available to the controller.
- The repository is available to the service.
- The HTTP request reaches the controller.
- The expected response is returned.

This provides confidence that the different parts of the application work together.

---

# Example Application

The example contains three application components:

```text
GreetingRepository
        ↑
        │
GreetingService
        ↑
        │
GreetingController
        ↑
        │
   HTTP Request
```

The request flow is:

```text
GET /greeting
      ↓
GreetingController
      ↓
GreetingService
      ↓
GreetingRepository
      ↓
"Hello from the repository!"
```

---

# GreetingRepository

The repository provides the greeting used by the application.

```java
@Repository
public class GreetingRepository {

    public String findGreeting() {
        return "Hello from the repository!";
    }
}
```

The `@Repository` annotation registers the class as a Spring bean.

Spring will automatically discover it during component scanning.

The repository contains a simple method:

```java
findGreeting()
```

which returns:

```text
Hello from the repository!
```

There is intentionally no database involved in this example.

The goal is to demonstrate integration testing rather than persistence.

---

# GreetingService

The service depends on `GreetingRepository`.

```java
@Service
public class GreetingService {

    private final GreetingRepository greetingRepository;

    public GreetingService(GreetingRepository greetingRepository) {
        this.greetingRepository = greetingRepository;
    }

    public String getGreeting() {
        return greetingRepository.findGreeting();
    }
}
```

The service is registered as a Spring bean using:

```java
@Service
```

Spring automatically injects the `GreetingRepository` through the constructor.

The service delegates the greeting lookup to the repository:

```text
GreetingService
      ↓
GreetingRepository
      ↓
findGreeting()
```

---

# GreetingController

The controller exposes the greeting through an HTTP endpoint.

```java
@RestController
public class GreetingController {

    private final GreetingService greetingService;

    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping("/greeting")
    public String greeting() {
        return greetingService.getGreeting();
    }
}
```

The controller is registered using:

```java
@RestController
```

and exposes:

```text
GET /greeting
```

When a request reaches this endpoint, the controller calls:

```java
greetingService.getGreeting();
```

The service then calls the repository.

The complete flow is therefore:

```text
GET /greeting
      ↓
GreetingController.greeting()
      ↓
GreetingService.getGreeting()
      ↓
GreetingRepository.findGreeting()
      ↓
"Hello from the repository!"
```

---

# Application

The application needs a Spring Boot entry point so that `@SpringBootTest` can discover the application configuration.

```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

`@SpringBootApplication` combines several important Spring Boot features, including component scanning and auto-configuration.

As a result, Spring can discover:

```text
GreetingController
GreetingService
GreetingRepository
```

and register them as beans.

---

# The Integration Test

The integration test is:

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
public class GreetingIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnGreeting() {
        String result = restTemplate.getForObject("/greeting", String.class);

        assertEquals("Hello from the repository!", result);
    }
}
```

This test uses several important Spring testing features.

---

# `@SpringBootTest`

The `@SpringBootTest` annotation tells Spring Boot to load the application context for the test.

```java
@SpringBootTest
```

This is different from a unit test.

A unit test might simply create an object:

```java
GreetingService service = new GreetingService(repository);
```

An integration test allows Spring to create and configure the objects:

```text
Spring Boot
    ↓
Application Context
    ↓
GreetingController
    ↓
GreetingService
    ↓
GreetingRepository
```

This allows the test to verify Spring's configuration and dependency injection as well as the application's behavior.

---

# `RANDOM_PORT`

The test uses:

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
```

The `RANDOM_PORT` setting tells Spring Boot to start an embedded web server on an available random port.

Conceptually:

```text
Spring Boot Application
        ↓
Embedded Web Server
        ↓
Random Available Port
        ↓
TestRestTemplate
        ↓
HTTP Request
```

Using a random port prevents the test from depending on a specific port such as `8080`.

This is particularly useful when tests are run in different environments or when multiple applications may be running simultaneously.

---

# `TestRestTemplate`

`TestRestTemplate` is a Spring Boot test utility for making HTTP requests against a running application.

In this example:

```java
@Autowired
private TestRestTemplate restTemplate;
```

The test can then make a request:

```java
String result = restTemplate.getForObject("/greeting", String.class);
```

The request is equivalent to making:

```text
GET /greeting
```

against the embedded application.

The response body is returned as a `String`.

---

# `@AutoConfigureTestRestTemplate`

The test also uses:

```java
@AutoConfigureTestRestTemplate
```

This tells Spring Boot to configure a `TestRestTemplate` for the test.

The configured object can then be injected:

```java
@Autowired
private TestRestTemplate restTemplate;
```

The important relationship is:

```text
@AutoConfigureTestRestTemplate
             ↓
Spring configures TestRestTemplate
             ↓
@Autowired
             ↓
TestRestTemplate
```

Without the appropriate test configuration, simply adding the `TestRestTemplate` dependency does not necessarily result in a `TestRestTemplate` bean being available for autowiring.

---

# Making the HTTP Request

The test makes the request with:

```java
String result = restTemplate.getForObject("/greeting", String.class);
```

The first argument is the endpoint:

```text
/greeting
```

The second argument specifies the expected response type:

```java
String.class
```

The result is therefore the response body returned by the controller.

For this example:

```text
Hello from the repository!
```

---

# Verifying the Response

The test verifies the response using JUnit:

```java
assertEquals("Hello from the repository!", result);
```

This confirms that the complete request flow produced the expected result.

The assertion is not testing the repository alone.

It is testing the result after the request has passed through:

```text
HTTP
 ↓
Controller
 ↓
Service
 ↓
Repository
```

---

# What Happens During the Test?

When the test starts, Spring Boot creates the application context.

Conceptually:

```text
@SpringBootTest
       ↓
Load Application
       ↓
Create ApplicationContext
       ↓
Discover Components
       ↓
Create GreetingRepository
       ↓
Create GreetingService
       ↓
Inject GreetingRepository
       ↓
Create GreetingController
       ↓
Inject GreetingService
       ↓
Start Embedded Web Server
       ↓
Configure TestRestTemplate
       ↓
Run Test
```

The test then makes:

```text
GET /greeting
```

The request travels through the application:

```text
TestRestTemplate
       ↓
Embedded Web Server
       ↓
GreetingController
       ↓
GreetingService
       ↓
GreetingRepository
       ↓
Response
```

Finally, the test verifies the response.

---

# Integration Test vs Unit Test

These two types of tests serve different purposes.

## Unit Test

A unit test generally focuses on one component.

For example:

```text
GreetingService
      ↓
Mock Repository
```

The repository may be replaced with a Mockito mock.

The goal is to verify the service's behavior independently.

## Integration Test

An integration test can use the real components:

```text
GreetingController
      ↓
GreetingService
      ↓
GreetingRepository
```

The goal is to verify that the components work together correctly.

---

# Integration Test vs `@SpringBootTest`

The previous `@SpringBootTest` example tested a service directly:

```java
String result = greetingService.greet("Spring");
```

That test loaded the Spring application context, but the test interacted directly with a Spring bean.

This integration test goes further:

```java
String result = restTemplate.getForObject("/greeting", String.class);
```

The test interacts with the application through HTTP.

The difference can be visualized as:

```text
@SpringBootTest service test

Test
 ↓
GreetingService
 ↓
Result
```

versus:

```text
HTTP integration test

Test
 ↓
HTTP Request
 ↓
Controller
 ↓
Service
 ↓
Repository
 ↓
HTTP Response
```

Both can use `@SpringBootTest`, but the second test exercises more of the application.

---

# Why Are Real Components Used?

This example intentionally does not use `@MockitoBean`.

The purpose is to test the integration between the components.

Using a mock would change the flow to:

```text
GreetingController
      ↓
GreetingService
      ↓
Mock GreetingRepository
```

Instead, the integration test uses:

```text
GreetingController
      ↓
GreetingService
      ↓
Real GreetingRepository
```

This means the test can detect problems such as:

- Missing Spring beans.
- Incorrect dependency injection.
- Incorrect component scanning.
- Incorrect controller mappings.
- Incorrect service wiring.
- Problems communicating between application layers.

---

# Integration Testing and the Application Context

One of the important benefits of integration testing with Spring Boot is that the test uses the Spring application context.

The application context manages:

```text
GreetingRepository
GreetingService
GreetingController
```

and their dependencies.

This means the test verifies not only application logic, but also the configuration that connects those components.

For example:

```text
GreetingController
       │
       │ injected
       ↓
GreetingService
       │
       │ injected
       ↓
GreetingRepository
```

If Spring could not create one of these dependencies, the test would fail before the HTTP request was made.

---

# Test Dependencies

The module uses Spring Boot's web starter:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

This provides the web application infrastructure required by the example.

The standard Spring Boot testing dependency is also included:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

For Spring Boot 4, the REST test client support is provided by:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-resttestclient</artifactId>
    <scope>test</scope>
</dependency>
```

The REST client module is also included:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-restclient</artifactId>
    <scope>test</scope>
</dependency>
```

The complete dependency section is:

```xml
<dependencies>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-resttestclient</artifactId>
        <scope>test</scope>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-restclient</artifactId>
        <scope>test</scope>
    </dependency>

</dependencies>
```

---

# When Should You Use Integration Tests?

Integration tests are particularly useful when you need to verify interactions between application components.

Common examples include:

- Controller → Service
- Service → Repository
- Repository → Database
- Application → External API
- Application → Messaging system
- Application → Security configuration
- Application → HTTP endpoints

For example:

```text
HTTP Request
      ↓
Controller
      ↓
Service
      ↓
Repository
      ↓
Database
```

An integration test can verify that the complete flow behaves correctly.

---

# Advantages of Integration Testing

Integration tests provide several benefits.

## Verify Component Integration

They verify that independently implemented components work together.

## Verify Spring Configuration

They can detect problems with:

- Bean creation.
- Dependency injection.
- Component scanning.
- Configuration.
- Application startup.

## Test Real Application Behavior

Tests can interact with the application using mechanisms similar to those used by real clients.

## Catch Problems Unit Tests May Miss

A collection of successful unit tests does not guarantee that the application's components are correctly connected.

Integration tests provide another layer of confidence.

---

# Trade-Offs

Integration tests are more comprehensive than unit tests, but they also have costs.

Starting the Spring application context and embedded web server takes more time than creating a few Java objects.

Conceptually:

```text
Unit Test
    ↓
Fast
    ↓
Small scope


Integration Test
    ↓
Starts application
    ↓
Larger scope
    ↓
More expensive
```

For this reason, a healthy test suite generally contains both unit tests and integration tests.

---

# Testing

Run the tests from the module directory:

```bash
mvn test
```

Or from the project root:

```bash
mvn -pl 11-spring-testing/integration-testing test
```

To verify the entire project:

```bash
mvn clean install
```

A successful test run confirms that Spring can:

1. Start the application context.
2. Create the required beans.
3. Inject their dependencies.
4. Start the embedded web server.
5. Handle the `/greeting` request.
6. Return the expected response.

---

# Expected Result

The integration test expects:

```text
GET /greeting
```

to return:

```text
Hello from the repository!
```

The test therefore verifies:

```text
Request
   ↓
/greeting
   ↓
GreetingController
   ↓
GreetingService
   ↓
GreetingRepository
   ↓
"Hello from the repository!"
```

---

# Key Takeaways

- Integration testing verifies that multiple parts of an application work together.
- `@SpringBootTest` loads the Spring Boot application context.
- `RANDOM_PORT` starts an embedded web server on an available port.
- `TestRestTemplate` can send HTTP requests to the running application.
- `@AutoConfigureTestRestTemplate` configures `TestRestTemplate` for use in the test.
- Integration tests can use real Spring-managed components instead of mocks.
- Testing through HTTP allows the test to exercise the controller, service, and repository layers together.
- Integration tests can detect configuration and dependency-injection problems that isolated unit tests may not detect.
- Unit tests and integration tests complement each other rather than replacing each other.

---

# Module 11 Complete

This example completes **Module 11 — Spring Testing**.

The module covered:

1. **Unit Testing**
2. **Spring TestContext**
3. **`@SpringBootTest`**
4. **`@MockitoBean`**
5. **`@TestConfiguration`**
6. **Integration Testing**