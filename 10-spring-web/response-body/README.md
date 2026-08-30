# Response Body

Demonstrates how Spring MVC writes a controller's return value directly to the HTTP response body using `@ResponseBody` and `@RestController`.

---

## What This Example Covers

- `@ResponseBody`
- `@RestController`
- Writing data to an HTTP response body
- The difference between a response body and a view
- Testing HTTP responses with `MockMvc`

---

## Application

The `Application` class bootstraps the Spring Boot application.

```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

---

## Controller

The controller exposes a simple endpoint that returns a string.

```java
@RestController
public class ResponseBodyController {

    @GetMapping("/api/message")
    public String getMessage() {
        return "Hello from Spring MVC";
    }
}
```

Because the class is annotated with `@RestController`, the return value is written directly to the HTTP response body.

Conceptually:

```text
GET /api/message
       │
       ▼
DispatcherServlet
       │
       ▼
ResponseBodyController
       │
       ▼
getMessage()
       │
       ▼
"Hello from Spring MVC"
       │
       ▼
HTTP Response Body
```

---

## `@RestController`

`@RestController` is a convenience annotation that combines:

```java
@Controller
@ResponseBody
```

Therefore:

```java
@RestController
public class ResponseBodyController {
    // ...
}
```

is effectively equivalent to:

```java
@Controller
@ResponseBody
public class ResponseBodyController {
    // ...
}
```

The `@ResponseBody` behavior tells Spring MVC that the return value should be written to the HTTP response rather than interpreted as a view name.

---

## `@ResponseBody`

`@ResponseBody` can also be applied directly to an individual controller method.

For example:

```java
@Controller
public class MessageController {

    @ResponseBody
    @GetMapping("/api/message")
    public String getMessage() {
        return "Hello from Spring MVC";
    }
}
```

This provides the same response-body behavior for that particular method.

Using `@RestController` is convenient when most or all endpoints in a controller return response bodies.

---

## Response Body vs View

Spring MVC controllers can return either a response body or a view.

A traditional MVC controller might return a view name:

```java
@Controller
public class PageController {

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
```

Here, `"home"` represents a view name.

With `@ResponseBody`:

```java
@Controller
public class MessageController {

    @ResponseBody
    @GetMapping("/api/message")
    public String getMessage() {
        return "Hello from Spring MVC";
    }
}
```

the string itself becomes the HTTP response body.

| Approach | Return value |
| --- | --- |
| `@Controller` | Usually interpreted as a view name |
| `@Controller` + `@ResponseBody` | Written to the response body |
| `@RestController` | Written to the response body by default |

---

## Running the Example

From the project root:

```bash
mvn -pl 10-spring-web/response-body spring-boot:run
```

Run the tests with:

```bash
mvn -pl 10-spring-web/response-body test
```

To verify the complete multi-module build:

```bash
mvn clean install
```

---

## Key Takeaways

- `@ResponseBody` tells Spring MVC to write a method's return value directly to the HTTP response body.
- `@RestController` combines `@Controller` and `@ResponseBody`.
- Response bodies can contain simple values such as strings or structured objects.
- `MockMvc` can be used to verify the HTTP status and response content.
- Returning a response body is different from returning a view name.

---

## Learning Progression

This example continues the Spring MVC learning path:

```text
DispatcherServlet
      ↓
Controllers
      ↓
Request Mapping
      ↓
Path Variables
      ↓
Request Parameters
      ↓
Request Body
      ↓
Response Body
```

The previous **Request Body** example demonstrated how Spring MVC reads data from an HTTP request. This example demonstrates the opposite direction: returning data from a controller to the HTTP response.

---

## Next

The next example will demonstrate **Model and View** and how Spring MVC can pass model data to a view for rendering.