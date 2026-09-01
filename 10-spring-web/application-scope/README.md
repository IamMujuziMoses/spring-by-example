# Application Scope

This example demonstrates how to use **Application Scope** in Spring Web applications.

Application-scoped beans have a lifecycle tied to the web application's `ServletContext`. A single instance is shared across all requests and sessions within the application.

---

## Concepts Covered

- `@ApplicationScope`
- Application-scoped Spring beans
- Bean lifetime
- Sharing state across requests
- Sharing state across sessions
- Difference between application, session, and request scope

---

## How Application Scope Works

An application-scoped bean is created once for the web application and shared across requests and sessions.

```java
@Component
@ApplicationScope
public class ApplicationScopedData {

    private final String id = UUID.randomUUID().toString();

    public String getId() {
        return id;
    }
}
```

The `@ApplicationScope` annotation tells Spring that the bean should have application scope.

Conceptually:

```text
Application
    │
    └── ApplicationScopedData
            │
            ├── Request 1
            ├── Request 2
            ├── Request 3
            └── Request 4
```

Every request receives the same application-scoped bean instance.

---

## Example

The controller exposes an endpoint that returns the identifier of the application-scoped bean:

```java
@RestController
public class ApplicationScopeController {

    private final ApplicationScopedData applicationScopedData;

    public ApplicationScopeController(ApplicationScopedData applicationScopedData) {
        this.applicationScopedData = applicationScopedData;
    }

    @GetMapping("/api/application")
    public String getApplicationScopeId() {
        return applicationScopedData.getId();
    }
}
```

Calling the endpoint multiple times returns the same identifier:

```bash
curl http://localhost:8080/api/application
```

Example response:

```text
8f0e6d50-4c9b-4c7d-a6d2-123456789abc
```

Calling it again:

```bash
curl http://localhost:8080/api/application
```

Returns the same value:

```text
8f0e6d50-4c9b-4c7d-a6d2-123456789abc
```

The same instance is therefore being used across requests.

---

## Application Scope vs Other Web Scopes

Spring provides several scopes for web applications.

### Request Scope

A request-scoped bean exists for the lifetime of a single HTTP request.

```text
Request 1 → Bean A
Request 2 → Bean B
Request 3 → Bean C
```

### Session Scope

A session-scoped bean exists for the lifetime of an HTTP session.

```text
Session A → Bean A
Session B → Bean B
```

### Application Scope

An application-scoped bean exists for the lifetime of the web application.

```text
Application
    │
    └── Bean A
          ↑
          ├── Session A
          ├── Session B
          └── Session C
```

| Scope | Lifetime |
|---|---|
| Request | One HTTP request |
| Session | One HTTP session |
| Application | One web application |

---

## Application Scope and Sessions

Application scope is not tied to an individual HTTP session.

For example:

```text
Application
    │
    ├── Session A ──┐
    │               │
    ├── Session B ──┼──→ ApplicationScopedData
    │               │
    └── Session C ──┘
```

All sessions access the same application-scoped bean.

This differs from session scope, where each session receives its own bean instance.

---

## Application Scope vs Singleton Scope

Application scope and Spring singleton scope can appear similar in a typical Spring Boot application, but they represent different concepts.

**Singleton scope:**

```text
One instance per Spring ApplicationContext
```

**Application scope:**

```text
One instance per ServletContext
```

In a typical Spring Boot web application, there is usually one application context and one servlet context, so the behavior can look identical.

---

## Running the Example

Run the tests:

```bash
mvn -pl 10-spring-web/application-scope test
```

Run the application:

```bash
mvn -pl 10-spring-web/application-scope spring-boot:run
```

Then access:

```text
GET http://localhost:8080/api/application
```

You can also use `curl`:

```bash
curl http://localhost:8080/api/application
```

---

## Building the Project

From the project root:

```bash
mvn clean install
```

---

## Learning Objectives

After completing this example, you should understand:

- What application scope means in Spring Web.
- How `@ApplicationScope` defines an application-scoped bean.
- How the lifetime of an application-scoped bean differs from request and session scope.
- How an application-scoped bean is shared across requests.
- How an application-scoped bean is shared across different sessions.
- The difference between application scope and singleton scope.

---

## Related Examples

This example is part of **Module 10 — Spring Web (Spring MVC)**.

Other scope examples in this module:

- Request Scope
- Session Scope
- Application Scope

Together, these examples demonstrate how Spring manages bean lifecycles at different levels of a web application.