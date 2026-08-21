# Rollback Rules

Spring's transaction management provides default rules for deciding whether a transaction should be rolled back when an exception is thrown.

This example demonstrates Spring's default rollback behavior and how `rollbackFor` and `noRollbackFor` can be used to customize those rules.

It builds on the previous `@Transactional` example by showing that **not every exception causes a transaction to roll back**.

---

## Learning Objectives

By the end of this example, you will understand:

- How Spring determines whether a transaction should roll back.
- The default rollback behavior for `RuntimeException`.
- The default behavior for checked exceptions.
- How to use `rollbackFor`.
- How to use `noRollbackFor`.
- How rollback rules can override Spring's default behavior.
- Why throwing an exception does not necessarily mean that a transaction will roll back.

---

# Default Rollback Behavior

When using:

```java
@Transactional
```

Spring applies default rollback rules.

By default:

```text
RuntimeException
       ↓
   Rollback
```

and:

```text
Checked Exception
       ↓
     Commit
```

For example:

```java
@Transactional
public void saveGreeting(String message) {

    greetingRepository.save(message);

    throw new RuntimeException("Something went wrong");
}
```

The `RuntimeException` causes the transaction to roll back.

However, a checked exception behaves differently:

```java
@Transactional
public void saveGreeting(String message) throws CheckedGreetingException {
    greetingRepository.save(message);

    throw new CheckedGreetingException("Something went wrong");
}
```

By default, the transaction is committed even though the checked exception is propagated to the caller.

---

# Why Do Rollback Rules Exist?

Applications sometimes need transaction behavior that differs from Spring's defaults.

For example, an application may want a checked exception to cause a rollback:

```text
Checked Exception
       ↓
  rollbackFor
       ↓
    Rollback
```

Or it may want a particular runtime exception not to cause a rollback:

```text
RuntimeException
       ↓
 noRollbackFor
       ↓
     Commit
```

Spring provides two commonly used attributes for this:

```java
rollbackFor
```

and:

```java
noRollbackFor
```

---

# Example

The example contains:

```text
AppConfig
    ↓
Spring configuration

GreetingRepository
    ↓
Database access

GreetingService
    ↓
Transactional operations

Main
    ↓
Demonstrates rollback behavior

GreetingServiceTest
    ↓
Verifies transaction outcomes
```

The service contains four operations demonstrating different rollback rules:

```text
saveWithRuntimeException()
        ↓
RuntimeException
        ↓
Rollback by default

saveWithCheckedException()
        ↓
Checked exception
        ↓
Commit by default

saveWithRollbackFor()
        ↓
Checked exception
        ↓
Rollback explicitly configured

saveWithNoRollbackFor()
        ↓
RuntimeException
        ↓
Commit explicitly configured
```

---

# GreetingRepository

The repository uses Spring's `JdbcTemplate` to persist greetings.

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

The `count()` method is useful for verifying whether a transaction committed or rolled back.

For example:

```text
Transaction commits
       ↓
INSERT remains
       ↓
count() = 1
```

Whereas:

```text
Transaction rolls back
       ↓
INSERT is undone
       ↓
count() = 0
```

---

# Runtime Exceptions

The first example relies on Spring's default rollback behavior.

```java
@Transactional
public void saveWithRuntimeException(String message) {
    greetingRepository.save(message);

    throw new UncheckedGreetingException("Something went wrong");
}
```

The database operation happens first:

```java
greetingRepository.save(message);
```

Then a runtime exception is thrown:

```java
throw new UncheckedGreetingException(
        "Something went wrong"
);
```

Because `UncheckedGreetingException` extends `RuntimeException`, Spring rolls the transaction back.

The result is:

```text
INSERT
  ↓
RuntimeException
  ↓
Rollback
  ↓
Row removed
```

Therefore:

```java
greetingService.count()
```

returns:

```text
0
```

---

# Checked Exceptions

The second example demonstrates Spring's default behavior for checked exceptions.

```java
@Transactional
public void saveWithCheckedException(String message) throws CheckedGreetingException {
    greetingRepository.save(message);

    throw new CheckedGreetingException("Something went wrong");
}
```

`CheckedGreetingException` extends `Exception`, not `RuntimeException`.

Therefore, Spring does **not** roll back the transaction by default.

The result is:

```text
INSERT
  ↓
Checked Exception
  ↓
Commit
  ↓
Row remains
```

The exception is still propagated to the caller.

For example:

```java
assertThrows(CheckedGreetingException.class,() -> greetingService.saveWithCheckedException("Checked greeting"));
```

But the row remains in the database:

```java
assertEquals(1, greetingService.count());
```

This is an important distinction:

> An exception being thrown does not automatically mean that the transaction is rolled back.

---

# `rollbackFor`

Spring allows the default rollback rules to be customized using:

```java
rollbackFor
```

For example:

```java
@Transactional(rollbackFor = CheckedGreetingException.class)
public void saveWithRollbackFor(String message) throws CheckedGreetingException {
    greetingRepository.save(message);

    throw new CheckedGreetingException("Something went wrong");
}
```

This explicitly tells Spring:

```text
CheckedGreetingException
        ↓
     Rollback
```

The database operation therefore does not remain committed.

The final count is:

```text
0
```

---

# Why Use `rollbackFor`?

`rollbackFor` is useful when a checked exception represents a failure that should invalidate the transaction.

For example:

```java
@Transactional(rollbackFor = CheckedGreetingException.class)
```

changes the behavior from:

```text
Checked Exception
       ↓
Commit
```

to:

```text
Checked Exception
       ↓
Rollback
```

This allows the transaction policy to match the application's business requirements.

---

# `noRollbackFor`

Spring also provides the opposite behavior:

```java
noRollbackFor
```

For example:

```java
@Transactional(noRollbackFor = UncheckedGreetingException.class)
public void saveWithNoRollbackFor(String message) {
    greetingRepository.save(message);

    throw new UncheckedGreetingException("Something went wrong");
}
```

Normally:

```text
RuntimeException
       ↓
Rollback
```

But the configured rule changes that:

```text
RuntimeException
       ↓
noRollbackFor
       ↓
Commit
```

The inserted row therefore remains in the database.

The final count is:

```text
1
```

---

# Important: `noRollbackFor` Does Not Suppress the Exception

A common misunderstanding is that:

```java
noRollbackFor = UncheckedGreetingException.class
```

means the exception will be ignored.

It does not.

The exception is still thrown:

```java
throw new UncheckedGreetingException("Something went wrong");
```

The caller can still catch it:

```java
try {
    greetingService.saveWithNoRollbackFor("Hello, Spring!");
} catch (RuntimeException e) {
    System.out.println("Transaction failed: " + e.getMessage());
}
```

The difference is that Spring does not roll back the transaction because of that exception.

Therefore:

```text
Exception
   ↓
Propagates to caller
   +
Transaction commits
```

---

# Comparing the Four Cases

| Method | Exception | Configuration | Result |
|---|---|---|---|
| `saveWithRuntimeException()` | Runtime | Default | Rollback |
| `saveWithCheckedException()` | Checked | Default | Commit |
| `saveWithRollbackFor()` | Checked | `rollbackFor` | Rollback |
| `saveWithNoRollbackFor()` | Runtime | `noRollbackFor` | Commit |

This gives us four different transaction outcomes:

```text
                    @Transactional
                          │
             ┌────────────┴────────────┐
             │                         │
       RuntimeException        Checked Exception
             │                         │
        Rollback                  Commit
        by default               by default
             │                         │
      noRollbackFor              rollbackFor
             │                         │
          Commit                  Rollback
```

---

# Main

The `Main` class demonstrates all four scenarios.

```java
public class Main {

    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingService greetingService = context.getBean(GreetingService.class);

            System.out.println("=== Runtime Exception ===");

            try {
                greetingService.saveWithRuntimeException("Runtime greeting");
            } catch (RuntimeException e) {
                System.out.println("Transaction failed: " + e.getMessage());
            }

            // Runtime exceptions roll back by default.
            System.out.println("Greetings: " + greetingService.count());
            System.out.println();
            System.out.println("=== Checked Exception ===");

            try {
                greetingService.saveWithCheckedException("Checked greeting");
            } catch (CheckedGreetingException e) {
                System.out.println("Transaction failed: " + e.getMessage());
            }

            // Checked exceptions do not roll back by default.
            System.out.println("Greetings: " + greetingService.count());
            System.out.println();
            System.out.println("=== rollbackFor ===");

            try {
                greetingService.saveWithRollbackFor("Rollback greeting");
            } catch (CheckedGreetingException e) {
                System.out.println("Transaction failed: " + e.getMessage());
            }

            // rollbackFor explicitly requests a rollback.
            System.out.println("Greetings: " + greetingService.count());
            System.out.println();
            System.out.println("=== noRollbackFor ===");

            try {
                greetingService.saveWithNoRollbackFor("No rollback greeting");
            } catch (RuntimeException e) {
                System.out.println("Transaction failed: " + e.getMessage());
            }

            // noRollbackFor overrides the default rollback behavior.
            System.out.println("Greetings: " + greetingService.count());
        }
    }
}
```

---

# Expected Output

Running the example produces:

```text
=== Runtime Exception ===
Transaction failed: Something went wrong
Greetings: 0

=== Checked Exception ===
Transaction failed: Something went wrong
Greetings: 1

=== rollbackFor ===
Transaction failed: Something went wrong
Greetings: 1

=== noRollbackFor ===
Transaction failed: Something went wrong
Greetings: 2
```

The important values are:

```text
RuntimeException
Greetings: 0
```

The transaction was rolled back.

Then:

```text
Checked Exception
Greetings: 1
```

The transaction was committed despite the checked exception.

Then:

```text
rollbackFor
Greetings: 1
```

The second insert was rolled back, so the count remains `1`.

Finally:

```text
noRollbackFor
Greetings: 2
```

The runtime exception did not cause a rollback, so the new row remains.

---

# How Spring Applies the Rollback Rule

When a transactional method throws an exception, Spring's transaction interceptor determines whether the transaction should be rolled back.

Conceptually:

```text
@Transactional method
        ↓
Target method executes
        ↓
Exception thrown
        ↓
Spring checks rollback rules
        ↓
┌───────────────────────────────┐
│ RuntimeException / Error?     │
│ Checked exception?            │
│ rollbackFor configured?       │
│ noRollbackFor configured?     │
└───────────────────────────────┘
        ↓
Commit or Rollback
```

The rollback decision is therefore part of Spring's transaction infrastructure rather than something that the application needs to implement manually.

---

# `rollbackFor` and `noRollbackFor`

The two attributes customize the transaction's rollback policy.

### `rollbackFor`

```java
@Transactional(rollbackFor = CheckedGreetingException.class)
```

Means:

> Roll back when this exception is thrown.

### `noRollbackFor`

```java
@Transactional(noRollbackFor = UncheckedGreetingException.class)
```

Means:

> Do not roll back when this exception is thrown.

These rules are useful when Spring's default behavior does not match the application's requirements.

---

# Why Rollback Rules Matter

Consider an operation that performs multiple database changes:

```text
Operation
    ↓
Save greeting
    ↓
Save audit record
    ↓
Update another record
    ↓
Exception
```

If the exception represents a failure that makes the entire operation invalid, a rollback may be appropriate:

```text
Exception
    ↓
Rollback
    ↓
All changes undone
```

However, some exceptions may represent conditions where the database changes should remain committed:

```text
Operation
    ↓
Save greeting
    ↓
Expected exception
    ↓
Commit
```

Rollback rules allow the transaction policy to reflect that distinction.

---

# Important Considerations

## Runtime Exceptions Roll Back by Default

Spring's default behavior is to roll back for:

```text
RuntimeException
Error
```

For example:

```java
throw new UncheckedGreetingException();
```

will normally cause a rollback.

---

## Checked Exceptions Do Not Roll Back by Default

A checked exception such as:

```java
public class CheckedGreetingException extends Exception {
}
```

does not cause a rollback by default.

If the transaction should roll back, explicitly configure:

```java
@Transactional(rollbackFor = CheckedGreetingException.class)
```

---

## Exceptions Can Still Propagate After Commit

The fact that a transaction commits does not mean the exception disappears.

For example:

```java
@Transactional(noRollbackFor = UncheckedGreetingException.class)
```

can result in:

```text
Database changes
       ↓
Commit
       ↓
Exception propagates
       ↓
Caller handles exception
```

This distinction is important when designing transactional service methods.

---

## Rollback Rules Should Reflect Business Semantics

Rollback rules should not be selected simply because an exception happens to extend `RuntimeException` or `Exception`.

Instead, consider what the exception means to the operation.

Ask:

```text
Does this exception mean the transaction's changes
are no longer valid?
```

If yes, rollback may be appropriate.

If the exception represents a condition where the changes should remain committed, `noRollbackFor` may be appropriate.

---

# Relationship to `@Transactional`

Rollback rules are configured directly on `@Transactional`:

```java
@Transactional
```

or:

```java
@Transactional(rollbackFor = CheckedGreetingException.class)
```

or:

```java
@Transactional(noRollbackFor = UncheckedGreetingException.class)
```

This makes rollback behavior part of the transaction boundary declared by the service method.

---

# Key Takeaways

- Spring provides default rollback rules for transactional methods.
- `RuntimeException` and `Error` trigger rollback by default.
- Checked exceptions do not trigger rollback by default.
- `rollbackFor` can explicitly make an exception a rollback condition.
- `noRollbackFor` can explicitly prevent rollback for an exception.
- `noRollbackFor` does not suppress or handle the exception.
- An exception can propagate to the caller even when the transaction commits.
- Rollback rules allow transaction behavior to match application and business requirements.
- The rollback decision is handled by Spring's transaction infrastructure.
- The correct rollback policy depends on what an exception means for the operation being performed.

---

## Dependencies

This example uses Spring JDBC, Spring TX, H2, and JUnit 5.

The H2 dependency is required because the example uses an embedded H2 database for demonstrating transaction behavior.

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

# Verification

Run the tests with:

```bash
mvn clean install
```

The example should build successfully and all rollback-rule tests should pass.

---

# What's Next?

The next example explores **Programmatic Transactions**.

So far, transaction boundaries and rollback rules have been declared using annotations such as:

```java
@Transactional
```

The next example will demonstrate how transaction management can instead be controlled explicitly through Spring's programmatic transaction APIs.
