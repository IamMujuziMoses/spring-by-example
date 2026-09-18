# Service Registration

OpenMRS relies heavily on Spring to manage its application services.

This example demonstrates how an OpenMRS-style service interface and implementation can be registered as a Spring-managed bean and retrieved from the Spring `ApplicationContext`.

The example focuses on the **Spring bean registration side of OpenMRS services**. The deeper service management and lookup mechanisms provided by `ServiceContext` are covered separately in the **ServiceContext** example.

## Learning Objectives

- Understand how services are represented in OpenMRS.
- Understand the relationship between service interfaces and implementations.
- Understand how `@Service` registers a service implementation as a Spring bean.
- Understand named Spring beans.
- Retrieve a registered service from the `ApplicationContext`.
- Understand Spring's singleton behavior for services.
- Connect Spring bean registration with OpenMRS service architecture.

---

## What Is Service Registration?

In OpenMRS, application functionality is commonly exposed through service interfaces.

A service typically consists of:

```text
Service Interface
       │
       ▼
Service Implementation
       │
       ▼
Spring Bean
       │
       ▼
OpenMRS Service Layer
```

For example, a service interface might define an operation:

```java
public interface GreetingService {

    String greet();
}
```

The implementation provides the behavior:

```java
@Service("greetingService")
public class GreetingServiceImpl implements GreetingService {

    @Override
    public String greet() {
        return "Hello from an OpenMRS service!";
    }
}
```

The `@Service` annotation tells Spring that the implementation should be registered as a managed bean.

---

## Service Interface

The service interface defines the operations exposed by the service.

```java
public interface GreetingService {

    String greet();
}
```

Keeping the interface separate from the implementation allows consumers to depend on the service contract rather than the implementation class.

This is consistent with the service-oriented structure commonly used by OpenMRS.

---

## Service Implementation

The implementation contains the actual service behavior.

```java
@Service("greetingService")
public class GreetingServiceImpl implements GreetingService {

    @Override
    public String greet() {
        return "Hello from an OpenMRS service!";
    }
}
```

The important part of this example is:

```java
@Service("greetingService")
```

This marks the class as a Spring-managed component and gives the Spring bean the explicit name `greetingService`.

An explicit service name is useful when a service needs to be retrieved by its conventional name.

---

## How Spring Registers the Service

The simplified registration process is:

```text
GreetingServiceImpl
        │
        │ @Service
        ▼
Component Scanning
        │
        ▼
BeanDefinition
        │
        ▼
BeanDefinitionRegistry
        │
        ▼
ApplicationContext
        │
        ▼
GreetingService bean
```

The `@Service` annotation itself does not create the service instance.

Instead, Spring discovers the class during component scanning and registers metadata describing the bean.

The application context can then create and manage the service instance.

---

## Retrieving the Service

Once the service has been registered, it can be retrieved from the Spring `ApplicationContext`.

```java
GreetingService greetingService = context.getBean("greetingService", GreetingService.class);
```

The caller works with the `GreetingService` interface rather than the concrete implementation.

```text
ApplicationContext
        │
        ▼
"greetingService"
        │
        ▼
GreetingService
        │
        ▼
GreetingServiceImpl
```

---

## Complete Example

The application creates an `AnnotationConfigApplicationContext` and enables component scanning.

```java

@ComponentScan("com.springbyexample.serviceregistration")
public class ServiceRegistrationApplication {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(ServiceRegistrationApplication.class)) {

            GreetingService greetingService = context.getBean("greetingService", GreetingService.class);

            System.out.println(greetingService.greet());
        }
    }
}
```

The application prints:

```text
Hello from an OpenMRS service!
```

---

## Singleton Service Behavior

Spring beans are singleton-scoped by default.

This means retrieving the service multiple times from the same application context returns the same instance.

```java
GreetingService first = context.getBean("greetingService", GreetingService.class);
GreetingService second = context.getBean("greetingService", GreetingService.class);
```

Therefore:

```java
first == second
```

is `true`.

This is important for services because the application generally interacts with a single managed service instance within a given Spring application context.

---

## Service Registration vs ServiceContext

This example intentionally focuses on **Spring service registration**.

There is another important part of OpenMRS's service architecture: `ServiceContext`.

The concepts can be separated as follows.

### Service Registration

```text
@Service
   │
   ▼
Spring Component Scanning
   │
   ▼
BeanDefinition
   │
   ▼
ApplicationContext
   │
   ▼
Service Bean
```

### ServiceContext

The later `ServiceContext` example will explore the OpenMRS-specific service management layer:

```text
Service Bean
     │
     ▼
ServiceContext
     │
     ▼
OpenMRS Service Lookup
     │
     ▼
Service Proxy
```

Keeping these examples separate makes it easier to understand the different responsibilities.

---

## Relationship to Real OpenMRS Services

OpenMRS contains many services following this general interface and implementation structure.

For example:

```text
PatientService
      │
      ▼
PatientServiceImpl
      │
      ▼
Spring-managed service
```

The implementation is managed by Spring while the interface provides the service contract used by consumers.

This example uses `GreetingService` to demonstrate the same general Spring registration concept without introducing the complexity of the full OpenMRS API.

---

## Dependencies

The example uses:

- Spring Context
- Spring Beans
- JUnit Jupiter

No database or full OpenMRS runtime is required.

The example intentionally uses standard Spring infrastructure to demonstrate the underlying service registration concept.

---

## Running the Example

From the `service-registration` directory:

```bash
mvn clean install
```

The application can then be run from the IDE using:

```text
ServiceRegistrationApplication
```

Expected output:

```text
Hello from an OpenMRS service!
```

---

## Key Takeaways

- OpenMRS services commonly have a service interface and implementation.
- Spring can manage the service implementation as a bean.
- `@Service` marks the implementation for component scanning.
- A service can be given an explicit Spring bean name.
- Bean registration and bean creation are separate steps.
- Services can be retrieved from the Spring `ApplicationContext`.
- Spring beans are singleton-scoped by default.
- Spring bean registration is one part of the broader OpenMRS service architecture.
- `ServiceContext` provides additional OpenMRS-specific service management and will be explored separately.

---

## What's Next?

The next example explores **Module Loading**.

It will examine how OpenMRS modules are discovered, loaded, initialized, and integrated with the Spring application context.

The goal is to move from understanding how individual services are registered to understanding how larger OpenMRS modules participate in the application lifecycle.