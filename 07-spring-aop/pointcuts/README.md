# Pointcuts

Spring AOP uses **pointcuts** to determine which method executions should be intercepted by an advice.

This example demonstrates how to define a pointcut using an `execution(...)` expression and how Spring applies advice only to methods that match the pointcut.

It builds on the previous **Before Advice**, **After Advice**, **After Returning Advice**, and **Around Advice** examples by focusing on how Spring determines **where** an advice should execute.

---

## Learning Objectives

By the end of this example, you will understand:

- What a pointcut is.
- Why pointcuts are important in Spring AOP.
- How to define a pointcut using an `execution(...)` expression.
- How a pointcut determines which methods an advice applies to.
- How to read an `execution(...)` pointcut expression.
- What the `*` wildcard represents.
- What the `..` wildcard represents.
- How to match a specific method.
- How to match all methods in a class.
- The difference between a pointcut and advice.
- How Spring uses pointcuts when creating AOP proxies.

---

# What Is a Pointcut?

A **pointcut** is a rule that determines which method executions an AOP advice should apply to.

For example:

```java
@Before("execution(* com.springbyexample.pointcuts.GreetingService.greet(..))")
public void logGreeting() {
    System.out.println("Greeting method called");
}
```

The pointcut is:

```java
execution(* com.springbyexample.pointcuts.GreetingService.greet(..))
```

It tells Spring:

> Apply this advice when `GreetingService.greet(...)` is executed.

Conceptually:

```text
                 Pointcut
                    ↓
             Which methods?
                    ↓
          GreetingService.greet()
                    ↓
               @Before
                    ↓
             Execute advice
```

A pointcut answers the question:

```text
"Where should this advice apply?"
```

---

# Pointcut vs Advice

Pointcuts and advice have different responsibilities.

### Pointcut

Determines **where** the behavior applies.

```text
Pointcut
   ↓
Which method?
```

### Advice

Determines **what** should happen.

```text
Advice
   ↓
What should happen?
```

For example:

```java
@Before("execution(* com.springbyexample.pointcuts.GreetingService.greet(..))")
public void logGreeting() {
    System.out.println("Greeting method called");
}
```

The two parts can be understood as:

```text
@Before
   ↓
What should happen?
   ↓
Execute logGreeting()

execution(...)
   ↓
Where should it happen?
   ↓
GreetingService.greet()
```

This separation allows the same advice behavior to be applied to different methods by changing the pointcut.

---

# Example

The example consists of three main components:

```text
AppConfig
    ↓
Spring configuration

GreetingService
    ↓
Target bean with multiple methods

LoggingAspect
    ↓
Advice with a pointcut
```

The service contains two methods:

```text
GreetingService
    ├── greet()
    └── farewell()
```

The pointcut is configured to match only:

```text
greet()
```

Therefore, the advice is applied to `greet()` but not `farewell()`.

---

# GreetingService

The target class contains two methods:

## `GreetingService`

```java
public class GreetingService {

    public String greet(String name) {
        return "Hello, " + name + "!";
    }

    public String farewell(String name) {
        return "Goodbye, " + name + "!";
    }
}
```

The two methods represent different join points that the pointcut can either match or ignore.

```text
GreetingService
      │
      ├── greet()
      │      ↓
      │   Pointcut matches
      │
      └── farewell()
             ↓
          No match
```

---

# Creating a Pointcut

The aspect uses a pointcut inside the `@Before` annotation:

```java
@Aspect
public class LoggingAspect {

    @Before("execution(* com.springbyexample.pointcuts.GreetingService.greet(..))")
    public void logGreeting() {
        System.out.println("Greeting method called");
    }
}
```

The pointcut expression is:

```java
execution(* com.springbyexample.pointcuts.GreetingService.greet(..))
```

This expression specifically targets:

```java
GreetingService.greet(...)
```

It does not target:

```java
GreetingService.farewell(...)
```

---

# Understanding `execution(...)`

The `execution(...)` designator is used to match method executions.

The general structure can be simplified as:

```text
execution(return-type declaring-type.method-name(arguments))
```

For example:

```java
execution(* com.springbyexample.pointcuts.GreetingService.greet(..))
```

can be broken down into:

```text
execution(
    *                                      → return type
    com.springbyexample.pointcuts.GreetingService → declaring type
    .greet                                 → method
    (..)                                   → arguments
)
```

Each part helps determine which methods should match the pointcut.

---

# The Return Type

The first `*` represents the return type:

```java
execution(* com.springbyexample.pointcuts.GreetingService.greet(..))
```

The wildcard:

```text
*
```

means any return type.

Therefore, the pointcut can match `greet()` regardless of its return type.

For example:

```java
String greet(String name)
```

would match.

A pointcut could also specify a concrete return type:

```java
execution(String com.springbyexample.pointcuts.GreetingService.greet(..))
```

This would specifically require the method to return `String`.

---

# The Declaring Type

The next part identifies the class containing the method:

```text
com.springbyexample.pointcuts.GreetingService
```

Therefore:

```java
execution(* com.springbyexample.pointcuts.GreetingService.greet(..))
```

targets methods declared by:

```java
GreetingService
```

This prevents the pointcut from accidentally matching a method with the same name in an unrelated class.

---

# The Method Name

The method name is:

```text
greet
```

Therefore:

```java
execution(* com.springbyexample.pointcuts.GreetingService.greet(..))
```

matches:

```java
GreetingService.greet(...)
```

but not:

```java
GreetingService.farewell(...)
```

This allows the pointcut to target a specific method.

---

# The Arguments

The final part is:

```text
(..)
```

The `..` wildcard means any number of arguments.

Therefore:

```java
execution(* com.springbyexample.pointcuts.GreetingService.greet(..))
```

can match methods such as:

```java
greet()
```

```java
greet("Spring")
```

```java
greet("Spring", "AOP")
```

as long as the method name and declaring type match.

---

# The `*` and `..` Wildcards

Two wildcards are particularly important when reading execution pointcuts.

## `*`

The `*` wildcard generally means "any single matching item" in the relevant part of the expression.

For example:

```java
execution(* com.springbyexample.pointcuts.GreetingService.greet(..))
```

uses:

```text
*
```

for the return type.

This means any return type.

---

## `..`

The `..` wildcard is commonly used to represent any number of arguments.

For example:

```java
greet(..)
```

can match:

```java
greet()
```

```java
greet("Spring")
```

```java
greet("Spring", "AOP")
```

---

# Matching a Specific Method

The current pointcut targets one specific method:

```java
execution(* com.springbyexample.pointcuts.GreetingService.greet(..))
```

Conceptually:

```text
GreetingService
      │
      ├── greet()      ← MATCH
      │
      └── farewell()   ← NO MATCH
```

Therefore:

```java
greetingService.greet("Spring");
```

triggers the advice.

But:

```java
greetingService.farewell("Spring");
```

does not trigger the advice.

---

# Matching All Methods in a Class

The method name can also be replaced with `*`:

```java
execution(* com.springbyexample.pointcuts.GreetingService.*(..))
```

This matches all methods in `GreetingService`.

Conceptually:

```text
GreetingService
      │
      ├── greet()      ← MATCH
      │
      └── farewell()   ← MATCH
```

This is broader than the pointcut used in the example.

The important difference is:

```java
GreetingService.greet(..)
```

targets one method.

While:

```java
GreetingService.*(..)
```

targets all methods in the class.

---

# Matching Methods in a Package

The pointcut can be made broader still:

```java
execution(* com.springbyexample.pointcuts.*.*(..))
```

This can match methods in types directly within the specified package.

Conceptually:

```text
com.springbyexample.pointcuts
        │
        ├── GreetingService.greet()
        │        ↓
        │      MATCH
        │
        ├── GreetingService.farewell()
        │        ↓
        │      MATCH
        │
        └── AnotherService.someMethod()
                 ↓
               MATCH
```

Broader pointcuts should be used carefully because they can cause advice to apply to more methods than intended.

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
Spring evaluates pointcuts
          ↓
Spring finds matching beans
          ↓
Spring creates proxies
          ↓
Advice can intercept matching methods
```

Without this configuration, Spring will not automatically create the proxy required for the advice to execute.

---

# How Spring Uses the Pointcut

When the application context starts, Spring processes the aspect and its pointcut.

The target service contains:

```text
GreetingService
    ├── greet()
    └── farewell()
```

The pointcut says:

```text
Match greet()
```

Spring therefore applies the advice to calls to `greet()`.

Conceptually:

```text
GreetingService
       ↓
Spring AOP evaluates pointcut
       ↓
greet() matches
       ↓
Create/apply proxy behavior
```

When `farewell()` is called, the pointcut does not match it, so the advice is not executed for that method.

---

# Running the Example

## `Main`

```java
public class Main {

    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingService greetingService = context.getBean(GreetingService.class);

            System.out.println(greetingService.greet("Spring"));
            System.out.println(greetingService.farewell("Spring"));
        }
    }
}
```

The application invokes both methods:

```java
greetingService.greet("Spring");
```

and:

```java
greetingService.farewell("Spring");
```

The pointcut only matches the first method.

---

# Expected Output

Running the example produces:

```text
Greeting method called
Hello, Spring!
Goodbye, Spring!
```

The important part is that:

```text
Greeting method called
```

appears only once.

The execution flow is:

```text
greet("Spring")
    ↓
Pointcut matches
    ↓
@Before advice
    ↓
Greeting method called
    ↓
greet() executes
    ↓
Hello, Spring!

farewell("Spring")
    ↓
Pointcut does not match
    ↓
farewell() executes
    ↓
Goodbye, Spring!
```

This demonstrates that the pointcut determines which method receives the advice.

---

# Why Pointcuts Matter

Without pointcuts, it would be difficult to control where cross-cutting behavior applies.

For example, suppose an application contains:

```text
PatientService
    ├── createPatient()
    ├── updatePatient()
    └── deletePatient()

OrderService
    ├── createOrder()
    ├── updateOrder()
    └── cancelOrder()
```

A logging aspect might need to apply to all service methods:

```text
PatientService.*()
OrderService.*()
```

Another security aspect might need to apply only to specific operations:

```text
PatientService.deletePatient()
OrderService.cancelOrder()
```

Pointcuts provide the mechanism for expressing these rules.

---

# Pointcuts and Advice Work Together

A useful way to remember the relationship is:

```text
Pointcut
    ↓
WHERE should behavior apply?

Advice
    ↓
WHAT should happen?

Spring AOP
    ↓
Connects them through proxies
```

For example:

```java
@Before("execution(* com.springbyexample.pointcuts.GreetingService.greet(..))")
public void logGreeting() {
    System.out.println("Greeting method called");
}
```

The pointcut determines:

```text
GreetingService.greet()
```

The advice determines:

```text
Print "Greeting method called"
```

Together they define:

```text
When greet() executes
        ↓
Run logGreeting()
```

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
3. Process the pointcut.
4. Create the AOP proxy.
5. Apply the advice to matching method calls.

---

# Pointcut Specificity

Pointcuts should generally be as specific as the requirement allows.

For example:

```java
execution(* com.springbyexample.pointcuts.GreetingService.greet(..))
```

is more specific than:

```java
execution(* com.springbyexample.pointcuts.GreetingService.*(..))
```

The first matches one method.

The second matches every method in the class.

A broad pointcut can unintentionally apply advice to methods that were not intended to be intercepted.

Therefore:

```text
Prefer
    ↓
Specific pointcut

When appropriate
    ↓
Broader pointcut
```

---

# Inline Pointcuts

The examples so far use an inline pointcut:

```java
@Before("execution(* com.springbyexample.pointcuts.GreetingService.greet(..))")
```

This is useful for simple examples and one-off pointcuts.

As applications become larger, pointcuts can be extracted into named pointcut methods.

For example:

```java
@Pointcut("execution(* com.springbyexample.pointcuts.GreetingService.greet(..))")
public void greetingMethods() {
}
```

Advice can then reference the named pointcut:

```java
@Before("greetingMethods()")
public void logGreeting() {
    System.out.println("Greeting method called");
}
```

Named pointcuts are useful when the same matching rule is shared by multiple advice methods.

This example focuses on inline pointcuts first so that the structure of the `execution(...)` expression is clear.

---

# Key Takeaways

- A pointcut determines which method executions an advice applies to.
- `execution(...)` is commonly used to match method executions.
- The return type, declaring type, method name, and arguments can be specified in an execution pointcut.
- `*` can be used as a wildcard.
- `..` can be used to match any number of arguments.
- A specific method can be targeted with a pointcut.
- A class's methods can be matched more broadly using wildcards.
- Pointcuts and advice have different responsibilities.
- The pointcut determines **where** behavior applies.
- The advice determines **what** behavior should execute.
- Spring evaluates pointcuts when applying AOP to Spring-managed beans.
- Spring AOP uses proxies to intercept matching method calls.
- More specific pointcuts reduce the risk of applying advice unintentionally.
- Named pointcuts can be used when matching rules need to be reused.

---

# What's Next?

The next example will introduce **Advice Ordering** and demonstrate how Spring determines the order in which multiple pieces of advice execute around the same method.