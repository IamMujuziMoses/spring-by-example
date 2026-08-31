# Exception Handling

Demonstrates how Spring MVC handles exceptions thrown during request processing using `@ExceptionHandler` and `@RestControllerAdvice`.

---

## What This Example Covers

- Exception handling in Spring MVC
- Custom application exceptions
- `@ExceptionHandler`
- `@RestControllerAdvice`
- `@ResponseStatus`
- Local exception handling
- Global exception handling
- Returning an appropriate HTTP status for an exception
- Testing exception handling with `MockMvc`

---

## Custom Exception

The example defines an application-specific exception for cases where an item cannot be found.

```java
public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(Long id) {
        super("Item not found: " + id);
    }
}
```

The exception extends `RuntimeException`, allowing it to be thrown during request processing without requiring the controller method to declare it.

---

## Controller

The controller throws the custom exception when an item does not exist.

```java
@RestController
public class ItemController {

    @GetMapping("/api/items/{id}")
    public String getItem(@PathVariable Long id) {

        if (id != 1) {
            throw new ItemNotFoundException(id);
        }

        return "Item: " + id;
    }
}
```

For an existing item:

```http
GET /api/items/1
```

the controller returns:

```text
Item: 1
```

For a missing item:

```http
GET /api/items/42
```

the controller throws:

```text
ItemNotFoundException
```

---

## `@ExceptionHandler`

Spring MVC provides `@ExceptionHandler` for handling exceptions thrown while processing controller requests.

An exception handler can be placed directly inside a controller:

```java
@ExceptionHandler(ItemNotFoundException.class)
@ResponseStatus(HttpStatus.NOT_FOUND)
public String handleItemNotFound(ItemNotFoundException exception) {
    return exception.getMessage();
}
```

This tells Spring:

> When `ItemNotFoundException` is thrown by this controller, invoke this method.

The handler can then determine the response returned to the client.

---

## Global Exception Handling

Instead of placing the exception handler inside every controller, Spring MVC provides `@ControllerAdvice` and `@RestControllerAdvice` for centralized exception handling.

This example uses:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleItemNotFound(ItemNotFoundException exception) {
        return exception.getMessage();
    }
}
```

The exception-handling flow is:

```text
HTTP Request
     │
     ▼
ItemController
     │
     │ Item does not exist
     ▼
ItemNotFoundException
     │
     ▼
GlobalExceptionHandler
     │
     │ @ExceptionHandler
     ▼
HTTP 404 NOT FOUND
```

---

## `@RestControllerAdvice`

`@RestControllerAdvice` provides centralized exception handling for REST controllers.

It is particularly useful when multiple controllers need consistent exception-handling behavior.

For example:

```text
ItemController ────────┐
                       │
UserController ────────┼──→ GlobalExceptionHandler
                       │
OrderController ───────┘
```

Instead of implementing the same exception-handling logic in each controller, the logic can be defined once in the advice class.

---

## `@ResponseStatus`

The exception handler uses:

```java
@ResponseStatus(HttpStatus.NOT_FOUND)
```

to tell Spring that the response should have a `404 NOT FOUND` status.

Without explicitly changing the status, the response would not communicate that the requested item could not be found using the appropriate HTTP status.

---

## Local vs Global Exception Handling

Spring MVC supports both local and global exception handling.

### Local

An exception handler can be defined directly inside a controller:

```java
@RestController
public class ItemController {

    // Controller methods

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleItemNotFound(ItemNotFoundException exception) {
        return exception.getMessage();
    }
}
```

This handler primarily applies to exceptions raised by that controller.

### Global

The handler can instead be moved into an advice class:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleItemNotFound(ItemNotFoundException exception) {
        return exception.getMessage();
    }
}
```

This allows the exception-handling behavior to be shared across controllers.

For larger applications, centralized exception handling can help keep controllers focused on request processing rather than error-handling logic.

---

## `@ControllerAdvice` vs `@RestControllerAdvice`

Spring provides two commonly used advice annotations.

### `@ControllerAdvice`

```java
@ControllerAdvice
```

is commonly used when handling exceptions for controllers that return views.

### `@RestControllerAdvice`

```java
@RestControllerAdvice
```

is designed for REST controllers and combines controller advice behavior with response-body semantics.

This example uses `@RestControllerAdvice` because the controller is a REST controller and the exception handler returns the response body directly.

---

## Dependencies

The example uses Spring Web and Spring MVC test support:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc-test</artifactId>
    <scope>test</scope>
</dependency>
```

---

## Running the Example

Run the module tests:

```bash
mvn -pl 10-spring-web/exception-handling test
```

Run the application:

```bash
mvn -pl 10-spring-web/exception-handling spring-boot:run
```

Test an existing item:

```bash
curl http://localhost:8080/api/items/1
```

Expected:

```text
Item: 1
```

Test a missing item:

```bash
curl http://localhost:8080/api/items/42
```

Expected response:

```text
Item not found: 42
```

The HTTP status should be:

```text
404 NOT FOUND
```

To verify the complete multi-module project:

```bash
mvn clean install
```

---

## Key Takeaways

- Exceptions can be thrown during Spring MVC request processing.
- `@ExceptionHandler` allows specific exceptions to be handled.
- Exception handlers can be defined locally inside controllers.
- `@ControllerAdvice` provides centralized exception handling.
- `@RestControllerAdvice` is useful for REST APIs.
- `@ResponseStatus` can be used to specify the HTTP status returned by an exception handler.
- Global exception handling avoids duplicating error-handling logic across controllers.
- `MockMvc` can verify both successful requests and exception responses.

---

## Next

The next example will demonstrate **Request Scope** and how Spring manages beans whose lifecycle is tied to an individual HTTP request.