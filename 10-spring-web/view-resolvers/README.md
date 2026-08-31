# View Resolvers

Demonstrates how Spring MVC uses view resolvers to map a logical view name returned by a controller to an actual view.

---

## What This Example Covers

- View resolvers in Spring MVC
- Logical view names
- `ViewResolver`
- `@Controller`
- `Model`
- Thymeleaf view resolution
- Mapping a logical view name to a template
- Testing MVC view resolution with `MockMvc`

---

## How View Resolution Works

A controller can return a logical view name rather than the physical location of a template.

For example:

```java
@Controller
public class ViewResolverController {

    @GetMapping("/items")
    public String getItem(Model model) {
        model.addAttribute("name", "Laptop");
        model.addAttribute("price", 999.99);

        return "item";
    }
}
```

The controller returns:

```text
item
```

This is a **logical view name**.

Spring MVC then uses its view-resolution infrastructure to determine which actual view corresponds to that name.

With Thymeleaf, the logical view name:

```text
item
```

is resolved to:

```text
src/main/resources/templates/item.html
```

The overall process is:

```text
HTTP Request
     │
     ▼
DispatcherServlet
     │
     ▼
Controller
     │
     ├── Model
     │     ├── name  = Laptop
     │     └── price = 999.99
     │
     └── "item"
          │
          ▼
     ViewResolver
          │
          ▼
     item.html
          │
          ▼
     Thymeleaf
          │
          ▼
     Rendered HTML
```

---

## Logical View Names

A controller should return a logical view name:

```java
return "item";
```

rather than coupling itself to the physical template location:

```java
return "templates/item.html";
```

This separation allows the controller to remain independent of the underlying view technology and template location.

The controller only needs to know:

```text
item
```

The view-resolution infrastructure handles the mapping to the actual view.

---

## View

This example uses Thymeleaf to render the view.

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

The values added to the model by the controller are available to the template:

```text
${name}
${price}
```

The resulting page displays:

```text
Laptop
Price: 999.99
```

---

## Model and View vs View Resolver

These concepts are related but serve different purposes.

### Model and View

The controller provides data to the view and identifies the logical view to render:

```java
model.addAttribute("name", "Laptop");
model.addAttribute("price", 999.99);

return "item";
```

The model contains:

```text
name  → Laptop
price → 999.99
```

and the controller returns:

```text
item
```

### View Resolver

The view resolver determines which actual view corresponds to:

```text
item
```

Conceptually:

```text
"item"
   │
   ▼
ViewResolver
   │
   ▼
item.html
```

Therefore:

```text
Model
  ↓
Carries data to the view

View name
  ↓
Identifies which logical view should be rendered

ViewResolver
  ↓
Maps the logical view name to an actual view
```

---

## `@Controller` vs `@RestController`

This example uses:

```java
@Controller
```

because the endpoint returns a view.

With:

```java
@Controller
public class ViewResolverController {

    @GetMapping("/items")
    public String getItem(Model model) {
        return "item";
    }
}
```

the string `"item"` represents a **logical view name**.

With:

```java
@RestController
public class ResponseBodyController {

    @GetMapping("/api/message")
    public String getMessage() {
        return "Hello from Spring MVC";
    }
}
```

the string becomes the **HTTP response body**.

The distinction is:

```text
@Controller
    ↓
"item"
    ↓
Logical view name
    ↓
ViewResolver
    ↓
Rendered view
```

versus:

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
mvn -pl 10-spring-web/view-resolvers test
```

Run the application:

```bash
mvn -pl 10-spring-web/view-resolvers spring-boot:run
```

Then open:

```text
http://localhost:8080/items
```

The application should render the `item.html` template.

To verify the complete multi-module project:

```bash
mvn clean install
```

---

## Key Takeaways

- A controller can return a logical view name.
- A logical view name is not necessarily the physical location of the view.
- Spring MVC uses view-resolution infrastructure to locate the appropriate view.
- Thymeleaf integrates with Spring MVC to render HTML templates.
- The `Model` carries data from the controller to the view.
- `@Controller` is used when a method should return a view.
- `@RestController` is used when the return value should become the response body.
- View resolution separates controller logic from the details of view rendering.

---

## Next

The next example will demonstrate **Exception Handling** in Spring MVC, including how controllers handle exceptions and how Spring maps exceptions to appropriate responses.