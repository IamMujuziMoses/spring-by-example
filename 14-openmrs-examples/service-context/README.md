# ServiceContext

This example demonstrates a simplified **OpenMRS-style `ServiceContext`** and how it acts as a service layer between application code and Spring-managed services.

The example builds on the Spring bean registration and configuration concepts covered in the previous Module 14 examples. It demonstrates how a service can be created by Spring, exposed through a service context, and retrieved either through the service layer or directly through Spring's `ApplicationContext`.

> **Note:** This is a simplified learning implementation. It demonstrates the core concepts behind OpenMRS `ServiceContext` rather than reproducing the complete OpenMRS implementation.

---

## Learning Objectives

By completing this example, you will learn:

- What `ServiceContext` represents in OpenMRS.
- How OpenMRS exposes Spring-managed services through a service layer.
- How a service can be registered with a service context.
- How services can be retrieved by their interface.
- How `ServiceContext` can interact with Spring's `ApplicationContext`.
- How registered Spring components can be retrieved by type or bean name.
- How the service layer differs from direct `ApplicationContext` access.
- How `ServiceContext` relates to OpenMRS's `Context` class.

---

## What Is ServiceContext?

In OpenMRS, `ServiceContext` provides access to the application's service layer.

Application code can request a service such as:

```java
GreetingService service = serviceContext.getService(GreetingService.class);
```

rather than needing to know how the service was created or configured.

Conceptually:

```text
Application Code
       │
       ▼
ServiceContext
       │
       ▼
OpenMRS Service
       │
       ▼
Spring-managed Object
```

The real OpenMRS `ServiceContext` also integrates with Spring's `ApplicationContext` to access registered components.

---

## Why Have a ServiceContext?

A service context provides a layer between application code and the underlying dependency injection container.

Without a service layer, application code might access Spring directly:

```java
applicationContext.getBean(GreetingService.class);
```

With a service context:

```java
serviceContext.getService(GreetingService.class);
```

The second approach allows the application to interact with the OpenMRS service layer without needing to know the details of the underlying Spring configuration.

---

## The Service Interface

The example begins with a simple service interface:

```java
public interface GreetingService {

    String greet();
}
```

The interface represents the contract exposed by the service.

Application code depends on the interface rather than the implementation.

---

## The Service Implementation

The implementation contains the actual service behavior:

```java
public class GreetingServiceImpl implements GreetingService {

    @Override
    public String greet() {
        return "Hello from the OpenMRS ServiceContext!";
    }
}
```

The implementation does not use `@Service`.

Instead, it is explicitly registered as a Spring bean in the example configuration:

```java
@Bean
public GreetingService greetingService() {
    return new GreetingServiceImpl();
}
```

This keeps the example focused on the relationship between Spring and `ServiceContext`.

---

## Creating the Spring ApplicationContext

Spring creates the service through the application configuration:

```java
@Configuration
static class AppConfig {

    @Bean
    public GreetingService greetingService() {
        return new GreetingServiceImpl();
    }
}
```

The application context then manages the resulting service instance.

```text
@Configuration
     │
     ▼
@Bean
     │
     ▼
ApplicationContext
     │
     ▼
GreetingService
```

---

## ServiceContext and ApplicationContext

The simplified `ServiceContext` implements `ApplicationContextAware`:

```java
public class ServiceContext implements ApplicationContextAware {
```

This allows Spring to provide the `ApplicationContext` to the service context.

The application context can then be stored:

```java
@Override
public void setApplicationContext(ApplicationContext applicationContext) {

    this.applicationContext = applicationContext;
}
```

This creates the connection:

```text
ServiceContext
      │
      ▼
ApplicationContext
      │
      ▼
Spring-managed Beans
```

---

## Registering a Service

The example provides a simple service registration method:

```java
public void setService(Class<?> serviceType, Object service) {

    services.put(serviceType, service);
}
```

The service can then be registered using its interface:

```java
serviceContext.setService(GreetingService.class, greetingService);
```

Conceptually:

```text
GreetingService.class
        │
        ▼
GreetingService instance
        │
        ▼
ServiceContext
```

---

## Retrieving a Service

Once registered, application code can retrieve the service by its interface:

```java
GreetingService service =
        serviceContext.getService(
                GreetingService.class
        );
```

The service context performs the lookup:

```java
public <T> T getService(Class<T> serviceType) {
    Object service = services.get(serviceType);

    if (service == null) {
        throw new IllegalStateException("No service registered for " + serviceType.getName());
    }

    return serviceType.cast(service);
}
```

The caller does not need to know how the service was instantiated.

---

## Registered Spring Components

The service context can also expose Spring-managed components.

The example provides:

```java
public <T> List<T> getRegisteredComponents(Class<T> type) {

    Map<String, T> beans = applicationContext.getBeansOfType(type);

    return new ArrayList<>(beans.values());
}
```

This delegates the lookup to Spring:

```java
applicationContext.getBeansOfType(type);
```

For example:

```java
List<GreetingService> services = serviceContext.getRegisteredComponents(GreetingService.class);
```

This allows the service context to retrieve all Spring beans matching a particular type.

---

## Retrieving a Component by Name

The example also supports retrieving a specific Spring component:

```java
GreetingService service = serviceContext.getRegisteredComponent("greetingService", GreetingService.class);
```

Internally, this delegates to:

```java
applicationContext.getBean(beanName, type);
```

The flow is therefore:

```text
Application Code
       │
       ▼
ServiceContext
       │
       ▼
ApplicationContext
       │
       ▼
BeanFactory
       │
       ▼
Spring Bean
```

---

## Complete Example

The application brings the concepts together:

```java
public class ServiceContextApplication {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext applicationContext = 
                     new AnnotationConfigApplicationContext(AppConfig.class)) {

            ServiceContext serviceContext = ServiceContext.getInstance();
            serviceContext.setApplicationContext(applicationContext);

            GreetingService greetingService = applicationContext.getBean(GreetingService.class);
            serviceContext.setService(GreetingService.class, greetingService);

            GreetingService service = serviceContext.getService(GreetingService.class);
            
            System.out.println(service.greet());

            GreetingService registeredComponent = 
                    serviceContext.getRegisteredComponent("greetingService", GreetingService.class);

            System.out.println(registeredComponent.greet());
        } finally {
            ServiceContext.destroyInstance();
        }
    }

    @Configuration
    static class AppConfig {

        @Bean
        public GreetingService greetingService() {
            return new GreetingServiceImpl();
        }
    }
}
```

Expected output:

```text
Hello from the OpenMRS ServiceContext!
Hello from the OpenMRS ServiceContext!
```

---

## Two Service Lookup Paths

This example demonstrates two related but distinct lookup mechanisms.

### Service Layer Lookup

```java
serviceContext.getService(GreetingService.class);
```

This represents the OpenMRS service abstraction.

```text
Application
     │
     ▼
ServiceContext
     │
     ▼
Service
```

### Spring Component Lookup

```java
serviceContext.getRegisteredComponent("greetingService",GreetingService.class);
```

This delegates to the Spring application context:

```text
Application
     │
     ▼
ServiceContext
     │
     ▼
ApplicationContext
     │
     ▼
Spring Bean
```

Understanding this distinction is important when studying how OpenMRS integrates its service architecture with Spring.

---

## ServiceContext as a Singleton

The example uses a singleton-style access pattern:

```java
public static synchronized ServiceContext getInstance() {
    if (instance == null) {
        instance = new ServiceContext();
    }

    return instance;
}
```

This means repeated calls return the same `ServiceContext`:

```java
ServiceContext first = ServiceContext.getInstance();

ServiceContext second = ServiceContext.getInstance();
```

Therefore:

```java
assertSame(first, second);
```

The real OpenMRS architecture also treats `ServiceContext` as a long-lived central service context.

---

## Destroying the ServiceContext

The example provides:

```java
public static synchronized void destroyInstance() {
    if (instance != null) {
        instance.services.clear();
        instance.applicationContext = null;
        instance = null;
    }
}
```

This allows the example and its tests to reset the context cleanly.

It is especially useful when running multiple tests that each need a fresh service context.

---

## Relationship to OpenMRS Context

The real OpenMRS architecture places `ServiceContext` underneath `Context`.

Conceptually:

```text
Application Code
       │
       ▼
     Context
       │
       ▼
ServiceContext
       │
       ├─────────────────┐
       ▼                 ▼
Service Layer     ApplicationContext
                         │
                         ▼
                    Spring Beans
```

`Context` provides the application-facing API for obtaining OpenMRS services, while `ServiceContext` contains the underlying service infrastructure.

For example, application code can work conceptually with:

```java
Context.getPatientService();
```

while the service infrastructure underneath ultimately relies on Spring-managed services.

---

## Relationship to Previous Examples

This example connects several concepts already covered in this project.

### Service Registration

The previous example demonstrated registering a Spring service:

```text
Service Interface
       │
       ▼
Service Implementation
       │
       ▼
Spring Bean
```

### OpenmrsBeanRegistrar

The previous example demonstrated programmatic registration:

```text
BeanDefinition
       │
       ▼
BeanDefinitionRegistry
       │
       ▼
Spring Bean
```

### ServiceContext

This example adds another layer:

```text
Spring Bean
     │
     ▼
ServiceContext
     │
     ▼
Application Code
```

Together:

```text
Bean Definition
      │
      ▼
Spring Container
      │
      ▼
Service
      │
      ▼
ServiceContext
      │
      ▼
OpenMRS Application Code
```

---

## Dependencies

The example requires:

- Spring Context
- JUnit Jupiter

Spring Context provides:

- `ApplicationContext`
- `ApplicationContextAware`
- `AnnotationConfigApplicationContext`
- `@Configuration`
- `@Bean`

---

## Running the Example

From the project root:

```bash
mvn -pl 14-openmrs-examples/service-context clean install
```

Run `ServiceContextApplication` from your IDE or using your preferred Java execution method.

Expected output:

```text
Hello from the OpenMRS ServiceContext!
Hello from the OpenMRS ServiceContext!
```

---

## Key Takeaways

- `ServiceContext` provides a service layer between application code and Spring-managed services.
- OpenMRS uses Spring as part of its service infrastructure.
- Services can be accessed through their service interfaces.
- `ServiceContext` can maintain service references.
- `ServiceContext` can also delegate component lookups to Spring's `ApplicationContext`.
- `getRegisteredComponents()` allows components to be retrieved by type.
- `getRegisteredComponent()` allows a component to be retrieved by bean name and type.
- `ServiceContext` is conceptually distinct from the underlying Spring `ApplicationContext`.
- The OpenMRS `Context` layer sits above `ServiceContext` in the broader architecture.
- This example connects Spring bean management with OpenMRS's service abstraction.

---

## Next

The next example will explore: _AOP in OpenMRS_

This example will demonstrate how Spring AOP concepts are applied within OpenMRS, including service interception and cross-cutting behavior such as authorization, logging, and transaction-related concerns.