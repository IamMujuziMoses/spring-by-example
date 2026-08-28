# Controllers Example

Demonstrates how controllers are defined in Spring MVC and how `@Controller` and `@RestController` are used to handle HTTP requests.

---

## Overview

Controllers are responsible for handling incoming HTTP requests in a Spring MVC application.

Spring provides two commonly used controller annotations:

- `@Controller` — defines an MVC controller, commonly used when returning views.
- `@RestController` — defines a REST controller where method return values are written directly to the HTTP response body.

This example demonstrates both approaches.

---

## Dependencies

The example uses Spring Boot Web for Spring MVC and the MVC test starter for testing.

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

`@SpringBootApplication` enables component scanning, allowing Spring Boot to discover the controllers in the application package.

---

## Using `@Controller`

`@Controller` is used to define a Spring MVC controller.

```java
@Controller
public class HelloController {

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello from Controller!";
    }
}
```

The `@ResponseBody` annotation tells Spring that the return value should be written directly to the HTTP response body.

Without `@ResponseBody`, Spring MVC treats the returned `String` as a view name.

---

## Using `@RestController`

`@RestController` is designed for REST-style controllers.

```java
@RestController
public class HelloRestController {

    @GetMapping("/rest-hello")
    public String hello() {
        return "Hello from RestController!";
    }
}
```

With `@RestController`, the return value is automatically written to the HTTP response body.

Conceptually:

```text
@RestController
      │
      ├── @Controller
      │
      └── @ResponseBody
```

This means that `@RestController` is effectively a convenience annotation that combines `@Controller` and `@ResponseBody`.

---

## `@Controller` vs `@RestController`

| Annotation | Typical Purpose | Return Value |
|---|---|---|
| `@Controller` | MVC applications that return views | Usually interpreted as a view name |
| `@RestController` | REST APIs | Written directly to the response body |

For example:

```java
@Controller
public class PageController {

    @GetMapping("/page")
    public String page() {
        return "home";
    }
}
```

Here, `"home"` is treated as a view name.

With `@ResponseBody`:

```java
@Controller
public class ApiController {

    @GetMapping("/message")
    @ResponseBody
    public String message() {
        return "Hello";
    }
}
```

The returned `"Hello"` becomes the HTTP response body.

With `@RestController`:

```java
@RestController
public class ApiController {

    @GetMapping("/message")
    public String message() {
        return "Hello";
    }
}
```

The result is also written directly to the response body.

---

## Running the Example

Start the application with your IDE by running:

```text
Application.main()
```

Or use Maven:

```bash
mvn spring-boot:run
```

The application starts on port `8080` by default.

### Controller Endpoint

```bash
curl http://localhost:8080/hello
```

Expected response:

```text
Hello from Controller!
```

### REST Controller Endpoint

```bash
curl http://localhost:8080/rest-hello
```

Expected response:

```text
Hello from RestController!
```

---

## What This Example Demonstrates

- What a Spring MVC controller is.
- How `@Controller` defines an MVC controller.
- How `@RestController` defines a REST controller.
- How `@ResponseBody` writes a method's return value to the HTTP response.
- The relationship between `@Controller`, `@ResponseBody`, and `@RestController`.
- How controllers are discovered through component scanning.
- How `MockMvc` can be used to test controller endpoints.

---

## Key Takeaway

A Spring MVC controller provides the entry point for application-specific request handling.

`@Controller` is commonly used when an application returns views, while `@RestController` is commonly used when building REST APIs.

The important distinction is how Spring interprets the controller method's return value:

```text
@Controller
    │
    ├── without @ResponseBody → View name
    │
    └── with @ResponseBody → HTTP response body

@RestController
    │
    └── HTTP response body
```

## Next

The next example will explore **Request Mapping**, including how Spring MVC maps different HTTP requests to controller methods using annotations such as:

- `@RequestMapping`
- `@GetMapping`
- `@PostMapping`
- `@PutMapping`
- `@DeleteMapping`
- `@PatchMapping`