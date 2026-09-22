# AOP in OpenMRS

This example demonstrates how **Spring AOP is applied to OpenMRS-style services** to implement cross-cutting concerns such as logging and authorization.

The example builds on the Spring AOP concepts covered in Module 7 and the `ServiceContext` concepts covered earlier in Module 14.

Instead of placing authorization and logging logic directly inside every service method, Spring AOP allows these concerns to be applied around service invocations through proxies and advice.

> **Note:** This is a simplified learning implementation inspired by the AOP integration used by OpenMRS. It does not attempt to reproduce the complete OpenMRS AOP infrastructure.

## Learning Objectives

By completing this example, you will learn:

- How Spring AOP can be applied to OpenMRS-style services.
- How `MethodInterceptor` can implement cross-cutting behavior.
- How `ProxyFactory` creates a service proxy.
- How advice executes around a service method.
- How authorization can be implemented as cross-cutting behavior.
- How logging can be applied without modifying service implementations.
- How multiple pieces of advice can be applied to the same service.
- How an OpenMRS-style service context can manage proxied services.

---

## What Is AOP?

**Aspect-Oriented Programming (AOP)** allows behavior that applies across multiple parts of an application to be separated from the application's core business logic.

Common cross-cutting concerns include:

- Authorization
- Logging
- Transactions
- Caching
- Auditing
- Security

For example, a service method should primarily contain its business logic:

```java
@Override
public String greet() {
    return "Hello from an OpenMRS AOP service!";
}
```

Authorization and logging do not need to be implemented inside the method itself.

Instead, they can be applied through AOP:

```text
Caller
  │
  ▼
AOP Proxy
  │
  ├── Authorization
  │
  ├── Logging
  │
  ▼
Service Method
```

---

## Why AOP Is Useful in OpenMRS

OpenMRS has many services that perform different business operations.

Adding authorization, logging, transaction handling, or other cross-cutting behavior directly to every service method would result in duplicated code.

AOP allows those concerns to be applied around service invocations instead.

For example:

```text
PatientService
     │
     ├── Authorization
     ├── Logging
     └── Transaction handling
```

The service itself can remain focused on its business responsibility.

---

## The Service Interface

The example uses a simple service interface:

```java
public interface GreetingService {

    String greet();
}
```

The interface represents the service contract exposed to callers.

---

## The Service Implementation

The service implementation contains only its business behavior:

```java
public class GreetingServiceImpl implements GreetingService {

    @Override
    public String greet() {
        return "Hello from an OpenMRS AOP service!";
    }
}
```

There is no authorization or logging code inside the service.

That behavior will be applied externally through AOP advice.

---

## Logging Advice

The example provides a `LoggingAdvice` implementation using Spring's AOP Alliance `MethodInterceptor`:

```java
public class LoggingAdvice implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation)
            throws Throwable {

        System.out.println(
                "Calling service method: "
                        + invocation.getMethod().getName()
        );

        Object result = invocation.proceed();

        System.out.println(
                "Completed service method: "
                        + invocation.getMethod().getName()
        );

        return result;
    }
}
```

The important operation is:

```java
invocation.proceed();
```

This allows the invocation to continue to the next interceptor or ultimately to the target service.

The resulting flow is:

```text
LoggingAdvice
      │
      ▼
Before
      │
      ▼
Service Method
      │
      ▼
After
```

---

## Authorization Advice

Authorization is another example of a cross-cutting concern.

The example implements it using another `MethodInterceptor`:

```java
public class AuthorizationAdvice
        implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation)
            throws Throwable {

        if (!authorized) {
            throw new SecurityException(
                    "User is not authorized"
            );
        }

        return invocation.proceed();
    }
}
```

The service method is only allowed to execute when authorization succeeds.

```text
Caller
  │
  ▼
AuthorizationAdvice
  │
  ├── Not authorized ──► SecurityException
  │
  └── Authorized ──────► Service Method
```

The real OpenMRS authorization infrastructure is more sophisticated and integrates with OpenMRS's security model. This example focuses on the underlying AOP mechanism.

---

## Creating the AOP Proxy

The example uses Spring's `ProxyFactory`:

```java
ProxyFactory proxyFactory =
        new ProxyFactory(service);

proxyFactory.addAdvice(advice);

T proxy = serviceType.cast(
        proxyFactory.getProxy()
);
```

The resulting object is a proxy around the original service.

```text
GreetingServiceImpl
       │
       ▼
ProxyFactory
       │
       ▼
AOP Proxy
       │
       ├── Advice
       │
       ▼
GreetingServiceImpl
```

The application interacts with the proxy through the same service interface.

---

## AopServiceContext

The `AopServiceContext` provides a simplified OpenMRS-style service context that stores the proxied service:

```java
public <T> void setService(
        Class<T> serviceType,
        T service,
        Advice advice) {

    ProxyFactory proxyFactory =
            new ProxyFactory(service);

    proxyFactory.addAdvice(advice);

    T proxy = serviceType.cast(
            proxyFactory.getProxy()
    );

    services.put(serviceType, proxy);
}
```

The important detail is that the context stores the **proxy**, rather than simply storing the original service implementation.

```text
Service Implementation
        │
        ▼
    ProxyFactory
        │
        ▼
     AOP Proxy
        │
        ▼
  AopServiceContext
        │
        ▼
     Application
```

---

## Retrieving the Proxied Service

The service can then be retrieved through the service context:

```java
GreetingService service =
        serviceContext.getService(
                GreetingService.class
        );
```

The caller does not need to know that the returned object is an AOP proxy.

It simply uses the service interface:

```java
service.greet();
```

---

## Complete Example

The application demonstrates the complete flow:

```java
GreetingService greetingService =
        new GreetingServiceImpl();

LoggingAdvice loggingAdvice =
        new LoggingAdvice();

AopServiceContext serviceContext =
        new AopServiceContext();

serviceContext.setService(
        GreetingService.class,
        greetingService,
        loggingAdvice
);

GreetingService proxiedService =
        serviceContext.getService(
                GreetingService.class
        );

System.out.println(
        proxiedService.greet()
);
```

Expected output:

```text
Calling service method: greet
Completed service method: greet
Hello from an OpenMRS AOP service!
```

---

## Authorization Example

The same service can be protected with authorization advice:

```java
AuthorizationAdvice authorizationAdvice =
        new AuthorizationAdvice(true);

serviceContext.setService(
        GreetingService.class,
        greetingService,
        authorizationAdvice
);

GreetingService service =
        serviceContext.getService(
                GreetingService.class
        );

System.out.println(service.greet());
```

When authorization is enabled, the service executes normally.

If authorization is disabled:

```java
authorizationAdvice.setAuthorized(false);

service.greet();
```

the proxy prevents the target method from executing and throws a `SecurityException`.

```text
Caller
  │
  ▼
AOP Proxy
  │
  ▼
AuthorizationAdvice
  │
  ▼
SecurityException
```

---

## Combining Multiple Advice

Multiple cross-cutting concerns can be applied to the same service.

For example:

```text
Caller
  │
  ▼
Authorization Advice
  │
  ▼
Logging Advice
  │
  ▼
Service Method
```

This allows the service implementation to remain simple:

```java
@Override
public String greet() {
    return "Hello from an OpenMRS AOP service!";
}
```

while the surrounding infrastructure handles additional behavior.

---

## Adding Advice to an Existing Proxy

The example also demonstrates adding additional advice after the service has already been proxied.

```java
serviceContext.addAdvice(
        GreetingService.class,
        authorizationAdvice
);
```

The service proxy can therefore evolve to contain multiple pieces of advice:

```text
AOP Proxy
   │
   ├── LoggingAdvice
   │
   ├── AuthorizationAdvice
   │
   └── Target Service
```

This illustrates how cross-cutting behavior can be composed around a service.

---

## Removing Advice

The example also supports removing advice:

```java
serviceContext.removeAdvice(
        GreetingService.class
);
```

After the advice is removed, the service can execute without that interceptor.

This demonstrates that the proxy is not simply a static wrapper; its advice configuration can be changed.

---

## Relationship to OpenMRS

OpenMRS uses Spring AOP concepts around its service infrastructure.

The real OpenMRS `ServiceContext` supports adding and removing `Advice` and `Advisor` instances and uses Spring's `ProxyFactory` to create proxies around services when necessary.

Conceptually:

```text
OpenMRS Service
       │
       ▼
ServiceContext
       │
       ▼
Spring ProxyFactory
       │
       ▼
AOP Proxy
       │
       ├── Authorization
       ├── Logging
       ├── Transactions
       └── Other Advice
       │
       ▼
Target Service
```

This example simplifies that architecture so that the relationship between OpenMRS services and Spring AOP is easier to understand.

---

## AOP and the Previous Spring Modules

This example connects directly to concepts covered earlier in the project.

### Module 7 — Spring AOP

The project previously covered:

- AOP concepts
- JDK dynamic proxies
- CGLIB proxies
- Aspects
- Advice
- Pointcuts
- Around advice
- Advice ordering

This example applies those concepts to an OpenMRS-style service layer.

### Module 13 — Spring Internals

The project also explored:

- Bean definitions
- Dependency injection
- Bean post processors
- AOP proxy creation
- `DefaultListableBeanFactory`

This example demonstrates how those mechanisms appear in a real-world Spring-based architecture.

### Module 14 — ServiceContext

The previous example introduced `ServiceContext`.

This example extends that concept:

```text
ServiceContext
      │
      ▼
AOP Proxy
      │
      ▼
Service
```

---

## Dependencies

The example requires:

- Spring AOP
- Spring Beans
- JUnit Jupiter

Spring AOP provides:

- `ProxyFactory`
- AOP proxy infrastructure
- `Advised`

```xml
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-aop</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-beans</artifactId>
        </dependency>

        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <scope>test</scope>
        </dependency>
```

Spring Beans provides the underlying Spring bean infrastructure and AOP Alliance integration.

---

## Running the Example

From the project root:

```bash
mvn -pl 14-openmrs-examples/aop-in-openmrs clean install
```

Run `AopInOpenmrsApplication` from your IDE or using your preferred Java execution method.

Expected output:

```text
Calling service method: greet
Completed service method: greet
Hello from an OpenMRS AOP service!
```

---

## Key Takeaways

- OpenMRS uses Spring AOP concepts around its service infrastructure.
- AOP allows cross-cutting concerns to remain separate from business logic.
- `MethodInterceptor` can implement behavior around service method invocations.
- `ProxyFactory` can create a proxy around a service.
- `ServiceContext` can manage proxied services and their advice.
- Authorization and logging are examples of cross-cutting concerns.
- Multiple pieces of advice can be applied to the same service.
- The application interacts with the service interface without needing to know about the proxy.
- The simplified implementation demonstrates the relationship between OpenMRS services, `ServiceContext`, and Spring AOP.

---

## Next

The next example will explore: _Transaction Management_

This example will demonstrate how transaction management is applied to OpenMRS services and how Spring's transaction infrastructure can surround service operations with transactional behavior.

---

#### [Back To Top ⬆️](#aop-in-openmrs)