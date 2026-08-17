# CGLIB Proxies

CGLIB proxies provide a class-based mechanism for creating proxy objects at runtime.

This example demonstrates how Spring's repackaged CGLIB classes can create a proxy by subclassing a target class and intercepting method calls through a `MethodInterceptor`.

It builds on the previous JDK Dynamic Proxy example and introduces the class-based proxying approach used by Spring AOP when a JDK dynamic proxy is not being used.

---

## Learning Objectives

By the end of this example, you will understand:

- What a CGLIB proxy is.
- Why CGLIB proxies are used.
- How CGLIB creates proxies by subclassing a target class.
- What `Enhancer` does.
- What `MethodInterceptor` does.
- How `MethodProxy` invokes the original method.
- How CGLIB differs from JDK dynamic proxies.
- Why CGLIB does not require an interface.
- How method calls are intercepted by a CGLIB proxy.
- How CGLIB proxies relate to Spring AOP.
- Why understanding class-based proxies is important when learning Spring AOP.

---

# What Is a CGLIB Proxy?

A CGLIB proxy is a runtime-generated subclass of a target class.

Unlike JDK dynamic proxies, which implement interfaces, CGLIB creates a new class that extends the target class.

Without a proxy:

```text
Caller
   ↓
OrderService
```

With a CGLIB proxy:

```text
Caller
   ↓
CGLIB Proxy
   ↓
OrderService
```

The generated proxy can intercept method calls and execute additional behavior before delegating to the original implementation.

---

# Why Do CGLIB Proxies Exist?

A proxy can be useful when additional behavior needs to be applied around an existing object without modifying the target class.

For example, we may want to:

- Log method calls.
- Measure execution time.
- Perform security checks.
- Validate method arguments.
- Add monitoring.
- Control access to an object.

A proxy provides an additional layer between the caller and the target:

```text
Caller
   ↓
Proxy
   ↓
Additional behavior
   ↓
Target method
```

This is one of the fundamental ideas behind proxy-based AOP.

---

# CGLIB Proxy Architecture

The example consists of two main components:

```text
             OrderService
                  ↑
                  │ extends
                  │
             CGLIB Proxy
                  ↑
                  │
               Caller
```

The method call flows through the generated subclass:

```text
orderService.createOrder(1001L)
              ↓
         CGLIB Proxy
              ↓
   MethodInterceptor.intercept()
              ↓
      OrderService.createOrder()
```

---

# The Target Class

Unlike the JDK Dynamic Proxy example, the target does not need to implement an interface.

## `OrderService`

```java
public class OrderService {

    public void createOrder(Long orderId) {
        System.out.println("Creating order: " + orderId);
    }
}
```

The class contains the actual business logic.

There is no interface:

```text
OrderService
```

is simply a concrete class.

This is one of the main differences from the JDK Dynamic Proxy example.

---

# The MethodInterceptor

CGLIB uses `MethodInterceptor` to intercept method calls.

## `LoggingMethodInterceptor`

```java
public class LoggingMethodInterceptor implements MethodInterceptor {

    private boolean invoked;

    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        invoked = true;

        System.out.println("Before method: " + method.getName());

        Object result = methodProxy.invokeSuper(object, args);

        System.out.println("After method: " + method.getName());

        return result;
    }

    public boolean wasInvoked() {
        return invoked;
    }
}
```

The important method is:

```java
intercept(...)
```

Whenever a method is invoked through the CGLIB proxy, the interceptor receives the call.

The interceptor can then:

1. Execute behavior before the target method.
2. Invoke the original method.
3. Execute behavior after the target method.
4. Return the result.

The flow is:

```text
Method call
     ↓
MethodInterceptor.intercept()
     ↓
Before behavior
     ↓
Target method
     ↓
After behavior
     ↓
Return result
```

---

# What Is `MethodProxy`?

The `MethodProxy` represents the method being intercepted and provides a way to invoke the original implementation.

In the example:

```java

methodProxy.invokeSuper(object, args);

```

invokes the original method implementation on the superclass.

Conceptually:

```text
CGLIB Proxy
     ↓
intercept()
     ↓
invokeSuper()
     ↓
OrderService.createOrder()
```

This is different from the JDK dynamic proxy approach, where an `InvocationHandler` invokes a method using reflection:

```java

method.invoke(target, args);

```

---

# Creating the Proxy

CGLIB provides the `Enhancer` class for creating subclass-based proxies.

## `Main`

```java

public class Main {

    public static void main(String[] args) {

        var enhancer = new Enhancer();

        enhancer.setSuperclass(OrderService.class);
        enhancer.setCallback(new LoggingMethodInterceptor());

        OrderService orderService = (OrderService) enhancer.create();

        orderService.createOrder(1001L);
    }
}
```

The important configuration is:

```java

enhancer.setSuperclass(OrderService.class);

```

This tells CGLIB that the generated proxy should extend `OrderService`.

Then:

```java

enhancer.setCallback(new LoggingMethodInterceptor());

```

registers the interceptor that will handle method calls.

Finally:

```java

enhancer.create();

```

creates the generated proxy object.

---

# Understanding `Enhancer`

`Enhancer` is the CGLIB class responsible for creating the subclass-based proxy.

The basic configuration is:

```java
var enhancer = new Enhancer();

enhancer.setSuperclass(OrderService.class);
enhancer.setCallback(new LoggingMethodInterceptor());

OrderService orderService = (OrderService) enhancer.create();
```

The relationship can be visualized as:

```text
        OrderService
             ↑
             │ extends
             │
      Generated CGLIB Class
             ↑
             │
        orderService
```

The generated class is created at runtime.

---

# Running the Example

Run:

```bash
mvn clean install
```

Then run `Main`.

The expected output is:

```text
Before method: createOrder
Creating order: 1001
After method: createOrder
```

The target class itself only contains:

```java

System.out.println("Creating order: " + orderId);

```

The before-and-after logging comes from the proxy.

---

# Understanding the Method Call

When this code executes:

```java
orderService.createOrder(1001L);
```

the call is intercepted by the CGLIB proxy.

The flow is:

```text
orderService.createOrder(1001L)
              ↓
         CGLIB Proxy
              ↓
   LoggingMethodInterceptor
              ↓
        intercept()
              ↓
   "Before method: createOrder"
              ↓
 methodProxy.invokeSuper(...)
              ↓
 OrderService.createOrder()
              ↓
   "Creating order: 1001"
              ↓
   "After method: createOrder"
```

The proxy therefore provides a layer around the target class.

---

# Why Doesn't CGLIB Require an Interface?

JDK dynamic proxies work by implementing interfaces.

For example:

```text
        OrderService
             ↑
       ┌─────┴─────┐
       │           │
     Target       Proxy
```

CGLIB takes a different approach.

It creates a subclass of the target class:

```text
       OrderService
            ↑
            │ extends
            │
       CGLIB Proxy
```

Because the proxy inherits from the target class, an interface is not required.

This means the following is possible:

```java
public class OrderService {

    public void createOrder(Long orderId) {
        // ...
    }
}
```

There is no need for:

```java
public interface OrderService {
    // ...
}
```

---

# CGLIB Proxy vs Direct Object

Without a proxy:

```text
Caller
  ↓
OrderService
  ↓
createOrder()
```

With a CGLIB proxy:

```text
Caller
  ↓
CGLIB Proxy
  ↓
MethodInterceptor
  ↓
OrderService
  ↓
createOrder()
```

The additional layer allows cross-cutting behavior to be applied without modifying the target implementation.

---

# Why Track `wasInvoked()`?

The `wasInvoked` field exists only to make the interception behavior observable in the test.

Without it, a test such as:

```java
orderService.createOrder(1001L);
```

would only demonstrate that the method executes successfully.

It would not directly prove that the `MethodInterceptor` handled the invocation.

The test therefore verifies the important behavior:

```text
Method call
     ↓
CGLIB proxy
     ↓
MethodInterceptor
     ↓
Target method
```

---

# Why Doesn't This Example Use Spring?

Like the JDK Dynamic Proxy example, this example intentionally creates the proxy directly.

It does not require:

```java
@Configuration
```

or:

```java
ApplicationContext
```

or:

```java
AnnotationConfigApplicationContext
```

The purpose is to understand the underlying proxy mechanism before introducing Spring AOP.

The learning progression is:

```text
JDK Dynamic Proxy
        ↓
CGLIB Proxy
        ↓
Understand proxy-based interception
        ↓
Spring AOP
```

Spring can manage proxy creation for us later.

---

# Spring's Repackaged CGLIB

This example uses the CGLIB classes provided by Spring:

```java
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
```

The example therefore does not need to add a separate CGLIB dependency.

The relevant Spring dependency is:

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-core</artifactId>
    <version>${spring.version}</version>
</dependency>
```

`spring-context` normally brings `spring-core` transitively, so an existing Spring project may already have these classes available.

---

# CGLIB and Final Classes

Because CGLIB creates a subclass of the target class, the target class must be extendable.

For example:

```java
public final class OrderService {
}
```

cannot be subclassed.

Therefore, a CGLIB proxy cannot extend it.

Similarly, methods that cannot be overridden cannot be intercepted in the same way.

For example:

```java
public final void createOrder(Long orderId) {
    // ...
}
```

cannot be overridden by the generated subclass.

Conceptually:

```text
CGLIB
  ↓
Creates subclass
  ↓
Overrides methods
  ↓
Intercepts calls
```

If the class or method prevents overriding, this approach has limitations.

---

# JDK Dynamic Proxy vs CGLIB

The two mechanisms can be compared as follows:

| Feature | JDK Dynamic Proxy | CGLIB |
|---|---|---|
| Proxy mechanism | Implements interfaces | Extends target class |
| Requires interface | Yes | No |
| Based on | Interfaces | Classes |
| Target class must be extendable | Not applicable | Yes |
| Can proxy concrete classes | Not directly | Yes |
| Uses | `InvocationHandler` | `MethodInterceptor` |
| Creates proxy at runtime | Yes | Yes |
| Used by Spring AOP | Yes | Yes |

The most important distinction is:

```text
JDK Dynamic Proxy
        ↓
Interface-based
```

versus:

```text
CGLIB
        ↓
Class-based
```

---

# JDK Dynamic Proxy Call Flow

The JDK approach uses an invocation handler:

```text
Caller
  ↓
JDK Dynamic Proxy
  ↓
InvocationHandler.invoke()
  ↓
Target method
```

The target and proxy share an interface:

```text
        OrderService
             ↑
       ┌─────┴─────┐
       │           │
     Target       Proxy
```

---

# CGLIB Call Flow

CGLIB uses a method interceptor:

```text
Caller
  ↓
CGLIB Proxy
  ↓
MethodInterceptor.intercept()
  ↓
MethodProxy.invokeSuper()
  ↓
Target method
```

The proxy extends the target:

```text
       OrderService
            ↑
            │
       CGLIB Proxy
```

---

# CGLIB and Spring AOP

Spring AOP uses proxies to apply advice to Spring-managed beans.

At a high level:

```text
Spring-managed bean
        ↓
Spring creates proxy
        ↓
Method invocation
        ↓
Advice / interceptors
        ↓
Target method
```

Spring can use JDK dynamic proxies or class-based proxies depending on the configuration and target.

Understanding CGLIB therefore helps explain how Spring can apply AOP behavior to concrete classes that do not implement interfaces.

---

# Why Is Proxying Important for AOP?

Aspect-Oriented Programming is concerned with separating cross-cutting concerns from business logic.

For example:

```text
Business logic
    ↓
OrderService.createOrder()
```

could be surrounded with:

```text
Logging
Security
Transactions
Performance monitoring
```

A proxy provides a place to intercept the method call:

```text
Caller
   ↓
Proxy
   ↓
Cross-cutting behavior
   ↓
Target method
```

This is one of the core mechanisms behind proxy-based AOP.

---

# Important Limitations

CGLIB proxies are powerful, but they have limitations.

## Final Classes

A final class cannot be subclassed:

```java
public final class OrderService {
}
```

Therefore, CGLIB cannot create a subclass proxy for it.

## Final Methods

A final method cannot be overridden:

```java
public final void createOrder(Long orderId) {
}
```

Therefore, it cannot be intercepted through the normal subclass-based mechanism.

## Constructors

The generated proxy is a subclass of the target class, so constructor behavior also needs to be considered when working with class-based proxies.

These limitations are important when understanding why proxying behavior can differ between classes and methods.

---

# Key Takeaways

- A CGLIB proxy is a runtime-generated subclass of a target class.
- CGLIB does not require the target class to implement an interface.
- `Enhancer` is used to create the proxy.
- `MethodInterceptor` intercepts method calls.
- `MethodProxy.invokeSuper()` invokes the original superclass implementation.
- CGLIB provides class-based proxying.
- The generated proxy extends the target class.
- The target class must generally be extendable for CGLIB proxying.
- Final classes cannot be subclassed.
- Final methods cannot be overridden and therefore cannot be intercepted in the same way.
- Spring provides repackaged CGLIB classes under `org.springframework.cglib`.
- This example does not require Spring application configuration.
- CGLIB is one of the proxying mechanisms relevant to Spring AOP.
- Understanding CGLIB makes Spring's proxy-based AOP behavior easier to understand.

---

# What's Next?

The next example introduces:

**Creating an Aspect**

---