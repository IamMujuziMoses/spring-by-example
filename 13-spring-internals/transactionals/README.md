# How `@Transactional` Works

This example demonstrates how Spring processes `@Transactional` metadata internally to manage transactions around method execution.

Rather than using `@EnableTransactionManagement` or relying on Spring Boot's automatic configuration, the example directly wires Spring's transaction infrastructure using `TransactionInterceptor`, `AnnotationTransactionAttributeSource`, and a custom `PlatformTransactionManager`.

The goal is to understand what happens **inside Spring when a transactional method is invoked**.

## Learning Objectives

By completing this example, you will understand:

- What `@Transactional` represents
- How Spring discovers transactional metadata
- What `TransactionAttributeSource` does
- What `TransactionAttribute` represents
- What `TransactionInterceptor` does
- How `PlatformTransactionManager` participates in transaction management
- How transactions begin and end around method execution
- How successful methods lead to commits
- How exceptions can lead to rollbacks
- Why `TransactionInterceptor` is an interceptor rather than the transaction manager itself
- How this relates to Spring's AOP infrastructure

---

## What Is `@Transactional`?

`@Transactional` is an annotation used to declare that a method or class should execute within a transaction.

For example:

```java
@Transactional
public String greet() {
    return greetingService.greet();
}
```

The annotation itself does **not** start or commit a transaction.

Instead, it provides metadata that Spring's transaction infrastructure can inspect.

Conceptually:

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
TransactionManager
```

This separation is important.

The annotation describes the desired transactional behavior, while Spring's infrastructure performs the actual transaction management.

---

## The Transactional Service

The example contains a service with two transactional methods:

```java
public class TransactionalService {

    private final GreetingService greetingService;

    public TransactionalService(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @Transactional
    public String greet() {
        return greetingService.greet();
    }

    @Transactional
    public String fail() {
        throw new IllegalStateException("Something went wrong!");
    }
}
```

The first method completes successfully.

The second method throws an exception.

This allows the example to demonstrate both sides of transaction handling:

```text
Successful invocation
        │
        ▼
   begin transaction
        │
        ▼
   execute method
        │
        ▼
      commit


Failed invocation
        │
        ▼
   begin transaction
        │
        ▼
   execute method
        │
        X
    exception
        │
        ▼
     rollback
```

---

## What Is `TransactionAttributeSource`?

Spring needs a way to determine whether a method is transactional and, if so, what transaction configuration applies to it.

That is the role of `TransactionAttributeSource`.

In this example we use:

```java
TransactionAttributeSource attributeSource = new AnnotationTransactionAttributeSource();
```

`AnnotationTransactionAttributeSource` understands Spring's transaction annotations, including `@Transactional`.

When the interceptor receives a method invocation, it can ask the attribute source for the transaction metadata associated with that method.

Conceptually:

```text
Method
  │
  ▼
TransactionAttributeSource
  │
  ▼
TransactionAttribute
```

The returned `TransactionAttribute` represents the transactional configuration Spring should use for the invocation.

---

## What Is `TransactionInterceptor`?

`TransactionInterceptor` is the component responsible for applying transaction management around a method invocation.

It acts as an interceptor.

Instead of simply doing:

```text
method()
```

the interceptor effectively creates a boundary around the invocation:

```text
begin transaction
       │
       ▼
   method()
       │
       ▼
commit transaction
```

If the method throws an exception:

```text
begin transaction
       │
       ▼
   method()
       │
       X
   exception
       │
       ▼
rollback transaction
```

The interceptor does not itself implement the database transaction operations.

It delegates those operations to a transaction manager.

---

## Configuring `TransactionInterceptor`

The example uses the no-argument constructor and configures the interceptor explicitly:

```java
TransactionInterceptor transactionInterceptor = new TransactionInterceptor();

transactionInterceptor.setTransactionManager(transactionManager);
transactionInterceptor.setTransactionAttributeSource(attributeSource);
```

This makes the relationships between the components explicit.

```text
TransactionInterceptor
        │
        ├── TransactionManager
        │
        └── TransactionAttributeSource
```

The interceptor needs both:

1. Transaction metadata — what transactional behavior applies?
2. A transaction manager — how should the transaction actually be managed?

---

## What Is `PlatformTransactionManager`?

`PlatformTransactionManager` is Spring's abstraction for transaction management.

The interceptor delegates transaction operations to it.

The interface provides operations corresponding to:

```text
getTransaction()
commit()
rollback()
```

The example provides a simple implementation:

```java
public class RecordingTransactionManager implements PlatformTransactionManager {

    private int transactionCount;
    
    private int commitCount;
    
    private int rollbackCount;

    @Override
    public TransactionStatus getTransaction(TransactionDefinition definition) {
        transactionCount++;
        return new SimpleTransactionStatus();
    }

    @Override
    public void commit(TransactionStatus status) {
        commitCount++;
    }

    @Override
    public void rollback(TransactionStatus status) {
        rollbackCount++;
    }
}
```

This transaction manager does not connect to a database.

Instead, it records how many transactions were started, committed, and rolled back.

That makes the behavior of `TransactionInterceptor` easy to observe.

---

## Why Use a Custom Transaction Manager?

A real application might use a transaction manager such as one backed by JDBC or JPA.

For example:

```text
Application
    │
    ▼
TransactionInterceptor
    │
    ▼
PlatformTransactionManager
    │
    ▼
Database transaction
```

That would introduce database configuration and infrastructure unrelated to this example.

The custom transaction manager lets us focus on the internal flow:

```text
TransactionInterceptor
        │
        ▼
RecordingTransactionManager
        │
        ├── begin
        ├── commit
        └── rollback
```

The important lesson is the interaction between the components, not database configuration.

---

## Registering Transaction Metadata

The `AnnotationTransactionAttributeSource` is responsible for interpreting the `@Transactional` annotation.

For:

```java
@Transactional
public String greet() {
    return greetingService.greet();
}
```

the flow begins with the method:

```text
TransactionalService.greet()
        │
        ▼
AnnotationTransactionAttributeSource
        │
        ▼
TransactionAttribute
```

The transaction interceptor uses that metadata to determine whether transaction management should be applied.

---

## Invoking the Interceptor

The example manually invokes the interceptor:

```java
Method greetMethod = TransactionalService.class.getMethod("greet");

Object result = transactionInterceptor.invoke(new SimpleMethodInvocation(transactionalService, greetMethod));
```

This is deliberately more explicit than normal Spring application code.

In a normal application, you would typically write:

```java
transactionalService.greet();
```

and Spring's AOP infrastructure would arrange for the transaction interceptor to participate in the invocation.

Here, we invoke the interceptor directly because the purpose of this example is to understand what the interceptor itself does.

The creation of the proxy that normally connects the application method call to this interceptor is covered separately in:

**#6 How AOP Proxies Are Created**

---

## Successful Transaction

When `greet()` is invoked, the interceptor finds the `@Transactional` metadata and starts a transaction.

The simplified flow is:

```text
transactionInterceptor.invoke()
            │
            ▼
Find TransactionAttribute
            │
            ▼
Start transaction
            │
            ▼
MethodInvocation.proceed()
            │
            ▼
TransactionalService.greet()
            │
            ▼
Return result
            │
            ▼
Commit transaction
```

The application prints:

```text
Hello from GreetingService!
Transactions started: 1
Transactions committed: 1
```

This demonstrates that the successful method execution was surrounded by transaction management.

---

## Failed Transaction

The `fail()` method deliberately throws an exception:

```java
@Transactional
public String fail() {
    throw new IllegalStateException("Something went wrong!");
}
```

The interceptor begins the transaction and then invokes the method.

The method throws:

```text
IllegalStateException
```

The interceptor handles the failure by rolling back the transaction.

Conceptually:

```text
transactionInterceptor.invoke()
            │
            ▼
Find TransactionAttribute
            │
            ▼
Start transaction
            │
            ▼
MethodInvocation.proceed()
            │
            ▼
TransactionalService.fail()
            │
            X
    IllegalStateException
            │
            ▼
Rollback transaction
            │
            ▼
Propagate exception
```

The application therefore prints:

```text
Something went wrong!
Transactions rolled back: 1
```

---

## Why `InvocationTargetException` Is Unwrapped

The example uses reflection to invoke the target method:

```java
return method.invoke(target);
```

Java reflection wraps an exception thrown by the target method inside `InvocationTargetException`.

That would mean the transaction interceptor receives:

```text
InvocationTargetException
        │
        └── IllegalStateException
```

instead of the original exception.

The example therefore unwraps the exception:

```java
@Override
public Object proceed() throws Throwable {
    try {
        return method.invoke(target);
    } catch (InvocationTargetException exception) {
        throw exception.getCause();
    }
}
```

This allows `TransactionInterceptor` to see the original `IllegalStateException`.

That is important because transaction rollback behavior depends on the exception that reaches the transaction infrastructure.

---

## The Complete Transaction Flow

The complete flow demonstrated by this example is:

```text
@Transactional
      │
      ▼
AnnotationTransactionAttributeSource
      │
      ▼
TransactionAttribute
      │
      ▼
TransactionInterceptor
      │
      ▼
PlatformTransactionManager
      │
      ├──────────────┐
      │              │
      ▼              ▼
 begin          Transactional method
                     │
                     ▼
             MethodInvocation.proceed()
                     │
             ┌───────┴────────┐
             │                │
          success          exception
             │                │
             ▼                ▼
          commit           rollback
```

---

## `@Transactional` Does Not Create the Transaction

It is important not to confuse the annotation with the infrastructure that processes it.

This:

```java
@Transactional
public void save() {
    // ...
}
```

only declares transactional metadata.

The actual behavior comes from infrastructure such as:

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
PlatformTransactionManager
```

This is why simply creating an object with:

```java
TransactionalService service = new TransactionalService(greetingService);
```

does not automatically make calls transactional.

The object needs to be invoked through the infrastructure that applies the interceptor.

---

## Why `new` Does Not Apply Transaction Management

Consider:

```java
TransactionalService service = new TransactionalService(greetingService);

service.greet();
```

There is no transaction interception here.

The method is called directly on the object.

In this example, we explicitly invoke the interceptor:

```java
transactionInterceptor.invoke(new SimpleMethodInvocation(service, greetMethod));
```

A normal Spring application hides this infrastructure behind an AOP proxy.

That is why the next example, **How AOP Proxies Are Created**, is important.

---

## Relationship to Dependency Injection

This example builds on the previous Module 13 topics.

Previously we examined:

```text
Bean registration
      │
      ▼
Dependency resolution
      │
      ▼
Component scanning
      │
      ▼
@Autowired processing
```

Now we add transaction interception:

```text
Bean registration
      │
      ▼
Dependency injection
      │
      ▼
Bean post-processing
      │
      ▼
Transactional metadata
      │
      ▼
TransactionInterceptor
```

The examples are progressively revealing the infrastructure behind normal Spring application code.

---

## Relationship to AOP Proxies

This example deliberately stops before proxy creation.

We manually call:

```java
transactionInterceptor.invoke(...)
```

In a normal Spring application, the developer does not normally call this method directly.

Instead:

```java
transactionalService.greet();
```

eventually reaches the transaction interceptor through Spring's AOP proxy infrastructure.

That distinction is the subject of the next example:

**How AOP Proxies Are Created**

The progression is therefore:

```text
#5 How @Transactional Works
        │
        ▼
TransactionInterceptor
        │
        ▼
#6 How AOP Proxies Are Created
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

---

## Complete Example

### `GreetingService.java`

```java
public class GreetingService {

    public String greet() {
        return "Hello from GreetingService!";
    }
}
```

### `TransactionalService.java`

```java
public class TransactionalService {

    private final GreetingService greetingService;

    public TransactionalService(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @Transactional
    public String greet() {
        return greetingService.greet();
    }

    @Transactional
    public String fail() {
        throw new IllegalStateException("Something went wrong!");
    }
}
```

### `RecordingTransactionManager.java`

```java
public class RecordingTransactionManager implements PlatformTransactionManager {

    private int transactionCount;
    
    private int commitCount;
    
    private int rollbackCount;

    @Override
    public TransactionStatus getTransaction(TransactionDefinition definition) {
        transactionCount++;
        return new SimpleTransactionStatus();
    }

    @Override
    public void commit(TransactionStatus status) {
        commitCount++;
    }

    @Override
    public void rollback(TransactionStatus status) {
        rollbackCount++;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public int getCommitCount() {
        return commitCount;
    }

    public int getRollbackCount() {
        return rollbackCount;
    }
}
```

### `TransactionalApplication.java`

```java
public class TransactionalApplication {

    public static void main(String[] args) throws Throwable {
        GreetingService greetingService = new GreetingService();
        TransactionalService transactionalService = new TransactionalService(greetingService);
        RecordingTransactionManager transactionManager = new RecordingTransactionManager();
        TransactionAttributeSource attributeSource = new AnnotationTransactionAttributeSource();

        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        transactionInterceptor.setTransactionManager(transactionManager);
        transactionInterceptor.setTransactionAttributeSource(attributeSource);

        Method greetMethod = TransactionalService.class.getMethod("greet");

        Object result = transactionInterceptor.invoke(new SimpleMethodInvocation(transactionalService, greetMethod));

        System.out.println(result);
        System.out.println("Transactions started: " + transactionManager.getTransactionCount());
        System.out.println("Transactions committed: " + transactionManager.getCommitCount());

        Method failMethod = TransactionalService.class.getMethod("fail");

        try {
            transactionInterceptor.invoke(new SimpleMethodInvocation(transactionalService, failMethod));
        } catch (IllegalStateException exception) {
            System.out.println(exception.getMessage());
        }

        System.out.println("Transactions rolled back: " + transactionManager.getRollbackCount());
    }
}
```

---

## Dependencies

This example requires:

- Spring TX
- Spring Context
- AOP Alliance
- JUnit Jupiter

```xml
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-tx</artifactId>
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
  -Dexec.mainClass=com.springbyexample.transactionals.TransactionalApplication
```

Expected output:

```text
Hello from GreetingService!
Transactions started: 1
Transactions committed: 1
Something went wrong!
Transactions rolled back: 1
```

---

## Key Takeaways

1. `@Transactional` provides transaction metadata; it does not manage transactions itself.
2. `AnnotationTransactionAttributeSource` discovers transactional metadata from annotations.
3. `TransactionAttribute` represents the transaction configuration associated with a method.
4. `TransactionInterceptor` applies transaction management around method invocation.
5. `PlatformTransactionManager` performs the actual transaction operations.
6. A successful invocation results in a commit.
7. An appropriate exception can result in a rollback.
8. `TransactionInterceptor` delegates transaction operations rather than implementing them itself.
9. Calling a transactional method directly on an object created with `new` does not automatically apply transaction interception.
10. Spring normally connects the interceptor to method calls through AOP proxy infrastructure.
11. Proxy creation is intentionally covered separately in **How AOP Proxies Are Created**.

---

## Next

**Next: How AOP Proxies Are Created**

The next example will explain how Spring creates the proxy that allows an ordinary method call.