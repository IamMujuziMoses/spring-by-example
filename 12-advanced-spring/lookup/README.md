# @Lookup

The **`@Lookup`** annotation is Spring's annotation-based mechanism for method injection.

It allows a Spring-managed bean to obtain another bean from the Spring container each time a method is called. This is particularly useful when a **singleton bean needs access to a prototype-scoped bean**, because normal dependency injection would otherwise inject a single instance when the singleton is created.

This example demonstrates `@Lookup` using a singleton `CommandManager` that retrieves a new prototype `Command` every time `process()` is called.

## Learning Objectives

By completing this example, you will learn how to:

- Understand the purpose of `@Lookup`
- Use `@Lookup` for method injection
- Understand the singleton/prototype scope problem
- Retrieve a prototype bean from a singleton bean
- Understand how Spring overrides `@Lookup` methods
- Configure prototype-scoped beans
- Verify that each lookup returns a new instance
- Understand the relationship between `@Lookup` and Method Injection

---

## What Is `@Lookup`?

`@Lookup` is a Spring annotation used to indicate that a method should be overridden by the Spring container to perform a bean lookup.

For example:

```java
@Lookup("command")
protected Command createCommand() {
    throw new UnsupportedOperationException("Spring should override this method");
}
```

The method implementation is not intended to be executed directly.

When Spring creates the managed `CommandManager`, it generates a subclass that overrides the `createCommand()` method.

Conceptually:

```text
CommandManager
      │
      │ @Lookup
      ▼
Spring-generated subclass
      │
      │ getBean("command")
      ▼
Command
```

Every invocation of the lookup method causes Spring to obtain the bean from the container.

---

## Why Does `@Lookup` Exist?

Consider a singleton bean that needs a new object every time a method is called.

Suppose `CommandManager` is a singleton:

```text
CommandManager
      │
      │ singleton
      ▼
one CommandManager instance
```

But `Command` should be prototype-scoped:

```text
process() → Command #1
process() → Command #2
process() → Command #3
```

A normal dependency injection approach would not automatically provide this behavior.

For example:

```java
@Autowired
private Command command;
```

The dependency is injected when the singleton is created. Calling `process()` repeatedly would not automatically create a new prototype instance.

`@Lookup` solves this by deferring the bean lookup until the method is invoked.

---

## The Singleton/Prototype Problem

Spring supports different bean scopes.

A singleton bean is created once per Spring container:

```text
ApplicationContext
      │
      └── CommandManager
              │
              └── one instance
```

A prototype bean produces a new instance whenever it is requested:

```text
getBean("command")
      │
      ├── Command #1
      │
      ├── Command #2
      │
      └── Command #3
```

The problem occurs when a singleton needs a fresh prototype instance during its lifetime.

`@Lookup` allows the singleton to ask the container for the prototype whenever necessary.

---

## Example

The example contains two main beans:

- `CommandManager` — a singleton bean using `@Lookup`
- `Command` — a prototype-scoped bean

### Command

```java
public class Command {

    private final String message;

    public Command(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
```

The `Command` object contains a message that can be retrieved after it has been created.

### CommandManager

```java
@Component
public class CommandManager {

    public Command process() {
        return createCommand();
    }

    @Lookup("command")
    protected Command createCommand() {
        throw new UnsupportedOperationException("Spring should override this method");
    }
}
```

The important part is:

```java
@Lookup("command")
protected Command createCommand() {
    throw new UnsupportedOperationException("Spring should override this method");
}
```

The exception is intentional.

The method is a placeholder because Spring is expected to override it in the managed bean.

The application should therefore call:

```java
commandManager.process();
```

rather than directly invoking:

```java
commandManager.createCommand();
```

### Lookup Configuration

The prototype `Command` bean is configured in `LookupConfig`:

```java
@Configuration
@ComponentScan(basePackageClasses = CommandManager.class)
public class LookupConfig {

    @Bean
    @Scope("prototype")
    public Command command() {
        return new Command("Hello from @Lookup!");
    }
}
```

There are two important parts here.

---

## Component Scanning

`CommandManager` is registered as a Spring-managed component:

```java
@Component
public class CommandManager {
```

The configuration discovers it using:

```java
@ComponentScan(basePackageClasses = CommandManager.class)
```

This allows Spring to process the `@Lookup` annotation when creating the bean.

---

## Prototype Scope

The `Command` bean is declared as:

```java
@Bean
@Scope("prototype")
public Command command() {
    return new Command("Hello from @Lookup!");
}
```

This means Spring creates a new `Command` instance whenever the bean is requested.

---

## How `@Lookup` Works

The application obtains the singleton `CommandManager`:

```java
CommandManager commandManager = applicationContext.getBean(CommandManager.class);
```

Then it calls:

```java
Command command = commandManager.process();
```

Inside `process()`:

```java
return createCommand();
```

Because `createCommand()` is annotated with `@Lookup`, Spring overrides the method in the managed object.

Conceptually, Spring provides behavior similar to:

```java
protected Command createCommand() {
    return applicationContext.getBean("command", Command.class);
}
```

The actual implementation is handled by Spring's bean infrastructure.

The important idea is:

```text
process()
   │
   ▼
createCommand()
   │
   │ @Lookup
   ▼
Spring container
   │
   ▼
command bean
   │
   │ prototype
   ▼
new Command instance
```

---

## Singleton Manager, Prototype Command

The scopes can be visualized as:

```text
ApplicationContext
       │
       ├── CommandManager
       │       │
       │       │ singleton
       │       ▼
       │   one instance
       │
       └── command
               │
               │ prototype
               ▼
          new instance
          per lookup
```

Calling:

```java
commandManager.process();
```

multiple times results in:

```text
process()
   └── Command #1

process()
   └── Command #2

process()
   └── Command #3
```

The `CommandManager` remains the same object while each lookup obtains a different `Command`.

---

## Application

The complete application is:

```java
public class LookupApplication {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(LookupConfig.class)) {

            CommandManager commandManager = applicationContext.getBean(CommandManager.class);
            Command command = commandManager.process();

            System.out.println(command.message());
        }
    }
}
```

Running the application produces:

```text
Hello from @Lookup!
```

---

## `@Lookup` vs Normal Dependency Injection

Normal dependency injection:

```text
Singleton
    │
    │ inject dependency
    ▼
Command instance
```

The dependency is injected when the singleton is created.

With `@Lookup`:

```text
Singleton
    │
    │ method call
    ▼
@Lookup method
    │
    ▼
Spring container
    │
    ▼
new prototype instance
```

The lookup happens when the method is called.

---

## `@Lookup` vs MethodReplacer

The previous **Method Injection** example demonstrated Spring's lower-level method replacement mechanism using:

- `MethodReplacer`
- `ReplaceOverride`
- `MethodOverrides`

`@Lookup` provides a simpler annotation-based mechanism.

```text
Method Injection
      │
      ├── MethodReplacer
      ├── ReplaceOverride
      └── @Lookup
```

The previous example focuses on the underlying method replacement infrastructure, while this example focuses specifically on the convenient `@Lookup` annotation.

---

## `@Lookup` vs `FactoryBean`

A `FactoryBean` focuses on custom creation of another object:

```text
FactoryBean
     │
     ▼
product object
```

`@Lookup` focuses on allowing a bean to obtain another bean from the Spring container when a method is called:

```text
@Lookup method
     │
     ▼
Spring container
     │
     ▼
bean instance
```

Both can solve object creation problems, but they address different extension points in Spring's bean infrastructure.

---

## Important Details

### The Method Must Be Overridable

Spring needs to override the lookup method.

Therefore, the method should not be:

```java
private
```

or:

```java
final
```

The containing class must also be suitable for Spring's subclass-based method injection.

### The Bean Must Be Spring Managed

`@Lookup` does not work when manually creating the object:

```java
CommandManager commandManager = new CommandManager();
```

Spring must create and manage the `CommandManager` instance.

Otherwise, the original method implementation executes:

```java
throw new UnsupportedOperationException("Spring should override this method");
```

### The Lookup Target Is a Spring Bean

The object returned by the lookup must be available in the Spring container.

In this example:

```java
@Bean
@Scope("prototype")
public Command command() {
    return new Command("Hello from @Lookup!");
}
```

The lookup explicitly targets:

```java
@Lookup("command")
```

---

## Dependencies

The example uses Spring Context:

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
</dependency>

<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
```

---

## Running the Example

From the `lookup` module:

```bash
mvn clean install
```

To run the application from an IDE, execute:

```text
LookupApplication
```

Expected output:

```text
Hello from @Lookup!
```

---

## Key Takeaways

- `@Lookup` is Spring's annotation-based method injection mechanism.
- Spring overrides the annotated method in the managed bean.
- The method can obtain another bean from the Spring container.
- `@Lookup` is particularly useful when a singleton needs a prototype-scoped dependency.
- Each lookup can return a new prototype instance.
- The lookup method should not be called on a manually instantiated object.
- The containing bean must be managed by Spring.
- `@Lookup` provides a convenient alternative to configuring lower-level method injection manually.

---

# Module 12 Complete

This example completes **Module 12 — Advanced Spring**.

The module covered:

1. **BeanFactory**
2. **ApplicationContext**
3. **FactoryBean**
4. **BeanDefinition**
5. **BeanDefinitionRegistry**
6. **ImportSelector**
7. **DeferredImportSelector**
8. **ImportBeanDefinitionRegistrar**
9. **Environment**
10. **Property Sources**
11. **Resource Loading**
12. **MessageSource**
13. **ConversionService**
14. **Validation**
15. **Spring Expression Language (SpEL)**
16. **Method Injection**
17. **`@Lookup`**
