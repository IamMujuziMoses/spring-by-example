# Method Injection

Method Injection is a Spring technique that allows the Spring container to provide an implementation for a method on a bean.

It is particularly useful when a long-lived bean, such as a singleton, needs to work with a dependency whose lifecycle is shorter, such as a prototype.

Instead of injecting the dependency once through a constructor or setter, Spring can intercept a method invocation and provide the appropriate object when the method is called.

This example demonstrates Method Injection using Spring's `MethodReplacer` and `ReplaceOverride` APIs.

## Learning Objectives

- Understand what Method Injection is
- Understand why Method Injection is needed
- Understand the lifecycle problem Method Injection can solve
- Learn how `MethodReplacer` works
- Learn how `ReplaceOverride` configures method replacement
- Understand `MethodOverrides`
- Learn how Spring replaces a method implementation
- Understand how a bean can delegate method behavior to another component
- Learn how to configure Method Injection programmatically
- Understand the relationship between Method Injection and `@Lookup`

---

## What Is Method Injection?

Method Injection allows Spring to replace the implementation of a method on a managed bean.

Normally, a Java class defines its own method implementation:

```java
public Command createCommand() {
    return new Command();
}
```

With Method Injection, Spring can replace that method's behavior when it creates the bean.

The basic idea is:

```text
Bean
 │
 │ method call
 ▼
Method Injection
 │
 ▼
Replacement Method
 │
 ▼
Result
```

Instead of the original method implementation being executed, Spring delegates the method invocation to a configured replacement.

---

## Why Does Method Injection Exist?

Method Injection is useful when a bean has a longer lifecycle than one of the objects it needs.

Consider a singleton `CommandManager` that needs a new `Command` each time it processes something.

With normal dependency injection:

```text
Singleton CommandManager
        │
        │ injected once
        ▼
Command
```

The same `Command` instance could be reused.

That may not be what the application needs.

Method Injection allows the singleton to request an object through a method:

```text
Singleton CommandManager
        │
        │ createCommand()
        ▼
Spring Method Injection
        │
        ▼
Command
```

The method can therefore obtain an appropriate object when it is invoked.

---

## The Lifecycle Problem

Spring beans are singleton-scoped by default.

For example:

```text
CommandManager
    │
    └── singleton
```

A prototype bean behaves differently:

```text
Command
    │
    └── new instance when requested
```

If a prototype is injected directly into a singleton:

```text
Singleton CommandManager
        │
        │ constructor/setter injection
        ▼
Prototype Command
```

the prototype is resolved when the singleton is created.

The singleton does not automatically ask the container for a new prototype every time it needs one.

Method Injection provides another mechanism:

```text
Singleton CommandManager
        │
        │ process()
        ▼
createCommand()
        │
        ▼
Spring-managed method replacement
        │
        ▼
Command
```

This is one of the classic use cases for Method Injection.

---

## Method Injection Components

This example uses several Spring classes:

| Component | Responsibility |
|---|---|
| `MethodReplacer` | Provides replacement behavior for a method |
| `ReplaceOverride` | Describes which method should be replaced |
| `MethodOverrides` | Stores method override metadata |
| `RootBeanDefinition` | Defines the bean managed by Spring |
| `DefaultListableBeanFactory` | Creates and manages the bean |

The relationship is:

```text
RootBeanDefinition
       │
       ▼
MethodOverrides
       │
       ▼
ReplaceOverride
       │
       ▼
MethodReplacer
       │
       ▼
Replacement behavior
```

---

## MethodReplacer

`MethodReplacer` is the interface used to provide replacement behavior for a method.

Its core method is:

```java
Object reimplement(Object target, Method method, Object[] arguments);
```

Spring calls this method when the configured method is invoked.

The parameters provide information about the invocation:

| Parameter | Meaning |
|---|---|
| `target` | The bean whose method was invoked |
| `method` | The method being replaced |
| `arguments` | Arguments passed to the method |

---

## CommandReplacer

This example provides a `CommandReplacer` implementation:

```java
public class CommandReplacer implements MethodReplacer {

    @Override
    public Object reimplement(Object target, Method method, Object[] arguments) {

        return new Command("Hello from Method Injection!");
    }
}
```

The replacement implementation creates the `Command` returned by `createCommand()`.

The original `createCommand()` implementation is therefore replaced by Spring.

---

## ReplaceOverride

`ReplaceOverride` describes a method that should be replaced by a `MethodReplacer`.

For example:

```java
new ReplaceOverride("createCommand","commandReplacer")
```

The two important values are:

```text
"createCommand"
       │
       ▼
Method to replace

"commandReplacer"
       │
       ▼
Bean providing replacement behavior
```

Spring uses this metadata when creating the bean.

---

## MethodOverrides

A `RootBeanDefinition` contains method override metadata.

The example adds a `ReplaceOverride`:

```java
commandManagerDefinition.getMethodOverrides().addOverride(new ReplaceOverride("createCommand","commandReplacer"));
```

Conceptually:

```text
CommandManager BeanDefinition
        │
        ▼
MethodOverrides
        │
        ▼
ReplaceOverride
        │
        ├── method: createCommand
        │
        └── replacer: commandReplacer
```

This tells Spring that the `createCommand()` method should be handled by the `commandReplacer` bean.

---

## CommandManager

The `CommandManager` exposes a method that Spring will replace:

```java
public abstract class CommandManager {

    public Command process() {

        return createCommand();
    }

    protected abstract Command createCommand();
}
```

The important method is:

```java
protected abstract Command createCommand();
```

The class doesn't provide the implementation.

Spring supplies the behavior through Method Injection.

The `process()` method remains normal Java code:

```text
process()
   │
   ▼
createCommand()
   │
   ▼
Spring replacement
   │
   ▼
Command
```

---

## Why Is CommandManager Abstract?

The example intentionally leaves `createCommand()` without a normal implementation.

This makes the purpose of Method Injection clear:

```java
protected abstract Command createCommand();
```

The application does not need to know how the command is created.

Instead, Spring's bean definition provides the method implementation.

This separates:

```text
Business operation
    │
    ▼
process()
```

from:

```text
Object creation
    │
    ▼
createCommand()
```

Spring controls the latter.

---

## Configuring Method Injection

Method Injection in this example is configured directly on a `RootBeanDefinition`.

First, register the replacer:

```java
beanFactory.registerBeanDefinition("commandReplacer",
        BeanDefinitionBuilder.rootBeanDefinition(CommandReplacer.class).getBeanDefinition());
```

Then create the `CommandManager` definition:

```java
RootBeanDefinition commandManagerDefinition = new RootBeanDefinition(CommandManager.class);
```

Then add the method override:

```java
commandManagerDefinition.getMethodOverrides().addOverride(
                new ReplaceOverride("createCommand","commandReplacer"));
```

Finally, register the definition:

```java
beanFactory.registerBeanDefinition("commandManager",commandManagerDefinition);
```

The complete relationship is:

```text
BeanFactory
    │
    ├── commandReplacer
    │
    └── commandManager
             │
             └── ReplaceOverride
                    │
                    └── createCommand()
                           │
                           ▼
                    CommandReplacer
```

---

## Retrieving the Bean

Once the bean definition is registered, the `CommandManager` is retrieved normally:

```java
CommandManager commandManager = beanFactory.getBean("commandManager", CommandManager.class);
```

The application does not manually instantiate the `CommandManager`.

Spring creates the managed instance and applies the configured method override.

---

## Calling the Replaced Method

The application calls:

```java
Command command = commandManager.process();
```

Inside `process()`:

```java
Command command = createCommand();
```

Spring intercepts the configured method and delegates the invocation to `CommandReplacer`.

The resulting flow is:

```text
commandManager.process()
        │
        ▼
createCommand()
        │
        ▼
ReplaceOverride
        │
        ▼
CommandReplacer.reimplement(...)
        │
        ▼
new Command(...)
        │
        ▼
Command
```

---

## Complete Example

### `Command.java`

```java
public class Command {

    private final String message;

    public Command(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
```

### `CommandManager.java`

```java
public abstract class CommandManager {

    public Command process() {

        return createCommand();
    }

    protected abstract Command createCommand();
}
```

### `CommandReplacer.java`

```java
public class CommandReplacer implements MethodReplacer {

    @Override
    public Object reimplement(Object target, Method method, Object[] arguments) {

        return new Command("Hello from Method Injection!");
    }
}
```

### `MethodInjectionApplication.java`

```java
public class MethodInjectionApplication {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        beanFactory.registerBeanDefinition("commandReplacer", 
                BeanDefinitionBuilder.rootBeanDefinition(CommandReplacer.class).getBeanDefinition());

        RootBeanDefinition commandManagerDefinition = new RootBeanDefinition(CommandManager.class);

        commandManagerDefinition.getMethodOverrides()
                .addOverride(new ReplaceOverride("createCommand", "commandReplacer"));

        beanFactory.registerBeanDefinition("commandManager", commandManagerDefinition);

        CommandManager commandManager = beanFactory.getBean("commandManager", CommandManager.class);

        Command command = commandManager.process();

        System.out.println(command.getMessage());
    }
}
```

Expected output:

```text
Hello from Method Injection!
```

---

## Test Configuration

The tests create the same bean-definition configuration used by the application:

```java
private CommandManager createCommandManager() {
    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

    beanFactory.registerBeanDefinition("commandReplacer",
            BeanDefinitionBuilder.rootBeanDefinition(CommandReplacer.class).getBeanDefinition());

    RootBeanDefinition commandManagerDefinition = new RootBeanDefinition(CommandManager.class);

    commandManagerDefinition.getMethodOverrides().addOverride(
                    new ReplaceOverride("createCommand", "commandReplacer"));

    beanFactory.registerBeanDefinition("commandManager", commandManagerDefinition);

    return beanFactory.getBean("commandManager", CommandManager.class);
}
```

This keeps the tests focused on the actual Spring container mechanism.

---

## Method Injection vs Normal Dependency Injection

Normal constructor injection supplies a dependency when the bean is created:

```text
Bean Creation
     │
     ▼
Constructor
     │
     ▼
Dependency injected
```

Method Injection changes how a method obtains an object:

```text
Bean Creation
     │
     ▼
Bean exists
     │
     ▼
Method invocation
     │
     ▼
Spring method replacement
     │
     ▼
Object returned
```

The difference is when the dependency is obtained.

---

## Method Injection vs FactoryBean

`FactoryBean` and Method Injection can both participate in object creation, but they operate differently.

A `FactoryBean` provides the object returned by:

```java
getBean(...)
```

Method Injection changes the behavior of a method on another bean.

```text
FactoryBean
    │
    ▼
Bean creation


Method Injection
    │
    ▼
Method behavior replacement
```

---

## Method Injection vs `@Lookup`

`@Lookup` is a higher-level and more convenient way to express a common Method Injection use case.

The lower-level approach demonstrated here uses:

```text
MethodOverrides
      │
      ▼
ReplaceOverride
      │
      ▼
MethodReplacer
```

The next example will focus specifically on:

```java
@Lookup
```

This provides a useful progression:

```text
Method Injection
      │
      ▼
Understand method replacement
      │
      ▼
@Lookup
      │
      ▼
Use annotation-based method injection
```

Keeping these as separate examples makes the underlying mechanism easier to understand before introducing the more convenient `@Lookup` abstraction.

---

## Method Injection Flow

The complete flow can be summarized as:

```text
1. Define CommandManager
          │
          ▼
2. Define createCommand()
          │
          ▼
3. Create RootBeanDefinition
          │
          ▼
4. Add ReplaceOverride
          │
          ▼
5. Register CommandReplacer
          │
          ▼
6. Spring creates CommandManager
          │
          ▼
7. process() calls createCommand()
          │
          ▼
8. Spring applies method replacement
          │
          ▼
9. CommandReplacer.reimplement()
          │
          ▼
10. Command returned
```

---

## Important Concepts

### Method Override

Spring's bean definition metadata can describe methods whose behavior should be overridden.

This is represented through `MethodOverrides`.

### ReplaceOverride

`ReplaceOverride` identifies a method that should be delegated to a `MethodReplacer`.

```java
new ReplaceOverride("createCommand","commandReplacer")
```

### MethodReplacer

`MethodReplacer` contains the replacement implementation.

```java
public Object reimplement(Object target, Method method, Object[] arguments)
```

### BeanDefinition

Method Injection is configured as part of the bean's definition.

```text
BeanDefinition
      │
      └── MethodOverrides
              │
              └── ReplaceOverride
```

This demonstrates how Spring's bean-definition metadata can influence the runtime behavior of a managed bean.

---

## Key Operations

| Operation | Purpose |
|---|---|
| `getMethodOverrides()` | Accesses method override metadata |
| `addOverride()` | Adds a method override |
| `ReplaceOverride` | Describes a method replacement |
| `MethodReplacer` | Provides replacement method behavior |
| `reimplement()` | Executes replacement logic |
| `registerBeanDefinition()` | Registers bean metadata |
| `getBean()` | Retrieves the Spring-managed bean |

---

## Dependencies

This example requires Spring Beans:

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-beans</artifactId>
</dependency>

<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
```

---

## Running the Example

Run the tests:

```bash
mvn clean test
```

Build the module:

```bash
mvn clean install
```

Run the application:

```bash
mvn exec:java
```

Or run `MethodInjectionApplication` directly from your IDE.

Expected output:

```text
Hello from Method Injection!
```

---

## Key Takeaways

- Method Injection allows Spring to replace or override method behavior on managed beans.
- It can help solve lifecycle problems involving long-lived beans and shorter-lived dependencies.
- `MethodReplacer` provides replacement method behavior.
- `ReplaceOverride` identifies the method that should be replaced.
- `MethodOverrides` stores method override metadata.
- Method Injection is configured through bean-definition metadata.
- `CommandManager` delegates object creation to `createCommand()`.
- Spring intercepts the configured method and delegates it to `CommandReplacer`.
- Method Injection is different from normal constructor and setter injection.
- Method Injection provides a lower-level mechanism that helps explain how `@Lookup` works.
- `@Lookup` will be covered separately in the next example.

---

## What's Next?

The next example is **`@Lookup`**.

It will demonstrate the annotation-based approach to method injection and show how Spring can provide a fresh bean instance whenever a lookup method is invoked.