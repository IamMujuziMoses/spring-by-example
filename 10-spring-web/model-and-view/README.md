# Model and View

Demonstrates how Spring MVC uses a `Model` and a view to render dynamic HTML responses.

---

## What This Example Covers

- `@Controller`
- `Model`
- `Model.addAttribute()`
- Logical view names
- Passing data from a controller to a view
- Thymeleaf templates
- Rendering model attributes in HTML
- Testing model attributes and view names with `MockMvc`

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

The controller adds data to the model and returns a logical view name.

```java
@Controller
public class ModelViewController {

    @GetMapping("/items")
    public String getItem(Model model) {
        model.addAttribute("name", "Laptop");
        model.addAttribute("price", 999.99);

        return "item";
    }
}
```

The controller produces two important things:

1. **Model data** containing the values needed by the view.
2. **A logical view name** identifying the view that should render the response.

The value returned by the controller:

```text
item
```

is a view name, not the response body.

---

## Model

The `Model` provides a way for a controller to pass data to a view.

```java
model.addAttribute("name", "Laptop");
model.addAttribute("price", 999.99);
```

This creates model attributes that can be accessed by the view.

Conceptually:

```text
Controller
    │
    ├── Model
    │    ├── name  → Laptop
    │    └── price → 999.99
    │
    └── View
         └── item
```

---

## View

This example uses Thymeleaf to render the HTML view.

The template is located at:

```text
src/main/resources/templates/item.html
```

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Item</title>
</head>
<body>

<h1 th:text="${name}">Item Name</h1>

<p>
    Price: <span th:text="${price}">0.00</span>
</p>

</body>
</html>
```

The Thymeleaf expressions:

```text
${name}
${price}
```

retrieve the corresponding values from the Spring MVC model.

The resulting HTML contains:

```text
Laptop
Price: 999.99
```

---

## View Resolution

The controller returns:

```java
return "item";
```

Spring MVC treats `item` as a logical view name.

The view resolution process can be represented as:

```text
GET /items
     │
     ▼
DispatcherServlet
     │
     ▼
ModelViewController
     │
     ├── Add model attributes
     │
     └── Return "item"
              │
              ▼
        View Resolution
              │
              ▼
        item.html
              │
              ▼
        Rendered HTML
              │
              ▼
        HTTP Response
```

The controller does not need to know the physical location of the template.

---

## `@Controller` vs `@RestController`

This example uses:

```java
@Controller
```

rather than:

```java
@RestController
```

This distinction is important.

With a traditional `@Controller`:

```java
@Controller
public class ModelViewController {

    @GetMapping("/items")
    public String getItem(Model model) {
        return "item";
    }
}
```

the returned string is interpreted as a **view name**.

With `@RestController`:

```java
@RestController
public class ResponseBodyController {

    @GetMapping("/api/message")
    public String getMessage() {
        return "Hello from Spring MVC";
    }
}
```

the returned string becomes the **HTTP response body**.

Therefore:

```text
@Controller
    ↓
"item"
    ↓
View name
    ↓
Rendered template
```

while:

```text
@RestController
    ↓
"Hello from Spring MVC"
    ↓
HTTP response body
```

---

## Dependencies

The example uses Spring MVC and Thymeleaf:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc-test</artifactId>
    <scope>test</scope>
</dependency>
```

---

## Running the Example

Run the tests:

```bash
mvn -pl 10-spring-web/model-and-view test
```

Run the application:

```bash
mvn -pl 10-spring-web/model-and-view spring-boot:run
```

Then open:

```text
http://localhost:8080/items
```

The rendered page should display:

```text
Laptop
Price: 999.99
```

To verify the complete multi-module build:

```bash
mvn clean install
```

---

## Key Takeaways

- `Model` allows controllers to pass data to views.
- `model.addAttribute()` adds values that can be accessed by the view.
- A `@Controller` method can return a logical view name.
- The view name is resolved to an actual template by Spring MVC's view-resolution mechanism.
- Thymeleaf can access model attributes using expressions such as `${name}`.
- `@Controller` and `@RestController` have different default behavior for returned values.
- `MockMvc` can verify both the view name and model attributes.

---

## Learning Progression

This continues the Spring MVC learning path:

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

The next example will demonstrate **View Resolvers** and how Spring MVC determines which view implementation should render a logical view name.