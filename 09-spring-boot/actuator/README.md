# Spring Boot Actuator

This example demonstrates how **Spring Boot Actuator** adds production-ready monitoring and management capabilities to a Spring Boot application.

---

## What This Example Demonstrates

- Adding Spring Boot Actuator
- Exposing Actuator endpoints over HTTP
- Configuring which endpoints are exposed
- Checking application health with `/actuator/health`
- Understanding liveness and readiness health groups

---

## Dependencies

The example uses `spring-boot-starter-actuator` to enable Actuator functionality and `spring-boot-starter-web` to provide the embedded web server needed to access the management endpoints over HTTP.

```xml
<dependencies>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>

</dependencies>
```

---

## Application

The application uses `@SpringBootApplication` and `SpringApplication.run()` to start the Spring Boot application.

```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

No explicit Actuator configuration is required in the Java application class. Spring Boot detects the Actuator dependency and automatically configures its infrastructure.

---

## Configuration

The example exposes the `health` and `info` endpoints:

```properties
management.endpoints.web.exposure.include=health,info
```

This makes the following endpoints available:

```text
/actuator/health
/actuator/info
```

Keeping the exposed endpoints explicit is preferable to exposing every available management endpoint by default.

---

## Running the Example

From the `actuator` directory, run:

```bash
mvn spring-boot:run
```

The application starts an embedded web server on port `8080`.

You can then access the health endpoint:

```text
http://localhost:8080/actuator/health
```

The application returns:

```json
{
  "groups": [
    "liveness",
    "readiness"
  ],
  "status": "UP"
}
```

The `UP` status indicates that the application's health is currently healthy.

---

## Health Groups

Spring Boot can expose health information through different health groups.

### Liveness

The **liveness** state indicates whether the application is alive and should continue running.

A failed liveness state generally means that restarting the application may be appropriate.

### Readiness

The **readiness** state indicates whether the application is ready to receive traffic.

This distinction is particularly useful in environments such as Kubernetes, where liveness and readiness probes can be used to determine whether an application should be restarted or receive traffic.

---

## How Actuator Works

Adding the Actuator starter allows Spring Boot to automatically configure management functionality:

```text
spring-boot-starter-actuator
            │
            ▼
   Actuator auto-configuration
            │
            ▼
     Management endpoints
            │
            ▼
     /actuator/health
     /actuator/info
```

The important idea is that we don't manually create controllers for these endpoints. Spring Boot provides the infrastructure and exposes the endpoints based on the application's configuration.

## Testing

The example includes a context-loading smoke test:

```java
@SpringBootTest
class ApplicationTest {

    @Test
    void shouldStartApplication() {
    }
}
```

The empty test method is intentional. `@SpringBootTest` starts the Spring Boot application context, so a failure to initialize the context causes the test to fail.

## Key Takeaways

- **Spring Boot Actuator** provides monitoring and management features for Spring Boot applications.
- Adding `spring-boot-starter-actuator` enables Actuator's auto-configuration.
- Actuator endpoints can be exposed over HTTP.
- `management.endpoints.web.exposure.include` controls which endpoints are exposed.
- `/actuator/health` provides information about the application's health.
- Liveness and readiness provide different views of application availability.
- Actuator reduces the amount of infrastructure code developers need to write themselves.

## Next

This completes **Module 9 — Spring Boot**.

The module covered:

- SpringApplication
- Auto Configuration
- Starter Dependencies
- Configuration Properties
- Profiles
- CommandLineRunner
- Actuator

Next up is **Module 10 — Spring Web (Spring MVC)**, where we will explore how Spring handles HTTP requests and builds web applications using the MVC framework.
