# Before Advice

Spring AOP provides a way to separate cross-cutting concerns from application business logic.

This example demonstrates how to use **Before Advice** to execute behavior before a target method runs.

It builds on the previous **Creating an Aspect** example by focusing specifically on the `@Before` advice annotation and how Spring applies it through a proxy.

---

## Learning Objectives

By the end of this example, you will understand:

- What Before Advice is.
- How to define Before Advice using `@Before`.
- How a pointcut determines which methods the advice applies to.
- How Spring executes advice before a target method.
- How Before Advice can implement cross-cutting concerns.
- How Spring AOP applies advice through proxies.
- Why the target class does not need to know about the advice.

---

# What Is Before Advice?

**Before Advice** is AOP behavior that executes before a matched method is invoked.

For example:

```java
@Before("execution(* com.springbyexample.aop.before.GreetingService.greet(..))")
public void beforeAdvice() {
    System.out.println("Before greeting");
}
```

When the target method is called:

```java
greetingService.greet("Spring");
```

Spring executes the advice first:

```text
greetingService.greet("Spring")
          ↓
     Before Advice
          ↓
    Target method
```

---

# Why Use Before Advice?

Before Advice is useful when behavior needs to happen before a method executes without modifying the target method itself.

Common examples include:

- Logging.
- Authorization checks.
- Auditing.
- Validation.
- Performance monitoring.
- Security checks.

Instead of adding the behavior directly to the target method:

```java
public String greet(String name) {

    System.out.println("Before greeting");

    return "Hello, " + name + "!";
}
```

the concern can be separated into an aspect:

```text
GreetingService
      ↓
Business logic

LoggingAspect
      ↓
Before Advice
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
Before Advice
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

# Creating Before Advice

The aspect uses the `@Before` annotation to define advice that executes before the target method.

## `LoggingAspect`

```java
@Aspect
public class LoggingAspect {

    @Before("execution(* com.springbyexample.aop.before.GreetingService.greet(..))")
    public void beforeAdvice() {
        System.out.println("Before greeting");
    }
}
```

The important part is:

```java
@Before(...)
```

This tells Spring that the annotated method should execute before a method matching the specified pointcut.

---

# Understanding `@Before`

The:

```java
@Before
```

annotation defines advice that runs before the matched method executes.

For example:

```java
@Before("execution(* com.springbyexample.aop.before.GreetingService.greet(..))")
public void beforeAdvice() {
    System.out.println("Before greeting");
}
```

The execution order is:

```text
1. greet() is called
       ↓
2. Before Advice executes
       ↓
3. greet() executes
       ↓
4. greet() returns its result
```

The advice does not replace the target method.

It executes before it.

---

# The Pointcut Expression

The example uses:

```java
execution(* com.springbyexample.aop.before.GreetingService.greet(..))
```

The pointcut identifies the method to which the advice should be applied.

The expression can be understood as:

```text
execution
   ↓
any return type
   ↓
com.springbyexample.aop.before.GreetingService
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
Before Advice
  ↓
GreetingService.greet()
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

Before the target method executes, Spring invokes the Before Advice.

---

### Expected Output

Running the example produces:

```text
Before greeting
Hello, Spring!
```

The first line comes from the aspect:

```java
System.out.println("Before greeting");
```

The second line is the result returned by:

```java
greetingService.greet("Spring");
```

This demonstrates that the advice executes before the target method.

---

# Understanding the Method Call

The application code appears to call:

```java
greetingService.greet("Spring");
```

But conceptually, the invocation goes through the Spring AOP proxy:

```text
greetingService.greet("Spring")
              ↓
        Spring AOP Proxy
              ↓
        Matching pointcut
              ↓
        Before Advice
              ↓
       "Before greeting"
              ↓
        Target method
              ↓
       "Hello, Spring!"
```

The target method itself does not contain any advice-related code.

---

# Before Advice vs Target Method

The target method:

```java
public String greet(String name) {
    return "Hello, " + name + "!";
}
```

only handles the greeting.

The aspect:

```java
@Before(...)
public void beforeAdvice() {
    System.out.println("Before greeting");
}
```

handles the cross-cutting behavior.

This separation allows the target class to remain focused on its own responsibility.

---

# Why Use an Aspect?

Without AOP, the target method could contain both business logic and cross-cutting behavior:

```java
public String greet(String name) {

    System.out.println("Before greeting");

    return "Hello, " + name + "!";
}
```

With AOP:

```text
GreetingService
      ↓
Greeting logic

LoggingAspect
      ↓
Before Advice
```

Spring combines them through its proxy:

```text
Caller
  ↓
Proxy
  ↓
Before Advice
  ↓
Target Method
```

This makes it possible to apply the same concern to multiple target methods without duplicating the behavior.

---

# Relationship to Creating an Aspect

The previous **Creating an Aspect** example introduced the basic structure of a Spring AOP aspect.

This example focuses specifically on:

```java
@Before
```

The relationship is:

```text
Creating an Aspect
        ↓
Introduces @Aspect
        ↓
Introduces advice
        ↓
Before Advice
        ↓
Focuses on @Before
```

The following examples can build on this foundation by introducing other advice types and more advanced pointcut expressions.

---

# Key Takeaways

- Before Advice executes before a matched target method.
- `@Before` is used to define Before Advice.
- A pointcut determines which methods the advice applies to.
- `@EnableAspectJAutoProxy` enables Spring's proxy-based AOP infrastructure.
- Spring applies the advice through an AOP proxy.
- The target class does not need to know about the aspect.
- Before Advice is useful for cross-cutting concerns such as logging, security, validation, and auditing.
- Spring-managed beans are required for Spring AOP to apply the advice automatically.
- `AopUtils.isAopProxy()` can be used to verify that a bean is proxied.
- Before Advice runs before the target method but does not replace it.

---

# What's Next?

**After Advice**

The next example will explore another Spring AOP advice type and demonstrate how its execution differs from Before Advice.