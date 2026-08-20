# `@Transactional`

Spring provides declarative transaction management through the `@Transactional` annotation.

This example demonstrates how to define a transaction boundary around a Spring-managed service method and how Spring uses a proxy to manage the transaction.

It also demonstrates how a successful method execution is committed and how a runtime exception causes the transaction to roll back.

---

## Learning Objectives

By the end of this example, you will understand:

- What a transaction is.
- Why transaction management is important.
- What `@Transactional` does.
- How Spring applies `@Transactional` through a proxy.
- How `@EnableTransactionManagement` enables annotation-driven transaction management.
- Why the target object must be managed by Spring.
- How a transaction is committed after successful execution.
- How a transaction is rolled back when a runtime exception occurs.
- How Spring's transaction management builds on the proxy concepts introduced in the Spring AOP module.

---

# What Is a Transaction?

A **transaction** is a group of database operations that should be treated as a single unit of work.

For example, an operation might involve:

```text
Create order
    ↓
Save order
    ↓
Save order items
    ↓
Update inventory
```

These operations should generally succeed together.

If something goes wrong halfway through:

```text
Create order
    ↓
Save order
    ↓
Update inventory
    ↓
ERROR
```

we may want all of the changes to be undone.

A transaction provides this behavior:

```text
Transaction
    ├── Operation 1
    ├── Operation 2
    ├── Operation 3
    └── Operation 4

        ↓

Success → COMMIT
Failure → ROLLBACK
```

---

# What Is `@Transactional`?

`@Transactional` is a Spring annotation used to define a transaction boundary around a method or class.

For example:

```java
@Transactional
public void saveGreeting(String message) {
    greetingRepository.save(message);
}
```

The annotation tells Spring that the method should execute within a transaction.

Conceptually:

```text
Caller
   ↓
Spring Proxy
   ↓
Begin transaction
   ↓
saveGreeting()
   ↓
Repository
   ↓
Commit transaction
```

If an exception occurs:

```text
Caller
   ↓
Spring Proxy
   ↓
Begin transaction
   ↓
saveGreeting()
   ↓
Repository
   ↓
Exception
   ↓
Rollback transaction
```

---

# Example

The example consists of four main components:

```text
AppConfig
    ↓
Spring configuration

GreetingRepository
    ↓
Database access

GreetingService
    ↓
Transactional service

Main
    ↓
Application entry point
```

The database used by the example is an embedded H2 database.

---

# Configuration

## `AppConfig`

```java
@Configuration
@ComponentScan
@EnableTransactionManagement
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema.sql")
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
```

There are three important parts of the configuration:

```java
@EnableTransactionManagement
```

```java
DataSource
```

and:

```java
PlatformTransactionManager
```

---

# `@EnableTransactionManagement`

```java
@EnableTransactionManagement
```

enables Spring's annotation-driven transaction management.

It allows Spring to detect:

```java
@Transactional
```

and create the infrastructure required to intercept transactional method calls.

Conceptually:

```text
@EnableTransactionManagement
            ↓
Spring transaction infrastructure
            ↓
@Transactional detected
            ↓
Transaction proxy/interceptor
            ↓
Transaction managed around method
```

Without transaction management being enabled, `@Transactional` would not provide the expected declarative transaction behavior in this configuration.

---

# `DataSource`

The example uses an embedded H2 database:

```java
@Bean
public DataSource dataSource() {
    return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript("classpath:schema.sql")
            .build();
}
```

The `DataSource` provides connections to the database.

The database is initialized using:

```text
src/main/resources/schema.sql
```

with:

```sql
CREATE TABLE greetings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    message VARCHAR(255)
);
```

---

# `JdbcTemplate`

The repository uses Spring's `JdbcTemplate` to execute SQL.

```java
@Bean
public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
}
```

The dependency flow is:

```text
DataSource
    ↓
JdbcTemplate
    ↓
GreetingRepository
```

---

# `PlatformTransactionManager`

Spring uses a transaction manager to coordinate transactions.

```java
@Bean
public PlatformTransactionManager transactionManager(DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
}
```

`DataSourceTransactionManager` manages transactions for JDBC operations performed through the configured `DataSource`.

Conceptually:

```text
@Transactional
      ↓
Transaction interceptor
      ↓
PlatformTransactionManager
      ↓
DataSource
      ↓
Database transaction
```

---

# GreetingRepository

The repository performs the actual database operation.

```java
@Repository
public class GreetingRepository {

    private final JdbcTemplate jdbcTemplate;

    public GreetingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(String message) {
        jdbcTemplate.update("INSERT INTO greetings(message) VALUES (?)", message);
    }

    public int count() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM greetings", Integer.class);
    }
}
```

The repository does not need to know how the transaction is managed.

It simply performs the database operation.

---

# GreetingService

The service defines the transaction boundary.

```java
@Service
public class GreetingService {

    private final GreetingRepository greetingRepository;

    public GreetingService(GreetingRepository greetingRepository) {
        this.greetingRepository = greetingRepository;
    }

    @Transactional
    public void saveGreeting(String message) {
        greetingRepository.save(message);
    }

    @Transactional
    public void saveGreetingAndFail(String message) {
        greetingRepository.save(message);

        throw new RuntimeException("Something went wrong");
    }
}
```

The important part is:

```java
@Transactional
```

Spring intercepts calls to these methods and manages the transaction around them.

---

# Successful Transaction

When the following method executes:

```java
@Transactional
public void saveGreeting(String message) {
    greetingRepository.save(message);
}
```

the conceptual flow is:

```text
Caller
   ↓
Spring AOP Proxy
   ↓
Begin transaction
   ↓
GreetingService.saveGreeting()
   ↓
GreetingRepository.save()
   ↓
INSERT
   ↓
Method completes successfully
   ↓
Commit
```

The inserted greeting remains in the database.

---

# Failed Transaction

Now consider:

```java
@Transactional
public void saveGreetingAndFail(String message) {
    greetingRepository.save(message);

    throw new RuntimeException("Something went wrong");
}
```

The database operation occurs before the exception:

```text
Begin transaction
      ↓
INSERT greeting
      ↓
RuntimeException
      ↓
Rollback
```

The transaction manager rolls back the transaction.

Therefore, the inserted greeting does not remain in the database.

---

# Running the Example

## `Main`

```java
public class Main {

    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingService greetingService = context.getBean(GreetingService.class);

            greetingService.saveGreeting("Hello, Spring!");
        }
    }
}
```

The service is retrieved from the Spring application context:

```java
GreetingService greetingService = context.getBean(GreetingService.class);
```

This is important because Spring must manage the service in order to apply the transactional proxy.

---

# Why Must the Bean Be Managed by Spring?

This works:

```java
GreetingService greetingService = context.getBean(GreetingService.class);
```

because Spring creates and manages the bean.

This does not automatically provide transaction management:

```java
GreetingService greetingService = new GreetingService(greetingRepository);
```

The manually created object is not wrapped by Spring's transaction proxy.

Conceptually:

```text
Spring-managed object:

Caller
  ↓
Proxy
  ↓
@Transactional
  ↓
Target


Manually created object:

Caller
  ↓
Target
```

This is one of the most important things to understand about declarative Spring transactions.

---

# The Transaction Proxy

Spring applies transaction management through proxy-based interception.

The application appears to call:

```java
greetingService.saveGreeting("Hello, Spring!");
```

but conceptually the call goes through a proxy:

```text
Caller
   ↓
Spring Transaction Proxy
   ↓
Transaction Interceptor
   ↓
Begin transaction
   ↓
GreetingService
   ↓
saveGreeting()
   ↓
Commit transaction
```

This connects directly to the concepts explored in **Module 7 — Spring AOP**.

---

# `@Transactional` and Spring AOP

There is a strong connection between the previous Spring AOP module and transaction management.

In Spring AOP, we learned:

```text
Caller
   ↓
Proxy
   ↓
Advice
   ↓
Target
```

With transactions, the same general proxy model is used:

```text
Caller
   ↓
Transaction Proxy
   ↓
Transaction Interceptor
   ↓
Target
```

The transaction interceptor is responsible for starting, committing, or rolling back the transaction.

This is one reason understanding Spring AOP makes Spring transaction management easier to understand.

---

# What Happens During a Successful Transaction?

Conceptually:

```text
1. Caller invokes transactional method
             ↓
2. Spring proxy intercepts invocation
             ↓
3. Transaction begins
             ↓
4. Target method executes
             ↓
5. Database operations execute
             ↓
6. Method completes successfully
             ↓
7. Transaction commits
```

---

# What Happens During a Failed Transaction?

Conceptually:

```text
1. Caller invokes transactional method
             ↓
2. Spring proxy intercepts invocation
             ↓
3. Transaction begins
             ↓
4. Target method executes
             ↓
5. Database operation executes
             ↓
6. Runtime exception is thrown
             ↓
7. Transaction is rolled back
             ↓
8. Exception is propagated to the caller
```

---

# Why Transactions Matter

Without a transaction, multiple database operations can leave the database in a partially updated state.

For example:

```text
Create order
      ↓
Success

Save order items
      ↓
Success

Update inventory
      ↓
Failure
```

Without a transaction, the earlier operations may already have been committed.

With a transaction:

```text
Begin transaction
      ↓
Create order
      ↓
Save order items
      ↓
Update inventory
      ↓
Failure
      ↓
ROLLBACK
```

The database can return to its previous consistent state.

---

# Declarative Transaction Management

`@Transactional` is an example of **declarative transaction management**.

The application declares the transaction boundary:

```java
@Transactional
public void saveGreeting(String message) {
    greetingRepository.save(message);
}
```

The application does not need to explicitly write:

```java
beginTransaction();
```

or:

```java
commit();
```

or:

```java
rollback();
```

Spring handles those operations through the transaction infrastructure.

This allows business logic to remain focused on the actual application behavior.

---

# Declarative vs Programmatic Transactions

This example uses declarative transaction management:

```java
@Transactional
public void saveGreeting(String message) {
    greetingRepository.save(message);
}
```

Programmatic transaction management explicitly controls the transaction in application code.

Conceptually:

```text
Declarative:

@Transactional
      ↓
Spring manages transaction
```

versus:

```text
Programmatic:

begin
  ↓
execute
  ↓
commit / rollback
```

Programmatic transaction management will be explored in a later example in this module.

---

# Important Considerations

## `@Transactional` Is Not a Database Transaction by Itself

The annotation does not directly communicate with the database.

Instead, Spring uses:

```text
@Transactional
      ↓
Transaction interceptor
      ↓
PlatformTransactionManager
      ↓
DataSource
      ↓
Database
```

The transaction manager is responsible for coordinating the actual transaction.

---

## Self-Invocation

Because Spring's declarative transaction management is proxy-based, calling a transactional method from another method on the same object can bypass the proxy.

For example:

```java
public void outerMethod() {
    innerMethod();
}

@Transactional
public void innerMethod() {
}
```

When `outerMethod()` calls `innerMethod()` directly, the call does not pass through the Spring proxy.

Conceptually:

```text
Caller
  ↓
Proxy
  ↓
outerMethod()
  ↓
innerMethod()
```

The second call occurs directly on the target object.

This means the `@Transactional` interception on `innerMethod()` may not be applied as expected.

This is an important consequence of Spring's proxy-based transaction model.

---

# Key Takeaways

- `@Transactional` provides declarative transaction management.
- `@EnableTransactionManagement` enables annotation-driven transaction management.
- Spring uses a transaction manager to coordinate transactions.
- `PlatformTransactionManager` provides the abstraction for transaction management.
- `DataSourceTransactionManager` manages transactions for JDBC access.
- Successful transactions are committed.
- Runtime exceptions normally cause transactional work to roll back.
- `@Transactional` is applied through Spring's proxy/interceptor infrastructure.
- The target object must be managed by Spring for declarative transaction management to work.
- Manually creating a transactional service bypasses Spring's proxy.
- Spring transaction management builds directly on the proxy concepts explored in Spring AOP.
- Self-invocation can bypass Spring's transactional proxy.
- `@Transactional` keeps transaction management separate from business logic.

---

# What's Next?

The next example explores:

**Transaction Propagation**

Transaction propagation determines how a transactional method behaves when it is called while another transaction is already active.

We'll explore concepts such as:

- `Propagation.REQUIRED`
- `Propagation.REQUIRES_NEW`
- Existing transaction participation
- Starting a new transaction

This will build on the transaction boundary introduced in this example.