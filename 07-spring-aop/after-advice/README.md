# After Advice

Spring AOP provides a way to separate cross-cutting concerns from application business logic.

This example demonstrates how to use **After Advice** to execute behavior after a target method completes.

It builds on the previous **Before Advice** example by introducing the `@After` annotation and showing how Spring applies After Advice through a proxy.

---

## Learning Objectives

By the end of this example, you will understand:

- What After Advice is.
- How to define After Advice using `@After`.
- How a pointcut determines which methods the advice applies to.
- When `@After` advice executes.
- How Spring applies After Advice through an AOP proxy.
- How `@After` differs from `@AfterReturning`.
- Why the target class does not need to know about the aspect.

---

# What Is After Advice?

**After Advice** is AOP behavior that executes after a matched target method completes.

For example:

```java
@After("execution(* com.springbyexample.afteradvice.GreetingService.greet(..))")
public void afterAdvice() {
    System.out.println("After greeting");
}
```

When the target method is called:

```java
greetingService.greet("Spring");
```

Spring executes the advice after the target method completes:

```text
greetingService.greet("Spring")
          ↓
     Target method
          ↓
      @After Advice
```

`@After` is sometimes described as **finally-style advice** because it runs when the matched method completes, including when the method completes by throwing an exception.

This is different from `@AfterReturning`, which only applies when the method completes successfully.

---

# Why Use After Advice?

After Advice is useful when behavior needs to happen after a method completes without modifying the target method itself.

Common examples include:

- Logging.
- Auditing.
- Resource cleanup.
- Monitoring.
- Tracking method execution.
- Releasing resources.

Instead of adding the behavior directly to the target method:

```java
public String greet(String name) {

    String greeting = "Hello, " + name + "!";

    System.out.println("After greeting");

    return greeting;
}
```

the concern can be separated into an aspect:

```text
GreetingService
      ↓
Business logic

LoggingAspect
      ↓
After Advice
```

Spring then connects them through its AOP proxy.

---

# Example

The example consists of three main components:

```text
AppConfig
    ↓
Spring configuration

GreetingService
    ↓
Target bean

LoggingAspect
    ↓
After Advice
```

Spring connects these components through its AOP infrastructure.

---

# GreetingService

The target class contains the application logic.

## `GreetingService`

```java
public class GreetingService {

    public String greet(String name) {
        return "Hello, " + name + "!";
    }
}
```

The target method has no knowledge of the aspect:

```java
public String greet(String name) {
    return "Hello, " + name + "!";
}
```

The logging behavior is handled separately by the aspect.

---

# Creating After Advice

The aspect uses the `@After` annotation to define advice that executes after the target method completes.

## `LoggingAspect`

```java
@Aspect
public class LoggingAspect {

    @After("execution(* com.springbyexample.afteradvice.GreetingService.greet(..))")
    public void afterAdvice() {
        System.out.println("After greeting");
    }
}
```

The important part is:

```java
@After(...)
```

This tells Spring that the annotated method should execute after a method matching the specified pointcut completes.

---

# Understanding `@After`

The:

```java
@After
```

annotation defines advice that runs after the matched method completes.

For example:

```java
@After("execution(* com.springbyexample.afteradvice.GreetingService.greet(..))")
public void afterAdvice() {
    System.out.println("After greeting");
}
```

The conceptual execution order is:

```text
1. greet() is called
       ↓
2. Target method executes
       ↓
3. Target method completes
       ↓
4. @After advice executes
       ↓
5. Control returns to the caller
```

An important detail is that `@After` is not the same as `@AfterReturning`.

`@After` executes when the method completes, whether it completes normally or by throwing an exception.

`@AfterReturning` is specifically used when a method successfully returns.

---

# The Pointcut Expression

The example uses:

```java
execution(* com.springbyexample.afteradvice.GreetingService.greet(..))
```

The pointcut identifies the method to which the advice should be applied.

The expression can be understood as:

```text
execution
   ↓
any return type
   ↓
com.springbyexample.afteradvice.GreetingService
   ↓
greet
   ↓
any arguments
```

The `*` represents any return type.

The `..` represents any number of arguments.

Pointcuts will be explored in greater detail in the dedicated **Pointcuts** example later in the module.

---

# Configuration

Spring needs to enable its proxy-based AOP infrastructure.

## `AppConfig`

```java
@Configuration
@EnableAspectJAutoProxy
public class AppConfig {

    @Bean
    public GreetingService greetingService() {
        return new GreetingService();
    }

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
```

The important configuration is:

```java
@EnableAspectJAutoProxy
```

This enables Spring's annotation-driven AOP proxy support.

The configuration also registers:

```text
GreetingService
LoggingAspect
```

as Spring beans.

---

# `@EnableAspectJAutoProxy`

The:

```java
@EnableAspectJAutoProxy
```

annotation enables Spring's proxy-based AOP infrastructure.

Conceptually:

```text
@EnableAspectJAutoProxy
          ↓
Spring AOP enabled
          ↓
Spring discovers aspects
          ↓
Spring finds matching beans
          ↓
Spring creates proxies
          ↓
Advice can intercept method calls
```

Without this configuration, Spring will not automatically create the proxy required for the advice to execute.

---

# How Spring Applies the Advice

When the application context starts, Spring discovers the target bean and the aspect.

It then creates a proxy around the target:

```text
             Spring Context
                    ↓
             LoggingAspect
                    ↓
                Spring AOP
                    ↓
                  Proxy
                    ↓
            GreetingService
```

When the application calls:

```java
greetingService.greet("Spring");
```

the call goes through the proxy.

Conceptually:

```text
Caller
  ↓
Spring AOP Proxy
  ↓
GreetingService.greet()
  ↓
@After Advice
```

---

# Running the Example

## `Main`

```java
public class Main {

    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingService greetingService = context.getBean(GreetingService.class);

            System.out.println(greetingService.greet("Spring"));
        }
    }
}
```

The service is retrieved from the Spring application context:

```java
GreetingService greetingService = context.getBean(GreetingService.class);
```

The method is then invoked:

```java
greetingService.greet("Spring");
```

The target method completes, the `@After` advice executes, and then control returns to `Main`.

---

# Expected Output

Running the example produces:

```text
After greeting
Hello, Spring!
```

At first, this output may appear surprising because `@After` is expected to execute after the target method.

The reason is the way the return value is printed in `Main`:

```java
System.out.println(greetingService.greet("Spring"));
```

The expression inside `System.out.println()` must first complete before `println()` can print the returned value.

The execution flow is therefore:

```text
Main
  ↓
greet() is called
  ↓
GreetingService.greet() executes
  ↓
greet() returns "Hello, Spring!"
  ↓
@After advice executes
  ↓
Control returns to Main
  ↓
System.out.println() prints "Hello, Spring!"
```

Therefore, the actual console output is:

```text
After greeting
Hello, Spring!
```

The important distinction is that the `@After` advice executes after the target method completes, but before the caller receives control and prints the returned value.

---

# `@After` vs `@AfterReturning`

It is important not to confuse `@After` with `@AfterReturning`.

### `@After`

Runs when the matched method completes, regardless of whether it completes normally or by throwing an exception.

```java
@After("execution(* com.springbyexample.afteradvice.GreetingService.greet(..))")
public void afterAdvice() {
    System.out.println("After greeting");
}
```

Conceptually:

```text
Target method
      ↓
 completes normally
      ↓
@After

OR

Target method
      ↓
 throws exception
      ↓
@After
```

### `@AfterReturning`

Runs only when the matched method completes successfully.

```text
Target method
      ↓
successful return
      ↓
@AfterReturning
```

This distinction will be explored in more detail in the **After Returning Advice** example.

---

# Before Advice vs After Advice

The previous example demonstrated `@Before`.

The execution flow for Before Advice is:

```text
Caller
  ↓
@Before Advice
  ↓
Target Method
```

This example demonstrates `@After`:

```text
Caller
  ↓
Target Method
  ↓
@After Advice
```

The two advice types therefore execute at different points in the method invocation.

---

# Key Takeaways

- After Advice executes when a matched method completes.
- `@After` is used to define After Advice.
- A pointcut determines which methods the advice applies to.
- `@EnableAspectJAutoProxy` enables Spring's proxy-based AOP infrastructure.
- Spring applies the advice through an AOP proxy.
- The target class does not need to know about the aspect.
- `@After` is useful for cross-cutting concerns such as logging, auditing, monitoring, and cleanup.
- `@After` can execute when a method completes normally or throws an exception.
- `@After` should not be confused with `@AfterReturning`.
- `@AfterReturning` only executes after a successful return.
- The output order in this example is affected by `System.out.println(greetingService.greet("Spring"))`.
- `AopUtils.isAopProxy()` can be used to verify that Spring created an AOP proxy.

---

# What's Next?

The next example will introduce **After Returning Advice** and demonstrate how it differs from `@After` by executing only when the target method successfully returns.