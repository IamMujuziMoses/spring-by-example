# Custom Spring Profiles

This example demonstrates how **Spring Profiles** can be used to select different bean configurations based on the active environment.

The example uses an OpenMRS-style service interface with separate development and production implementations. Spring determines which implementation is registered based on the active profile.

> **Note:** This is a simplified learning example inspired by how Spring can be used in a real-world OpenMRS-style application. It does not reproduce OpenMRS's complete profile configuration.

## Learning Objectives

By completing this example, you will understand:

- What Spring Profiles are.
- How `@Profile` controls bean registration.
- How Spring's `Environment` manages active profiles.
- How to activate profiles programmatically.
- How different implementations can be selected for different environments.
- How profile-specific configuration works.
- How multiple profiles can be active.
- How Spring Profiles relate to OpenMRS-style configuration.

---

## What Are Spring Profiles?

Spring Profiles provide a mechanism for registering different beans depending on the environment in which an application is running.

For example, an application might need different configurations for:

```text
Development
Production
Testing
Local
Staging
```

Instead of changing the application code between environments, Spring can activate the appropriate profile.

Conceptually:

```text
                    Spring Application
                           │
                           ▼
                    Active Profile
                           │
              ┌────────────┴────────────┐
              ▼                         ▼
        development                 production
              │                         │
              ▼                         ▼
      Development Beans         Production Beans
```

---

## Why Use Profiles?

Different environments often require different configurations.

For example:

```text
Development
    ├── Local database
    ├── Debug logging
    └── Development services

Production
    ├── Production database
    ├── Production logging
    └── Production services
```

Spring Profiles allow these differences to be expressed through configuration rather than by modifying application code.

---

## 1. Service Interface

The example starts with a simple service interface:

```java
public interface GreetingService {

    String greet();
}
```

The application depends on the interface rather than a specific implementation.

This allows different implementations to be selected depending on the active profile.

---

## 2. Development Implementation

The development implementation is:

```java
public class DevelopmentGreetingService implements GreetingService {

    @Override
    public String greet() {
        return "Hello from the development environment!";
    }
}
```

This represents a service implementation intended for development.

---

## 3. Production Implementation

The production implementation is:

```java
public class ProductionGreetingService implements GreetingService {

    @Override
    public String greet() {
        return "Hello from the production environment!";
    }
}
```

Both implementations implement the same interface:

```text
              GreetingService
                    ▲
                    │
          ┌─────────┴─────────┐
          │                   │
          │                   │
DevelopmentGreetingService  ProductionGreetingService
```

The application does not need to know which implementation will be used.

---

## 4. Development Configuration

The development configuration is:

```java
@Configuration
@Profile("development")
public class DevelopmentConfig {

    @Bean
    public GreetingService greetingService() {
        return new DevelopmentGreetingService();
    }
}
```

The important part is:

```java
@Profile("development")
```

This tells Spring that the configuration is only eligible when the `development` profile is active.

When the profile is active:

```text
development
     │
     ▼
DevelopmentConfig
     │
     ▼
GreetingService
     │
     ▼
DevelopmentGreetingService
```

---

## 5. Production Configuration

The production configuration follows the same pattern:

```java
@Configuration
@Profile("production")
public class ProductionConfig {

    @Bean
    public GreetingService greetingService() {
        return new ProductionGreetingService();
    }
}
```

When the `production` profile is active:

```text
production
     │
     ▼
ProductionConfig
     │
     ▼
GreetingService
     │
     ▼
ProductionGreetingService
```

---

## 6. Root Configuration

The example imports both profile-specific configurations:

```java
@Configuration
@Import({ DevelopmentConfig.class, ProductionConfig.class })
public class OpenmrsProfileConfig {
}
```

Both configurations are available for Spring to process.

However, only the configuration whose profile matches the active environment is eligible.

For example:

```text
Active profile: development

DevelopmentConfig
        │
        ▼
      ACTIVE

ProductionConfig
        │
        ▼
     INACTIVE
```

---

## 7. Activating a Profile

The application creates an `AnnotationConfigApplicationContext`:

```java
AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
```

The profile is then activated:

```java
context.getEnvironment().setActiveProfiles("development");
```

The configuration is registered:

```java
context.register(OpenmrsProfileConfig.class);
```

Finally, the context is refreshed:

```java
context.refresh();
```

The order is important:

```text
Create Context
      │
      ▼
Set Active Profile
      │
      ▼
Register Configuration
      │
      ▼
Refresh Context
      │
      ▼
Process @Profile
      │
      ▼
Create Eligible Beans
```

The profile should therefore be activated before the context is refreshed.

---

## 8. Complete Application

The complete application is:

```java
public class CustomSpringProfilesApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles("development");
        context.register(OpenmrsProfileConfig.class);
        context.refresh();

        GreetingService service = context.getBean(GreetingService.class);

        System.out.println("Active profiles: " + String.join(", ", context.getEnvironment().getActiveProfiles()));
        System.out.println(service.greet());

        context.close();
    }
}
```

The application activates:

```text
development
```

and retrieves:

```java
GreetingService
```

without knowing which implementation Spring created.

---

## 9. Running With the Development Profile

With:

```java
context.getEnvironment().setActiveProfiles("development");
```

Spring selects:

```text
DevelopmentConfig
        │
        ▼
DevelopmentGreetingService
```

The output is:

```text
Active profiles: development
Hello from the development environment!
```

---

## 10. Running With the Production Profile

Changing:

```java
context.getEnvironment().setActiveProfiles("development");
```

to:

```java
context.getEnvironment().setActiveProfiles("production");
```

causes Spring to select:

```text
ProductionConfig
        │
        ▼
ProductionGreetingService
```

The application code that retrieves the service remains unchanged:

```java
GreetingService service = context.getBean(GreetingService.class);
```

The output becomes:

```text
Active profiles: production
Hello from the production environment!
```

This demonstrates one of the main benefits of profiles: the application can depend on the same abstraction while Spring selects the appropriate implementation.

---

## 11. How `@Profile` Works

The `@Profile` annotation is used to make configuration conditional.

For example:

```java
@Configuration
@Profile("development")
public class DevelopmentConfig {
}
```

Spring checks the active profiles when processing the configuration.

Conceptually:

```text
                  Active Profiles
                         │
                         ▼
                  "development"
                         │
              ┌──────────┴──────────┐
              │                     │
              ▼                     ▼
     @Profile("development")  @Profile("production")
              │                     │
              ▼                     ▼
            Match               No Match
              │                     │
              ▼                     X
      Register config
```

This means profile selection takes place during Spring's configuration and bean-registration process.

---

## 12. Profiles and Bean Registration

Profiles affect whether configuration is eligible for processing.

Consider:

```java
@Configuration
@Profile("development")
public class DevelopmentConfig {

    @Bean
    public GreetingService greetingService() {
        return new DevelopmentGreetingService();
    }
}
```

When `development` is active, Spring registers the bean:

```text
development
    │
    ▼
DevelopmentConfig
    │
    ▼
greetingService
```

When `production` is active, the configuration is not eligible:

```text
production
    │
    ▼
DevelopmentConfig
    │
    ▼
Not registered
```

The production configuration can then provide the same bean:

```text
production
    │
    ▼
ProductionConfig
    │
    ▼
greetingService
```

---

## 13. Method-Level `@Profile`

`@Profile` can also be applied to individual bean methods.

For example:

```java
@Configuration
public class AppConfig {

    @Bean
    @Profile("development")
    public GreetingService developmentGreetingService() {
        return new DevelopmentGreetingService();
    }

    @Bean
    @Profile("production")
    public GreetingService productionGreetingService() {
        return new ProductionGreetingService();
    }
}
```

This allows different beans within the same configuration class to be profile-specific.

The example uses class-level profiles because they make the configuration boundaries easier to understand.

---

## 14. The Spring `Environment`

Profiles are managed through Spring's `Environment`.

The application can access the environment with:

```java
context.getEnvironment()
```

Active profiles can be retrieved using:

```java
context.getEnvironment().getActiveProfiles()
```

For example:

```java
String[] activeProfiles = context.getEnvironment().getActiveProfiles();
```

The environment therefore provides information about the current configuration context.

Conceptually:

```text
ApplicationContext
       │
       ▼
Environment
       │
       ├── Active Profiles
       ├── Default Profiles
       └── Property Sources
```

This connects the example with the `Environment` and Property Sources concepts covered earlier in Module 12.

---

## 15. Multiple Active Profiles

Spring allows multiple profiles to be active simultaneously.

For example:

```java
context.getEnvironment().setActiveProfiles("development","local");
```

The environment then contains:

```text
Active Profiles

- development
- local
```

This can be useful when different configuration concerns need to be combined.

For example:

```text
development
      +
local
      +
debug
```

could represent a developer's local environment.

---

## 16. Default Profiles

Spring also supports a default profile.

A configuration can specify:

```java
@Configuration
@Profile("default")
public class DefaultConfig {

    @Bean
    public GreetingService greetingService() {
        return new DevelopmentGreetingService();
    }
}
```

The `default` profile is used when no other profile is active.

This can provide fallback configuration for an application.

---

## 17. Activating Profiles Through Configuration

Profiles do not have to be activated programmatically.

Spring also supports:

```properties
spring.profiles.active=development
```

Multiple profiles can be specified:

```properties
spring.profiles.active=development,local
```

Spring Boot applications can also activate profiles from the command line:

```bash
java -jar application.jar \
    --spring.profiles.active=production
```

This example uses:

```java
context.getEnvironment().setActiveProfiles(...)
```

because the purpose of the example is to demonstrate the underlying Spring `Environment` mechanism rather than Spring Boot configuration.

---

## 18. Relationship to OpenMRS

OpenMRS uses Spring extensively as part of its application and service infrastructure.

The current OpenMRS `ServiceContext` works with a Spring `ApplicationContext`, and Spring provides service implementations through dependency injection.

Profiles can be useful in applications with environment-specific configuration because the same service contract can be backed by different implementations or configurations.

Conceptually:

```text
                     OpenMRS
                        │
                        ▼
                Spring Environment
                        │
                        ▼
                  Active Profile
                        │
             ┌──────────┴──────────┐
             ▼                     ▼
        Development            Production
             │                     │
             ▼                     ▼
     Development Config     Production Config
             │                     │
             └──────────┬──────────┘
                        ▼
                  Service Layer
```

This example does not claim that these exact `development` and `production` classes are part of OpenMRS core. They are simplified classes used to demonstrate how Spring Profiles can be applied to an OpenMRS-style service architecture.

---

## 19. Relationship to Previous Module 14 Examples

This example builds on the concepts introduced throughout Module 14.

### Service Registration

The Service Registration example demonstrated how a service can be registered with Spring.

```text
Service
   │
   ▼
Spring Container
```

### Module Loading

The Module Loading example demonstrated how OpenMRS-style modules can participate in application lifecycle events.

```text
Module
   │
   ▼
Module Loader
   │
   ▼
Spring Context
```

### XML to Java Configuration

The XML to Java Configuration example demonstrated how Spring configuration can be migrated from XML to Java configuration.

```text
XML Configuration
       │
       ▼
Java Configuration
```

### OpenmrsBeanRegistrar

The OpenmrsBeanRegistrar example demonstrated programmatic bean registration.

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

The ServiceContext example demonstrated how OpenMRS-style infrastructure can provide access to services.

```text
Service
   │
   ▼
ServiceContext
   │
   ▼
ApplicationContext
```

### AOP in OpenMRS

The AOP example demonstrated how service methods can be intercepted.

```text
Service
   │
   ▼
AOP Proxy
   │
   ▼
Target Service
```

### Transaction Management

The Transaction Management example demonstrated how transaction behavior can be applied around service invocations.

```text
Service
   │
   ▼
Transactional Proxy
   │
   ▼
Transaction Interceptor
   │
   ▼
Target Service
```

### Custom Spring Profiles

Profiles determine which configuration and service implementation is registered for the current environment.

```text
Environment
    │
    ▼
Active Profile
    │
    ▼
Configuration
    │
    ▼
Service
    │
    ▼
AOP / Transactions
```

Together, these examples show several layers of Spring's integration into an OpenMRS-style application.

---

## 20. Dependencies

The example uses Spring Context:

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

## 21. Running the Example

From the project root:

```bash
mvn clean install
```

Run the application:

```bash
mvn exec:java \
    -Dexec.mainClass=com.springbyexample.customspringprofiles.CustomSpringProfilesApplication
```

The application activates the `development` profile by default.

Expected output:

```text
Active profiles: development
Hello from the development environment!
```

Change:

```java
context.getEnvironment().setActiveProfiles("development");
```

to:

```java
context.getEnvironment().setActiveProfiles("production");
```

and run the application again.

Expected output:

```text
Active profiles: production
Hello from the production environment!
```

---

## Key Takeaways

- Spring Profiles allow configuration to vary between environments.
- `@Profile` controls whether a configuration or bean is eligible for registration.
- Profiles are managed through Spring's `Environment`.
- Profiles must be activated before the application context is refreshed when using programmatic activation.
- Different implementations of the same service interface can be selected using profiles.
- Multiple profiles can be active simultaneously.
- Spring supports both class-level and method-level `@Profile`.
- A default profile can provide fallback configuration.
- Profiles can also be activated through configuration properties such as `spring.profiles.active`.
- Profiles participate in Spring's conditional configuration and bean-registration process.
- The example demonstrates how this mechanism can be applied to an OpenMRS-style service layer.

---

## Module 14 Complete

With **Custom Spring Profiles** complete, all planned Module 14 examples have now been covered:

```text
Module 14 — OpenMRS Examples
│
├── Service Registration
├── Module Loading
├── XML to Java Configuration
├── OpenmrsBeanRegistrar
├── ServiceContext
├── AOP in OpenMRS
├── Transaction Management
└── Custom Spring Profiles
```

Module 14 has connected the Spring concepts explored throughout the repository with simplified examples inspired by a real-world OpenMRS architecture.

### Next

With Module 14 complete, the next module can move beyond OpenMRS-specific examples and continue exploring Spring's application-level capabilities.

---

#### [Back To Top ⬆️](#custom-spring-profiles)