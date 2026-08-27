# DispatcherServlet Example

Demonstrates how Spring MVC's `DispatcherServlet` acts as the front controller for handling HTTP requests in a Spring Boot application.

---

## Overview

`DispatcherServlet` is the central component of Spring MVC. It receives incoming HTTP requests and coordinates the process of finding the appropriate handler, invoking it, and returning the response.

In this example, Spring Boot automatically configures and registers the `DispatcherServlet` for the application.

The request flow is:

```text
HTTP Request
     │
     ▼
DispatcherServlet
     │
     ▼
HandlerMapping
     │
     ▼
HelloController
     │
     ▼
HTTP Response
```

---

## Dependencies

The example uses Spring Boot Web for the application and Spring Boot's MVC test starter for testing.

```xml
<dependencies>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webmvc-test</artifactId>
        <scope>test</scope>
    </dependency>

</dependencies>
```

---

## Application

The application is bootstrapped using `SpringApplication.run()`.

```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

Spring Boot automatically configures the web application and registers the `DispatcherServlet`.

---

## Controller

A simple controller provides an endpoint for the example.

```java
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from DispatcherServlet!";
    }
}
```

When a client sends a `GET` request to `/hello`, the request is received by the `DispatcherServlet`, which determines that `HelloController` should handle it.

---

## Configuration

The application runs on port `8080`.

```properties
server.port=8080
```

---

## Running the Example

Run the application with Maven:

```bash
mvn spring-boot:run
```

Or run `Application.main()` from your IDE.

Once the application has started, make a request:

```bash
curl http://localhost:8080/hello
```

The response should be:

```text
Hello from DispatcherServlet!
```

---

## What This Example Demonstrates

- What `DispatcherServlet` is.
- The role of `DispatcherServlet` in Spring MVC.
- How Spring Boot automatically configures the `DispatcherServlet`.
- How incoming HTTP requests are routed to controllers.
- The relationship between `DispatcherServlet` and `HandlerMapping`.
- How a controller handles a request.
- How `MockMvc` can be used to test Spring MVC request handling.

---

## Key Takeaway

The `DispatcherServlet` is the **front controller** of Spring MVC.

Rather than each controller receiving HTTP requests directly, requests first pass through the `DispatcherServlet`, which coordinates the MVC request-processing pipeline.

Spring Boot hides much of the configuration required to set this up, allowing us to focus on the application itself.

---

## Next

The next example will focus on **Controllers**, exploring how Spring MVC controllers are defined and how they participate in handling web requests.