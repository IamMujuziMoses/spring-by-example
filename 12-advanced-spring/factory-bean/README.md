# FactoryBean

This example demonstrates Spring's **`FactoryBean`** mechanism and how it differs from the core `BeanFactory` container.

A `FactoryBean` is a special Spring-managed bean that acts as a factory for another object. When Spring retrieves a bean backed by a `FactoryBean`, it normally returns the object produced by the factory rather than the factory itself.

This example uses a `GreetingServiceFactoryBean` to create `GreetingService` instances.

---

## Learning Objectives

By completing this example, you will understand:

- What `FactoryBean` is
- How `FactoryBean` differs from `BeanFactory`
- How to implement `FactoryBean<T>`
- How `getObject()` creates the product object
- How `getObjectType()` describes the produced object
- How Spring exposes a `FactoryBean`'s product through `getBean()`
- How to retrieve the actual `FactoryBean` using the `&` prefix
- How Spring manages the factory and its produced object

---

## What Is FactoryBean?

`FactoryBean` is a Spring interface used when a bean definition represents a **factory for another object**.

A simplified view is:

```text
ApplicationContext
       │
       ▼
 FactoryBean
       │
       │ getObject()
       ▼
 Product Object
```

For example:

```java
public class GreetingServiceFactoryBean implements FactoryBean<GreetingService> {

    @Override
    public GreetingService getObject() {
        return new GreetingService("Hello from FactoryBean!");
    }

    @Override
    public Class<?> getObjectType() {
        return GreetingService.class;
    }
}
```

Here:

- `GreetingServiceFactoryBean` is the factory.
- `GreetingService` is the product.
- `getObject()` creates the product.
- `getObjectType()` tells Spring what type of object the factory produces.

---

## Why Does FactoryBean Exist?

Normally, Spring can create a bean directly:

```java
@Bean
public GreetingService greetingService() {
    return new GreetingService("Hello!");
}
```

However, some objects require specialized or reusable creation logic.

`FactoryBean` provides a standard Spring mechanism for separating:

```text
Bean definition
      │
      ▼
Factory
      │
      ▼
Object creation
      │
      ▼
Product
```

The factory itself becomes part of the Spring container while Spring exposes its product as the normal bean.

---

## FactoryBean vs BeanFactory

The names are similar, but they represent completely different concepts.

| | `BeanFactory` | `FactoryBean` |
|---|---|---|
| Purpose | Provides the IoC container | Creates/provides another object |
| Role | Container | Factory inside the container |
| Key method | `getBean()` | `getObject()` |
| Example | `DefaultListableBeanFactory` | `GreetingServiceFactoryBean` |
| Manages beans | Yes | Itself is managed as a bean |
| Produces a bean | Manages bean creation | Produces a specific object |

A useful way to remember the difference is:

```text
BeanFactory
    ↓
"Give me a bean."

FactoryBean
    ↓
"I'll make the bean."
```

`BeanFactory` is the container.

`FactoryBean` is a special bean managed by the container.

---

## Implementing FactoryBean

A `FactoryBean` implementation typically implements:

```java
FactoryBean<T>
```

where `T` represents the type of object it produces.

For example:

```java
public class GreetingServiceFactoryBean implements FactoryBean<GreetingService> {
```

The interface requires the implementation to provide `getObject()` and `getObjectType()`.

---

## getObject()

`getObject()` is responsible for creating or returning the object produced by the factory.

```java
@Override
public GreetingService getObject() {
    return new GreetingService("Hello from FactoryBean!");
}
```

Spring uses this method when an application requests the product bean.

Conceptually:

```text
getBean("greetingService")
            │
            ▼
GreetingServiceFactoryBean
            │
            ▼
       getObject()
            │
            ▼
     GreetingService
```

---

## getObjectType()

`getObjectType()` tells Spring the type of object produced by the factory.

```java
@Override
public Class<?> getObjectType() {
    return GreetingService.class;
}
```

This allows Spring to know the product type without necessarily creating the product object.

---

## Registering a FactoryBean

The factory can be registered using Java configuration:

```java
@Configuration
public class FactoryBeanConfig {

    @Bean
    public GreetingServiceFactoryBean greetingService() {
        return new GreetingServiceFactoryBean();
    }
}
```

The important detail is that the bean definition is for the factory:

```text
greetingService
       │
       ▼
GreetingServiceFactoryBean
```

But when the application requests `greetingService`, Spring normally returns the factory's product:

```text
greetingService
       │
       ▼
GreetingServiceFactoryBean
       │
       │ getObject()
       ▼
GreetingService
```

---

## Retrieving the Product

The normal lookup:

```java
GreetingService greetingService = applicationContext.getBean("greetingService", GreetingService.class);
```

returns the object produced by the `FactoryBean`.

Therefore:

```java
greetingService.getClass()
```

is:

```text
GreetingService
```

rather than:

```text
GreetingServiceFactoryBean
```

---

## Retrieving the FactoryBean Itself

Spring provides a special `&` prefix for retrieving the actual `FactoryBean`.

For example:

```java
Object factory = applicationContext.getBean("&greetingService");
```

This returns:

```text
GreetingServiceFactoryBean
```

The difference is:

```text
getBean("greetingService")
        │
        ▼
GreetingService
```

versus:

```text
getBean("&greetingService")
        │
        ▼
GreetingServiceFactoryBean
```

This is one of the most important behaviors to understand when working with `FactoryBean`.

---

## Complete Example

### GreetingService

```java
public class GreetingService {

    private final String message;

    public GreetingService(String message) {
        this.message = message;
    }

    public String greet() {
        return message;
    }
}
```

### GreetingServiceFactoryBean

```java
public class GreetingServiceFactoryBean implements FactoryBean<GreetingService> {

    @Override
    public GreetingService getObject() {
        return new GreetingService("Hello from FactoryBean!");
    }

    @Override
    public Class<?> getObjectType() {
        return GreetingService.class;
    }
}
```

### FactoryBeanConfig

```java
@Configuration
public class FactoryBeanConfig {

    @Bean
    public GreetingServiceFactoryBean greetingService() {
        return new GreetingServiceFactoryBean();
    }
}
```

### Application

```java
public class FactoryBeanApplication {

    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(FactoryBeanConfig.class)) {

            GreetingService greetingService = applicationContext.getBean("greetingService", GreetingService.class);

            System.out.println(greetingService.greet());
        }
    }
}
```

The application produces:

```text
Hello from FactoryBean!
```

---

## FactoryBean Resolution

The complete lookup process can be visualized as:

```text
                ApplicationContext
                       │
                       ▼
          "greetingService" requested
                       │
                       ▼
          GreetingServiceFactoryBean
                       │
                       │ getObject()
                       ▼
               GreetingService
                       │
                       ▼
             Returned to caller
```

When the factory itself is requested:

```text
                ApplicationContext
                       │
                       ▼
         "&greetingService" requested
                       │
                       ▼
          GreetingServiceFactoryBean
                       │
                       ▼
             Returned to caller
```

---

## FactoryBean and Singleton Behavior

By default, the object produced by a `FactoryBean` follows singleton behavior.

Therefore, repeated lookups of the product normally return the same instance:

```java
GreetingService first = applicationContext.getBean("greetingService", GreetingService.class);

GreetingService second = applicationContext.getBean("greetingService", GreetingService.class);
```

The references point to the same managed object:

```text
                 FactoryBean
                      │
                  getObject()
                      │
                      ▼
              GreetingService
                 ▲        ▲
                 │        │
               first    second
```

This behavior can be verified with:

```java
assertSame(first, second);
```

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
mvn -pl 12-advanced-spring/factory-bean clean test
```

---

## Key Takeaways

- `BeanFactory` and `FactoryBean` are different concepts.
- `BeanFactory` is a Spring IoC container.
- `FactoryBean` is a special Spring-managed factory.
- `getObject()` produces the object exposed by the factory.
- `getObjectType()` tells Spring the type of the produced object.
- `getBean("name")` normally returns the product of a `FactoryBean`.
- `getBean("&name")` returns the actual `FactoryBean`.
- `FactoryBean` is useful when an object's creation requires specialized factory logic.
- Factory-produced objects are singleton-scoped by default.

---

## Next

The next example focuses specifically on **BeanDefinition**, the metadata Spring uses to describe how a bean should be created and managed.