# Around Advice

Spring AOP provides a way to separate cross-cutting concerns from application business logic.

This example demonstrates how to use **Around Advice** to execute behavior before and after a target method and how `ProceedingJoinPoint` controls the invocation of the target method.

It builds on the previous **Before Advice**, **After Advice**, and **After Returning Advice** examples by introducing the most flexible type of Spring AOP advice.

---

## Learning Objectives

By the end of this example, you will understand:

- What Around Advice is.
- How to define Around Advice using `@Around`.
- What `ProceedingJoinPoint` represents.
- Why `proceed()` is important.
- How Around Advice can execute code before and after a target method.
- How Around Advice can access the method invocation.
- How Around Advice can inspect method arguments.
- How Around Advice can access and modify the returned value.
- How Around Advice can affect exception handling.
- What happens when `proceed()` is not called.
- Why Around Advice should be used carefully.
- How Spring applies Around Advice through an AOP proxy.

---

# What Is Around Advice?

**Around Advice** is advice that surrounds the execution of a target method.

Unlike `@Before`, `@After`, and `@AfterReturning`, Around Advice can control whether the target method executes at all.

For example:

```java
@Around("execution(* com.springbyexample.aroundadvice.GreetingService.greet(..))")
public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

    System.out.println("Before greeting");

    Object result = joinPoint.proceed();

    System.out.println("After greeting");

    return result;
}
```

The advice executes around the target method:

```text
Caller
  ↓
@Around Advice
  ↓
Before logic
  ↓
proceed()
  ↓
Target method
  ↓
After logic
  ↓
Return result
  ↓
Caller
```

The key difference is that the advice controls the point at which the target method is invoked.

---

# Why Use Around Advice?

Around Advice is useful when behavior needs to surround a method invocation.

Common examples include:

- Logging.
- Performance monitoring.
- Transactions.
- Security checks.
- Caching.
- Retry logic.
- Exception handling.
- Modifying method arguments.
- Modifying returned values.

For example, a performance-monitoring aspect could record the time before and after the target method:

```java
long start = System.currentTimeMillis();

Object result = joinPoint.proceed();

long elapsed = System.currentTimeMillis() - start;
```

The target method itself does not need to know about the monitoring logic.

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
Around Advice
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

The additional behavior is handled by the aspect.

---

# Creating Around Advice

The aspect uses the `@Around` annotation:

## `LoggingAspect`

```java
@Aspect
public class LoggingAspect {

    @Around("execution(* com.springbyexample.aroundadvice.GreetingService.greet(..))")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

        System.out.println("Before greeting");

        Object result = joinPoint.proceed();

        System.out.println("After greeting");

        return result;
    }
}
```

There are two important parts:

```java
@Around(...)
```

and:

```java
ProceedingJoinPoint joinPoint
```

The `@Around` annotation identifies the advice.

The `ProceedingJoinPoint` provides access to the current method invocation and allows the advice to continue the invocation using:

```java
joinPoint.proceed();
```

---

# What Is `ProceedingJoinPoint`?

A `ProceedingJoinPoint` represents the method invocation being intercepted by Around Advice.

It provides information and control over the invocation.

For example:

```java
Object result = joinPoint.proceed();
```

continues the invocation and causes the target method to execute.

Conceptually:

```text
ProceedingJoinPoint
        ↓
Current method invocation
        ↓
proceed()
        ↓
Target method
```

This makes `ProceedingJoinPoint` different from the join point available to other advice types.

Around Advice needs a way to control the continuation of the invocation, so Spring provides `ProceedingJoinPoint`.

---

# Why Is `proceed()` Important?

The:

```java
proceed()
```

method tells Spring to continue the intercepted method invocation.

In this example:

```java
Object result = joinPoint.proceed();
```

causes:

```java
GreetingService.greet("Spring")
```

to execute.

The execution flow is:

```text
@Around Advice
      ↓
Before greeting
      ↓
proceed()
      ↓
GreetingService.greet()
      ↓
Returns "Hello, Spring!"
      ↓
After greeting
      ↓
return result
```

Without:

```java
joinPoint.proceed();
```

the target method will not execute.

---

# What Happens If `proceed()` Is Not Called?

Around Advice has control over whether the target method executes.

For example:

```java
@Around("execution(* com.springbyexample.aroundadvice.GreetingService.greet(..))")
public Object aroundAdvice(ProceedingJoinPoint joinPoint) {

    System.out.println("Around advice");

    return "Blocked";
}
```

Because `proceed()` is never called:

```text
Caller
  ↓
Around Advice
  ↓
proceed() NOT called
  ↓
Target method does not execute
  ↓
"Blocked" returned
```

This is one of the most powerful characteristics of Around Advice.

It also means Around Advice should be used carefully.

Accidentally omitting `proceed()` can prevent the target method from executing.

---

# The Pointcut Expression

The example uses:

```java
execution(* com.springbyexample.aroundadvice.GreetingService.greet(..))
```

The pointcut identifies the method to which the advice should be applied.

The expression can be understood as:

```text
execution
   ↓
any return type
   ↓
com.springbyexample.aroundadvice.GreetingService
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

The configuration registers:

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
@Around Advice
  ↓
proceed()
  ↓
GreetingService
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

The Around Advice executes before and after the target method.

---

# Expected Output

Running the example produces:

```text
Before greeting
After greeting
Hello, Spring!
```

The output order can be understood from the execution flow.

The call in `Main` is:

```java
System.out.println(greetingService.greet("Spring"));
```

The expression passed to `System.out.println()` must first complete before `println()` can print the returned value.

The execution flow is:

```text
Main
  ↓
greet() is called
  ↓
@Around advice starts
  ↓
"Before greeting"
  ↓
proceed()
  ↓
GreetingService.greet()
  ↓
returns "Hello, Spring!"
  ↓
"After greeting"
  ↓
Around advice returns result
  ↓
Control returns to Main
  ↓
System.out.println() prints "Hello, Spring!"
```

Therefore, the output is:

```text
Before greeting
After greeting
Hello, Spring!
```

The important point is that both messages from the Around Advice occur before `System.out.println()` in `Main` prints the returned value.

---

# Accessing Method Arguments

`ProceedingJoinPoint` can also provide access to the arguments passed to the target method.

For example:

```java
Object[] arguments = joinPoint.getArgs();
```

For:

```java
greetingService.greet("Spring");
```

the arguments contain:

```text
["Spring"]
```

An aspect could inspect them:

```java
@Around("execution(* com.springbyexample.aroundadvice.GreetingService.greet(..))")
public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

    Object[] arguments = joinPoint.getArgs();

    System.out.println("Arguments: " + Arrays.toString(arguments));

    return joinPoint.proceed();
}
```

This can be useful for logging, validation, authorization, or other cross-cutting concerns.

---

# Modifying Method Arguments

Around Advice can also provide different arguments when calling `proceed()`.

For example:

```java
Object[] arguments = joinPoint.getArgs();

arguments[0] = "Spring AOP";

Object result = joinPoint.proceed(arguments);
```

The target method would then receive:

```text
Spring AOP
```

instead of:

```text
Spring
```

Conceptually:

```text
Caller
  ↓
greet("Spring")
  ↓
Around Advice
  ↓
Modify arguments
  ↓
proceed("Spring AOP")
  ↓
Target method
```

This demonstrates why Around Advice should be used carefully: it can change the behavior of the target invocation.

---

# Modifying the Return Value

Around Advice can also inspect or modify the result returned by the target method.

For example:

```java
Object result = joinPoint.proceed();

return result + " Welcome!";
```

If the target method returns:

```text
Hello, Spring!
```

the caller would receive:

```text
Hello, Spring! Welcome!
```

The flow becomes:

```text
Target method
      ↓
Returns result
      ↓
Around Advice
      ↓
Modify result
      ↓
Return modified result
```

This is another capability that makes Around Advice more powerful than the simpler advice types.

---

# Exception Handling

Around Advice can also handle exceptions thrown by the target method.

Because `proceed()` can throw an exception, the advice method declares:

```java
throws Throwable
```

For example:

```java
@Around("execution(* com.springbyexample.aroundadvice.GreetingService.greet(..))")
public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

    try {
        return joinPoint.proceed();
    }
    catch (Exception exception) {
        System.out.println("Greeting failed");

        throw exception;
    }
}
```

The advice can therefore perform additional behavior around exceptions while still allowing the exception to propagate.

This makes Around Advice useful for concerns such as:

- Logging failures.
- Retry logic.
- Exception translation.
- Monitoring.
- Cleanup.

---

# Around Advice Compared With Other Advice

The advice types introduced so far operate at different points in the method invocation.

## Before Advice

```text
Caller
  ↓
@Before
  ↓
Target
```

Before Advice cannot control whether the target executes.

---

## After Advice

```text
Caller
  ↓
Target
  ↓
@After
```

After Advice executes when the target method completes.

---

## After Returning Advice

```text
Caller
  ↓
Target
  ↓
Successful return
  ↓
@AfterReturning
```

After Returning Advice only executes after a successful return and can access the returned value.

---

## Around Advice

```text
Caller
  ↓
@Around
  ↓
Before logic
  ↓
proceed()
  ↓
Target
  ↓
After logic
  ↓
Return result
  ↓
Caller
```

Around Advice can control the entire invocation.

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

# Why Around Advice Should Be Used Carefully

Around Advice is the most powerful advice type introduced in this module so far.

It can:

- Prevent the target method from executing.
- Modify method arguments.
- Modify returned values.
- Handle exceptions.
- Execute arbitrary logic before and after the target method.

This flexibility also makes it easier to accidentally change application behavior.

For example, forgetting:

```java
joinPoint.proceed();
```

means the target method will never execute.

Similarly, changing arguments or return values can introduce unexpected behavior.

For simple concerns, a more specific advice type is often preferable.

For example:

```text
Need behavior before method?
        ↓
Use @Before

Need behavior after completion?
        ↓
Use @After

Need behavior after successful return?
        ↓
Use @AfterReturning

Need control over the entire invocation?
        ↓
Use @Around
```

A good rule is to use the **least powerful advice type that solves the problem**.

---

# Key Takeaways

- Around Advice surrounds the execution of a target method.
- `@Around` is used to define Around Advice.
- `ProceedingJoinPoint` represents the intercepted method invocation.
- `proceed()` continues the invocation and executes the target method.
- If `proceed()` is not called, the target method does not execute.
- Around Advice can execute logic before and after the target method.
- Around Advice can inspect method arguments.
- Around Advice can modify method arguments.
- Around Advice can inspect returned values.
- Around Advice can modify returned values.
- Around Advice can handle exceptions.
- Spring applies Around Advice through an AOP proxy.
- Around Advice is powerful and should therefore be used carefully.
- When possible, prefer a more specific advice type when it is sufficient for the requirement.

---

# What's Next?

The next example will introduce **Pointcuts** and explain how pointcut expressions determine which methods an advice applies to.