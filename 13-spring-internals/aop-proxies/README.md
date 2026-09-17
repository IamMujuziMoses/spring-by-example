# How AOP Proxies Are Created

This example demonstrates how Spring creates AOP proxies and how method calls through those proxies are intercepted before reaching the target object.

The example uses Spring's `ProxyFactory` to programmatically create an AOP proxy around a target service and adds a `MethodInterceptor` to demonstrate the invocation chain.

It also demonstrates the difference between JDK dynamic proxies and class-based proxies.

The goal is to understand the mechanism that connects a normal method call to Spring's AOP infrastructure.

## Learning Objectives

By completing this example, you will understand:

- What an AOP proxy is
- Why Spring uses proxies for AOP
- What a target object is
- What `ProxyFactory` does
- How advice is attached to a proxy
- How `MethodInterceptor` participates in method invocation
- How `MethodInvocation.proceed()` reaches the target object
- How JDK dynamic proxies work with interfaces
- How class-based proxies work
- How `AopUtils` can identify Spring AOP proxies
- How a proxy differs from its target object
- How AOP proxies connect method calls to interceptors
- How AOP proxies relate to `@Transactional`

---

## What Is an AOP Proxy?

An AOP proxy is an object that sits between the caller and the target object.

Instead of the caller invoking the target directly:

```text
Caller
   │
   ▼
Target
```

the call goes through a proxy:

```text
Caller
   │
   ▼
Proxy
   │
   ▼
Interceptor
   │
   ▼
Target
```

The proxy allows Spring to execute additional behavior around the target method.

Examples of cross-cutting behavior include:

- Logging
- Security checks
- Transaction management
- Performance measurement
- Caching
- Custom application behavior

---

## Why Does Spring Use Proxies?

Many Spring features need to execute behavior before or after a method call.

For example, `@Transactional` requires Spring to start a transaction before a method executes and commit or roll back the transaction afterward.

The proxy provides a boundary around the target method:

```text
Caller
  │
  ▼
Proxy
  │
  ▼
TransactionInterceptor
  │
  ├── begin transaction
  │
  ▼
Target method
  │
  ├── success → commit
  │
  └── exception → rollback
```

The caller can simply write:

```java
service.save();
```

without explicitly invoking the transaction interceptor.

---

## What Is the Target Object?

The target is the actual object containing the business logic.

In this example:

```java
GreetingService target =
        new GreetingServiceImpl();
```

The target is:

```text
GreetingServiceImpl
```

It contains the actual implementation:

```java
@Override
public String greet() {
    return "Hello from GreetingService!";
}
```

The target itself does not know anything about the interceptor.

The cross-cutting behavior is applied externally by the proxy and its interceptor chain.

---

## What Is `ProxyFactory`?

`ProxyFactory` is a Spring AOP utility that can create an AOP proxy around a target object.

The basic flow is:

```java
GreetingService target = new GreetingServiceImpl();

ProxyFactory proxyFactory = new ProxyFactory(target);

proxyFactory.addAdvice(new LoggingInterceptor());

GreetingService proxy = (GreetingService) proxyFactory.getProxy();
```

Conceptually:

```text
Target
  │
  ▼
ProxyFactory
  │
  ├── Target
  │
  └── Advice
  │
  ▼
AOP Proxy
```

The returned proxy is a different object from the target.

---

## Adding Advice

The example adds a `MethodInterceptor`:

```java
proxyFactory.addAdvice(new LoggingInterceptor());
```

The interceptor is:

```java
public class LoggingInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        System.out.println("Before method invocation");

        Object result = invocation.proceed();

        System.out.println("After method invocation");

        return result;
    }
}
```

The interceptor surrounds the target invocation:

```text
Before method invocation
        │
        ▼
invocation.proceed()
        │
        ▼
Target method
        │
        ▼
After method invocation
```

---

## What Is `MethodInterceptor`?

`MethodInterceptor` is an AOP Alliance interceptor interface used by Spring AOP.

It provides an `invoke()` method:

```java
Object invoke(MethodInvocation invocation) throws Throwable;
```

The interceptor receives a `MethodInvocation` representing the method call.

It can then:

1. Execute code before the method.
2. Call `invocation.proceed()`.
3. Execute code after the method.
4. Return the result.
5. Handle or propagate exceptions.

For example:

```java
@Override
public Object invoke(MethodInvocation invocation) throws Throwable {

    System.out.println("Before");

    Object result = invocation.proceed();

    System.out.println("After");

    return result;
}
```

---

## What Does `proceed()` Do?

`MethodInvocation.proceed()` continues the invocation chain.

In this example:

```java
Object result = invocation.proceed();
```

eventually results in the target method being called:

```java
GreetingServiceImpl.greet();
```

The simplified flow is:

```text
Proxy
  │
  ▼
LoggingInterceptor.invoke()
  │
  ▼
MethodInvocation.proceed()
  │
  ▼
GreetingServiceImpl.greet()
```

If multiple interceptors are configured, `proceed()` continues through the remaining invocation chain before eventually reaching the target.

---

## Creating the Proxy

The application first creates the target:

```java
GreetingService target = new GreetingServiceImpl();
```

Then creates a `ProxyFactory`:

```java
ProxyFactory proxyFactory = new ProxyFactory(target);
```

Then adds the interceptor:

```java
proxyFactory.addAdvice(new LoggingInterceptor());
```

Finally, it asks the factory to create the proxy:

```java
GreetingService proxy = (GreetingService) proxyFactory.getProxy();
```

At this point there are two different objects:

```text
target
  │
  └── GreetingServiceImpl

proxy
  │
  └── Spring-generated proxy
```

The proxy delegates to the target while applying the configured advice.

---

## Target vs Proxy

The target and proxy are separate objects.

This can be demonstrated with:

```java
assertNotSame(target, proxy);
```

The relationship is:

```text
                  ┌────────────────────┐
                  │       Proxy        │
Caller ──────────►│ LoggingInterceptor │
                  └─────────┬──────────┘
                            │
                            ▼
                  ┌────────────────────┐
                  │       Target       │
                  │ GreetingServiceImpl│
                  └────────────────────┘
```

The proxy is responsible for interception.

The target is responsible for the actual business operation.

---

## JDK Dynamic Proxies

The first example uses an interface:

```java
public interface GreetingService {

    String greet();
}
```

and an implementation:

```java
public class GreetingServiceImpl implements GreetingService {
    // ...
}
```

Because the target exposes an interface, Spring can create a JDK dynamic proxy.

The proxy implements the target interface:

```text
GreetingService
       ▲
       │
       │ implements
       │
JDK Dynamic Proxy
       │
       ▼
GreetingServiceImpl
```

Therefore this works:

```java
GreetingService proxy = (GreetingService) proxyFactory.getProxy();
```

The exact generated proxy class name is JVM-dependent.

For that reason, the tests should verify the proxy's behavior and type rather than asserting a specific generated class name.

---

## Class-Based Proxies

Spring can also create a proxy based on the target class rather than its interface.

This can be requested using:

```java
proxyFactory.setProxyTargetClass(true);
```

For example:

```java
GreetingServiceImpl target = new GreetingServiceImpl();
ProxyFactory proxyFactory = new ProxyFactory(target);

proxyFactory.setProxyTargetClass(true);
proxyFactory.addAdvice(new LoggingInterceptor());

GreetingServiceImpl proxy = (GreetingServiceImpl) proxyFactory.getProxy();
```

The important configuration is:

```java
proxyFactory.setProxyTargetClass(true);
```

This tells the factory to use class-based proxying instead of interface-based JDK proxying.

The resulting proxy is based on the target class.

Conceptually:

```text
GreetingServiceImpl
        │
        ▼
Class-based proxy
        │
        ▼
Target object
```

---

## JDK Proxy vs Class-Based Proxy

Spring AOP can use different proxying mechanisms.

### JDK Dynamic Proxy

```text
Target implements interface
          │
          ▼
    JDK Dynamic Proxy
          │
          ▼
        Target
```

The proxy implements the target's interfaces.

### Class-Based Proxy

```text
       Target class
           │
           ▼
    Class-based proxy
           │
           ▼
        Target
```

The proxy is generated as a subclass of the target class.

The example demonstrates both approaches so that the distinction is visible.

---

## Detecting an AOP Proxy

Spring provides `AopUtils` for checking whether an object is an AOP proxy.

```java
AopUtils.isAopProxy(proxy);
```

For the generated proxy:

```java
assertTrue(AopUtils.isAopProxy(proxy));
```

This returns `true`.

The original target is not an AOP proxy:

```java
assertFalse(AopUtils.isAopProxy(target));
```

The distinction is:

```text
target → ordinary object

proxy  → Spring AOP proxy
```

---

## Complete Example

### `GreetingService.java`

```java
public interface GreetingService {

    String greet();
}
```

### `GreetingServiceImpl.java`

```java
public class GreetingServiceImpl implements GreetingService {

    @Override
    public String greet() {
        return "Hello from GreetingService!";
    }
}
```

### `LoggingInterceptor.java`

```java
public class LoggingInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        System.out.println("Before method invocation");

        Object result = invocation.proceed();

        System.out.println("After method invocation");

        return result;
    }
}
```

### `AopProxyApplication.java`

```java
public class AopProxyApplication {

    public static void main(String[] args) {
        GreetingService target = new GreetingServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new LoggingInterceptor());

        GreetingService proxy = (GreetingService) proxyFactory.getProxy();

        System.out.println("Target type: " + target.getClass().getName());
        System.out.println("Proxy type: " + proxy.getClass().getName());
        System.out.println("Is AOP proxy: " + AopUtils.isAopProxy(proxy));
        System.out.println(proxy.greet());
    }
}
```

### Class-Based Proxy Example

The same `ProxyFactory` can be configured for class-based proxying:

```java
GreetingServiceImpl target = new GreetingServiceImpl();
ProxyFactory proxyFactory = new ProxyFactory(target);

proxyFactory.setProxyTargetClass(true);
proxyFactory.addAdvice(new LoggingInterceptor());

GreetingServiceImpl proxy = (GreetingServiceImpl) proxyFactory.getProxy();
```

The important configuration is:

```java
proxyFactory.setProxyTargetClass(true);
```

This requests class-based proxying instead of JDK interface-based proxying.

---

## The Complete AOP Invocation Flow

The complete flow demonstrated by this example is:

```text
Caller
  │
  │ proxy.greet()
  ▼
AOP Proxy
  │
  ▼
LoggingInterceptor.invoke()
  │
  │ before
  ▼
MethodInvocation.proceed()
  │
  ▼
Target Method
  │
  ▼
GreetingServiceImpl.greet()
  │
  ▼
return result
  │
  ▼
LoggingInterceptor
  │
  │ after
  ▼
Caller
```

This is the fundamental mechanism behind many Spring AOP features.

---

## How This Explains `@Transactional`

The previous example demonstrated `@Transactional` and `TransactionInterceptor`.

We saw the transaction infrastructure as:

```text
@Transactional
      │
      ▼
AnnotationTransactionAttributeSource
      │
      ▼
TransactionInterceptor
      │
      ▼
TransactionManager
```

This example now provides the missing proxy layer:

```text
Caller
  │
  ▼
AOP Proxy
  │
  ▼
TransactionInterceptor
  │
  ▼
Target method
```

Putting both examples together:

```text
@Transactional
      │
      ▼
Transaction metadata
      │
      ▼
TransactionInterceptor
      │
      ▼
AOP Proxy
      │
      ▼
Target method
```

The proxy is what allows Spring to intercept a method call and route it through the configured interceptor chain.

---

## Why Direct Calls Bypass the Proxy

Consider:

```java
GreetingService target = new GreetingServiceImpl();

target.greet();
```

This is a direct call to the target.

There is no proxy involved:

```text
Caller
  │
  ▼
Target
```

Therefore no AOP advice runs.

With the proxy:

```java
proxy.greet();
```

the call becomes:

```text
Caller
  │
  ▼
Proxy
  │
  ▼
Interceptor
  │
  ▼
Target
```

This is especially important for understanding Spring's `@Transactional` behavior.

A method call that never passes through the Spring proxy cannot be intercepted by that proxy.

---

## Why This Example Comes After `@Transactional`

The Module 13 progression is intentional.

### #5 How `@Transactional` Works

We learned:

```text
@Transactional
      │
      ▼
TransactionInterceptor
      │
      ▼
TransactionManager
```

### #6 How AOP Proxies Are Created

We now add:

```text
Caller
  │
  ▼
AOP Proxy
  │
  ▼
Interceptor
  │
  ▼
Target
```

Together:

```text
Caller
  │
  ▼
AOP Proxy
  │
  ▼
TransactionInterceptor
  │
  ▼
TransactionManager
  │
  ▼
Target method
```

This gives us a clearer understanding of how Spring's declarative features are connected internally.

---

## Why `ProxyFactory` Is Used Here

In a typical Spring application, developers don't normally create AOP proxies manually.

Instead, Spring's auto-proxy infrastructure detects applicable beans and creates proxies automatically.

This example uses `ProxyFactory` because it makes the mechanics visible.

We can see explicitly:

```java
ProxyFactory proxyFactory = new ProxyFactory(target);
```

then:

```java
proxyFactory.addAdvice(new LoggingInterceptor());
```

and finally:

```java
GreetingService proxy = (GreetingService) proxyFactory.getProxy();
```

There is no hidden configuration.

Every step is visible.

---

## What This Example Does Not Cover

This example intentionally does not cover:

- `@Aspect`
- `@EnableAspectJAutoProxy`
- `AnnotationAwareAspectJAutoProxyCreator`
- Automatic proxy creation
- Bean post-processor-driven proxy creation
- `@Transactional` auto-proxy configuration

Those mechanisms build upon the proxy infrastructure demonstrated here.

They will be explored as the Module 13 examples progress.

---

## Dependencies

This example requires:

- Spring AOP
- AOP Alliance
- JUnit Jupiter

```xml
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-aop</artifactId>
        </dependency>

        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <scope>test</scope>
        </dependency>
```

The module inherits the project's Java and Spring versions from the parent Maven configuration.

---

## Running the Example

From the module directory:

```bash
mvn clean test
```

To run the application:

```bash
mvn compile exec:java \
  -Dexec.mainClass=com.springbyexample.aopproxies.AopProxyApplication
```

Expected output will be similar to:

```text
Target type: com.springbyexample.aopproxies.GreetingServiceImpl
Proxy type: jdk.proxy...
Is AOP proxy: true
Before method invocation
After method invocation
Hello from GreetingService!
```

The exact proxy class name is generated at runtime and can vary between JVM executions.

---

## Key Takeaways

1. An AOP proxy sits between the caller and the target object.
2. `ProxyFactory` can programmatically create a Spring AOP proxy.
3. Advice can be attached to a proxy using `addAdvice()`.
4. `MethodInterceptor` can execute behavior around a method invocation.
5. `MethodInvocation.proceed()` continues the invocation chain toward the target.
6. The proxy and target are separate objects.
7. Spring can use JDK dynamic proxies when working with interfaces.
8. Spring can use class-based proxies when configured with `setProxyTargetClass(true)`.
9. `AopUtils.isAopProxy()` can identify Spring AOP proxies.
10. A direct call on the target bypasses the proxy and its interceptors.
11. `@Transactional` relies on this proxy-based interception model in typical Spring configuration.
12. `ProxyFactory` exposes the fundamental proxy mechanism that higher-level Spring AOP infrastructure automates.

---

## Next

**Next: How Bean Post Processors Work**

The next example moves from manually creating a proxy to understanding one of Spring's most important extension points: `BeanPostProcessor`.

This will help explain how Spring can inspect, modify, wrap, or replace beans during the bean lifecycle.