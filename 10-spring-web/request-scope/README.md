# Request Scope

Demonstrates how Spring manages beans whose lifecycle is tied to an individual HTTP request using `@RequestScope`.

---

## What This Example Covers

- Spring bean scopes
- Request-scoped beans
- `@RequestScope`
- Request-scoped bean lifecycle
- Scoped proxies
- Injecting request-scoped beans into controllers
- Testing request scope with `MockMvc`

---

## What Is Request Scope?

Spring beans are created according to their configured scope.

A request-scoped bean is associated with a single HTTP request.

```java
@Component
@RequestScope
public class RequestScopedData {

    private final String id = UUID.randomUUID().toString();

    public String getId() {
        return id;
    }
}
```

`@RequestScope` tells Spring to create a new instance of the bean for each HTTP request.

Conceptually:

```text
Request 1
    ↓
RequestScopedData #1

Request 2
    ↓
RequestScopedData #2

Request 3
    ↓
RequestScopedData #3
```

Each request receives its own instance.

---

## Request-Scoped Bean

The example uses a UUID to make each instance identifiable:

```java
@Component
@RequestScope
public class RequestScopedData {

    private final String id = UUID.randomUUID().toString();

    public String getId() {
        return id;
    }
}
```

The UUID is generated when the bean instance is created.

Therefore, different requests should produce different UUIDs.

---

## Controller

The request-scoped bean is injected into a controller:

```java
@RestController
public class RequestScopeController {

    private final RequestScopedData requestScopedData;

    public RequestScopeController(RequestScopedData requestScopedData) {
        this.requestScopedData = requestScopedData;
    }

    @GetMapping("/api/request")
    public String getRequestScopeId() {
        return requestScopedData.getId();
    }
}
```

Calling:

```text
GET /api/request
```

returns the ID of the request-scoped bean associated with that request.

For example:

```text
Request 1 → 8f0e6d50-...
Request 2 → a12c9e71-...
Request 3 → 5b73f2c4-...
```

The values should be different because each request receives a different bean instance.

---

## Request Scope Lifecycle

The lifecycle of a request-scoped bean can be represented as:

```text
HTTP Request begins
        │
        ▼
Create request-scoped bean
        │
        ▼
Controller uses bean
        │
        ▼
HTTP Response
        │
        ▼
Request ends
        │
        ▼
Request-scoped bean lifecycle ends
```

The next HTTP request receives a new instance.

---

## Scoped Proxies

The controller itself is normally a singleton bean, while `RequestScopedData` has request scope.

This means Spring needs a way for the singleton controller to access the correct request-specific bean.

Conceptually, Spring uses a scoped proxy:

```text
RequestScopeController
        │
        ▼
   Scoped Proxy
        │
        ├── Request 1 → RequestScopedData #1
        ├── Request 2 → RequestScopedData #2
        └── Request 3 → RequestScopedData #3
```

The controller can therefore depend on the request-scoped bean without manually managing its lifecycle.

---

## Request Scope vs Singleton Scope

Spring's default bean scope is singleton.

A singleton bean behaves conceptually like:

```text
Application
    │
    └── MyBean #1
          ↑
          ├── Request 1
          ├── Request 2
          └── Request 3
```

A request-scoped bean behaves like:

```text
Application
    │
    ├── Request 1 → MyBean #1
    ├── Request 2 → MyBean #2
    └── Request 3 → MyBean #3
```

The key difference is the lifetime of the bean.

| Scope | Lifecycle |
|---|---|
| Singleton | One instance per Spring application context |
| Request | One instance per HTTP request |
| Session | One instance per HTTP session |
| Application | One instance per `ServletContext` |

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
mvn -pl 10-spring-web/request-scope test
```

Run the application:

```bash
mvn -pl 10-spring-web/request-scope spring-boot:run
```

Call the endpoint:

```bash
curl http://localhost:8080/api/request
```

Call it again:

```bash
curl http://localhost:8080/api/request
```

The two responses should contain different UUIDs.

For example:

```text
8f0e6d50-...
```

and:

```text
a12c9e71-...
```

To verify the complete multi-module project:

```bash
mvn clean install
```

---

## Key Takeaways

- Spring supports multiple bean scopes.
- `@RequestScope` creates a bean instance for each HTTP request.
- The same request-scoped instance can be used throughout a request.
- Different HTTP requests receive different instances.
- Request-scoped beans can be injected into singleton controllers.
- Spring uses scoped-proxy infrastructure to connect different bean lifecycles.
- `MockMvc` can be used to verify request-scoped behavior.

---

## Learning Progression

This continues Module 10 — Spring Web:

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
      ↓
Model and View
      ↓
View Resolvers
      ↓
Exception Handling
      ↓
Request Scope
      ↓
Session Scope
      ↓
Application Scope
```

----

## Next

The next example will demonstrate **Session Scope** and how Spring manages a bean whose lifecycle is tied to an HTTP session.