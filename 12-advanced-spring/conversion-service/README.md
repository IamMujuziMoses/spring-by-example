# ConversionService

Spring provides the `ConversionService` abstraction for converting values from one type to another.

Instead of requiring application code to manually parse and transform values, Spring provides a centralized conversion system that can determine whether a conversion is supported and perform the conversion when possible.

This example demonstrates Spring's built-in `ConversionService` using `DefaultConversionService`.

## Learning Objectives

By completing this example, you will understand:

- What `ConversionService` is
- Why Spring provides a type conversion abstraction
- How `DefaultConversionService` works
- How to check whether a conversion is supported
- How to convert values using `convert()`
- How Spring performs built-in type conversions
- How `String` values can be converted into other types
- The role of `Converter` and `GenericConverter`
- The difference between the `ConversionService` abstraction and its implementation
- How to test type conversion

---

## What Is ConversionService?

`ConversionService` is a Spring abstraction for converting an object from one type to another.

The interface provides two primary operations:

```java
boolean canConvert(Class<?> sourceType, Class<?> targetType);

<T> T convert(Object source, Class<T> targetType);
```

For example:

```java
ConversionService conversionService = new DefaultConversionService();

Integer number = conversionService.convert("42", Integer.class);
```

The source value is a `String`:

```text
"42"
```

and Spring converts it into an `Integer`:

```text
42
```

The application does not need to manually call:

```java
Integer.parseInt("42");
```

Instead, it delegates the conversion to Spring's conversion system.

---

## Why Does ConversionService Exist?

Applications frequently receive values in one type while needing to work with them as another type.

For example, configuration values are commonly represented as strings:

```text
server.port=8080
feature.enabled=true
```

The application may need those values as:

```java
Integer
Boolean
```

Without a conversion abstraction, application code would need to perform conversions manually:

```java
Integer port = Integer.parseInt("8080");
Boolean enabled = Boolean.parseBoolean("true");
```

Spring provides a general-purpose conversion mechanism so that different parts of the framework can consistently convert values.

The basic idea is:

```text
Source value
     │
     ▼
ConversionService
     │
     ▼
Target type
```

---

## ConversionService Interface

The central abstraction is:

```java
org.springframework.core.convert.ConversionService
```

It allows application code to ask Spring whether a conversion is supported and then perform the conversion.

### Checking Conversion Support

The `canConvert()` method determines whether Spring has a conversion path between two types.

```java
boolean supported = conversionService.canConvert(String.class, Integer.class);
```

For example:

```java
conversionService.canConvert(String.class, Integer.class);
```

returns `true` when the conversion service supports converting a `String` to an `Integer`.

### Performing a Conversion

The `convert()` method performs the actual conversion.

```java
Integer number = conversionService.convert("42", Integer.class);
```

The result is:

```text
42
```

---

## DefaultConversionService

`DefaultConversionService` is a general-purpose implementation of `ConversionService`.

It provides many standard conversions that are useful in typical Spring applications.

For example:

```java
ConversionService conversionService = new DefaultConversionService();
```

The application can then use the abstraction:

```java
Integer number = conversionService.convert("42", Integer.class);
```

This demonstrates an important Spring design principle:

```text
Application
     │
     ▼
ConversionService
     │
     ▼
DefaultConversionService
```

The application depends on the `ConversionService` interface rather than directly depending on implementation-specific behavior.

---

## String to Integer Conversion

A common conversion is from `String` to `Integer`.

```java
ConversionService conversionService = new DefaultConversionService();

Integer number = conversionService.convert("42", Integer.class);

System.out.println(number);
```

Output:

```text
42
```

Spring identifies an appropriate built-in converter and uses it to perform the conversion.

Conceptually:

```text
"42"
 │
 │ String → Integer
 ▼
 42
```

---

## String to Boolean Conversion

`DefaultConversionService` also supports common boolean conversions.

```java
Boolean enabled = conversionService.convert("true", Boolean.class);
```

The resulting value is:

```text
true
```

The conversion can be visualized as:

```text
"true"
   │
   │ String → Boolean
   ▼
 true
```

---

## canConvert()

Before attempting a conversion, an application can check whether the conversion is supported.

```java
boolean supported = conversionService.canConvert(String.class, Integer.class);
```

The result can then be checked:

```java
if (supported) {
    Integer number = conversionService.convert("42", Integer.class);
}
```

This is useful when the application needs to determine whether a conversion path exists before performing it.

---

## ConversionService vs Manual Conversion

Without `ConversionService`, application code might perform individual conversions itself:

```java
Integer number = Integer.parseInt("42");

Boolean enabled = Boolean.parseBoolean("true");
```

With Spring:

```java
ConversionService conversionService = new DefaultConversionService();

Integer number = conversionService.convert("42", Integer.class);

Boolean enabled = conversionService.convert("true", Boolean.class);
```

The advantage is that conversion logic can be handled through a common abstraction.

This becomes particularly useful when Spring itself needs to convert values between different types.

---

## ConversionService and Converters

Spring's conversion system is extensible.

The conversion infrastructure can use different kinds of converters, including:

- `Converter`
- `ConverterFactory`
- `GenericConverter`

A simple `Converter` defines a conversion between a source and target type:

```java
Converter<String, Integer>
```

Conceptually:

```text
String
  │
  │ Converter
  ▼
Integer
```

This example intentionally uses Spring's built-in converters rather than defining a custom converter.

Custom conversion will be easier to understand after first learning how the `ConversionService` abstraction works.

---

## Conversion Flow

The overall conversion process can be visualized as:

```text
Application
     │
     │ convert("42", Integer.class)
     ▼
ConversionService
     │
     ▼
DefaultConversionService
     │
     │ finds appropriate converter
     ▼
String → Integer conversion
     │
     ▼
Integer value: 42
```

The application therefore does not need to know which converter performs the actual conversion.

---

## Complete Example

### ConversionServiceApplication.java

```java
public class ConversionServiceApplication {

    public static void main(String[] args) {
        ConversionService conversionService = new DefaultConversionService();

        Integer number = conversionService.convert("42", Integer.class);
        Boolean enabled = conversionService.convert("true", Boolean.class);

        System.out.println("Number: " + number);
        System.out.println("Enabled: " + enabled);
    }
}
```

Running the application produces:

```text
Number: 42
Enabled: true
```

---

## ConversionService vs Environment

The previous example introduced Spring's `Environment`.

`Environment` provides access to environment information and properties:

```java
environment.getProperty("application.name");
```

`ConversionService` focuses on converting values:

```java
conversionService.convert("42",Integer.class);
```

The two abstractions can work together.

For example, an environment may provide:

```text
server.port = "8080"
```

while application code needs:

```java
Integer
```

Spring's conversion infrastructure can help bridge these different representations.

Conceptually:

```text
Environment
     │
     │ "8080"
     ▼
ConversionService
     │
     ▼
Integer 8080
```

---

## ConversionService vs Property Sources

`PropertySource` determines where a property comes from.

For example:

```text
applicationProperties
        │
        └── server.port = "8080"
```

`ConversionService` is concerned with converting the value.

```text
PropertySource
      │
      │ "8080"
      ▼
Environment
      │
      ▼
ConversionService
      │
      ▼
Integer 8080
```

This separates two different responsibilities:

```text
PropertySource
    → provides values

Environment
    → exposes environment and property information

ConversionService
    → converts values between types
```

---

## Key Operations

| Operation | Purpose |
|---|---|
| `canConvert()` | Checks whether a conversion is supported |
| `convert()` | Converts a value to a target type |
| `DefaultConversionService` | Provides a general-purpose conversion implementation |

---

## Dependencies

This example only requires Spring Core because the conversion infrastructure is part of `spring-core`.

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

From the `conversion-service` directory:

```bash
mvn clean test
```

To build the complete project:

```bash
mvn clean install
```

To run the application from the module:

```bash
mvn exec:java
```

Or run `ConversionServiceApplication` directly from the IDE.

---

## Key Takeaways

- `ConversionService` is Spring's abstraction for type conversion.
- `DefaultConversionService` provides a general-purpose implementation.
- `canConvert()` checks whether a conversion is supported.
- `convert()` performs the conversion.
- Spring provides many built-in conversions.
- Values such as `String` can be converted into types such as `Integer` and `Boolean`.
- Spring's conversion infrastructure is extensible through converter interfaces.
- Applications can depend on the `ConversionService` abstraction rather than a concrete implementation.
- `ConversionService` separates type conversion from application-specific logic.
- The conversion system is used throughout Spring's broader infrastructure.

---

## What's Next?

The next example covers **Validation**.

It will introduce Spring's validation abstraction and show how objects can be validated using Spring's validation infrastructure.
