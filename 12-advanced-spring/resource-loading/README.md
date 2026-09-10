# Resource Loading

Spring provides a consistent abstraction for accessing resources through the `Resource` interface and `ResourceLoader`.

Instead of working directly with Java-specific APIs for classpath files, filesystem files, or URLs, Spring provides a common resource abstraction that can represent different kinds of resources.

This example demonstrates how to use `ResourceLoader` to load a classpath resource and how to access its metadata and contents through the `Resource` interface.

---

## Learning Objectives

By completing this example, you will understand:

- What Spring's `Resource` abstraction is
- Why Spring provides the `Resource` interface
- What `ResourceLoader` does
- How `DefaultResourceLoader` works
- How to load a classpath resource
- How to check whether a resource exists
- How to retrieve resource metadata
- How to read resource contents
- How Spring abstracts different resource locations
- The difference between `Resource` and `ResourceLoader`

---

## What Is Resource Loading?

Applications frequently need to access resources such as:

- Configuration files
- XML files
- JSON files
- Templates
- Images
- Text files
- Files on the filesystem
- Resources available through URLs
- Resources packaged inside the application

Java provides several APIs for accessing these resources, but the APIs can differ depending on where the resource is located.

Spring provides the `Resource` abstraction to give applications a consistent way to work with resources.

The main abstraction is:

```java
org.springframework.core.io.Resource
```

Spring also provides:

```java
org.springframework.core.io.ResourceLoader
```

which is responsible for loading resources.

The relationship can be summarized as:

```text
Resource location
       │
       ▼
ResourceLoader
       │
       │ getResource(...)
       ▼
Resource
       │
       ├── Metadata
       ├── Existence
       └── Content
```

---

## Why Does Resource Loading Exist?

Without an abstraction, application code may need to know exactly how a resource should be accessed.

For example, loading a classpath resource can involve class-loader APIs, while loading a filesystem resource can involve `java.io.File` or NIO APIs.

Spring provides a common abstraction:

```text
Application code
       │
       ▼
    Resource
       │
       ├── Classpath resource
       ├── File resource
       ├── URL resource
       └── Other resource types
```

The application can work with the `Resource` interface without needing to directly manage all of these underlying resource mechanisms.

---

## The Resource Interface

`Resource` represents an external resource.

It is located in:

```java
org.springframework.core.io.Resource
```

A `Resource` provides operations for inspecting and accessing a resource.

Some commonly used methods include:

```java
boolean exists();

String getFilename();

InputStream getInputStream();
```

For example:

```java
Resource resource = ...;

if (resource.exists()) {
    System.out.println(resource.getFilename());
}
```

The `Resource` object represents the resource itself.

It does not necessarily mean that the resource's contents have already been loaded into memory.

---

## ResourceLoader

`ResourceLoader` is Spring's abstraction for loading resources.

It is located in:

```java
org.springframework.core.io.ResourceLoader
```

Its primary operation is:

```java
Resource getResource(String location);
```

For example:

```java
ResourceLoader resourceLoader = new DefaultResourceLoader();

Resource resource = resourceLoader.getResource("classpath:greeting.txt");
```

The loader receives a resource location and returns a `Resource` representation.

---

## DefaultResourceLoader

`DefaultResourceLoader` is Spring's basic implementation of `ResourceLoader`.

It can resolve resource locations such as:

```text
classpath:greeting.txt
```

and other supported resource locations.

For this example:

```java
ResourceLoader resourceLoader = new DefaultResourceLoader();
```

The application depends on the `ResourceLoader` interface while using `DefaultResourceLoader` as its implementation.

This follows the familiar programming-to-an-interface approach:

```text
ResourceLoader
      ▲
      │
      │ implements
      │
DefaultResourceLoader
```

---

## Classpath Resources

A classpath resource is a resource packaged with the application.

The example contains:

```text
src/main/resources/
└── greeting.txt
```

Maven places files from `src/main/resources` onto the application's runtime classpath.

The resource can then be referenced using:

```text
classpath:greeting.txt
```

For example:

```java
Resource resource = resourceLoader.getResource("classpath:greeting.txt");
```

The `classpath:` prefix tells Spring that the resource should be resolved from the classpath.

---

## Loading a Resource

The first step is to create a `ResourceLoader`:

```java
ResourceLoader resourceLoader = new DefaultResourceLoader();
```

The resource can then be loaded:

```java
Resource resource = resourceLoader.getResource("classpath:greeting.txt");
```

At this point, the application has a `Resource` object representing the classpath resource.

```text
"classpath:greeting.txt"
          │
          ▼
   ResourceLoader
          │
          ▼
       Resource
```

---

## Checking Whether a Resource Exists

The `Resource` interface provides:

```java
boolean exists();
```

This can be used to verify that the resource is available.

For example:

```java
if (resource.exists()) {
    System.out.println("Resource exists");
}
```

The example prints:

```text
Resource exists: true
```

This is useful when resource availability is not guaranteed.

---

## Retrieving Resource Metadata

A `Resource` can provide information about the resource.

For example:

```java
String filename = resource.getFilename();
```

For the example resource:

```text
greeting.txt
```

The application can print:

```java
System.out.println("Resource filename: "+ resource.getFilename());
```

---

## Reading Resource Contents

A resource's contents can be accessed through:

```java
InputStream getInputStream();
```

For example:

```java
try (InputStream inputStream = resource.getInputStream()) {
    String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

    System.out.println(content);
}
```

The `InputStream` should be closed after use, which is why the example uses try-with-resources.

The resource content is:

```text
Hello from a Spring Resource!
```

---

## Complete Example

The complete application is:

```java
public class ResourceLoadingApplication {

    public static void main(String[] args) throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();

        Resource resource = resourceLoader.getResource("classpath:greeting.txt");

        System.out.println("Resource exists: " + resource.exists());
        System.out.println("Resource filename: " + resource.getFilename());

        try (InputStream inputStream = resource.getInputStream()) {
            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            System.out.println("Resource content: " + content);
        }
    }
}
```

The application demonstrates the complete resource-loading flow:

```text
Create ResourceLoader
        │
        ▼
Load "classpath:greeting.txt"
        │
        ▼
      Resource
        │
        ├── exists()
        │
        ├── getFilename()
        │
        └── getInputStream()
                  │
                  ▼
             Read content
```

---

## Resource Location Prefixes

Spring supports different resource location styles.

A classpath resource can be specified using:

```text
classpath:greeting.txt
```

A filesystem resource can be represented using:

```text
file:/path/to/greeting.txt
```

A URL resource can be represented using a URL:

```text
https://example.com/resource.txt
```

The important idea is that application code can work with the common `Resource` abstraction rather than directly depending on a specific resource implementation.

```text
Resource
   │
   ├── ClassPathResource
   ├── FileSystemResource
   ├── UrlResource
   └── Other Resource implementations
```

---

## Resource vs ResourceLoader

Although these interfaces work together, they have different responsibilities.

| Type | Responsibility |
|---|---|
| `Resource` | Represents a resource |
| `ResourceLoader` | Loads resources |
| `DefaultResourceLoader` | Basic `ResourceLoader` implementation |

The relationship is:

```text
ResourceLoader
      │
      │ getResource(...)
      ▼
Resource
      │
      ├── exists()
      ├── getFilename()
      └── getInputStream()
```

A simple way to remember the distinction is:

```text
ResourceLoader → "Find/load the resource"

Resource       → "Work with the resource"
```

---

## Resource Loading and Environment

The previous example in Module 12 demonstrated Spring's `Environment` abstraction.

`Environment` answers questions about the application's environment, such as:

```text
Which profiles are active?
What properties are available?
Where should configuration values come from?
```

`Resource` answers a different question:

```text
How can I access this external resource?
```

These abstractions can work together in real Spring applications, but they have different responsibilities.

```text
Environment
    │
    └── Environment and configuration values

ResourceLoader
    │
    └── Locates resources

Resource
    │
    └── Represents and accesses resources
```

This example intentionally focuses on resource loading rather than property resolution.

---

## Key Resource Loading Operations

| Operation | Purpose |
|---|---|
| `getResource()` | Loads a resource |
| `exists()` | Checks whether the resource exists |
| `getFilename()` | Retrieves the resource filename |
| `getInputStream()` | Opens the resource for reading |

Example:

```java
ResourceLoader loader = new DefaultResourceLoader();
Resource resource = loader.getResource("classpath:greeting.txt");

if (resource.exists()) {
    System.out.println(resource.getFilename());
}
```

---

## Dependencies

This example only requires Spring Core because the resource abstraction is part of Spring Core.

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-core</artifactId>
</dependency>

<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
```

---

## Running the Example

From the `resource-loading` directory, run:

```bash
mvn clean install
```

To run the application directly from an IDE, execute:

```text
ResourceLoadingApplication
```

Expected output:

```text
Resource exists: true
Resource filename: greeting.txt
Resource content: Hello from a Spring Resource!
```

---

## Key Takeaways

- Spring provides the `Resource` abstraction for working with external resources.
- `ResourceLoader` is responsible for locating and loading resources.
- `DefaultResourceLoader` provides a basic implementation of `ResourceLoader`.
- Classpath resources can be referenced with the `classpath:` prefix.
- `Resource` provides operations for inspecting and accessing a resource.
- `exists()` checks whether a resource is available.
- `getFilename()` retrieves the resource filename.
- `getInputStream()` provides access to the resource contents.
- Spring's resource abstraction hides differences between different resource locations.
- `Resource` and `ResourceLoader` have different responsibilities.
- Resource loading is separate from property resolution through `Environment`.

---

## Next

The next example in Module 12 - Advanced Spring will cover:

**MessageSource**

This will demonstrate Spring's abstraction for resolving messages, including support for localized and internationalized messages.