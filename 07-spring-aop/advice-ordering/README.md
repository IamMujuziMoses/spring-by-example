# Advice Ordering

Spring AOP allows multiple aspects and advice methods to apply to the same target method.

When multiple pieces of advice match the same method, Spring needs to determine the order in which they execute.

This example demonstrates how to control advice ordering using `@Order` and how ordering differs between advice that executes before and after the target method.

It brings together the concepts introduced throughout the Spring AOP module, including aspects, proxies, advice, pointcuts, and different advice types.

---

## Learning Objectives

By the end of this example, you will understand:

- Why advice ordering is important.
- What happens when multiple aspects apply to the same method.
- How to use `@Order`.
- How Spring determines which aspect has higher precedence.
- Why lower `@Order` values have higher precedence.
- How `@Before` advice is ordered.
- How `@After` advice is ordered.
- Why after advice executes in reverse order.
- How multiple aspects surround the same target method.
- How advice ordering relates to Spring's proxy-based AOP model.

---

# What Is Advice Ordering?

An application can have multiple aspects that apply to the same method.

For example:

```text
GreetingService.greet()
        ↑
        │
 ┌──────┴──────┐
 │             │
Logging      Security
Aspect        Aspect
```

Both aspects may contain advice that matches:

```java
GreetingService.greet(..)
```

Spring therefore needs to determine which advice executes first.

Advice ordering allows this execution order to be controlled explicitly.

---

# Example

This example contains:

```text
GreetingService
       ↓
Target method

LoggingAspect
       ↓
@Order(1)

SecurityAspect
       ↓
@Order(2)
```

Both aspects apply advice to:

```java
GreetingService.greet(..)
```

The ordering is:

```text
@Order(1)
    ↓
LoggingAspect

@Order(2)
    ↓
SecurityAspect
```

Lower values have higher precedence.

Therefore:

```text
1
```

runs before:

```text
2
```

when entering the target method.

---

# GreetingService

The target class contains a simple greeting method.

## `GreetingService`

```java
public class GreetingService {

    public String greet(String name) {
        return "Hello, " + name + "!";
    }
}
```

The service contains only the business logic.

It does not know about either aspect.

---

# Logging Aspect

The first aspect is the logging aspect.

## `LoggingAspect`

```java
@Aspect
@Order(1)
public class LoggingAspect {

    @Before("execution(* com.springbyexample.adviceordering.GreetingService.greet(..))")
    public void beforeGreeting() {
        System.out.println("Logging: Before greeting");
    }

    @After("execution(* com.springbyexample.adviceordering.GreetingService.greet(..))")
    public void afterGreeting() {
        System.out.println("Logging: After greeting");
    }
}
```

The important annotation is:

```java
@Order(1)
```

This gives the logging aspect higher precedence than an aspect with:

```java
@Order(2)
```

---

# Security Aspect

The second aspect is the security aspect.

## `SecurityAspect`

```java
@Aspect
@Order(2)
public class SecurityAspect {

    @Before("execution(* com.springbyexample.adviceordering.GreetingService.greet(..))")
    public void checkAccess() {
        System.out.println("Security: Checking access");
    }

    @After("execution(* com.springbyexample.adviceordering.GreetingService.greet(..))")
    public void afterSecurityCheck() {
        System.out.println("Security: After greeting");
    }
}
```

This aspect has:

```java
@Order(2)
```

which gives it lower precedence than:

```java
@Order(1)
```

---

# Understanding `@Order`

The `@Order` annotation controls the precedence of Spring-managed aspects.

For example:

```java
@Order(1)
public class LoggingAspect {
}
```

and:

```java
@Order(2)
public class SecurityAspect {
}
```

means:

```text
LoggingAspect
    ↓
Higher precedence

SecurityAspect
    ↓
Lower precedence
```

The important rule is:

> Lower order values have higher precedence.

Therefore:

```text
@Order(1)
```

has higher precedence than:

```text
@Order(2)
```

which has higher precedence than:

```text
@Order(3)
```

and so on.

---

# Why Does Ordering Matter?

Imagine an application has several cross-cutting concerns:

```text
Security
Logging
Transactions
Caching
Monitoring
```

The order in which these concerns execute can affect application behavior.

For example:

```text
Security
    ↓
Transaction
    ↓
Business logic
```

may have different behavior from:

```text
Transaction
    ↓
Security
    ↓
Business logic
```

Advice ordering allows an application to make this ordering explicit.

---

# Before Advice Ordering

In this example:

```java
@Order(1)
public class LoggingAspect {
}
```

and:

```java
@Order(2)
public class SecurityAspect {
}
```

the `@Before` advice executes in precedence order.

Therefore:

```text
Logging @Before
        ↓
Security @Before
        ↓
Target method
```

The lower order value runs first.

Conceptually:

```text
Caller
   ↓
Logging @Before
   ↓
Security @Before
   ↓
GreetingService.greet()
```

---

# After Advice Ordering

`@After` advice behaves differently.

After advice executes as the invocation unwinds.

Therefore, when multiple aspects surround the same method, the after advice runs in reverse precedence order.

With:

```text
LoggingAspect @Order(1)
SecurityAspect @Order(2)
```

the execution is:

```text
Logging @Before
        ↓
Security @Before
        ↓
Target
        ↓
Security @After
        ↓
Logging @After
```

This can be understood as nested execution.

Conceptually:

```text
Logging
┌─────────────────────────────┐
│ Security                    │
│ ┌─────────────────────────┐ │
│ │ Target method            │ │
│ └─────────────────────────┘ │
│ Security after              │
└─────────────────────────────┘
Logging after
```

The aspect with higher precedence effectively wraps the lower-precedence aspect.

---

# Complete Execution Flow

The complete invocation is:

```text
Caller
  ↓
Logging @Before
  ↓
Security @Before
  ↓
GreetingService.greet()
  ↓
Security @After
  ↓
Logging @After
  ↓
return "Hello, Spring!"
  ↓
Caller
```

The output is therefore:

```text
Logging: Before greeting
Security: Checking access
Security: After greeting
Logging: After greeting
Hello, Spring!
```

---

# Why Does `Hello, Spring!` Appear Last?

The application calls:

```java
System.out.println(greetingService.greet("Spring"));
```

The method argument to `println()` must first be evaluated.

Therefore, Java effectively needs to complete:

```java
greetingService.greet("Spring")
```

before `println()` can print the returned value.

The execution is:

```text
greet("Spring")
    ↓
Logging @Before
    ↓
Security @Before
    ↓
GreetingService.greet()
    ↓
Security @After
    ↓
Logging @After
    ↓
returns "Hello, Spring!"
    ↓
System.out.println()
    ↓
Hello, Spring!
```

The final line is therefore printed by `Main`, not by the aspect.

---

# Configuration

Spring must enable its proxy-based AOP infrastructure and register both aspects.

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

    @Bean
    public SecurityAspect securityAspect() {
        return new SecurityAspect();
    }
}
```

The application context contains:

```text
GreetingService
LoggingAspect
SecurityAspect
```

Spring then processes the aspects and creates the required proxy around the target service.

---

# `@EnableAspectJAutoProxy`

The:

```java
@EnableAspectJAutoProxy
```

annotation enables Spring's annotation-driven AOP proxy infrastructure.

Conceptually:

```text
@EnableAspectJAutoProxy
        ↓
Spring AOP enabled
        ↓
Discover aspects
        ↓
Evaluate pointcuts
        ↓
Determine advice ordering
        ↓
Create proxy
        ↓
Intercept matching method calls
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

The service is obtained from the Spring application context:

```java
GreetingService greetingService = context.getBean(GreetingService.class);
```

The method invocation then passes through the Spring AOP proxy.

---

# Expected Output

```text
Logging: Before greeting
Security: Checking access
Security: After greeting
Logging: After greeting
Hello, Spring!
```

The ordering demonstrates:

```text
@Order(1)
    ↓
Logging before

@Order(2)
    ↓
Security before

Target
    ↓
GreetingService.greet()

@Order(2)
    ↓
Security after

@Order(1)
    ↓
Logging after
```

---

# Advice Ordering as Nested Execution

A useful way to understand ordering is to imagine each aspect as a wrapper.

With:

```text
Logging = @Order(1)
Security = @Order(2)
```

the structure is conceptually:

```text
Logging
    ↓
    Security
        ↓
        Target
        ↑
    Security
    ↑
Logging
```

Therefore:

```text
Entering the invocation:
    Logging → Security → Target

Leaving the invocation:
    Target → Security → Logging
```

This is why `@Before` and `@After` appear to have opposite ordering.

---

# Multiple Advice in the Same Aspect

Advice ordering also matters when an aspect contains multiple advice methods.

For example:

```java
@Before(...)
public void firstAdvice() {
}
```

and:

```java
@Before(...)
public void secondAdvice() {
}
```

If multiple advice methods in the same aspect match the same join point, their relative ordering should not be relied upon unless it is explicitly defined by the applicable Spring AOP ordering rules.

When a specific order is important, separating concerns into independently ordered aspects is often clearer.

For this example, the two separate aspects make the ordering explicit.

---

# Ordering vs Pointcuts

Pointcuts determine:

```text
WHERE
```

advice applies.

Ordering determines:

```text
WHEN
```

multiple matching pieces of advice execute relative to each other.

For example:

```text
Pointcut
    ↓
GreetingService.greet()
    ↓
Both aspects match
    ↓
@Order determines precedence
```

This gives the two concepts different responsibilities:

```text
Pointcut
    ↓
Which methods?

Advice
    ↓
What behavior?

@Order
    ↓
Which advice executes first?
```

---

# Ordering and Around Advice

The same ordering concept applies to Around Advice.

For example:

```java
@Aspect
@Order(1)
public class FirstAspect {
}
```

and:

```java
@Aspect
@Order(2)
public class SecondAspect {
}
```

can conceptually form:

```text
FirstAspect
    ↓
SecondAspect
    ↓
Target
    ↑
SecondAspect
    ↑
FirstAspect
```

Around Advice makes the wrapping behavior especially visible because it explicitly calls:

```java
joinPoint.proceed();
```

Advice ordering therefore becomes particularly important when multiple Around Advice methods are involved.

---

# Why Use the Spring Application Context in the Test?

Spring AOP is applied through Spring's proxy infrastructure.

Creating the service manually:

```java
GreetingService greetingService = new GreetingService();
```

would not apply the Spring-managed aspects.

Instead, the test creates:

```java
new AnnotationConfigApplicationContext(AppConfig.class);
```

This allows Spring to:

1. Create the target bean.
2. Create both aspect beans.
3. Process their pointcuts.
4. Read their `@Order` values.
5. Create the AOP proxy.
6. Apply the advice in the correct order.

---

# Why Explicit Ordering Is Useful

Explicit ordering makes the relationship between cross-cutting concerns clear.

For example:

```text
Security
    ↓
Transactions
    ↓
Business logic
```

might be intentional.

Likewise:

```text
Logging
    ↓
Security
    ↓
Business logic
```

may be useful when security failures should also be logged.

Without explicit ordering, relying on incidental ordering can make an application harder to understand and maintain.

When the order matters, use:

```java
@Order(...)
```

to make the intended precedence clear.

---

# Key Takeaways

- Multiple aspects can apply to the same target method.
- Spring needs to determine the order in which matching advice executes.
- `@Order` can be used to control aspect precedence.
- Lower `@Order` values have higher precedence.
- `@Before` advice executes according to aspect precedence when entering the target invocation.
- `@After` advice executes in reverse precedence as the invocation unwinds.
- Higher-precedence aspects effectively wrap lower-precedence aspects.
- Pointcuts determine which methods an advice applies to.
- `@Order` determines the precedence between matching aspects.
- Spring applies ordered advice through its proxy-based AOP infrastructure.
- Explicit ordering is useful when the order of cross-cutting concerns affects application behavior.
- The returned value is printed by the caller only after the complete proxied invocation has finished.
- Advice ordering is especially important when multiple Around Advice implementations are involved.

---

# Module 7 Summary

The Spring AOP module has now covered the core concepts of proxy-based aspect-oriented programming.

The progression was:

```text
What is AOP?
      ↓
JDK Dynamic Proxies
      ↓
CGLIB Proxies
      ↓
Creating an Aspect
      ↓
Before Advice
      ↓
After Advice
      ↓
After Returning Advice
      ↓
Around Advice
      ↓
Pointcuts
      ↓
Advice Ordering
```

Together, these examples demonstrate how Spring can apply cross-cutting behavior to application services without requiring the target classes to contain that behavior directly.

The overall model is:

```text
Caller
   ↓
Spring AOP Proxy
   ↓
Pointcut matching
   ↓
Ordered Advice
   ↓
Target Method
   ↓
Ordered Advice
   ↓
Return value
```

This provides the foundation for understanding more advanced Spring AOP features and how frameworks such as Spring use proxy-based interception internally.