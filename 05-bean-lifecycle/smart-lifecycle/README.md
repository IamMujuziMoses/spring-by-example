# SmartLifecycle

`SmartLifecycle` is a Spring interface for beans that have an explicit start and stop lifecycle.

It extends Spring's `Lifecycle` abstraction and adds features such as automatic startup and lifecycle phase ordering.

This example demonstrates how Spring automatically starts a `SmartLifecycle` bean when the application context starts and stops it when the context shuts down.

---

## Learning Objectives

By the end of this example, you will understand:

- What `SmartLifecycle` is.
- How `SmartLifecycle` extends Spring's `Lifecycle` abstraction.
- How Spring starts `SmartLifecycle` beans.
- How Spring stops `SmartLifecycle` beans.
- The purpose of `start()`.
- The purpose of `stop()`.
- The purpose of `isRunning()`.
- The purpose of `isAutoStartup()`.
- The purpose of `getPhase()`.
- How lifecycle phases control startup and shutdown ordering.
- How `SmartLifecycle` differs from `@PostConstruct`.
- How `SmartLifecycle` differs from `InitializingBean`.
- How `SmartLifecycle` can be used for components that need coordinated startup and shutdown.

---

# What Is SmartLifecycle?

`SmartLifecycle` is an extension of Spring's `Lifecycle` interface.

The basic `Lifecycle` contract provides methods for controlling whether a component is running:

```java
public interface Lifecycle {

    void start();

    void stop();

    boolean isRunning();
}
```

`SmartLifecycle` builds on this by providing additional lifecycle behavior:

```java
public interface SmartLifecycle extends Lifecycle {

    default boolean isAutoStartup();

    default int getPhase();

    default void stop(Runnable callback);
}
```

This makes `SmartLifecycle` useful for components that need to participate in the startup and shutdown process of the Spring application context.

---

# Why Use SmartLifecycle?

Some beans are not simply initialized and then left alone.

For example:

- Scheduled processes.
- Message consumers.
- Background workers.
- Polling services.
- Connection managers.
- Event processing components.

These components may need to:

```text
Start
  ↓
Run
  ↓
Stop
```

`SmartLifecycle` provides a way for these components to participate in that lifecycle.

---

# Basic Lifecycle

A simplified lifecycle looks like:

```text
ApplicationContext starts
        ↓
SmartLifecycle.start()
        ↓
Component running
        ↓
Application runs
        ↓
ApplicationContext closes
        ↓
SmartLifecycle.stop()
        ↓
Component stopped
```

The important distinction is that `start()` and `stop()` are controlled by the Spring container.

The application does not need to call them manually.

---

# SmartLifecycle Example

This example contains two classes:

```text
ReportScheduler
        ↑
        │
ReportSchedulerLifecycle
        ↑
        │
   SmartLifecycle
```

`ReportScheduler` represents the component that needs to be started and stopped.

`ReportSchedulerLifecycle` connects that component to Spring's lifecycle management.

---

# ReportScheduler

```java
public class ReportScheduler {

    private boolean running;

    public void start() {
        running = true;
        System.out.println("Report scheduler started");
    }

    public void stop() {
        running = false;
        System.out.println("Report scheduler stopped");
    }

    public boolean isRunning() {
        return running;
    }
}
```

`ReportScheduler` is an ordinary Java class.

It doesn't implement `SmartLifecycle`.

Instead, another class is responsible for integrating it with Spring's lifecycle system.

This separation keeps the example focused on what Spring is responsible for.

---

# ReportSchedulerLifecycle

```java
public class ReportSchedulerLifecycle implements SmartLifecycle {

    private final ReportScheduler scheduler;

    public ReportSchedulerLifecycle(ReportScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void start() {
        scheduler.start();
    }

    @Override
    public void stop() {
        scheduler.stop();
    }

    @Override
    public boolean isRunning() {
        return scheduler.isRunning();
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public int getPhase() {
        return 0;
    }
}
```

The lifecycle adapter implements `SmartLifecycle` and delegates the lifecycle operations to `ReportScheduler`.

---

# `start()`

```java
@Override
public void start() {
    scheduler.start();
}
```

Spring calls `start()` when the lifecycle component is started.

Because `isAutoStartup()` returns `true`, Spring automatically starts this component when the application context starts.

The flow is:

```text
ApplicationContext starts
        ↓
SmartLifecycle.start()
        ↓
ReportScheduler.start()
        ↓
running = true
```

---

# `stop()`

```java
@Override
public void stop() {
    scheduler.stop();
}
```

Spring calls `stop()` when the application context shuts down.

The flow becomes:

```text
ApplicationContext closes
        ↓
SmartLifecycle.stop()
        ↓
ReportScheduler.stop()
        ↓
running = false
```

This allows resources and background processes to be shut down cleanly.

---

# `isRunning()`

```java
@Override
public boolean isRunning() {
    return scheduler.isRunning();
}
```

Spring uses `isRunning()` to determine the current state of the lifecycle component.

In our example, the state is maintained by `ReportScheduler`.

```text
running = false
        ↓
Component stopped


running = true
        ↓
Component running
```

---

# `isAutoStartup()`

```java
@Override
public boolean isAutoStartup() {
    return true;
}
```

Returning `true` tells Spring to automatically start the component during application context startup.

Conceptually:

```text
ApplicationContext starts
        ↓
isAutoStartup() → true
        ↓
start()
```

If the method returned `false`, the component would not automatically start as part of the normal context startup process.

---

# `getPhase()`

```java
@Override
public int getPhase() {
    return 0;
}
```

The phase determines the ordering of lifecycle components when multiple components participate in startup and shutdown.

For example:

```text
Phase 0
    ↓
Phase 100
    ↓
Phase 200
```

Components with lower phase values start before components with higher phase values.

During shutdown, the ordering is reversed:

```text
Phase 200
    ↓
Phase 100
    ↓
Phase 0
```

This allows components to establish dependencies between their startup and shutdown sequences.

For this example, there is only one lifecycle component, so we use phase `0`.

---

# AppConfig

```java
@Configuration
public class AppConfig {

    @Bean
    public ReportScheduler reportScheduler() {
        return new ReportScheduler();
    }

    @Bean
    public ReportSchedulerLifecycle reportSchedulerLifecycle(ReportScheduler scheduler) {

        return new ReportSchedulerLifecycle(scheduler);
    }
}
```

Both components are registered with Spring.

The lifecycle adapter receives `ReportScheduler` through constructor injection:

```java
public ReportSchedulerLifecycle(ReportScheduler scheduler) {
    this.scheduler = scheduler;
}
```

Spring provides the `ReportScheduler` instance when creating the lifecycle adapter.

---

# Application Startup

The application creates an `AnnotationConfigApplicationContext`:

```java

try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) { }

```

Creating the context causes Spring to initialize the application.

Because `ReportSchedulerLifecycle` implements `SmartLifecycle` and `isAutoStartup()` returns `true`, Spring starts it automatically.

The flow is:

```text
AnnotationConfigApplicationContext
            ↓
       Application startup
            ↓
ReportSchedulerLifecycle.start()
            ↓
ReportScheduler.start()
            ↓
running = true
```

---

# Main

```java
public class Main {

    public static void main(String[] args) {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            var scheduler = context.getBean(ReportScheduler.class);

            System.out.println("Scheduler running: " + scheduler.isRunning());
        }
    }
}
```

There is no explicit call to:

```java

scheduler.start();

```

Spring handles that automatically.

The expected output includes:

```text
Report scheduler started
Scheduler running: true
```

---

# Application Shutdown

The application uses try-with-resources:

```java

try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) { }

```

When execution leaves the block, the application context is closed.

Closing the context triggers the lifecycle shutdown.

```text
try block ends
        ↓
ApplicationContext.close()
        ↓
SmartLifecycle.stop()
        ↓
ReportScheduler.stop()
        ↓
running = false
```

The output therefore also includes:

```text
Report scheduler stopped
```

---

# Why Does This Differ From `@PostConstruct`?

`@PostConstruct` is an initialization callback.

For example:

```java
@PostConstruct
public void initialize() {
    // Initialization
}
```

It is called during bean initialization.

The bean is then considered initialized.

`SmartLifecycle` is different because the component has an ongoing running state:

```text
@PostConstruct

Bean creation
     ↓
Initialization
     ↓
Bean ready
```

Whereas:

```text
SmartLifecycle

Context starts
     ↓
Component starts
     ↓
Component running
     ↓
Context stops
     ↓
Component stops
```

This makes `SmartLifecycle` more appropriate for components that actively operate while the application is running.

---

# SmartLifecycle vs InitializingBean

`InitializingBean` provides an initialization callback:

```java
@Override
public void afterPropertiesSet() {
    // Initialization
}
```

The method is called once during bean initialization.

It does not represent an ongoing running state.

`SmartLifecycle`, on the other hand, provides:

```java

start();
stop();
isRunning();

```

This allows a component to participate in an active application lifecycle.

---

# SmartLifecycle vs DisposableBean

`DisposableBean` provides a destruction callback:

```java
@Override
public void destroy() {
    // Cleanup
}
```

`SmartLifecycle` provides both startup and shutdown behavior:

```text
start()
  ↓
running
  ↓
stop()
```

So a useful comparison is:

| Mechanism | Purpose |
|---|---|
| `@PostConstruct` | Initialization callback |
| `InitializingBean` | Initialization callback |
| `@PreDestroy` | Destruction callback |
| `DisposableBean` | Destruction callback |
| `SmartLifecycle` | Coordinated startup and shutdown |

---

# Lifecycle Phases

One of the most useful features of `SmartLifecycle` is phase ordering.

Imagine an application has three lifecycle components:

```text
DatabaseConnection
    phase 0

MessageConsumer
    phase 100

ReportScheduler
    phase 200
```

During startup:

```text
DatabaseConnection
       ↓
MessageConsumer
       ↓
ReportScheduler
```

The database starts first because it has the lowest phase.

The message consumer starts after the database.

The scheduler starts last.

During shutdown, the order is reversed:

```text
ReportScheduler
       ↓
MessageConsumer
       ↓
DatabaseConnection
```

This allows components that depend on other components to shut down before those dependencies.

---

# Why Lifecycle Ordering Matters

Consider a message consumer that depends on a database.

During startup:

```text
Database
    ↓
Message consumer
```

The database should be available before the consumer starts processing messages.

During shutdown:

```text
Message consumer
    ↓
Database
```

The consumer should stop before the database is disconnected.

Lifecycle phases can help establish this ordering.

---

# Automatic Startup

`SmartLifecycle` components are automatically started by the application context by default.

Our implementation makes this explicit:

```java
@Override
public boolean isAutoStartup() {
    return true;
}
```

This is different from manually controlling the component:

```java

scheduler.start();

```

The purpose of `SmartLifecycle` is to allow Spring's application context to coordinate this behavior.

---

# Important Considerations

## Don't Use SmartLifecycle for Ordinary Initialization

If a bean only needs to perform some setup after its dependencies have been injected, `@PostConstruct` or `InitializingBean` is usually a better fit.

For example:

```text
Load configuration
Validate state
Prepare resources
```

These are initialization tasks.

`SmartLifecycle` is more appropriate when the component has an active running state.

Examples include:

```text
Start background worker
Start message listener
Start polling process
Start scheduler
```

---

## `SmartLifecycle` Is Container-Aware

The application doesn't have to manually coordinate:

```java

scheduler.start();
scheduler.stop();

```

Instead, Spring coordinates the component with the application context.

This is especially useful when an application has many independently managed lifecycle components.

---

# Complete Lifecycle Picture

At this point, Module 5 has covered several different parts of the Spring bean lifecycle.

A simplified picture is:

```text
                     Bean Creation
                          ↓
               Dependencies populated
                          ↓
          BeanPostProcessor (before)
                          ↓
                Initialization
                 ↙              ↘
        @PostConstruct    InitializingBean
                 ↘              ↙
                          ↓
          BeanPostProcessor (after)
                          ↓
                      Bean Ready
                          ↓
                    Bean in Use
                          ↓
                 Application Shutdown
                          ↓
             SmartLifecycle.stop()
                          ↓
                  Destruction
                   ↙           ↘
             @PreDestroy   DisposableBean
                          ↓
                    Bean Destroyed
```

`BeanFactoryPostProcessor` operates at an earlier container stage:

```text
Configuration
     ↓
BeanDefinitions
     ↓
BeanFactoryPostProcessor
     ↓
Bean Creation
```

This gives us a broader understanding of how Spring manages beans.

---

# Key Takeaways

- `SmartLifecycle` extends Spring's `Lifecycle` abstraction.
- It provides `start()`, `stop()`, and `isRunning()` behavior.
- `isAutoStartup()` determines whether Spring automatically starts the component.
- `getPhase()` controls lifecycle ordering.
- Lower phase values start earlier.
- Higher phase values start later.
- Shutdown occurs in the reverse phase order.
- `SmartLifecycle` is useful for active components that need coordinated startup and shutdown.
- `@PostConstruct` and `InitializingBean` are primarily initialization mechanisms.
- `@PreDestroy` and `DisposableBean` are primarily destruction mechanisms.
- `SmartLifecycle` connects a component's running state to the Spring application context.
- Spring can automatically start and stop `SmartLifecycle` components as the application context starts and shuts down.

---

# Module 5 Complete

With `SmartLifecycle`, we have completed **Module 5 — Bean Lifecycle**.

The module has progressed from basic initialization and destruction callbacks to advanced container lifecycle processing:

```text
InitializingBean
        ↓
DisposableBean
        ↓
@PostConstruct
        ↓
@PreDestroy
        ↓
BeanPostProcessor
        ↓
BeanFactoryPostProcessor
        ↓
SmartLifecycle
```

The next module will build on these Spring container fundamentals and introduce the next major concept in the learning path.