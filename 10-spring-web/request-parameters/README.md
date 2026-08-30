# Request Parameters

Demonstrates how Spring MVC uses `@RequestParam` to extract query parameters from HTTP requests.

---

## Overview

Request parameters are values provided in the query string of a URL.

For example:

```text
GET /api/items?category=books
```

Here, `category` is a request parameter.

Spring MVC provides the `@RequestParam` annotation to bind these values to controller method parameters.

This example demonstrates:

- Extracting a required request parameter.
- Extracting multiple request parameters.
- Providing default values for optional parameters.
- Converting request parameter values to Java types.

---

## Dependencies

The example uses Spring Boot's web starter and MVC test support.

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

Dependency versions are managed by the parent POM.

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

## Using `@RequestParam`

A request parameter can be extracted using `@RequestParam`.

```java
@GetMapping("/api/items")
public String getItems(@RequestParam String category) {
    return "Category: " + category;
}
```

A request such as:

```text
GET /api/items?category=books
```

binds the value `books` to the `category` method parameter.

The resulting response is:

```text
Category: books
```

### Explicit Parameter Name

The parameter name can also be specified explicitly:

```java
@GetMapping("/api/items")
public String getItems(@RequestParam("category") String category) {
    return "Category: " + category;
}
```

This makes the relationship between the HTTP parameter and Java parameter explicit.

---

## Multiple Request Parameters

A controller method can accept multiple request parameters.

```java
@GetMapping("/api/search")
public String search(
        @RequestParam String query,
        @RequestParam(defaultValue = "10") int limit) {

    return "Query: " + query + ", Limit: " + limit;
}
```

A request such as:

```text
GET /api/search?query=spring&limit=20
```

produces:

```text
Query: spring, Limit: 20
```

---

## Default Values

`@RequestParam` supports a `defaultValue` attribute.

```java
@RequestParam(defaultValue = "10") int limit
```

If the request does not contain `limit`, Spring uses `10`.

For example:

```text
GET /api/search?query=spring
```

produces:

```text
Query: spring, Limit: 10
```

This is useful for optional query parameters that have a sensible default.

---

## Required Parameters

Request parameters are required by default.

```java
@GetMapping("/api/items")
public String getItems(@RequestParam String category) {
    return "Category: " + category;
}
```

The following request contains the required parameter:

```text
GET /api/items?category=books
```

If `category` is omitted:

```text
GET /api/items
```

Spring MVC returns a client error because the required request parameter is missing.

A parameter can be made optional by providing a default value or by using `required = false`.

For example:

```java
@GetMapping("/api/items")
public String getItems(
        @RequestParam(required = false) String category) {

    return "Category: " + category;
}
```

---

## Type Conversion

Spring automatically converts request parameter values to compatible Java types.

For example:

```java
@GetMapping("/api/search")
public String search(@RequestParam String query, @RequestParam int limit) {

    return "Query: " + query + ", Limit: " + limit;
}
```

Although HTTP parameters arrive as text:

```text
GET /api/search?query=spring&limit=20
```

Spring converts:

```text
"20"
```

into:

```java
int limit = 20;
```

---

## Request Parameters vs Path Variables

Request parameters and path variables both allow values to be supplied by the client, but they are represented differently in the URL.

### Path Variable

```text
GET /api/items/42
```

The value is part of the URL path:

```java
@GetMapping("/api/items/{id}")
public String getItem(@PathVariable Long id) {
    return "Item: " + id;
}
```

### Request Parameter

```text
GET /api/items?id=42
```

The value is part of the query string:

```java
@GetMapping("/api/items")
public String getItem(@RequestParam Long id) {
    return "Item: " + id;
}
```

A useful rule of thumb is:

- Use **path variables** when the value identifies a resource in the URL path.
- Use **request parameters** for filtering, searching, sorting, pagination, and other optional query information.

---

## Running the Example

From the project root:

```bash
mvn clean install
```

To run only this module:

```bash
mvn -pl 10-spring-web/request-parameters spring-boot:run
```

Alternatively, run the `Application` class from your IDE.

---

## Key Takeaways

- `@RequestParam` binds query-string parameters to controller method arguments.
- Request parameters are supplied after `?` in the URL.
- Multiple request parameters can be used in a single controller method.
- Request parameters are required by default.
- `defaultValue` can provide a fallback value.
- Spring automatically converts request parameter values to compatible Java types.
- `MockMvc` can be used to test request-parameter handling without starting an external server.
- Request parameters are different from path variables because they belong to the URL query string rather than the URL path.

---

## Next

The next example will demonstrate **Request Body**, showing how Spring MVC extracts data from the body of an HTTP request using `@RequestBody`.