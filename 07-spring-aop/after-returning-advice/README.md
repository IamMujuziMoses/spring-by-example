# After Returning Advice

Spring AOP provides a way to separate cross-cutting concerns from application business logic.

This example demonstrates how to use **After Returning Advice** to execute behavior after a target method successfully returns.

It builds on the previous **After Advice** example by introducing the `@AfterReturning` annotation and showing how the returned value can be accessed by the advice.

---

## Learning Objectives

By the end of this example, you will understand:

- What After Returning Advice is.
- How to define After Returning Advice using `@AfterReturning`.
- How a pointcut determines which methods the advice applies to.
- How to access a method's returned value from advice.
- When `@AfterReturning` advice executes.
- How `@AfterReturning` differs from `@After`.
- How Spring applies After Returning Advice through an AOP proxy.
- Why `@AfterReturning` does not execute when the target method throws an exception.

---

# What Is After Returning Advice?

**After Returning Advice** is AOP behavior that executes after a matched target method successfully returns.

For example:

```java
@AfterReturning(pointcut = "execution(* com.springbyexample.afterreturning.GreetingService.greet(..))",
        returning = "result")
public void afterReturningAdvice(String result) {
    System.out.println("Returned: " + result);
}
```

When the target method successfully returns:

```java
greetingService.greet("Spring");
```

Spring executes the advice:

```text
GreetingService.greet("Spring")
          ↓
Target method executes
          ↓
Returns "Hello, Spring!"
          ↓
@AfterReturning Advice
          ↓
Returned value available to advice
```

Unlike `@After`, `@AfterReturning` only executes when the target method completes successfully.

---

# Why Use After Returning Advice?

After Returning Advice is useful when behavior needs to happen after a method successfully returns.

Common examples include:

- Logging returned values.
- Auditing successful operations.
- Processing results.
- Updating metrics.
- Caching results.
- Triggering follow-up behavior.

Instead of adding the behavior directly to the target method:

```java
public String greet(String name) {

    String greeting = "Hello, " + name + "!";

    System.out.println("Returned: " + greeting);

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
After Returning Advice
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
After Returning Advice
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

# Creating After Returning Advice

The aspect uses the `@AfterReturning` annotation to define advice that executes after the target method successfully returns.

## `LoggingAspect`

```java
@Aspect
public class LoggingAspect {

    @AfterReturning(pointcut = "execution(* com.springbyexample.afterreturning.GreetingService.greet(..))",
            returning = "result")
    public void afterReturningAdvice(String result) {
        System.out.println("Returned: " + result);
    }
}
```

There are two important parts:

```java
@AfterReturning(...)
```

and:

```java
returning = "result"
```

The `@AfterReturning` annotation defines the advice.

The `returning` attribute tells Spring which advice parameter should receive the returned value.

---

# Understanding `returning`

Consider:

```java
@AfterReturning(pointcut = "execution(* com.springbyexample.afterreturning.GreetingService.greet(..))",
        returning = "result")
public void afterReturningAdvice(String result) {
    System.out.println("Returned: " + result);
}
```

The value:

```java
returning = "result"
```

matches the advice method parameter:

```java
String result
```

Spring passes the target method's returned value into that parameter.

The flow is:

```text
GreetingService.greet()
        ↓
Returns "Hello, Spring!"
        ↓
Spring captures return value
        ↓
result = "Hello, Spring!"
        ↓
afterReturningAdvice(result)
```

This allows the advice to inspect the returned value.

---

# The Pointcut Expression

The example uses:

```java
execution(* com.springbyexample.afterreturning.GreetingService.greet(..))
```

The pointcut identifies the method to which the advice should be applied.

The expression can be understood as:

```text
execution
   ↓
any return type
   ↓
com.springbyexample.afterreturning.GreetingService
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
Successful return
  ↓
@AfterReturning Advice
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

The target method successfully returns its value, after which the `@AfterReturning` advice executes.

---

# Expected Output

Running the example produces:

```text
Returned: Hello, Spring!
Hello, Spring!
```

At first, the output order may appear surprising.

The call in `Main` is:

```java
System.out.println(greetingService.greet("Spring"));
```

The expression passed to `System.out.println()` must first complete before `println()` can print the returned value.

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
@AfterReturning advice executes
  ↓
Advice receives "Hello, Spring!"
  ↓
Control returns to Main
  ↓
System.out.println() prints "Hello, Spring!"
```

Therefore, the output is:

```text
Returned: Hello, Spring!
Hello, Spring!
```

The important point is that `@AfterReturning` executes after the target method successfully returns, but before the caller receives control.

---

# `@After` vs `@AfterReturning`

It is important to distinguish these two advice types.

## `@After`

`@After` executes when the matched method completes.

It can execute after either a normal return or an exception.

```text
Target method
      ↓
Normal completion
      ↓
@After

OR

Target method
      ↓
Exception
      ↓
@After
```

For example:

```java
@After("execution(* com.springbyexample.afterreturning.GreetingService.greet(..))")
public void afterAdvice() {
    System.out.println("After greeting");
}
```

## `@AfterReturning`

`@AfterReturning` executes only when the matched method successfully returns.

```text
Target method
      ↓
Successful return
      ↓
@AfterReturning
```

It can also access the returned value:

```java
@AfterReturning(pointcut = "execution(* com.springbyexample.afterreturning.GreetingService.greet(..))",
        returning = "result")
public void afterReturningAdvice(String result) {
    System.out.println("Returned: " + result);
}
```

The key distinction is:

```text
@After
    ↓
Method completion
    ↓
Normal return OR exception

@AfterReturning
    ↓
Successful return only
    ↓
Returned value available
```

---

# What Happens When an Exception Occurs?

Suppose the target method throws an exception:

```java
public String greet(String name) {

    if (name == null) {
        throw new IllegalArgumentException("Name must not be null");
    }

    return "Hello, " + name + "!";
}
```

The `@AfterReturning` advice does not execute because the method did not successfully return.

The flow is:

```text
greet()
  ↓
Exception
  ↓
@AfterReturning does not execute
```

This behavior makes `@AfterReturning` appropriate when the additional behavior should only happen after successful operations.

For behavior that should execute regardless of whether the method succeeds or fails, `@After` is more appropriate.

---

# Why Does the Test Use Spring's Application Context?

Spring AOP is applied through Spring's proxy infrastructure.

Therefore, this would not demonstrate Spring AOP:

```java
GreetingService greetingService = new GreetingService();
```

The object would simply be a normal Java object.

Instead, the test creates:

```java
new AnnotationConfigApplicationContext(AppConfig.class);
```

This allows Spring to:

1. Create the target bean.
2. Create the aspect bean.
3. Process the aspect.
4. Create the AOP proxy.
5. Apply the advice to matching method calls.

---

# Before, After, and After Returning

The advice examples now demonstrate three different points in a method invocation.

## Before Advice

```text
Caller
  ↓
@Before Advice
  ↓
Target Method
```

## After Advice

```text
Caller
  ↓
Target Method
  ↓
@After Advice
```

## After Returning Advice

```text
Caller
  ↓
Target Method
  ↓
Successful return
  ↓
@AfterReturning Advice
```

The main difference between `@After` and `@AfterReturning` is that `@AfterReturning` only runs after a successful return and can receive the returned value.

---

# Key Takeaways

- After Returning Advice executes after a matched method successfully returns.
- `@AfterReturning` is used to define After Returning Advice.
- A pointcut determines which methods the advice applies to.
- The `returning` attribute allows the advice to receive the target method's returned value.
- `@AfterReturning` only executes after successful method completion.
- `@AfterReturning` does not execute when the target method throws an exception.
- `@After` can execute after both normal completion and exceptions.
- `@EnableAspectJAutoProxy` enables Spring's proxy-based AOP infrastructure.
- Spring applies the advice through an AOP proxy.
- The target class does not need to know about the aspect.
- `AopUtils.isAopProxy()` can be used to verify that Spring created an AOP proxy.

---

# What's Next?

The next example will introduce **Around Advice** and demonstrate how `@Around` can control behavior before and after a target method executes.