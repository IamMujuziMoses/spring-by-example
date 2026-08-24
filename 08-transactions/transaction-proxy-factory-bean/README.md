# TransactionProxyFactoryBean

Spring provides several ways to apply transaction management to application code.

This example demonstrates the older `TransactionProxyFactoryBean` approach, where Spring explicitly creates a transactional proxy around a target service.

Unlike the `@Transactional` example, the target service does not contain any transaction annotation. Instead, transaction behavior is configured externally through Spring XML configuration.

This example also connects Spring transaction management with the proxy concepts explored earlier in the Spring AOP module.

---

## Learning Objectives

By the end of this example, you will understand:

- What `TransactionProxyFactoryBean` is.
- How Spring creates a transactional proxy around a target object.
- How transaction configuration can be defined externally from the target class.
- How `TransactionInterceptor` participates in transaction management.
- How `DataSourceTransactionManager` manages JDBC transactions.
- How transaction attributes can be configured using XML.
- How `PROPAGATION_REQUIRED` can be configured without `@Transactional`.
- How transaction rollback works through a proxy.
- The difference between a target bean and its transactional proxy.
- How `TransactionProxyFactoryBean` relates to Spring AOP proxies.
- Why modern Spring applications generally prefer `@Transactional`.

---

# What Is `TransactionProxyFactoryBean`?

`TransactionProxyFactoryBean` is a Spring `FactoryBean` that creates a transactional AOP proxy around a target object.

Conceptually:

```text
Caller
   ↓
TransactionProxyFactoryBean
   ↓
Transactional Proxy
   ↓
TransactionInterceptor
   ↓
TransactionManager
   ↓
Target Service
   ↓
Repository
```

The target service does not need to know that it is participating in a transaction.

Instead, the transaction behavior is applied by the proxy.

---

# Why Does It Exist?

Before annotation-driven transaction management became the common approach, Spring applications could configure transactional behavior explicitly using XML.

For example:

```xml
<bean id="greetingService" class="org.springframework.transaction.interceptor.TransactionProxyFactoryBean">
    <property name="target" ref="greetingServiceTarget"/>
    <property name="transactionManager" ref="transactionManager"/>
    <property name="transactionAttributes">
        <props>
            <prop key="saveGreeting">
                PROPAGATION_REQUIRED
            </prop>
        </props>
    </property>

</bean>
```

This configuration tells Spring:

```text
When saveGreeting() is called
        ↓
Start or join a transaction
        ↓
Invoke the target method
        ↓
Commit if successful
        ↓
Rollback if the method fails
```

The important point is that the target class itself does not contain transaction configuration.

---

# Target vs Proxy

One of the most important concepts in this example is the difference between the **target object** and the **proxy**.

The target is configured as:

```xml
<bean id="greetingServiceTarget" class="com.springbyexample.transactionproxyfactorybean.GreetingService">
    <constructor-arg ref="greetingRepository"/>
</bean>
```

This creates the actual `GreetingService` object.

The transactional proxy is configured separately:

```xml
<bean id="greetingService" class="org.springframework.transaction.interceptor.TransactionProxyFactoryBean">
```

The relationship is:

```text
greetingService
      ↓
TransactionProxyFactoryBean
      ↓
Transactional Proxy
      ↓
greetingServiceTarget
      ↓
GreetingService
```

Application code retrieves:

```java
context.getBean("greetingService", GreetingService.class);
```

It therefore receives the proxy rather than directly retrieving `greetingServiceTarget`.

---

# GreetingService

The target service contains only application logic.

```java
public class GreetingService {

    private final GreetingRepository greetingRepository;

    public GreetingService(GreetingRepository greetingRepository) {
        this.greetingRepository = greetingRepository;
    }

    public void saveGreeting(String message) {
        greetingRepository.save(message);
    }

    public void saveGreetingAndFail(String message) {
        greetingRepository.save(message);
        throw new RuntimeException("Something went wrong");
    }

    public int countGreetings() {
        return greetingRepository.count();
    }
}
```

Notice that there is no:

```java
@Transactional
```

annotation.

The service does not need to know that transactions are being applied.

---

# GreetingRepository

The repository uses `JdbcTemplate` to interact with the database.

```java
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

The repository is not responsible for transaction management either.

Its responsibility is simply database access.

---

# Database Configuration

The example uses an embedded H2 database.

The database is configured using `EmbeddedDatabaseFactoryBean`:

```xml
<bean id="dataSource" class="org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactoryBean">
    <property name="databaseName" value="transactionProxyFactoryBeanDb"/>
    <property name="databaseType" value="H2"/>
    <property name="databasePopulator">
        <bean class="org.springframework.jdbc.datasource.init.ResourceDatabasePopulator">
            <constructor-arg value="classpath:schema.sql"/>
        </bean>
    </property>
</bean>
```

The database schema is loaded from:

```text
classpath:schema.sql
```

The schema contains:

```sql
CREATE TABLE greetings ( id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    message VARCHAR(255) NOT NULL
);
```

---

# JdbcTemplate

The `JdbcTemplate` is configured using the data source:

```xml
<bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
    <constructor-arg ref="dataSource"/>
</bean>
```

The repository then receives it through constructor injection:

```xml
<bean id="greetingRepository" class="com.springbyexample.transactionproxyfactorybean.GreetingRepository">
    <constructor-arg ref="jdbcTemplate"/>
</bean>
```

The resulting dependency chain is:

```text
DataSource
    ↓
JdbcTemplate
    ↓
GreetingRepository
    ↓
GreetingService
```

---

# Transaction Manager

The example uses Spring's `DataSourceTransactionManager`:

```xml
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
</bean>
```

The transaction manager is responsible for managing transactions associated with the JDBC data source.

Conceptually:

```text
TransactionProxyFactoryBean
          ↓
TransactionInterceptor
          ↓
DataSourceTransactionManager
          ↓
JDBC Connection
          ↓
Database
```

---

# Configuring the Target

The actual service is registered as:

```xml
<bean id="greetingServiceTarget" class="com.springbyexample.transactionproxyfactorybean.GreetingService">
    <constructor-arg ref="greetingRepository"/>
</bean>
```

This bean is the target of the transaction proxy.

It is deliberately named:

```text
greetingServiceTarget
```

to make the proxy relationship easier to understand.

---

# Creating the Transaction Proxy

The important configuration is:

```xml
<bean id="greetingService" class="org.springframework.transaction.interceptor.TransactionProxyFactoryBean">
    <property name="target" ref="greetingServiceTarget"/>
    <property name="transactionManager" ref="transactionManager"/>
    <property name="transactionAttributes">
        <props>
            <prop key="saveGreeting">PROPAGATION_REQUIRED</prop>
            <prop key="saveGreetingAndFail">PROPAGATION_REQUIRED</prop>
            <prop key="countGreetings">PROPAGATION_REQUIRED,readOnly</prop>
        </props>
    </property>

</bean>
```

Three properties are particularly important.

---

# `target`

The:

```xml
<property name="target" ref="greetingServiceTarget"/>
```

property identifies the object that the proxy should wrap.

```text
Proxy
  ↓
GreetingService target
```

---

# `transactionManager`

The:

```xml
<property name="transactionManager" ref="transactionManager"/>
```

property tells the proxy which transaction manager should control the transaction.

In this example:

```text
TransactionProxyFactoryBean
        ↓
DataSourceTransactionManager
        ↓
H2 database
```

---

# `transactionAttributes`

The:

```xml
<property name="transactionAttributes">
```

property defines transaction behavior for individual methods.

For example:

```xml
<prop key="saveGreeting">PROPAGATION_REQUIRED</prop>
```

means that `saveGreeting()` should execute with:

```text
PROPAGATION_REQUIRED
```

Similarly:

```xml
<prop key="saveGreetingAndFail">PROPAGATION_REQUIRED</prop>
```

makes the failing operation transactional.

---

# Transaction Attributes

Transaction attributes allow the proxy to define transaction behavior without modifying the target class.

For example:

```xml
<prop key="saveGreeting">PROPAGATION_REQUIRED</prop>
```

Conceptually:

```text
saveGreeting()
      ↓
TransactionProxyFactoryBean
      ↓
PROPAGATION_REQUIRED
      ↓
TransactionInterceptor
      ↓
GreetingService
```

This is similar in purpose to:

```java
@Transactional
public void saveGreeting(String message) {
}
```

but the configuration is external.

---

# `PROPAGATION_REQUIRED`

The example uses:

```text
PROPAGATION_REQUIRED
```

This is Spring's default transaction propagation behavior.

It means:

- Join an existing transaction if one exists.
- Otherwise create a new transaction.

Conceptually:

```text
Existing transaction?
       /       \
     yes        no
      ↓          ↓
   Join it    Create one
```

Propagation is explored in more detail in the dedicated **Transaction Propagation** example.

---

# Read-Only Transactions

The `countGreetings()` method is configured as:

```xml
<prop key="countGreetings">PROPAGATION_REQUIRED,readOnly</prop>
```

The:

```text
readOnly
```

attribute indicates that the transaction is intended for read operations.

This communicates the intended usage of the transaction to the transaction infrastructure.

---

# Successful Transaction

The application can call:

```java
greetingService.saveGreeting("Hello, Spring!");
```

The call goes through the transaction proxy.

Conceptually:

```text
Main
 ↓
greetingService
 ↓
TransactionProxyFactoryBean
 ↓
TransactionInterceptor
 ↓
Begin transaction
 ↓
GreetingService
 ↓
GreetingRepository
 ↓
INSERT
 ↓
Return successfully
 ↓
Commit transaction
```

The greeting is therefore persisted.

---

# Failed Transaction

The example also contains:

```java
public void saveGreetingAndFail(String message) {
    greetingRepository.save(message);
    throw new RuntimeException("Something went wrong");
}
```

The database insert happens before the exception.

However, the method is configured with:

```xml
<prop key="saveGreetingAndFail">PROPAGATION_REQUIRED</prop>
```

The transaction proxy therefore detects the failure and rolls back the transaction.

Conceptually:

```text
Main
 ↓
Transaction Proxy
 ↓
Begin transaction
 ↓
GreetingService
 ↓
INSERT
 ↓
RuntimeException
 ↓
Rollback
 ↓
Database unchanged
```

---

# Main

The example can be executed with:

```java
public class Main {

    public static void main(String[] args) {
        try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml")) {

            GreetingService greetingService = context.getBean("greetingService", GreetingService.class);

            System.out.println("=== Successful Transaction ===");
            greetingService.saveGreeting("Hello, Spring!");

            System.out.println("Greetings: " + greetingService.countGreetings());
            System.out.println();
            System.out.println("=== Rolled Back Transaction ===");

            try {
                greetingService.saveGreetingAndFail("Hello, TransactionProxyFactoryBean!");
            } catch (RuntimeException e) {
                System.out.println("Transaction failed: " + e.getMessage());
            }

            System.out.println("Greetings: " + greetingService.countGreetings());
        }
    }
}
```

The first operation succeeds.

The second operation fails and is rolled back.

---

# Expected Output

The output should be similar to:

```text
=== Successful Transaction ===
Greetings: 1

=== Rolled Back Transaction ===
Transaction failed: Something went wrong
Greetings: 1
```

The important part is that the second operation does not increase the number of greetings.

Before the failed operation:

```text
Greetings: 1
```

After the failed operation:

```text
Greetings: 1
```

The attempted insert was rolled back.

---

# How the Proxy Applies the Transaction

The application code appears to call:

```java
greetingService.saveGreetingAndFail("Hello, TransactionProxyFactoryBean!");
```

But the object returned from the application context is the transactional proxy.

The call therefore behaves conceptually like:

```text
Caller
  ↓
Transaction Proxy
  ↓
TransactionInterceptor
  ↓
Begin transaction
  ↓
GreetingService
  ↓
GreetingRepository
  ↓
INSERT
  ↓
RuntimeException
  ↓
Rollback
```

The target service itself does not contain any transaction management code.

---

# Relationship to Spring AOP

`TransactionProxyFactoryBean` is closely related to the proxy concepts explored in Module 7.

In Spring AOP, a proxy can intercept a method invocation and execute additional behavior.

For transactions:

```text
Caller
   ↓
AOP Proxy
   ↓
Transaction Interceptor
   ↓
Target
```

For the previous AOP examples, the proxy could execute:

```text
@Before
@After
@AfterReturning
@Around
```

For transaction management, the interceptor controls:

```text
Begin transaction
      ↓
Invoke target
      ↓
Commit or rollback
```

The underlying proxy concept is the same.

---

# `TransactionProxyFactoryBean` vs `@Transactional`

Modern Spring applications generally use:

```java
@Transactional
public void saveGreeting(String message) {
    // implementations
}
```

instead of explicitly configuring:

```xml
<bean id="greetingService" class="org.springframework.transaction.interceptor.TransactionProxyFactoryBean">
```

The difference is primarily how the transaction metadata is declared.

### `@Transactional`

```text
@Transactional
      ↓
Annotation metadata
      ↓
Spring AOP infrastructure
      ↓
Transactional proxy
```

### `TransactionProxyFactoryBean`

```text
XML configuration
      ↓
TransactionProxyFactoryBean
      ↓
Transactional proxy
```

Both approaches ultimately rely on Spring's proxy-based transaction infrastructure.

---

# Why Learn `TransactionProxyFactoryBean`?

Although it is not normally the preferred approach for new applications, understanding it is useful when working with older Spring applications.

You may encounter XML configurations such as:

```xml
<bean id="someService" class="org.springframework.transaction.interceptor.TransactionProxyFactoryBean">
```

Understanding what this configuration does makes it easier to understand:

- Existing Spring applications.
- Legacy XML configuration.
- Spring transaction interceptors.
- Spring AOP proxies.
- How declarative transactions evolved.
- The relationship between `@Transactional` and transaction proxies.

---

# Comparing the Two Approaches

| `@Transactional` | `TransactionProxyFactoryBean` |
|---|---|
| Annotation-based | XML-based |
| Common in modern Spring applications | Common in older Spring applications |
| Transaction metadata is close to the method | Transaction metadata is external |
| Less configuration | More explicit configuration |
| Uses Spring's proxy infrastructure | Uses Spring's proxy infrastructure |
| Easier to maintain in modern applications | Useful for legacy configurations |

The underlying idea remains similar:

```text
Transaction metadata
        ↓
Spring transaction infrastructure
        ↓
Proxy
        ↓
Target
```

---

# Why the Target Does Not Need `@Transactional`

The following class contains no transaction annotation:

```java
public class GreetingService {

    public void saveGreeting(String message) {
        greetingRepository.save(message);
    }
}
```

Yet it still participates in transactions.

That is because the transaction is applied by the proxy:

```text
GreetingService
      ↑
      |
Transactional Proxy
      ↑
      |
TransactionProxyFactoryBean
```

This demonstrates that transaction management does not necessarily need to be implemented directly inside the target class.

---

# The Complete Flow

The complete application can be visualized as:

```text
                    Main
                     ↓
              greetingService
                     ↓
       TransactionProxyFactoryBean
                     ↓
          Transactional Proxy
                     ↓
         TransactionInterceptor
                     ↓
         DataSourceTransactionManager
                     ↓
                Transaction
                     ↓
              GreetingService
                     ↓
            GreetingRepository
                     ↓
                JdbcTemplate
                     ↓
                  DataSource
                     ↓
                   H2 DB
```

This is the central concept of the example.

---

# Important Considerations

## It Is a Legacy Configuration Style

`TransactionProxyFactoryBean` is an older way of configuring declarative transactions.

For new Spring applications, `@Transactional` is generally simpler and easier to maintain.

This example exists primarily to understand how transaction proxies work and to provide context for legacy Spring applications.

---

## The Proxy Must Be Used

The transaction is applied when the method is invoked through the proxy.

Conceptually:

```text
Caller
   ↓
Proxy
   ↓
Transaction
   ↓
Target
```

If application code bypasses the proxy and directly invokes the target object, the configured transaction interceptor is bypassed.

This is one of the important characteristics of proxy-based transaction management.

---

## Configuration Is External

The service contains no transaction configuration:

```java
public class GreetingService {
}
```

The transaction behavior is instead defined in:

```text
applicationContext.xml
```

This provides a clear separation between business logic and transaction configuration, although the XML configuration is more verbose than modern annotation-based configuration.

---

# Verification

Run:

```bash
mvn clean install
```

The example should build successfully and all transaction tests should pass.

---

# Key Takeaways

- `TransactionProxyFactoryBean` creates a transactional proxy around a target object.
- The target service does not need `@Transactional`.
- Transaction behavior can be configured externally using XML.
- `transactionManager` determines which transaction manager controls the transaction.
- `transactionAttributes` define transaction behavior for individual methods.
- `PROPAGATION_REQUIRED` joins an existing transaction or creates a new one.
- A `RuntimeException` can cause the transaction to roll back.
- The application retrieves the proxy rather than directly interacting with the target.
- `TransactionProxyFactoryBean` builds on Spring's proxy-based AOP infrastructure.
- `@Transactional` provides a more modern and convenient way to declare transactions.
- Understanding `TransactionProxyFactoryBean` is valuable when working with legacy Spring applications.

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

# Module 8 — Spring Transactions

This example concludes the Spring Transactions module.

The module covered:

- `@Transactional`
- Transaction Propagation
- Isolation Levels
- Rollback Rules
- Programmatic Transactions
- **`TransactionProxyFactoryBean`**

Together, these examples demonstrate several different approaches to transaction management and the underlying concepts that connect them.

---

# What's Next?

The next module will build on these Spring fundamentals and introduce the next major Spring concept in the learning path.