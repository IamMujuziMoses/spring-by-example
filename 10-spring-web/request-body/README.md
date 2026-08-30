# Request Body

Demonstrates how Spring MVC uses `@RequestBody` to read data from an HTTP request body and deserialize JSON into a Java object.

---

## What This Example Covers

- `@RequestBody`
- Reading JSON from an HTTP request body
- JSON-to-Java object deserialization
- HTTP `Content-Type`
- Spring MVC HTTP message converters
- Testing request bodies with `MockMvc`

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

## Request Object

The request body is represented by a Java record:

```java
public record Item(String name, double price) {
}
```

---

## Controller

`@RequestBody` tells Spring MVC to read the HTTP request body and convert it into the specified Java type.

```java
@RestController
public class RequestBodyController {

    @PostMapping("/api/items")
    public String createItem(@RequestBody Item item) {
        return "Created item: " + item.name();
    }
}
```

For example, the endpoint accepts:

```http
POST /api/items
Content-Type: application/json
```

with the following JSON body:

```json
{
    "name": "Laptop",
    "price": 999.99
}
```

Spring MVC deserializes the JSON into an `Item` object:

```text
HTTP Request
     │
     │ JSON
     ▼
@RequestBody
     │
     ▼
HTTP Message Converter
     │
     ▼
Item
     │
     ▼
Controller
```

The controller can then work directly with the Java object instead of manually parsing the JSON.

---

## `Content-Type`

The request should specify:

```http
Content-Type: application/json
```

This tells the server that the request body contains JSON.

With `MockMvc`, this is specified using:

```java
.contentType(MediaType.APPLICATION_JSON)
```

---


## Key Concepts

### `@RequestBody`

`@RequestBody` binds the HTTP request body to a method parameter.

```java
public String createItem(@RequestBody Item item)
```

Spring handles the conversion from the request representation into the Java object.

### HTTP Message Converters

Spring MVC uses `HttpMessageConverter` implementations to convert HTTP request and response bodies between Java objects and formats such as JSON.

For JSON requests, Spring Boot's web starter provides the necessary JSON support.

### `MockMvc`

`MockMvc` allows MVC controllers to be tested without starting a real web server.

```java
mockMvc.perform(post("/api/items"))
```

This makes it useful for focused controller tests.

---

## Running the Example

From the project root:

```bash
mvn -pl 10-spring-web/request-body spring-boot:run
```

Or run the tests directly:

```bash
mvn -pl 10-spring-web/request-body test
```

To verify the complete multi-module build:

```bash
mvn clean install
```

---

## Learning Progression

This example builds on the previous Spring MVC examples:

```text
Request Mapping
      ↓
Path Variables
      ↓
Request Parameters
      ↓
Request Body
```

The previous examples demonstrated extracting values from the URL path and query string. This example introduces extracting structured data from the HTTP request body.

---

## Next

The next example will demonstrate **Response Body** and how Spring MVC converts Java return values into HTTP responses.