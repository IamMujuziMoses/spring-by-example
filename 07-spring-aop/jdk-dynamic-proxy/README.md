# JDK Dynamic Proxies

JDK Dynamic Proxies are a Java mechanism for creating proxy objects at runtime that implement one or more interfaces.

This example demonstrates how a JDK dynamic proxy can intercept method calls and delegate them to a target object through an `InvocationHandler`.

It provides a foundation for understanding proxy-based behavior before introducing Spring AOP.

---

## Learning Objectives

By the end of this example, you will understand:

- What a JDK dynamic proxy is.
- Why JDK dynamic proxies are used.
- How `Proxy.newProxyInstance()` creates a proxy.
- What `InvocationHandler` does.
- How method calls are intercepted by a proxy.
- How a proxy delegates calls to a target object.
- Why JDK dynamic proxies are interface-based.
- How additional behavior can be added before and after a method invocation.
- How JDK dynamic proxies relate to Spring AOP.
- Why understanding proxies is important when learning Spring AOP.

---

# What Is a JDK Dynamic Proxy?

A **JDK Dynamic Proxy** is a runtime-generated object that implements one or more interfaces.

Instead of calling a target object directly:

```text
Caller
   ↓
Target Object
```

a proxy can be placed between the caller and the target:

```text
Caller
   ↓
JDK Dynamic Proxy
   ↓
InvocationHandler
   ↓
Target Object
```

The proxy can therefore intercept method calls and perform additional behavior before delegating to the target object.

---

# Why Do JDK Dynamic Proxies Exist?

A proxy can be useful when we want to add behavior around an existing object without modifying the object's implementation.

For example, we may want to:

- Log method calls.
- Measure execution time.
- Perform security checks.
- Validate method arguments.
- Add monitoring.
- Control access to an object.

Instead of adding this behavior directly to the target class:

```java
public class OrderService {

    public void createOrder(Long orderId) {

        System.out.println("Before method");

        // Business logic

        System.out.println("After method");
    }
}
```

we can place the behavior in a proxy:

```text
Caller
   ↓
Proxy
   ↓
Additional behavior
   ↓
Target
```

This is one of the fundamental ideas behind proxy-based AOP.

---

# JDK Dynamic Proxy Architecture

The example consists of three main components:

```text
             OrderService
                  ↑
                  │ implements
                  │
          OrderServiceImpl
             (Target)
                  ↑
                  │ invoked by
                  │
    LoggingInvocationHandler
                  ↑
                  │ handles calls from
                  │
          JDK Dynamic Proxy
```

A method call flows through the proxy before reaching the target object:

```text
orderService.createOrder(1001L)
              ↓
        JDK Dynamic Proxy
              ↓
    LoggingInvocationHandler
              ↓
      OrderServiceImpl
              ↓
       createOrder()
```

---

# The Interface

JDK dynamic proxies are **interface-based**.

The example starts with an `OrderService` interface.

## `OrderService`

```java

public interface OrderService {

    void createOrder(Long orderId);
}
```

The interface defines the contract that both the target and proxy will use.

---

# The Target Object

The target object contains the actual business logic.

## `OrderServiceImpl`

```java

public class OrderServiceImpl implements OrderService {

    @Override
    public void createOrder(Long orderId) {
        System.out.println("Creating order: " + orderId);
    }
}
```

This class does not know anything about the proxy.

It simply implements the `OrderService` interface.

---

# The InvocationHandler

The `InvocationHandler` is responsible for handling method calls made through the proxy.

## `LoggingInvocationHandler`

```java
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LoggingInvocationHandler implements InvocationHandler {

    private final Object target;

    public LoggingInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("Before method: " + method.getName());

        Object result = method.invoke(target, args);

        System.out.println("After method: " + method.getName());

        return result;
    }
}
```

The important method is:

```java
invoke(...)
```

Whenever a method is called through the proxy, the invocation handler receives the call.

The handler can then:

1. Execute additional behavior.
2. Invoke the target method.
3. Execute additional behavior afterward.
4. Return the target method's result.

The flow is:

```text
Method call
     ↓
InvocationHandler.invoke()
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

# Creating the Proxy

Java provides the `Proxy.newProxyInstance()` method for creating JDK dynamic proxies.

## `Main`

```java
import java.lang.reflect.Proxy;

public class Main {

    public static void main(String[] args) {

        var target = new OrderServiceImpl();

        var handler = new LoggingInvocationHandler(target);

        OrderService orderService = (OrderService) Proxy.newProxyInstance(OrderService.class.getClassLoader(),
                new Class<?>[]{OrderService.class}, handler);

        orderService.createOrder(1001L);
    }
}
```

The proxy is created with:

```java

Proxy.newProxyInstance(OrderService.class.getClassLoader(),
        new Class<?>[]{OrderService.class},handler);

```

The three arguments are:

### Class Loader

```java
OrderService.class.getClassLoader()
```

The class loader is used to define the generated proxy class.

### Interfaces

```java
new Class<?>[]{OrderService.class}
```

This tells Java which interfaces the generated proxy should implement.

This is why JDK dynamic proxies require an interface.

### Invocation Handler

```java
handler
```

The invocation handler receives method calls made through the proxy.

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

Notice that `OrderServiceImpl` only contains:

```java
System.out.println("Creating order: " + orderId);
```

The logging behavior comes from the proxy.

---

# Understanding the Method Call

When this code executes:

```java
orderService.createOrder(1001L);
```

the call does not go directly to `OrderServiceImpl`.

Instead:

```text
orderService
     ↓
JDK Dynamic Proxy
     ↓
LoggingInvocationHandler.invoke()
     ↓
"Before method: createOrder"
     ↓
OrderServiceImpl.createOrder()
     ↓
"Creating order: 1001"
     ↓
"After method: createOrder"
```

The proxy therefore provides a layer around the target object.

---

# Why Does the Variable Use `OrderService`?

The proxy is assigned to:

```java
OrderService orderService
```

rather than:

```java
OrderServiceImpl orderService
```

because the JDK dynamic proxy implements the interface.

The target object is:

```text
OrderServiceImpl
```

while the proxy is:

```text
JDK Proxy implementing OrderService
```

Therefore:

```java
OrderService orderService = proxy;
```

works.

But a JDK dynamic proxy cannot simply be treated as:

```java
OrderServiceImpl orderService = proxy;
```

because the generated proxy is not a subclass of `OrderServiceImpl`.

---

# JDK Dynamic Proxies Are Interface-Based

This is one of the most important concepts in this example.

The target implements:

```text
OrderService
     ↑
     │
OrderServiceImpl
```

and the proxy also implements:

```text
OrderService
     ↑
     │
JDK Dynamic Proxy
```

Conceptually:

```text
             OrderService
                  ↑
          ┌───────┴───────┐
          │               │
 OrderServiceImpl     JDK Proxy
    (target)           (proxy)
```

The proxy and target share the same interface.

This allows the caller to interact with either one through the same contract.

---

# What Happens Inside `invoke()`?

Consider:

```java
@Override
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

    System.out.println("Before method: " + method.getName());

    Object result = method.invoke(target, args);

    System.out.println("After method: " + method.getName());

    return result;
}
```

The first statement:

```java
System.out.println("Before method: " + method.getName());
```

runs before the target method.

Then:

```java
method.invoke(target, args);
```

calls the actual target method.

Finally:

```java
System.out.println("After method: " + method.getName());
```

runs after the target method completes.

This gives us a simple interception mechanism.

---

# Proxying Without Modifying the Target

One useful property of proxies is that the target class does not need to know that it is being proxied.

The target remains:

```java
public class OrderServiceImpl implements OrderService {

    @Override
    public void createOrder(Long orderId) {
        System.out.println("Creating order: " + orderId);
    }
}
```

The logging behavior exists separately:

```java
public class LoggingInvocationHandler
        implements InvocationHandler {

    // Proxy behavior
}
```

This provides a basic separation between:

```text
Business logic
      +
Additional behavior
```

---

# Why Doesn't This Example Use Spring?

This example intentionally does **not** use:

```java
@Configuration
```

or:

```java
AnnotationConfigApplicationContext
```

or:

```java
ApplicationContext
```

The purpose is to understand the underlying Java mechanism first.

The proxy is created directly with:

```java
Proxy.newProxyInstance(...)
```

This keeps the example focused on JDK dynamic proxies rather than Spring configuration.

The progression is:

```text
Java JDK Dynamic Proxy
        ↓
Understand proxying
        ↓
Understand method interception
        ↓
Understand InvocationHandler
        ↓
Spring AOP
```

Later, Spring will manage much of this proxy creation and interception for us.

---

# JDK Dynamic Proxy vs Direct Method Invocation

Without a proxy:

```text
Caller
  ↓
OrderServiceImpl
  ↓
createOrder()
```

With a JDK dynamic proxy:

```text
Caller
  ↓
JDK Proxy
  ↓
InvocationHandler
  ↓
OrderServiceImpl
  ↓
createOrder()
```

The additional layer gives us a place to apply cross-cutting behavior.

---

# JDK Dynamic Proxies and AOP

The proxy mechanism demonstrated here is closely related to the concepts introduced in the previous AOP example.

AOP allows us to separate cross-cutting concerns such as logging.

A proxy provides a mechanism for intercepting method calls.

Together:

```text
Cross-cutting concern
        ↓
      Advice
        ↓
      Proxy
        ↓
   Target method
```

The `LoggingInvocationHandler` in this example is a simple demonstration of this idea.

It is not yet a Spring AOP aspect.

---

# JDK Dynamic Proxies and Spring AOP

Spring AOP uses proxies to apply advice to Spring-managed beans.

At a high level:

```text
Spring-managed bean
        ↓
Spring creates proxy
        ↓
Method invocation
        ↓
Interceptors / advice
        ↓
Target method
```

Depending on the situation, Spring can use different proxying mechanisms.

One of those mechanisms is a JDK dynamic proxy.

This is why understanding the Java proxy mechanism helps when learning Spring AOP.

---

# Important Limitation

JDK dynamic proxies require interfaces.

For example:

```java
public interface OrderService {

    void createOrder(Long orderId);
}
```

The target implements the interface:

```java
public class OrderServiceImpl implements OrderService {
    // ...
}
```

The proxy implements the same interface.

The relationship is:

```text
OrderService
     ↑
     ├── OrderServiceImpl
     │      (target)
     │
     └── JDK Dynamic Proxy
            (proxy)
```

If there is no interface, a JDK dynamic proxy cannot simply proxy the concrete class.

This limitation leads to another important proxy mechanism:

**CGLIB proxies.**

---

# JDK Dynamic Proxy vs CGLIB

The two approaches can be summarized at a high level:

| Feature | JDK Dynamic Proxy | CGLIB |
|---|---|---|
| Based on | Interfaces | Classes |
| Requires interface | Yes | No |
| Proxy type | Implements interface | Subclasses target |
| Created at runtime | Yes | Yes |
| Used by Spring AOP | Yes | Yes |

The important distinction is:

```text
JDK Dynamic Proxy
        ↓
Interface-based
```

while:

```text
CGLIB
        ↓
Class-based
```

The next example will explore CGLIB proxies in more detail.

---

# Key Takeaways

- A JDK dynamic proxy is a runtime-generated object that implements one or more interfaces.
- JDK dynamic proxies are created using `Proxy.newProxyInstance()`.
- `InvocationHandler` receives method calls made through the proxy.
- The handler can execute additional behavior before and after the target method.
- The target object does not need to know that a proxy exists.
- JDK dynamic proxies are interface-based.
- The proxy and target can share the same interface.
- JDK dynamic proxies are one mechanism that can be used for method interception.
- Spring AOP uses proxy-based interception.
- This example does not require Spring configuration because it demonstrates the underlying Java mechanism directly.
- Understanding JDK dynamic proxies provides a foundation for understanding Spring AOP.
- JDK dynamic proxies cannot directly proxy a concrete class without an interface.
- CGLIB provides a class-based proxying mechanism.

---

# What's Next?

The next example introduces:

**CGLIB Proxies**

JDK dynamic proxies are interface-based:

```text
Interface
    ↑
    ├── Target
    └── JDK Proxy
```

CGLIB takes a different approach:

```text
Target Class
     ↑
     │ extends
     │
CGLIB Proxy
```

Understanding both mechanisms will make it easier to understand how Spring chooses and creates proxies when we begin creating actual Spring AOP aspects.