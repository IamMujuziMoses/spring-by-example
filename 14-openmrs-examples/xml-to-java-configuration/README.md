# XML to Java Configuration

OpenMRS has historically used Spring XML configuration in parts of its application infrastructure.

This example demonstrates how a Spring bean configured using XML can be represented using Java configuration with `@Configuration` and `@Bean`.

It also demonstrates how XML and Java configuration can **coexist during a migration**, allowing an application to gradually move from XML configuration to Java-based configuration.

## Learning Objectives

- Understand traditional Spring XML bean configuration.
- Understand the equivalent Java-based configuration.
- Understand the role of `@Configuration`.
- Understand the role of `@Bean`.
- Understand how `@ImportResource` allows Java configuration to import existing XML configuration.
- Understand how XML and Java configuration can coexist.
- Understand why configuration migration can be performed incrementally.
- Connect Spring configuration migration with the type of configuration modernization that can occur in a real-world project such as OpenMRS.

---

## What Is XML Configuration?

Before Java configuration became widely used, Spring applications commonly defined beans using XML.

A bean could be declared like this:

```xml
<bean id="greetingService" class="com.springbyexample.xmltojavaconfiguration.GreetingServiceImpl"/>
```

The XML tells Spring:

- Create a bean named `greetingService`.
- Use `GreetingServiceImpl` as its implementation class.
- Manage the resulting object in the Spring container.

Conceptually:

```text
XML Configuration
        │
        ▼
     <bean>
        │
        ▼
   BeanDefinition
        │
        ▼
Spring Container
        │
        ▼
 Managed Bean
```

---

## What Is Java Configuration?

Spring also supports defining beans using Java configuration.

The equivalent configuration is:

```java
@Configuration
public class JavaConfig {

    @Bean
    public GreetingService javaGreetingService() {
        return new GreetingServiceImpl();
    }
}
```

The `@Configuration` annotation identifies the class as a source of bean definitions.

The `@Bean` annotation tells Spring that the returned object should be managed as a Spring bean.

Conceptually:

```text
@Configuration
      │
      ▼
    @Bean
      │
      ▼
 BeanDefinition
      │
      ▼
Spring Container
      │
      ▼
 Managed Bean
```

---

## XML Configuration vs Java Configuration

The two approaches express the same fundamental concept: providing bean definitions to the Spring container.

### XML

```xml
<bean id="greetingService" class="com.springbyexample.xmltojavaconfiguration.GreetingServiceImpl"/>
```

### Java

```java
@Bean
public GreetingService javaGreetingService() {
    return new GreetingServiceImpl();
}
```

The configuration mechanism is different, but both result in a Spring-managed bean.

```text
             XML                         Java
              │                           │
              ▼                           ▼
           <bean>                       @Bean
              │                           │
              └────────────┬──────────────┘
                           ▼
                    BeanDefinition
                           │
                           ▼
                    Spring Container
```

---

## Service Interface

The example uses a simple service interface.

```java
public interface GreetingService {

    String greet();
}
```

The interface allows both configurations to expose the same service contract.

---

## Service Implementation

The implementation contains the actual behavior.

```java
public class GreetingServiceImpl implements GreetingService {

    @Override
    public String greet() {
        return "Hello from a configured service!";
    }
}
```

Notice that the class does **not** use `@Component` or `@Service`.

This is intentional.

The purpose of this example is to demonstrate explicit bean configuration rather than component scanning.

The two configuration approaches are therefore responsible for registering the implementation.

---

## XML Bean Definition

The XML configuration defines the service using a `<bean>` element.

```xml
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
           http://www.springframework.org/schema/beans
           https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="greetingService"
          class="com.springbyexample.xmltojavaconfiguration.GreetingServiceImpl"/>

</beans>
```

The bean is registered with the name:

```text
greetingService
```

It can then be retrieved from the Spring context:

```java
GreetingService greetingService = context.getBean("greetingService", GreetingService.class);
```

---

## Java Bean Definition

The same implementation can be registered using Java configuration.

```java
@Configuration
public class JavaConfig {

    @Bean
    public GreetingService javaGreetingService() {
        return new GreetingServiceImpl();
    }
}
```

The method name becomes the default bean name:

```text
javaGreetingService
```

The resulting bean can be retrieved with:

```java
GreetingService greetingService = context.getBean("javaGreetingService", GreetingService.class);
```

---

## Importing Existing XML Configuration

Java configuration does not require existing XML configuration to be removed immediately.

Spring provides `@ImportResource` for importing XML configuration into a Java configuration class.

```java
@Configuration
@ImportResource("classpath:applicationContext.xml")
public class JavaConfig {

    @Bean
    public GreetingService javaGreetingService() {
        return new GreetingServiceImpl();
    }
}
```

This allows the application context to contain both:

```text
Java Configuration
      │
      ├── @Bean definitions
      │
      └── Imported XML definitions
```

This is useful when migrating a large application incrementally.

---

## XML and Java Configuration Coexisting

The example intentionally gives the XML and Java beans different names:

```text
XML:
greetingService

Java:
javaGreetingService
```

This allows both beans to exist in the same application context.

```text
AnnotationConfigApplicationContext
              │
              ├── greetingService
              │      └── XML definition
              │
              └── javaGreetingService
                     └── Java @Bean definition
```

Both beans implement:

```java
GreetingService
```

Therefore, the application can retrieve either implementation through the same interface.

---

## Java Configuration

The complete Java configuration is:

```java
@Configuration
@ImportResource("classpath:applicationContext.xml")
public class JavaConfig {

    @Bean
    public GreetingService javaGreetingService() {
        return new GreetingServiceImpl();
    }
}
```

There are two important annotations here.

### `@Configuration`

```java
@Configuration
```

Marks the class as a source of Spring bean definitions.

### `@Bean`

```java
@Bean
public GreetingService javaGreetingService() {
    return new GreetingServiceImpl();
}
```

Registers the object returned by the method as a Spring-managed bean.

### `@ImportResource`

```java
@ImportResource("classpath:applicationContext.xml")
```

Imports the existing XML configuration into the application context.

Together, these annotations allow the application to use both configuration styles.

---

## Complete Application

The application creates an `AnnotationConfigApplicationContext` using the Java configuration.

```java
public class XmlToJavaConfigurationApplication {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class)) {

            GreetingService xmlGreetingService = context.getBean("greetingService", GreetingService.class);
            GreetingService javaGreetingService = context.getBean("javaGreetingService", GreetingService.class);

            System.out.println("XML service: " + xmlGreetingService.greet());
            System.out.println("Java service: " + javaGreetingService.greet());
        }
    }
}
```

The application can retrieve both beans from the same context.

Expected output:

```text
XML service: Hello from a configured service!
Java service: Hello from a configured service!
```

---

## The Migration Pattern

A real migration can happen gradually.

### Step 1 — Existing XML Configuration

An application may initially contain:

```xml
<bean id="greetingService" class="com.springbyexample.xmltojavaconfiguration.GreetingServiceImpl"/>
```

### Step 2 — Introduce Java Configuration

Java configuration can then be introduced:

```java
@Configuration
public class JavaConfig {

    @Bean
    public GreetingService javaGreetingService() {
        return new GreetingServiceImpl();
    }
}
```

### Step 3 — Import Existing XML

Existing XML can continue to be used:

```java
@Configuration
@ImportResource("classpath:applicationContext.xml")
public class JavaConfig {

    @Bean
    public GreetingService javaGreetingService() {
        return new GreetingServiceImpl();
    }
}
```

### Step 4 — Gradually Migrate

As XML configuration is converted, the corresponding `@Bean` definitions can replace the old XML definitions.

Conceptually:

```text
Existing Application
        │
        ▼
   XML Configuration
        │
        ▼
Introduce Java Configuration
        │
        ▼
@ImportResource
        │
        ▼
XML + Java Configuration
        │
        ▼
Gradual Migration
        │
        ▼
Java Configuration
```

This is the main migration pattern demonstrated by this example.

---

## Why Incremental Migration Matters

Large applications may contain a significant amount of existing XML configuration.

Replacing all configuration at once can introduce unnecessary risk.

A gradual approach allows an application to:

- Keep existing XML configuration working.
- Introduce Java configuration for new functionality.
- Migrate existing configuration incrementally.
- Test each migration step independently.
- Remove XML definitions after their Java equivalents are verified.

This makes configuration modernization easier to manage.

---

## Relationship to OpenMRS

OpenMRS contains Spring configuration that has historically included XML-based configuration.

For example, OpenMRS service infrastructure includes XML application context configuration alongside Java-based Spring configuration and registration mechanisms.

This makes XML-to-Java configuration an important concept when studying how Spring is used within OpenMRS.

The example does not attempt to migrate an actual OpenMRS configuration file.

Instead, it isolates the underlying Spring mechanism:

```text
XML Bean Definition
        │
        ▼
Existing Spring Configuration
        │
        ▼
Java Configuration
        │
        ▼
@Configuration + @Bean
        │
        ▼
Spring ApplicationContext
```

The next OpenMRS-specific examples can then build on this foundation by examining mechanisms such as `OpenmrsBeanRegistrar` and `ServiceContext`.

---

## Configuration Flow

The complete flow can be summarized as:

```text
                    Configuration Sources
                            │
                ┌───────────┴───────────┐
                │                       │
                ▼                       ▼
          XML Configuration       Java Configuration
                │                       │
             <bean>              @Configuration
                │                       │
                │                      @Bean
                │                       │
                └───────────┬───────────┘
                            ▼
                     Spring Container
                            │
                            ▼
                    Bean Definitions
                            │
                            ▼
                    Managed Bean Instances
```

During migration:

```text
Existing XML
     │
     ▼
@ImportResource
     │
     ▼
Java Configuration
     │
     ├── Existing XML beans
     │
     └── New @Bean definitions
```

---

## Dependencies

The example uses:

- Spring Context
- Spring Beans
- JUnit Jupiter

No database or OpenMRS runtime is required.

The example intentionally uses standard Spring infrastructure so that the XML-to-Java configuration mechanism can be understood independently.

---

## Running the Example

From the `xml-to-java-configuration` directory:

```bash
mvn clean install
```

The application can then be run from the IDE using:

```text
XmlToJavaConfigurationApplication
```

Expected output:

```text
XML service: Hello from a configured service!
Java service: Hello from a configured service!
```

---

## Key Takeaways

- Spring can define beans using XML configuration.
- Spring can also define beans using Java configuration.
- `<bean>` and `@Bean` are different configuration mechanisms that ultimately provide bean definitions to the Spring container.
- `@Configuration` identifies a Java configuration class.
- `@Bean` declares a Spring-managed bean.
- `@ImportResource` allows Java configuration to import existing XML configuration.
- XML and Java configuration can coexist within the same application context.
- Configuration migration can therefore be performed incrementally.
- Avoiding component scanning in this example keeps the focus on explicit configuration.
- The same principles can be applied when modernizing Spring configuration in a larger project such as OpenMRS.

---

## What's Next?

The next example explores **`OpenmrsBeanRegistrar`**.

It will move from general Spring XML-to-Java configuration into OpenMRS-specific infrastructure and demonstrate how OpenMRS can programmatically register beans with Spring's `BeanDefinitionRegistry`.