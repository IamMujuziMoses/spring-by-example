# Session Scope

Demonstrates how Spring manages beans whose lifecycle is tied to an individual HTTP session using `@SessionScope`.

---

## What This Example Covers

- Spring bean scopes
- Session-scoped beans
- `@SessionScope`
- Session-scoped bean lifecycle
- Scoped proxies
- Reusing a bean across requests in the same session
- Creating separate bean instances for different sessions
- Testing session scope with `MockMvc` and `MockHttpSession`

---

## What Is Session Scope?

Spring supports different bean scopes that determine how long a bean instance lives.

A session-scoped bean is associated with an individual HTTP session.

```java
@Component
@SessionScope
public class SessionScopedData {

    private final String id = UUID.randomUUID().toString();

    public String getId() {
        return id;
    }
}
```

`@SessionScope` tells Spring to create one instance of the bean for each HTTP session.

Conceptually:

```text
Session A
    │
    └── SessionScopedData #1
            ↑
            ├── Request 1
            ├── Request 2
            └── Request 3

Session B
    │
    └── SessionScopedData #2
            ↑
            ├── Request 1
            └── Request 2
```

Requests belonging to the same session reuse the same bean instance.

Different sessions receive different bean instances.

---

## Session-Scoped Bean

The example uses a UUID to make each bean instance identifiable:

```java
@Component
@SessionScope
public class SessionScopedData {

    private final String id = UUID.randomUUID().toString();

    public String getId() {
        return id;
    }
}
```

The UUID is generated when the session-scoped bean is created.

Therefore:

- Requests in the same session return the same UUID.
- Requests in different sessions return different UUIDs.

---

## Controller

The session-scoped bean is injected into a REST controller:

```java
@RestController
public class SessionScopeController {

    private final SessionScopedData sessionScopedData;

    public SessionScopeController(SessionScopedData sessionScopedData) {
        this.sessionScopedData = sessionScopedData;
    }

    @GetMapping("/api/session")
    public String getSessionScopeId() {
        return sessionScopedData.getId();
    }
}
```

Calling:

```text
GET /api/session
```

returns the ID of the `SessionScopedData` associated with the current HTTP session.

----

## Session Scope Lifecycle

The lifecycle can be represented as:

```text
HTTP Session begins
        │
        ▼
Create session-scoped bean
        │
        ▼
Request 1 ──→ Use bean
        │
        ▼
Request 2 ──→ Use same bean
        │
        ▼
Request 3 ──→ Use same bean
        │
        ▼
HTTP Session ends
        │
        ▼
Session-scoped bean lifecycle ends
```

A new HTTP session receives a new instance.

---

## Session Scope vs Request Scope

The most important difference is how long the bean instance is reused.

### Request Scope

A new instance is created for every HTTP request:

```text
Request 1 → Bean A
Request 2 → Bean B
Request 3 → Bean C
```

### Session Scope

The same instance is reused for requests belonging to the same HTTP session:

```text
Session A
    Request 1 → Bean A
    Request 2 → Bean A
    Request 3 → Bean A

Session B
    Request 1 → Bean B
    Request 2 → Bean B
```

---

## Bean Scope Comparison

| Scope | Lifecycle |
|---|---|
| Singleton | One instance per Spring application context |
| Request | One instance per HTTP request |
| Session | One instance per HTTP session |
| Application | One instance per `ServletContext` |

---

## Scoped Proxies

The controller is normally a singleton bean, while `SessionScopedData` has session scope.

Spring therefore needs a mechanism that allows the singleton controller to access the correct session-specific bean.

Conceptually:

```text
SessionScopeController
        │
        ▼
   Scoped Proxy
        │
        ├── Session A → SessionScopedData #1
        │
        └── Session B → SessionScopedData #2
```

Spring manages this association automatically.

The controller does not need to manually retrieve the session-scoped bean from `HttpSession`.

---

## `MockHttpSession`

`MockHttpSession` provides a mock HTTP session that can be attached to `MockMvc` requests:

```java
MockHttpSession session = new MockHttpSession();

mockMvc.perform(get("/api/session").session(session));
```

Using the same session object for multiple requests allows the test to simulate multiple requests from the same user session.

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
mvn -pl 10-spring-web/session-scope test
```

Run the application:

```bash
mvn -pl 10-spring-web/session-scope spring-boot:run
```

Call the endpoint while maintaining the session:

```bash
curl -c cookies.txt http://localhost:8080/api/session
```

Call it again using the same session cookie:

```bash
curl -b cookies.txt -c cookies.txt http://localhost:8080/api/session
```

Both responses should contain the same UUID.

Starting a new session should produce a different UUID.

To verify the complete multi-module project:

```bash
mvn clean install
```

---

## Key Takeaways

- Spring supports multiple bean scopes.
- `@SessionScope` creates a bean instance for each HTTP session.
- The same session-scoped instance is reused across requests in that session.
- Different HTTP sessions receive different bean instances.
- Session-scoped beans can be injected into singleton controllers.
- Spring uses scoped-proxy infrastructure to manage the different lifecycles.
- `MockHttpSession` can be used with `MockMvc` to test session-scoped behavior.
- Session scope lasts longer than request scope but is shorter than application-wide singleton scope.

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

---

## Next

The next example will demonstrate **Application Scope** and how Spring manages a bean whose lifecycle is associated with the application's `ServletContext`.