# Property Sources

This example demonstrates Spring's **property source infrastructure** and how properties can be added to and retrieved through the `Environment`.

It builds on the previous `Environment` example. While `Environment` provides access to properties, **`PropertySource`** represents an individual source of those properties.

The example uses `MapPropertySource` to demonstrate adding custom properties to the environment and how property source precedence affects property resolution.

---

## Learning Objectives

By completing this example, you will understand:

- What `PropertySource` is
- What `MutablePropertySources` is
- How `MapPropertySource` stores properties
- How to add a property source to an environment
- How to retrieve properties through `Environment`
- How multiple property sources can be configured
- How property source precedence affects property resolution

---

## What Is PropertySource?

`PropertySource` is Spring's abstraction for a source of name-value properties.

A property source has a name and provides properties that can be looked up by key.

Conceptually:

```text
PropertySource
        │
        ├── Name
        │
        └── Properties
                │
                ├── application.name
                ├── application.version
                └── ...
```

For example, a property source might contain:

```text
application.name = Spring by Example
application.version = 1.0
```

Spring provides different `PropertySource` implementations for different sources of configuration.

---

## Why Does PropertySource Exist?

Applications can receive configuration from many different sources.

For example:

```text
Application Properties
        │
        ├── Map
        ├── System Properties
        ├── Environment Variables
        ├── Configuration Files
        └── Other Sources
```

Spring represents these sources using the `PropertySource` abstraction.

This gives the `Environment` a consistent way to resolve properties regardless of where they originate.

The relationship can be viewed as:

```text
PropertySource
       │
       │ contains
       ▼
 Properties
       │
       │ managed by
       ▼
MutablePropertySources
       │
       │ exposed through
       ▼
Environment
       │
       │ resolves
       ▼
Property Value
```

---

## PropertySource and Environment

The previous example introduced the `Environment` abstraction.

The `Environment` provides access to properties:

```java
String value =
        environment.getProperty("application.name");
```

The actual properties can come from one or more `PropertySource` objects.

```text
Environment
      │
      ▼
MutablePropertySources
      │
      ├── PropertySource
      │
      ├── PropertySource
      │
      └── PropertySource
```

When a property is requested, the environment searches its property sources to resolve the value.

---

## MapPropertySource

`MapPropertySource` is a convenient `PropertySource` implementation backed by a `Map`.

For example:

```java
Map<String, Object> properties = Map.of("application.name", "Spring by Example", "application.version", "1.0");
```

The map can be wrapped in a `MapPropertySource`:

```java
MapPropertySource propertySource = new MapPropertySource("applicationProperties", properties);
```

The first argument is the name of the property source.

The second argument contains the properties.

Conceptually:

```text
Map
 │
 ├── application.name → Spring by Example
 └── application.version → 1.0
              │
              ▼
      MapPropertySource
              │
              ▼
   applicationProperties
```

---

## Adding a PropertySource

Property sources are managed by `MutablePropertySources`.

They can be accessed through the environment:

```java
MutablePropertySources propertySources = environment.getPropertySources();
```

A property source can then be added:

```java
propertySources.addFirst(propertySource);
```

For example:

```java
ConfigurableEnvironment environment = applicationContext.getEnvironment();

MapPropertySource propertySource = new MapPropertySource("applicationProperties",
                Map.of("application.name", "Spring by Example"));

environment.getPropertySources().addFirst(propertySource);
```

After the property source has been added, the environment can resolve the property.

```java
String applicationName = environment.getProperty("application.name");
```

---

## Property Source Precedence

The order of property sources is important.

Spring searches property sources according to their precedence.

For example:

```text
PropertySources
       │
       ├── higherPriority
       │       └── application.name = "Higher Priority"
       │
       └── lowerPriority
               └── application.name = "Lower Priority"
```

When the application requests:

```java
environment.getProperty("application.name");
```

the value from the higher-priority property source is returned.

```text
application.name
       │
       ▼
higherPriority
       │
       ▼
"Higher Priority"
```

This allows higher-priority configuration to override values from lower-priority sources.

---

## addFirst()

`addFirst()` places a property source at the beginning of the collection.

```java
environment.getPropertySources().addFirst(propertySource);
```

This gives the property source higher precedence than property sources later in the collection.

For example:

```text
Before:

higherPriority
lowerPriority


addFirst(newSource)


After:

newSource
higherPriority
lowerPriority
```

If the same property exists in multiple sources, the earlier source takes precedence.

---

## addLast()

`addLast()` places a property source at the end of the collection.

```java
environment.getPropertySources().addLast(propertySource);
```

This gives it lower precedence than property sources earlier in the collection.

For example:

```text
Before:

higherPriority
lowerPriority


addLast(newSource)


After:

higherPriority
lowerPriority
newSource
```

---

## Retrieving Properties

Once a property source has been added, properties can be retrieved through the `Environment`.

```java
String value = environment.getProperty("application.name");
```

For example:

```java
MapPropertySource propertySource = new MapPropertySource("applicationProperties",
                Map.of("application.name", "Spring by Example"));

environment.getPropertySources().addFirst(propertySource);

String applicationName = environment.getProperty("application.name");

System.out.println(applicationName);
```

The output is:

```text
Spring by Example
```

---

## Multiple Property Sources

An environment can contain multiple property sources.

For example:

```text
Environment
      │
      ▼
MutablePropertySources
      │
      ├── applicationProperties
      │       ├── application.name
      │       └── application.version
      │
      ├── systemProperties
      │       └── ...
      │
      └── otherSource
              └── ...
```

When a property is requested, Spring searches the available sources according to their order.

---

## Complete Example

### PropertySourcesApplication

```java
public class PropertySourcesApplication {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext()) {

            ConfigurableEnvironment environment = applicationContext.getEnvironment();

            Map<String, Object> properties = Map.of("application.name", "Spring by Example", "application.version", "1.0");

            MapPropertySource propertySource = new MapPropertySource("applicationProperties", properties);

            environment.getPropertySources().addFirst(propertySource);

            System.out.println("Application name: " + environment.getProperty("application.name"));
            System.out.println("Application version: " + environment.getProperty("application.version"));
        }
    }
}
```

The application produces:

```text
Application name: Spring by Example
Application version: 1.0
```

---

## Inspecting Property Sources

The environment exposes its property sources through `getPropertySources()`:

```java
MutablePropertySources propertySources = environment.getPropertySources();
```

A property source can be retrieved by name:

```java
PropertySource<?> propertySource = propertySources.get("applicationProperties");
```

This can be used to inspect whether a particular property source has been registered.

For example:

```java
assertNotNull(environment.getPropertySources().get("applicationProperties"));
```

---

## PropertySource vs Property Value

It is important to distinguish between a property source and an individual property.

A `PropertySource` represents the source:

```text
applicationProperties
```

The property is an individual entry within that source:

```text
application.name = Spring by Example
```

Conceptually:

```text
PropertySource
"applicationProperties"
        │
        ├── application.name
        │       └── Spring by Example
        │
        └── application.version
                └── 1.0
```

---

## Property Source Resolution

The property resolution process can be summarized as:

```text
environment.getProperty("application.name")
                    │
                    ▼
          MutablePropertySources
                    │
                    ▼
        Search property sources
                    │
          ┌─────────┴─────────┐
          ▼                   ▼
    First source         Next source
    contains key?        contains key?
          │                   │
         Yes                  ...
          │
          ▼
     Return value
```

The first matching property source determines the resolved value.

---

## Key Operations

The main operations demonstrated in this example are:

| Operation | Purpose |
|---|---|
| `getPropertySources()` | Retrieves the environment's property sources |
| `addFirst()` | Adds a property source with higher precedence |
| `addLast()` | Adds a property source with lower precedence |
| `get()` | Retrieves a property source by name |
| `getProperty()` | Resolves a property through the environment |

---

## Dependencies

This example requires Spring Context and JUnit.

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
</dependency>

<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
```

---

## Running the Example

From the project root:

```bash
mvn clean install
```

To run the tests for this module:

```bash
mvn -pl 12-advanced-spring/property-sources clean test
```

---

## Key Takeaways

- `PropertySource` represents a source of configuration properties.
- `MapPropertySource` provides a property source backed by a `Map`.
- `MutablePropertySources` manages the collection of property sources.
- Property sources can be added with `addFirst()` or `addLast()`.
- `addFirst()` gives a property source higher precedence.
- `addLast()` gives a property source lower precedence.
- `Environment#getProperty()` resolves properties across the configured property sources.
- When the same property exists in multiple sources, property source order determines which value is returned.
- The `Environment` provides property access while `PropertySource` represents where those properties come from.

---

## Next

The next example focuses specifically on **Resource Loading** and demonstrates how Spring's resource abstraction provides a consistent way to access resources from different locations.