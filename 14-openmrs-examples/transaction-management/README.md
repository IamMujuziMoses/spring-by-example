# Transaction Management

This example demonstrates how **Spring transaction management** can be applied to an OpenMRS-style service layer.

The example builds a transactional service proxy using `TransactionProxyFactoryBean` and a `PlatformTransactionManager`. It demonstrates how a service invocation starts a transaction, commits when the method completes successfully, and rolls back when the method throws an exception.

Rather than connecting to a real database, the example uses a custom `RecordingTransactionManager` so the transaction lifecycle can be observed directly.

> **Note:** This is a simplified learning example inspired by the way OpenMRS integrates services with Spring. It does not reproduce the complete OpenMRS transaction infrastructure.

## Learning Objectives

By completing this example, you will understand:

- How Spring transaction management works around service methods.
- How `TransactionProxyFactoryBean` creates a transactional proxy.
- How `TransactionInterceptor` participates in transaction management.
- How `PlatformTransactionManager` controls transaction boundaries.
- What happens when a transactional method succeeds.
- What happens when a transactional method throws an exception.
- How transactions are committed and rolled back.
- How transaction management builds on Spring AOP.
- How transaction management can be applied to an OpenMRS-style service layer.

---

## What Is Transaction Management?

Transaction management controls a group of operations so that they behave as a single unit of work.

A transaction generally follows this lifecycle:

```text
Begin Transaction
       │
       ▼
Execute Business Logic
       │
       ├───────────────┐
       │               │
       ▼               ▼
   Success          Failure
       │               │
       ▼               ▼
    Commit          Rollback
```

If the operation succeeds, the transaction is committed.

If the operation fails, the transaction can be rolled back.

This is particularly important in applications that perform multiple related database operations.

---

## Why Is Transaction Management Important in OpenMRS?

OpenMRS contains a service-oriented architecture where business operations are exposed through services.

A service method may perform multiple persistence operations:

```text
Service Method
      │
      ├── Create entity
      ├── Update entity
      ├── Save relationship
      └── Update related data
```

These operations often need to participate in the same transaction.

Without transaction management, a failure halfway through the operation could leave partially persisted data.

With transaction management:

```text
Begin Transaction
      │
      ├── Operation 1
      ├── Operation 2
      ├── Operation 3
      │
      ▼
    Success
      │
      ▼
    Commit
```

If an operation fails:

```text
Begin Transaction
      │
      ├── Operation 1
      ├── Operation 2
      ├── Operation 3
      │       │
      │       ▼
      │    Exception
      │
      ▼
   Rollback
```

The transaction boundary therefore protects the consistency of the operation.

---


## 1. The Service Interface

The example starts with a simple service interface.

```java
public interface GreetingService {

    String saveGreeting();

    String fail();
}
```

The service contains two operations:

- `saveGreeting()` represents a successful operation.
- `fail()` deliberately throws an exception so that rollback behavior can be demonstrated.

The service itself does not contain any transaction-management code.

This is intentional.

Transaction management will be applied externally through a Spring proxy.

---

## 2. The Service Implementation

```java
public class GreetingServiceImpl implements GreetingService {

    @Override
    public String saveGreeting() {
        return "Greeting saved successfully!";
    }

    @Override
    public String fail() {
        throw new IllegalStateException("Something went wrong!");
    }
}
```

The implementation only contains business logic.

It does not explicitly call:

```java
transactionManager.begin();
transactionManager.commit();
transactionManager.rollback();
```

Instead, Spring handles the transaction boundary around the service.

This keeps transaction management separate from business logic.

---

## 3. The Transaction Manager

The example uses a custom `RecordingTransactionManager`.

```java
public class RecordingTransactionManager implements PlatformTransactionManager {

    private final AtomicInteger transactionCount = new AtomicInteger();
    
    private final AtomicInteger commitCount = new AtomicInteger();
    
    private final AtomicInteger rollbackCount = new AtomicInteger();

    @Override
    public TransactionStatus getTransaction(TransactionDefinition definition) {
        transactionCount.incrementAndGet();

        System.out.println("Transaction started");

        return new SimpleTransactionStatus();
    }

    @Override
    public void commit(TransactionStatus status) {
        commitCount.incrementAndGet();

        System.out.println("Transaction committed");
    }

    @Override
    public void rollback(TransactionStatus status) {
        rollbackCount.incrementAndGet();

        System.out.println("Transaction rolled back");
    }

    public int getTransactionCount() {
        return transactionCount.get();
    }

    public int getCommitCount() {
        return commitCount.get();
    }

    public int getRollbackCount() {
        return rollbackCount.get();
    }
}
```

### Why Use a Custom Transaction Manager?

A real application would normally connect the transaction manager to a persistence technology.

For example:

```text
Application
    │
    ▼
Transaction Manager
    │
    ▼
Database / Persistence Layer
```

For this learning example, a real database would add unnecessary complexity.

Instead, `RecordingTransactionManager` records:

- How many transactions started.
- How many transactions committed.
- How many transactions rolled back.

This makes the transaction lifecycle visible.

---

## 4. Creating the Transactional Proxy

The `TransactionalServiceContext` is responsible for exposing the service through a transactional proxy.

```java
public class TransactionalServiceContext {

    private final Map<Class<?>, Object> services = new ConcurrentHashMap<>();

    private final PlatformTransactionManager transactionManager;

    public TransactionalServiceContext(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public <T> void setService(Class<T> serviceType, T service) {
        TransactionProxyFactoryBean proxyFactory = new TransactionProxyFactoryBean();
        proxyFactory.setTarget(service);
        proxyFactory.setTransactionManager(transactionManager);

        Properties transactionAttributes = new Properties();
        transactionAttributes.setProperty("saveGreeting", "PROPAGATION_REQUIRED");
        transactionAttributes.setProperty("fail", "PROPAGATION_REQUIRED");

        proxyFactory.setTransactionAttributes(transactionAttributes);
        proxyFactory.afterPropertiesSet();

        try {
            services.put(serviceType, proxyFactory.getObject());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to create transactional service proxy", e);
        }
    }

    public <T> T getService(Class<T> serviceType) {
        Object service = services.get(serviceType);

        if (service == null) {
            throw new IllegalStateException("No service registered for " + serviceType.getName());
        }

        return serviceType.cast(service);
    }
}
```

The important part is:

```java
TransactionProxyFactoryBean proxyFactory = new TransactionProxyFactoryBean();
```

The target service is then configured:

```java
proxyFactory.setTarget(service);
```

The transaction manager is supplied:

```java
proxyFactory.setTransactionManager(transactionManager);
```

And transaction attributes are configured:

```java
Properties transactionAttributes = new Properties();

transactionAttributes.setProperty("saveGreeting","PROPAGATION_REQUIRED");
```

The resulting object is a transactional proxy.

---

## 5. The Transaction Proxy

Conceptually, the relationship looks like this:

```text
GreetingServiceImpl
        │
        │ target
        ▼
TransactionProxyFactoryBean
        │
        ▼
TransactionInterceptor
        │
        ▼
Transactional Proxy
        │
        ▼
GreetingService
```

The application receives the proxy rather than directly interacting with the implementation.

So when this code executes:

```java
service.saveGreeting();
```

the call effectively passes through the transaction infrastructure:

```text
service.saveGreeting()
        │
        ▼
Transactional Proxy
        │
        ▼
Begin Transaction
        │
        ▼
GreetingServiceImpl.saveGreeting()
        │
        ▼
Commit Transaction
```

If the service throws an exception:

```text
service.fail()
        │
        ▼
Transactional Proxy
        │
        ▼
Begin Transaction
        │
        ▼
GreetingServiceImpl.fail()
        │
        ▼
Exception
        │
        ▼
Rollback Transaction
```

This is the connection between transaction management and AOP.

---

## 6. `PROPAGATION_REQUIRED`

The example uses:

```text
PROPAGATION_REQUIRED
```

This means the operation should execute within a transaction.

If a transaction already exists, the operation can participate in it.

If there is no existing transaction, Spring creates one.

Conceptually:

```text
Existing Transaction?
       │
   ┌───┴───┐
  Yes      No
   │        │
   ▼        ▼
Join      Create
existing  transaction
transaction
```

This is one of the most common transaction propagation behaviors.

---

## 7. The Application

The application creates the transaction manager:

```java
RecordingTransactionManager transactionManager = new RecordingTransactionManager();
```

Then creates the service context:

```java
TransactionalServiceContext serviceContext = new TransactionalServiceContext(transactionManager);
```

The service is registered:

```java
serviceContext.setService(GreetingService.class,new GreetingServiceImpl());
```

The service is then retrieved:

```java
GreetingService service = serviceContext.getService(GreetingService.class);
```

At this point, `service` is the transactional proxy.

---

## 8. Successful Transaction

The application invokes:

```java
System.out.println(service.saveGreeting());
```

The transaction lifecycle is:

```text
service.saveGreeting()
        │
        ▼
Transaction started
        │
        ▼
GreetingServiceImpl.saveGreeting()
        │
        ▼
Greeting returned
        │
        ▼
Transaction committed
```

The output is:

```text
Transaction started
Greeting saved successfully!
Transaction committed
```

The transaction manager therefore records:

```text
Transactions started: 1
Transactions committed: 1
Transactions rolled back: 0
```

---

## 9. Failed Transaction

The application then invokes:

```java
try {
    service.fail();
} catch (IllegalStateException e) {
    System.out.println(e.getMessage());
}
```

The service deliberately throws:

```java
throw new IllegalStateException("Something went wrong!");
```

The transaction infrastructure detects the failure and rolls the transaction back.

The lifecycle becomes:

```text
service.fail()
      │
      ▼
Transaction started
      │
      ▼
GreetingServiceImpl.fail()
      │
      ▼
IllegalStateException
      │
      ▼
Transaction rolled back
```

The output includes:

```text
Transaction started
Transaction rolled back
Something went wrong!
```

---

## 10. Complete Application

The complete application looks like this:

```java
public class TransactionManagementApplication {

    public static void main(String[] args) {
        RecordingTransactionManager transactionManager = new RecordingTransactionManager();
        TransactionalServiceContext serviceContext = new TransactionalServiceContext(transactionManager);
        serviceContext.setService(GreetingService.class, new GreetingServiceImpl());

        GreetingService service = serviceContext.getService(GreetingService.class);

        System.out.println(service.saveGreeting());

        try {
            service.fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Transactions started: " + transactionManager.getTransactionCount());
        System.out.println("Transactions committed: " + transactionManager.getCommitCount());
        System.out.println("Transactions rolled back: " + transactionManager.getRollbackCount());
    }
}
```

Expected output:

```text
Transaction started
Greeting saved successfully!
Transaction committed
Transaction started
Transaction rolled back
Something went wrong!
Transactions started: 2
Transactions committed: 1
Transactions rolled back: 1
```

---


## 11. Relationship Between AOP and Transactions

Transaction management is closely related to the AOP example from this module.

Previously, the AOP example demonstrated:

```text
Service
   │
   ▼
AOP Proxy
   │
   ├── Logging Advice
   ├── Authorization Advice
   │
   ▼
Target Service
```

Transaction management adds another piece of advice:

```text
Service
   │
   ▼
AOP Proxy
   │
   ▼
Transaction Interceptor
   │
   ▼
Target Service
```

This allows transaction management to remain separate from business logic.

The service does not need to know how transactions are started, committed, or rolled back.

---

## 12. Relationship to OpenMRS

OpenMRS uses Spring as part of its service infrastructure.

Conceptually, the relationship can be viewed as:

```text
OpenMRS Service
       │
       ▼
Spring Service Infrastructure
       │
       ▼
AOP / Transaction Interceptors
       │
       ▼
Transaction Manager
       │
       ▼
Persistence Layer
```

This example simplifies that architecture to focus specifically on the transaction boundary.

It should therefore be viewed as an **OpenMRS-style learning example**, rather than a copy of the complete OpenMRS implementation.

---

## 13. Relationship to Previous Examples

This example builds on several concepts introduced earlier in the repository.

### Module 7 — Spring AOP

The AOP module introduced:

- Proxies
- Advice
- Method interception
- `MethodInterceptor`
- `ProxyFactory`

Transaction management uses the same general proxy/interceptor model.

### Module 13 — Spring Internals

The Spring Internals module demonstrated:

- How AOP proxies are created.
- How `TransactionInterceptor` works.
- How Spring infrastructure participates in method invocation.

This example applies those concepts to a service layer.

### Module 14 — ServiceContext

The previous `ServiceContext` example demonstrated how OpenMRS-style infrastructure can expose services.

This example extends that idea by exposing a service through a transactional proxy.

---

## 14. Dependencies

The example uses the following Spring modules:

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aop</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-tx</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-beans</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
</dependency>
```

`spring-context` is required because `TransactionProxyFactoryBean` depends on Spring Context infrastructure.

The test suite uses JUnit Jupiter.

---

## 15. Running the Example

From the project root:

```bash
mvn clean install
```

To run the application from the module:

```bash
mvn exec:java \
    -Dexec.mainClass=com.springbyexample.transactionmanagement.TransactionManagementApplication
```

Or run `TransactionManagementApplication` directly from your IDE.

---

## Key Takeaways

- Transaction management defines a boundary around service operations.
- Spring can apply transaction behavior through proxies and interceptors.
- `TransactionProxyFactoryBean` can create a transactional service proxy.
- `TransactionInterceptor` connects method invocation with transaction management.
- `PlatformTransactionManager` controls transaction operations.
- Successful operations result in a commit.
- Failed operations can result in a rollback.
- `PROPAGATION_REQUIRED` allows a method to participate in an existing transaction or create one when necessary.
- Transaction management keeps business logic separate from transaction infrastructure.
- OpenMRS service infrastructure integrates closely with Spring, making these concepts relevant to understanding its architecture.
- This example intentionally simplifies OpenMRS's real transaction infrastructure to make the Spring transaction lifecycle easier to understand.

---

## Next

The next example will explore **Custom Spring Profiles** in OpenMRS.

The example will demonstrate how Spring profiles can be used to activate different configurations depending on the runtime environment.

---

#### [Back To Top ⬆️](#transaction-management)