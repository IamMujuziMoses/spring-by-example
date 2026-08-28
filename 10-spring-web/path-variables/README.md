# Path Variables Example

Demonstrates how Spring MVC captures dynamic values from URL paths using `@PathVariable`.

---

## Overview

A path variable is a value embedded directly in the URL path.

For example:

```text
GET /api/items/42
```

The `42` is a path variable that can be captured by a controller method:

```java
@GetMapping("/api/items/{id}")
public String getItem(@PathVariable Long id) {
    return "Item: " + id;
}
```

Spring matches `{id}` in the request mapping with the `id` method parameter and converts the value to the required Java type.

---

## Application

The application is bootstrapped using Spring Boot's `SpringApplication`.

```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

---

## Using `@PathVariable`

`@PathVariable` binds a method parameter to a variable in the URL path.

```java
@GetMapping("/api/items/{id}")
public String getItem(@PathVariable Long id) {
    return "Item: " + id;
}
```

The `{id}` portion of the mapping represents a variable value.

A request such as:

```text
GET /api/items/42
```

results in:

```text
id = 42
```

The controller returns:

```text
Item: 42
```

---

## Multiple Path Variables

A request can contain multiple path variables.

```java
@GetMapping("/api/users/{userId}/items/{itemId}")
public String getUserItem(@PathVariable Long userId, @PathVariable Long itemId) {
    return "User: " + userId + ", Item: " + itemId;
}
```

For example:

```text
GET /api/users/10/items/42
```

Spring extracts:

```text
userId = 10
itemId = 42
```

and the response is:

```text
User: 10, Item: 42
```

---

## Explicit Path Variable Names

The name can also be specified explicitly using the `value` or `name` attribute.

```java
@GetMapping("/api/items/{id}")
public String getItem(@PathVariable("id") Long itemId) {
    return "Item: " + itemId;
}
```

Here:

- `{id}` is the name in the URL template.
- `itemId` is the Java method parameter.
- `@PathVariable("id")` tells Spring that they are connected.

---

## Parameter Name Discovery

This project enables Java parameter name metadata in the root Maven configuration:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.15.0</version>

    <configuration>
        <release>${java.version}</release>
        <parameters>true</parameters>
    </configuration>
</plugin>
```

The `<parameters>true</parameters>` configuration causes Maven to compile the application with the `-parameters` compiler option.

This allows Spring to discover method parameter names at runtime.

Therefore, this works:

```java
@GetMapping("/api/items/{id}")
public String getItem(@PathVariable Long id) {
    return "Item: " + id;
}
```

Without parameter-name metadata, Spring may not be able to determine that the Java parameter `id` corresponds to `{id}`.

Explicitly specifying the variable name also avoids this requirement:

```java
@PathVariable("id") Long itemId
```

---

## Dependencies

The example uses Spring Boot Web and the Spring MVC test support.

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

## Running the Example

Start the application:

```bash
mvn spring-boot:run
```

The application starts on port `8080` by default.

### Single Path Variable

Make a request:

```bash
curl http://localhost:8080/api/items/42
```

Expected response:

```text
Item: 42
```

### Multiple Path Variables

Make a request:

```bash
curl http://localhost:8080/api/users/10/items/42
```

Expected response:

```text
User: 10, Item: 42
```

---

## Path Variables vs Request Parameters

Path variables are part of the URL path:

```text
GET /api/items/42
```

Request parameters are supplied separately as query parameters:

```text
GET /api/items?id=42
```

This example focuses on path variables. The request parameter example will demonstrate `@RequestParam`.

---

## What This Example Demonstrates

- What a path variable is.
- How `@PathVariable` works.
- How path variables are declared using `{variable}` in request mappings.
- How Spring binds URL values to controller method parameters.
- How multiple path variables can be used in one endpoint.
- How Spring converts path variable values to Java types.
- How to explicitly specify a path variable name.
- Why Java parameter-name metadata can be important when using Spring MVC.
- How to test path variable mappings with `MockMvc`.

---

## Key Takeaway

`@PathVariable` is used when a value is part of the URL path itself.

For example:

```text
/api/items/{id}
```

maps a request such as:

```text
/api/items/42
```

to:

```java
@GetMapping("/api/items/{id}")
public String getItem(@PathVariable Long id) {
    return "Item: " + id;
}
```

The value `42` is extracted from the URL and provided to the controller method as `id`.

---

## Next

The next example will explore **Request Parameters** and how Spring MVC handles values supplied as query parameters using `@RequestParam`.

For example:

```text
GET /api/items?id=42
```

can be handled using:

```java
@GetMapping("/api/items")
public String getItem(@RequestParam Long id) {
    return "Item: " + id;
}
```