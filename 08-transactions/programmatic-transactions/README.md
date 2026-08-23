# Programmatic Transactions

Spring provides more than one way to manage transactions.

The previous examples used declarative transaction management with:

```java
@Transactional
```

This example demonstrates **programmatic transaction management** using Spring's `TransactionTemplate`.

Instead of annotating a method and allowing Spring to determine the transaction boundary around the method, the application explicitly defines which code should execute inside a transaction.

---

## Learning Objectives

By the end of this example, you will understand:

- What programmatic transaction management is.
- How `TransactionTemplate` is used.
- How `TransactionTemplate` works with `PlatformTransactionManager`.
- How to execute code inside a transaction programmatically.
- How to explicitly mark a transaction for rollback.
- How exceptions affect a programmatic transaction.
- The difference between declarative and programmatic transaction management.
- Why `TransactionTemplate` is useful for explicit transaction boundaries.

---

# Declarative vs Programmatic Transactions

With declarative transaction management, a transaction is declared using:

```java
@Transactional
public void saveGreeting(String message) {
    greetingRepository.save(message);
}
```

Spring creates the transaction around the method automatically.

Conceptually:

```text
Method invocation
       ↓
Spring transaction interceptor
       ↓
Begin transaction
       ↓
Method executes
       ↓
Commit / Rollback
```

With programmatic transaction management, the application explicitly defines the transactional code:

```java
transactionTemplate.executeWithoutResult(status -> {greetingRepository.save(message);});
```

The flow becomes:

```text
Application code
       ↓
TransactionTemplate
       ↓
Begin transaction
       ↓
Execute callback
       ↓
Commit / Rollback
```

---

# Why Programmatic Transactions?

Declarative transactions are usually preferred because they keep transaction management separate from application logic.

However, there are situations where the application needs more direct control over transaction boundaries.

For example:

- A transaction boundary needs to be determined dynamically.
- Different parts of a method need different transaction behavior.
- The application needs to explicitly mark a transaction for rollback.
- Transaction management is required around a specific block of code rather than an entire method.
- A framework or infrastructure component needs direct transaction control.

Programmatic transactions provide that additional control.

---

# Example

The example consists of:

```text
AppConfig
    ↓
Spring configuration

GreetingRepository
    ↓
Database access

GreetingService
    ↓
Programmatic transactions

Main
    ↓
Demonstrates transaction behavior

GreetingServiceTest
    ↓
Verifies transaction outcomes
```

---

# Configuration

## `AppConfig`

```java
@Configuration
@ComponentScan
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("schema.sql").build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }
}
```

There are two important transaction-related beans:

```java
@Bean
public PlatformTransactionManager transactionManager(DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
}
```

and:

```java
@Bean
public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
    return new TransactionTemplate(transactionManager);
}
```

---

# `PlatformTransactionManager`

`PlatformTransactionManager` is Spring's central abstraction for transaction management.

```java
PlatformTransactionManager
```

provides the underlying transaction operations that Spring uses to:

- Begin transactions.
- Commit transactions.
- Roll back transactions.

For this JDBC example, the implementation is:

```java
DataSourceTransactionManager
```

which manages transactions for the configured `DataSource`.

Conceptually:

```text
PlatformTransactionManager
          ↓
DataSourceTransactionManager
          ↓
       DataSource
          ↓
       Database
```

---

# `TransactionTemplate`

`TransactionTemplate` provides a convenient programmatic API around the transaction manager.

The application can write:

```java
transactionTemplate.executeWithoutResult(status -> {greetingRepository.save(message);});
```

instead of manually calling:

```java
transactionManager.getTransaction(...)
transactionManager.commit(...)
transactionManager.rollback(...)
```

This makes `TransactionTemplate` a useful abstraction for programmatic transaction management.

---

# GreetingRepository

The repository uses `JdbcTemplate` to access the database.

```java
@Repository
public class GreetingRepository {

    private final JdbcTemplate jdbcTemplate;

    public GreetingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(String message) {
        jdbcTemplate.update("INSERT INTO greetings (message) VALUES (?)", message);
    }

    public int count() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM greetings", Integer.class);

        return count != null ? count : 0;
    }
}
```

The `count()` method allows the examples and tests to verify whether a transaction committed or rolled back.

---

# Successful Transaction

The simplest programmatic transaction uses:

```java
transactionTemplate.executeWithoutResult(...)
```

For example:

```java
public void saveGreeting(String message) {
    transactionTemplate.executeWithoutResult(status -> {greetingRepository.save(message);});
}
```

The code inside the callback executes within the transaction.

Conceptually:

```text
saveGreeting()
      ↓
TransactionTemplate
      ↓
Begin transaction
      ↓
save greeting
      ↓
Callback completes
      ↓
Commit
```

The inserted row remains in the database.

---

# Explicit Rollback

Programmatic transactions also provide access to the transaction status.

The callback receives:

```java
TransactionStatus status
```

This can be used to explicitly mark the transaction for rollback:

```java
public void saveGreetingAndRollback(String message) {
    transactionTemplate.executeWithoutResult(status -> {greetingRepository.save(message);

        status.setRollbackOnly();
    });
}
```

The database operation occurs:

```text
INSERT
  ↓
setRollbackOnly()
  ↓
Transaction ends
  ↓
Rollback
```

The inserted row therefore does not remain in the database.

---

# What Does `setRollbackOnly()` Mean?

The call:

```java
status.setRollbackOnly();
```

does not immediately perform the rollback.

Instead, it marks the current transaction as requiring rollback.

Conceptually:

```text
Transaction running
       ↓
setRollbackOnly()
       ↓
Transaction marked for rollback
       ↓
Callback completes
       ↓
Spring rolls transaction back
```

This is different from manually calling a rollback operation.

The transaction infrastructure remains responsible for completing the transaction.

---

# Rollback Because of an Exception

A transaction can also roll back when an exception is thrown.

```java
public void saveGreetingAndFail(String message) {
    transactionTemplate.executeWithoutResult(status -> {greetingRepository.save(message);

        throw new GreetingException("Something went wrong");
    });
}
```

The flow is:

```text
Begin transaction
       ↓
Insert greeting
       ↓
Exception thrown
       ↓
Transaction fails
       ↓
Rollback
```

The inserted greeting does not remain in the database.

---

# GreetingService

The complete service contains the three transaction scenarios:

```java
@Service
public class GreetingService {

    private final GreetingRepository greetingRepository;
    
    private final TransactionTemplate transactionTemplate;

    public GreetingService(GreetingRepository greetingRepository, TransactionTemplate transactionTemplate) {
        this.greetingRepository = greetingRepository;
        this.transactionTemplate = transactionTemplate;
    }

    public void saveGreeting(String message) {
        transactionTemplate.executeWithoutResult(status -> {greetingRepository.save(message);});
    }

    public void saveGreetingAndRollback(String message) {
        transactionTemplate.executeWithoutResult(status -> {greetingRepository.save(message);

            // Explicitly mark the transaction for rollback.
            status.setRollbackOnly();
        });
    }

    public void saveGreetingAndFail(String message) {
        transactionTemplate.executeWithoutResult(status -> {greetingRepository.save(message);

            throw new GreetingException("Something went wrong");
        });
    }

    public int count() {
        return greetingRepository.count();
    }
}
```

---

# Main

The `Main` class demonstrates the three scenarios.

```java
public class Main {

    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            GreetingService greetingService = context.getBean(GreetingService.class);

            System.out.println("=== Commit ===");

            greetingService.saveGreeting("Hello, Spring!");

            System.out.println("Greetings: " + greetingService.count());
            System.out.println();
            System.out.println("=== Explicit Rollback ===");

            greetingService.saveGreetingAndRollback(
                    "Hello, Rollback!"
            );

            // setRollbackOnly() prevents the inserted row from being committed.
            System.out.println("Greetings: " + greetingService.count());
            System.out.println();
            System.out.println("=== Exception Rollback ===");

            try {
                greetingService.saveGreetingAndFail("Hello, Failure!");
            } catch (GreetingException e) {
                System.out.println("Transaction failed: " + e.getMessage());
            }

            // The failed transaction does not commit its changes.
            System.out.println("Greetings: " + greetingService.count());
        }
    }
}
```

---

# Expected Output

Running the example produces:

```text
=== Commit ===
Greetings: 1

=== Explicit Rollback ===
Greetings: 1

=== Exception Rollback ===
Transaction failed: Something went wrong
Greetings: 1
```

The first transaction commits:

```text
Greetings: 1
```

The second transaction is explicitly marked for rollback, so the count remains:

```text
Greetings: 1
```

The third transaction throws an exception and rolls back, so the count remains:

```text
Greetings: 1
```

---

# TransactionTemplate Flow

The main API used by this example is:

```java
transactionTemplate.executeWithoutResult(status -> {
    // Transactional code
});
```

Conceptually:

```text
TransactionTemplate
        ↓
Start transaction
        ↓
Execute callback
        ↓
     ┌──┴──┐
     │     │
 Success  Failure
     │     │
 Commit  Rollback
```

This allows transaction boundaries to be explicitly defined around a particular block of code.

---

# Declarative vs Programmatic Transactions

The previous `@Transactional` example uses declarative transaction management:

```java
@Transactional
public void saveGreeting(String message) {
    greetingRepository.save(message);
}
```

The transaction boundary is associated with the method.

With `TransactionTemplate`:

```java
public void saveGreeting(String message) {
    transactionTemplate.executeWithoutResult(status -> {greetingRepository.save(message);});
}
```

the transaction boundary is explicitly defined in application code.

The comparison is:

| Declarative | Programmatic |
|---|---|
| `@Transactional` | `TransactionTemplate` |
| Transaction boundary declared with an annotation | Transaction boundary defined in code |
| Less transaction-management code | More explicit control |
| Usually preferred for service methods | Useful when explicit control is required |

---

# Why Use `TransactionTemplate`?

`TransactionTemplate` is useful when the application needs explicit control over the transactional block.

For example:

```java
transactionTemplate.executeWithoutResult(status -> {

    // Only this block executes inside the transaction.

});
```

This can be useful when the transaction boundary cannot be expressed cleanly as an annotation on an entire method.

It also provides access to the current transaction status:

```java
status.setRollbackOnly();
```

---

# `TransactionTemplate` vs `PlatformTransactionManager`

It is possible to use `PlatformTransactionManager` directly.

That approach requires manually managing transaction lifecycle operations such as:

```text
getTransaction()
commit()
rollback()
```

`TransactionTemplate` provides a higher-level abstraction over this process.

Conceptually:

```text
PlatformTransactionManager
          ↓
TransactionTemplate
          ↓
Application code
```

Instead of manually handling transaction completion, the application provides a callback and Spring manages the transaction lifecycle.

---

# Why Not Always Use Programmatic Transactions?

Programmatic transaction management gives more control, but that control comes with additional code.

Compare:

```java
@Transactional
public void saveGreeting(String message) {
    greetingRepository.save(message);
}
```

with:

```java
public void saveGreeting(String message) {
    transactionTemplate.executeWithoutResult(status -> {greetingRepository.save(message);});
}
```

The declarative version is simpler and keeps transaction management separate from the business logic.

For typical service-layer transactions, declarative transaction management is often easier to maintain.

Programmatic transactions are useful when the application genuinely needs explicit transaction boundaries or transaction status control.

---

# Important Considerations

## `TransactionTemplate` Does Not Create the Transaction Manager

`TransactionTemplate` depends on a:

```java
PlatformTransactionManager
```

For this example:

```java
DataSourceTransactionManager
```

is configured as the transaction manager.

The relationship is:

```text
DataSource
    ↓
DataSourceTransactionManager
    ↓
TransactionTemplate
    ↓
GreetingService
```

---

## `setRollbackOnly()` Does Not Immediately Roll Back

This:

```java
status.setRollbackOnly();
```

marks the transaction for rollback.

It does not immediately execute the rollback.

Spring completes the transaction when the callback finishes.

---

## Exceptions Still Propagate

When:

```java
throw new GreetingException("Something went wrong");
```

is executed inside the callback, the exception is propagated to the caller.

The transaction is also rolled back.

Therefore, the application can handle the exception normally:

```java
try {
    greetingService.saveGreetingAndFail("Hello, Failure!");
} catch (GreetingException e) {
    System.out.println(e.getMessage());
}
```

---

# Verification

Run:

```bash
mvn clean install
```

The example should build successfully and all transaction tests should pass.

---

# Key Takeaways

- Spring supports both declarative and programmatic transaction management.
- `@Transactional` provides declarative transaction management.
- `TransactionTemplate` provides a convenient programmatic transaction API.
- `TransactionTemplate` uses a `PlatformTransactionManager` underneath.
- `DataSourceTransactionManager` manages transactions for the JDBC `DataSource`.
- `executeWithoutResult()` executes a callback inside a transaction.
- `TransactionStatus` can be used to mark a transaction for rollback.
- `setRollbackOnly()` marks the current transaction for rollback.
- An exception thrown inside the transaction can cause the transaction to roll back.
- Programmatic transactions provide more explicit control but generally require more code.
- Declarative transactions are often preferable for straightforward service-layer transactions.
- `TransactionTemplate` is useful when transaction boundaries need to be controlled explicitly.

---

# Dependencies

This example uses Spring JDBC, Spring TX, H2, and JUnit 5.

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
    <version>${spring.version}</version>
</dependency>

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-tx</artifactId>
    <version>${spring.version}</version>
</dependency>

<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>2.3.232</version>
    <scope>runtime</scope>
</dependency>
```

---

# What's Next?

The final example in this module explores:

**TransactionProxyFactoryBean**

This will demonstrate Spring's older proxy-based approach to declarative transaction management.
