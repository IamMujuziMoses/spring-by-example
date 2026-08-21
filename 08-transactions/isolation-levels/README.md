# Transaction Isolation Levels

Spring provides transaction isolation levels to control how transactions interact with data modified by other concurrent transactions.

This example demonstrates how to configure transaction isolation using Spring's `@Transactional` annotation.

It builds on the previous transaction examples by showing that **transaction propagation** and **transaction isolation** solve different problems:

- Propagation determines how a method participates in an existing transaction.
- Isolation determines how one transaction can see changes made by other concurrent transactions.

---

## Learning Objectives

By the end of this example, you will understand:

- What transaction isolation means.
- Why transaction isolation is important.
- How to configure isolation with `@Transactional`.
- The four standard isolation levels supported by Spring.
- What dirty reads are.
- What non-repeatable reads are.
- What phantom reads are.
- The trade-off between consistency and concurrency.
- The difference between transaction propagation and isolation.
- Why isolation behavior can depend on the underlying database.
- Why sequential tests alone cannot demonstrate concurrency-related isolation behavior.

---

# What Is Transaction Isolation?

Transaction isolation describes how much one transaction is separated from other transactions that are executing at the same time.

Consider two transactions:

```text
Transaction A                 Transaction B
     │                              │
     │ Read balance                 │
     │                              │
     │                         Update balance
     │                              │
     │ Read balance again           │
     │                              │
```

Depending on the isolation level, Transaction A may or may not see changes made by Transaction B.

The purpose of isolation is to control these interactions and provide predictable behavior when multiple transactions access the same data concurrently.

---

# Why Does Transaction Isolation Matter?

Without transaction isolation, concurrent transactions can interfere with each other in ways that produce unexpected results.

For example:

```text
Initial balance: $1000

Transaction A:
    Reads $1000

Transaction B:
    Changes balance to $1500

Transaction A:
    Reads the balance again
```

Should Transaction A see:

```text
$1000
```

or:

```text
$1500
```

The answer depends on the isolation level and the database being used.

Isolation levels provide a way to define these guarantees.

---

# The Four Isolation Levels

Spring exposes the standard isolation levels through the `Isolation` enum.

```java
Isolation.READ_UNCOMMITTED
Isolation.READ_COMMITTED
Isolation.REPEATABLE_READ
Isolation.SERIALIZABLE
```

They can be thought of as progressively stronger isolation:

```text
READ_UNCOMMITTED
       ↓
READ_COMMITTED
       ↓
REPEATABLE_READ
       ↓
SERIALIZABLE
```

Higher isolation generally provides stronger consistency guarantees, but may reduce concurrency and increase locking.

---

# Isolation Level Comparison

| Isolation Level | Dirty Reads | Non-Repeatable Reads | Phantom Reads |
|---|---|---|---|
| `READ_UNCOMMITTED` | Possible | Possible | Possible |
| `READ_COMMITTED` | Prevented | Possible | Possible |
| `REPEATABLE_READ` | Prevented | Prevented | Database-dependent |
| `SERIALIZABLE` | Prevented | Prevented | Prevented |

The exact behavior can vary between database systems, so isolation semantics should always be considered together with the database being used.

---

# Dirty Reads

A **dirty read** occurs when one transaction reads data that another transaction has changed but has not committed yet.

For example:

```text
Transaction A
    │
    │ UPDATE balance = 1500
    │
    │ Not committed yet
    │
    └───────────────┐
                    │
Transaction B       │
    │               │
    └── READ balance
            │
            ↓
          1500
```

If Transaction A later rolls back:

```text
Transaction A
    UPDATE balance = 1500
    ROLLBACK
```

Transaction B has read a value that was never actually committed.

That is a dirty read.

`READ_UNCOMMITTED` allows dirty reads.

The other standard isolation levels prevent them.

---

# Non-Repeatable Reads

A **non-repeatable read** occurs when the same transaction reads the same row twice and gets different values because another transaction committed a change between the reads.

For example:

```text
Transaction A
    │
    │ SELECT balance
    │ → $1000
    │
    │
    │ SELECT balance
    │ → $1500
    │
```

Meanwhile:

```text
Transaction B
    │
    └── UPDATE balance = $1500
        COMMIT
```

Transaction A read two different values for the same row during one transaction.

That is a non-repeatable read.

`READ_COMMITTED` prevents dirty reads but does not necessarily prevent non-repeatable reads.

---

# Phantom Reads

A **phantom read** occurs when a transaction executes the same query more than once and sees a different set of rows because another transaction inserted or deleted matching rows.

For example:

```sql
SELECT * FROM accounts WHERE balance > 1000;
```

The first execution might return:

```text
Account 1
Account 2
```

Another transaction inserts:

```text
Account 3
```

The first transaction executes the query again:

```text
Account 1
Account 2
Account 3
```

The additional row is the "phantom."

`SERIALIZABLE` provides the strongest protection against phantom reads.

---

# Example

The example uses a simple `accounts` table.

```text
Account
    │
    ├── id
    ├── owner
    └── balance
```

The application contains:

```text
AppConfig
    ↓
Database configuration

AccountRepository
    ↓
Database access

AccountService
    ↓
Transactional operations

Main
    ↓
Application entry point
```

---

# AccountRepository

The repository uses `JdbcTemplate` to interact with the database.

```java
@Repository
public class AccountRepository {

    private final JdbcTemplate jdbcTemplate;

    public AccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BigDecimal findBalance(Long accountId) {
        return jdbcTemplate.queryForObject("SELECT balance FROM accounts WHERE id = ?", BigDecimal.class, accountId);
    }

    public void updateBalance(Long accountId, BigDecimal balance) {
        jdbcTemplate.update("UPDATE accounts SET balance = ? WHERE id = ?", balance, accountId);
    }
}
```

The repository is intentionally unaware of transaction isolation.

Transaction configuration belongs to the service layer.

---

# AccountService

The service demonstrates different isolation levels.

```java
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BigDecimal readWithReadCommitted(Long accountId) {
        return accountRepository.findBalance(accountId);
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public BigDecimal readWithReadUncommitted(Long accountId) {
        return accountRepository.findBalance(accountId);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public BigDecimal readWithRepeatableRead(Long accountId) {
        return accountRepository.findBalance(accountId);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public BigDecimal readWithSerializable(Long accountId) {
        return accountRepository.findBalance(accountId);
    }

    @Transactional
    public void updateBalance(Long accountId, BigDecimal balance) {
        accountRepository.updateBalance(accountId, balance);
    }
}
```

The important part is:

```java
@Transactional(isolation = Isolation.READ_COMMITTED)
```

The `isolation` attribute specifies the isolation level for the transaction.

---

# `READ_UNCOMMITTED`

```java
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
public BigDecimal readWithReadUncommitted(Long accountId) {
    return accountRepository.findBalance(accountId);
}
```

`READ_UNCOMMITTED` provides the weakest isolation.

A transaction may be able to see changes made by another transaction before those changes are committed.

This means dirty reads are possible.

It can provide greater concurrency, but at the cost of weaker consistency guarantees.

---

# `READ_COMMITTED`

```java
@Transactional(isolation = Isolation.READ_COMMITTED)
public BigDecimal readWithReadCommitted(Long accountId) {
    return accountRepository.findBalance(accountId);
}
```

`READ_COMMITTED` prevents a transaction from reading uncommitted changes from another transaction.

This means dirty reads are prevented.

However, another transaction may commit a change between two reads, meaning non-repeatable reads can still occur.

---

# `REPEATABLE_READ`

```java
@Transactional(isolation = Isolation.REPEATABLE_READ)
public BigDecimal readWithRepeatableRead(Long accountId) {
    return accountRepository.findBalance(accountId);
}
```

`REPEATABLE_READ` provides stronger guarantees.

Once a transaction reads a row, subsequent reads of that row should provide a consistent result according to the database's isolation implementation.

This prevents non-repeatable reads.

The exact handling of phantom reads depends on the database.

---

# `SERIALIZABLE`

```java
@Transactional(isolation = Isolation.SERIALIZABLE)
public BigDecimal readWithSerializable(Long accountId) {
    return accountRepository.findBalance(accountId);
}
```

`SERIALIZABLE` provides the strongest standard isolation level.

It attempts to make concurrent transactions behave as though they were executed sequentially.

This provides the strongest consistency guarantees but can reduce concurrency and increase locking or transaction contention.

---

# Configuration

The application uses an embedded H2 database.

```java
@Configuration
@ComponentScan
@EnableTransactionManagement
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
}
```

The important configuration is:

```java
@EnableTransactionManagement
```

This enables Spring's annotation-driven transaction management.

---

# Database Schema

The example uses a simple `accounts` table.

```sql
CREATE TABLE accounts ( id BIGINT PRIMARY KEY,
    owner VARCHAR(255) NOT NULL,
    balance DECIMAL(10, 2) NOT NULL
);

INSERT INTO accounts (id, owner, balance)
VALUES (1, 'Spring', 1000.00);
```

The initial account balance is:

```text
$1000.00
```

---

# Running the Example

The application can retrieve the `AccountService` from the Spring context and perform transactional operations.

```java
public class Main {

    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            AccountService accountService = context.getBean(AccountService.class);

            System.out.println("Initial balance: " + accountService.readWithReadCommitted(1L));

            accountService.updateBalance(1L, new BigDecimal("1500.00"));

            System.out.println("Updated balance: " + accountService.readWithReadCommitted(1L));
        }
    }
}
```

Expected output:

```text
Initial balance: 1000.00
Updated balance: 1500.00
```

---

# Why the Main Example Does Not Demonstrate Dirty Reads

It is important to distinguish between **configuring** an isolation level and **observing** its concurrency behavior.

Calling:

```java
accountService.readWithReadCommitted(1L);
```

and then:

```java
accountService.updateBalance(1L, ...);
```

sequentially does not demonstrate a dirty read.

Dirty reads, non-repeatable reads, and phantom reads are concurrency phenomena.

They require transactions that overlap in time.

Conceptually:

```text
Transaction A
      │
      ├── Read
      │
      │       Transaction B
      │              │
      │              ├── Update
      │              └── Commit
      │
      └── Read again
```

The isolation level determines what Transaction A can observe from Transaction B.

This is why the example focuses on the configuration and semantics of isolation levels rather than pretending that a simple sequential test demonstrates concurrent behavior.

---

# Isolation vs Propagation

Isolation and propagation are related but solve different problems.

## Propagation

Propagation answers:

> What should happen when a transactional method is called while another transaction already exists?

For example:

```java
@Transactional(propagation = Propagation.REQUIRED)
```

or:

```java
@Transactional(propagation = Propagation.REQUIRES_NEW)
```

Propagation controls how transactions are joined, suspended, or created.

---

## Isolation

Isolation answers:

> How should this transaction interact with other concurrent transactions?

For example:

```java
@Transactional(isolation = Isolation.READ_COMMITTED)
```

Isolation controls the visibility and consistency of concurrent database operations.

The distinction can be summarized as:

```text
Propagation
    ↓
Which transaction should this method participate in?

Isolation
    ↓
How isolated should that transaction be from other transactions?
```

---

# Spring's `Isolation` Enum

Spring provides these isolation levels:

```java
Isolation.DEFAULT
Isolation.READ_UNCOMMITTED
Isolation.READ_COMMITTED
Isolation.REPEATABLE_READ
Isolation.SERIALIZABLE
```

`DEFAULT` is special.

It means that Spring should use the database's default isolation level.

For example:

```java
@Transactional(isolation = Isolation.DEFAULT)
```

This does not force a specific isolation level.

Instead, the database determines the default.

---

# Why `DEFAULT` Is Often Appropriate

Database systems can have different default isolation levels and different implementations of the standard isolation levels.

Using:

```java
Isolation.DEFAULT
```

allows the application to use the database's configured default.

When an application requires a specific isolation guarantee, an explicit isolation level can be used.

For example:

```java
@Transactional(isolation = Isolation.SERIALIZABLE)
```

However, stronger isolation should not automatically be considered better.

---

# The Performance Trade-Off

Higher isolation generally means stronger consistency.

But stronger isolation can also mean:

- More locking.
- More contention.
- Lower concurrency.
- Longer-running transactions.
- Increased chances of transaction conflicts.

Conceptually:

```text
More concurrency
      ↑
      │
READ_UNCOMMITTED
      │
READ_COMMITTED
      │
REPEATABLE_READ
      │
SERIALIZABLE
      │
      ↓
Stronger isolation
```

The correct isolation level depends on the consistency requirements of the application.

---

# Why Isolation Tests Can Be Difficult

Isolation behavior is inherently dependent on concurrent execution.

A realistic isolation test may require:

```text
Thread 1
    ↓
Transaction A

Thread 2
    ↓
Transaction B
```

Both transactions need to overlap at carefully controlled points.

This can require:

- Multiple threads.
- Synchronization primitives.
- Transaction boundaries that remain open.
- Database-specific behavior.
- Careful cleanup between tests.

Therefore, this example does not attempt to create a fragile concurrency test simply to produce a particular result.

Instead, it establishes the Spring configuration and the conceptual behavior of each isolation level.

---

# Spring AOP Connection

Just like the previous Spring AOP examples, `@Transactional` is applied through Spring infrastructure.

The application calls:

```java
accountService.updateBalance(...);
```

Conceptually, the call goes through a proxy:

```text
Caller
  ↓
Spring Proxy
  ↓
Transaction Interceptor
  ↓
Begin Transaction
  ↓
AccountService
  ↓
AccountRepository
  ↓
Database
  ↓
Commit / Rollback
```

This connects the transaction module with the proxy and AOP concepts explored previously.

---

# Important Considerations

## Isolation Is a Database Concern

Spring provides a consistent API for configuring isolation:

```java
@Transactional(isolation = Isolation.READ_COMMITTED)
```

But the actual behavior is implemented by the underlying transaction manager and database.

Different databases can have different locking and concurrency implementations.

---

## Stronger Isolation Is Not Always Better

It may be tempting to use:

```java
Isolation.SERIALIZABLE
```

everywhere because it provides the strongest guarantees.

That can unnecessarily reduce concurrency.

The appropriate isolation level should be selected based on the application's consistency requirements.

---

## Isolation Only Applies to Transactions

The isolation setting is meaningful within a transaction.

For example:

```java
@Transactional(isolation = Isolation.READ_COMMITTED)
public BigDecimal readBalance(Long accountId) {
    return accountRepository.findBalance(accountId);
}
```

Spring starts and manages the transaction around the method invocation.

---

## Self-Invocation Still Matters

Spring's annotation-driven transaction management is proxy-based.

Therefore, a method calling another transactional method on the same object can bypass the proxy:

```java
public void outer() {
    inner();
}

@Transactional
public void inner() {
}
```

The call:

```java
inner();
```

is an internal method call and does not pass through the Spring proxy.

This is another reason understanding Spring's proxy model is important.

---

# Key Takeaways

- Transaction isolation controls how concurrent transactions interact with each other.
- Spring configures isolation through the `isolation` attribute of `@Transactional`.
- `READ_UNCOMMITTED` provides the weakest standard isolation.
- `READ_COMMITTED` prevents dirty reads.
- `REPEATABLE_READ` also prevents non-repeatable reads.
- `SERIALIZABLE` provides the strongest standard isolation.
- Higher isolation can reduce concurrency and increase contention.
- Isolation and propagation solve different problems.
- `Isolation.DEFAULT` uses the database's default isolation level.
- Dirty reads, non-repeatable reads, and phantom reads require concurrent transactions to observe.
- A simple sequential test does not prove the behavior of an isolation level.
- Actual isolation semantics can depend on the database.
- Spring applies transactional behavior through its proxy-based infrastructure.

---

# Dependencies

This example requires Spring JDBC, Spring TX, and H2.

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
    <version>${h2.version}</version>
</dependency>
```

The H2 dependency is required because the example uses an embedded H2 database for demonstrating the transaction infrastructure.

---

# What's Next?

The next example explores:

**Rollback Rules**

It will demonstrate how Spring determines whether a transaction should be rolled back when a method throws an exception.