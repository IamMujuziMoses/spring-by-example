# Request Mapping Example

Demonstrates how Spring MVC maps HTTP requests to controller methods using `@RequestMapping` and the HTTP method-specific mapping annotations.

---

## Overview

Spring MVC uses request mappings to determine which controller method should handle an incoming HTTP request.

The `@RequestMapping` annotation can be used to map requests based on properties such as:

- URL path
- HTTP method
- Request parameters
- Request headers
- Content type

Spring also provides specialized annotations for common HTTP methods:

- `@GetMapping`
- `@PostMapping`
- `@PutMapping`
- `@PatchMapping`
- `@DeleteMapping`

This example focuses on URL and HTTP method mappings.

---

## Dependencies

The example uses Spring Boot Web for Spring MVC and the Spring MVC test starter for controller testing.

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

---

## Request Mapping

The `@RequestMapping` annotation can be used at the class level to define a common path for controller endpoints.

```java
@RestController
@RequestMapping("/api")
public class RequestMappingController {
}
```

All request mappings declared inside this controller are prefixed with `/api`.

For example:

```java
@GetMapping("/items")
public String getItems() {
    return "GET request";
}
```

maps to:

```text
GET /api/items
```

---

## HTTP Method Mappings

Spring provides composed annotations for the most common HTTP methods.

```java
@RestController
@RequestMapping("/api")
public class RequestMappingController {

    @GetMapping("/items")
    public String getItems() {
        return "GET request";
    }

    @PostMapping("/items")
    public String createItem() {
        return "POST request";
    }

    @PutMapping("/items")
    public String updateItem() {
        return "PUT request";
    }

    @PatchMapping("/items")
    public String partiallyUpdateItem() {
        return "PATCH request";
    }

    @DeleteMapping("/items")
    public String deleteItem() {
        return "DELETE request";
    }
}
```

The same URL can therefore be mapped to different controller methods depending on the HTTP method.

| HTTP Method | Annotation | Endpoint | Controller Method |
|---|---|---|---|
| GET | `@GetMapping` | `/api/items` | `getItems()` |
| POST | `@PostMapping` | `/api/items` | `createItem()` |
| PUT | `@PutMapping` | `/api/items` | `updateItem()` |
| PATCH | `@PatchMapping` | `/api/items` | `partiallyUpdateItem()` |
| DELETE | `@DeleteMapping` | `/api/items` | `deleteItem()` |

---

## `@RequestMapping` vs HTTP Method Mappings

`@GetMapping`, `@PostMapping`, `@PutMapping`, `@PatchMapping`, and `@DeleteMapping` are specialized forms of `@RequestMapping`.

For example:

```java
@GetMapping("/items")
public String getItems() {
    return "GET request";
}
```

is equivalent in intent to:

```java
@RequestMapping(
        path = "/items",
        method = RequestMethod.GET
)
public String getItems() {
    return "GET request";
}
```

The HTTP method-specific annotations make the controller easier to read because the intended HTTP method is immediately visible.

---

## Running the Example

Start the application using your IDE by running:

```text
Application.main()
```

Or use Maven:

```bash
mvn spring-boot:run
```

The application starts on port `8080` by default.

### GET

```bash
curl http://localhost:8080/api/items
```

Expected response:

```text
GET request
```

### POST

```bash
curl -X POST http://localhost:8080/api/items
```

Expected response:

```text
POST request
```

### PUT

```bash
curl -X PUT http://localhost:8080/api/items
```

Expected response:

```text
PUT request
```

### PATCH

```bash
curl -X PATCH http://localhost:8080/api/items
```

Expected response:

```text
PATCH request
```

### DELETE

```bash
curl -X DELETE http://localhost:8080/api/items
```

Expected response:

```text
DELETE request
```

---

## What This Example Demonstrates

- What request mapping means in Spring MVC.
- How `@RequestMapping` defines request mappings.
- How class-level mappings provide a common URL prefix.
- How `@GetMapping` maps GET requests.
- How `@PostMapping` maps POST requests.
- How `@PutMapping` maps PUT requests.
- How `@PatchMapping` maps PATCH requests.
- How `@DeleteMapping` maps DELETE requests.
- How the same URL can handle different HTTP methods.
- How `MockMvc` can be used to test request mappings.

---

## Key Takeaway

Request mappings connect incoming HTTP requests to specific controller methods.

A class-level mapping can define a common path:

```text
@RequestMapping("/api")
        │
        ├── GET    /api/items
        ├── POST   /api/items
        ├── PUT    /api/items
        ├── PATCH  /api/items
        └── DELETE /api/items
```

HTTP method-specific annotations make these mappings explicit and readable.

---

## Next

The next example will explore **Path Variables** and how values embedded in a URL can be captured and passed to controller methods using `@PathVariable`.

For example:

```text
GET /api/items/42
```

can be mapped to:

```java
@GetMapping("/items/{id}")
public String getItem(@PathVariable Long id) {
    return "Item: " + id;
}
```