# Module Loading

OpenMRS is designed as a modular application. Functionality can be packaged into modules that are loaded and started as part of the OpenMRS application lifecycle.

This example demonstrates the **OpenMRS module loading lifecycle** using a simplified, self-contained implementation.

The example focuses on the distinction between **loading** a module and **starting** a module, as well as the lifecycle callbacks that occur during module startup.

## Learning Objectives

- Understand the purpose of modules in OpenMRS.
- Understand the difference between loading and starting a module.
- Understand the role of a module loader.
- Understand module lifecycle callbacks.
- Understand how module startup relates to the Spring application context.
- Understand the relationship between the simplified example and OpenMRS's real module infrastructure.

---

## What Is an OpenMRS Module?

OpenMRS uses a modular architecture that allows functionality to be packaged separately from the core application.

An OpenMRS module is typically packaged as an `.omod` file and can provide additional functionality to the application.

Conceptually:

```text
OpenMRS Core
     │
     ├── Module A
     ├── Module B
     └── Module C
```

Each module can have its own functionality, configuration, services, and Spring components.

This allows OpenMRS to be extended without placing every feature directly inside the core application.

---

## Module Loading vs Module Starting

Loading and starting are separate lifecycle operations.

### Loading

Loading makes a module available to the application.

```text
Module
  │
  ▼
Load
  │
  ▼
Loaded
```

At this point, the module has been loaded, but it has not necessarily started executing its functionality.

### Starting

Starting moves a loaded module into its running state.

```text
Loaded Module
      │
      ▼
    Start
      │
      ▼
Module Lifecycle
      │
      ▼
   Running
```

This distinction is important because a module may be loaded before it is started.

---

## Module Loading Lifecycle

The simplified lifecycle demonstrated by this example is:

```text
ModuleLoader.load()
        │
        ▼
   Module Loaded
        │
        ▼
ModuleLoader.start()
        │
        ▼
willRefreshContext()
        │
        ▼
Spring Context Refresh
        │
        ▼
contextRefreshed()
        │
        ▼
started()
        │
        ▼
   Module Running
```

The lifecycle demonstrates the relationship between OpenMRS module startup and the Spring application context.

---

## The Example Module

The example uses a simple `ExampleModule` class to represent a loaded OpenMRS module.

```java
public class ExampleModule {

    private final String moduleId;
    
    private boolean loaded;
    
    private boolean started;

    public ExampleModule(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public boolean isStarted() {
        return started;
    }

    void markLoaded() {
        loaded = true;
    }

    void markStarted() {
        started = true;
    }
}
```

The module maintains two pieces of lifecycle state:

- `loaded` — whether the module has been loaded.
- `started` — whether the module has been started.

Initially:

```text
loaded = false
started = false
```

After loading:

```text
loaded = true
started = false
```

After starting:

```text
loaded = true
started = true
```

---

## Loading a Module

The `ModuleLoader` is responsible for loading the module.

```java
public void load(ExampleModule module) {
    if (module.isLoaded()) {
        return;
    }

    module.markLoaded();
}
```

Calling:

```java
moduleLoader.load(module);
```

changes the module from:

```text
Not Loaded
```

to:

```text
Loaded
```

It does not start the module.

This demonstrates an important lifecycle distinction:

```text
load() != start()
```

---

## Starting a Module

A module must be loaded before it can be started.

```java
public void start(ExampleModule module) {
    if (!module.isLoaded()) {
        throw new IllegalStateException("Module must be loaded before it can be started");
    }

    if (module.isStarted()) {
        return;
    }

    activator.willRefreshContext();

    refreshSpringContext();

    activator.contextRefreshed();
    activator.started();

    module.markStarted();
}
```

The method first verifies that the module has been loaded.

```java
if (!module.isLoaded()) {
    throw new IllegalStateException("Module must be loaded before it can be started");
}
```

This prevents an invalid lifecycle transition:

```text
Not Loaded
     │
     └── start() ──X──> Running
```

Instead, the correct sequence is:

```text
Not Loaded
     │
     ▼
   load()
     │
     ▼
  Loaded
     │
     ▼
   start()
     │
     ▼
  Running
```

---

## Module Activator

The `ExampleModuleActivator` represents the lifecycle callbacks associated with module startup.

```java
public class ExampleModuleActivator {

    public void willRefreshContext() {
        System.out.println("Module will refresh Spring context");
    }

    public void contextRefreshed() {
        System.out.println("Module Spring context refreshed");
    }

    public void started() {
        System.out.println("Module started");
    }
}
```

The callbacks represent three important points in the lifecycle.

### `willRefreshContext()`

This callback occurs before the Spring application context is refreshed.

```text
Module startup
      │
      ▼
willRefreshContext()
      │
      ▼
Spring context refresh
```

### `contextRefreshed()`

This callback occurs after the Spring context has been refreshed.

```text
Spring context refresh
      │
      ▼
contextRefreshed()
```

### `started()`

This callback represents the module reaching its started state.

```text
contextRefreshed()
      │
      ▼
started()
      │
      ▼
Module Running
```

---

## Why Does the Spring Context Matter?

OpenMRS modules can contribute Spring-managed components to the application.

Conceptually:

```text
OpenMRS Module
      │
      ├── Services
      ├── Components
      ├── Configuration
      └── Spring Beans
              │
              ▼
      Spring ApplicationContext
```

Therefore, module startup and Spring context initialization are closely related.

A module may need to perform certain operations before or after the Spring context is refreshed.

This is why the example explicitly includes:

```java
activator.willRefreshContext();

refreshSpringContext();

activator.contextRefreshed();
```

The `refreshSpringContext()` method is intentionally simplified. It represents the point where the module lifecycle interacts with Spring without requiring the complete OpenMRS runtime.

---

## Complete Example

The application brings the pieces together.

```java
public class ModuleLoadingApplication {

    public static void main(String[] args) {
        ExampleModule module = new ExampleModule("example-module");
        ExampleModuleActivator activator = new ExampleModuleActivator();
        ModuleLoader moduleLoader = new ModuleLoader(activator);

        moduleLoader.load(module);

        System.out.println("Module loaded: " + module.isLoaded());

        moduleLoader.start(module);

        System.out.println("Module started: " + module.isStarted());
    }
}
```

The output is:

```text
Module loaded: true
Module will refresh Spring context
Refreshing Spring context
Module Spring context refreshed
Module started
Module started: true
```

This output makes the lifecycle visible:

```text
Module loaded
      ↓
willRefreshContext()
      ↓
Spring context refresh
      ↓
contextRefreshed()
      ↓
started()
      ↓
Module started
```

---

## Preventing Invalid Lifecycle Transitions

The example prevents a module from being started before it has been loaded.

```java
assertThrows(IllegalStateException.class,() -> moduleLoader.start(module));
```

This demonstrates:

```text
start()
  │
  ▼
Is module loaded?
  │
  ├── No → IllegalStateException
  │
  └── Yes → Continue startup
```

The loader also prevents the same lifecycle operation from being performed unnecessarily.

```java
if (module.isLoaded()) {
    return;
}
```

and:

```java
if (module.isStarted()) {
    return;
}
```

This makes the example's lifecycle operations idempotent.

---

## Relationship to OpenMRS

This example is intentionally simplified, but it models important concepts from the real OpenMRS module infrastructure.

The real OpenMRS application has dedicated module infrastructure for loading and starting modules, including classes such as:

- `ModuleUtil`
- `ModuleFactory`
- `Module`
- `ModuleActivator`

The simplified example avoids requiring the entire OpenMRS runtime so that the lifecycle can be studied independently.

Conceptually, the real architecture can be viewed as:

```text
OpenMRS Startup
      │
      ▼
ModuleUtil
      │
      ▼
ModuleFactory
      │
      ├── Load modules
      │
      └── Start modules
              │
              ▼
       Module Lifecycle
              │
              ▼
       Spring Integration
```

The actual OpenMRS implementation contains additional concerns such as module dependencies, mandatory modules, module directories, module descriptors, class loading, and application-context integration.

Those concerns are intentionally outside the scope of this small learning example.

---

## Why Not Use `ModuleFactory` Directly?

The goal of `spring-by-example` is to isolate individual concepts.

Using the full OpenMRS `ModuleFactory` would introduce a significant amount of OpenMRS infrastructure that is not necessary to understand the fundamental lifecycle.

Instead, this example provides a small `ModuleLoader` that makes the lifecycle explicit:

```text
load()
  ↓
loaded
  ↓
start()
  ↓
willRefreshContext()
  ↓
Spring context refresh
  ↓
contextRefreshed()
  ↓
started()
```

This makes the underlying concept easier to understand before examining the actual OpenMRS implementation.

---

## Dependencies

The example uses:

- JUnit Jupiter for testing.

No database or full OpenMRS runtime is required.

The example intentionally keeps the implementation self-contained so that the module lifecycle can be explored without additional OpenMRS infrastructure.

---

## Running the Example

From the `module-loading` directory:

```bash
mvn clean install
```

The application can then be run from the IDE using:

```text
ModuleLoadingApplication
```

Expected output:

```text
Module loaded: true
Module will refresh Spring context
Refreshing Spring context
Module Spring context refreshed
Module started
Module started: true
```

---

## Key Takeaways

- OpenMRS uses a modular architecture to extend the core application.
- A module being **loaded** does not necessarily mean it has been **started**.
- Module loading and module startup are separate lifecycle stages.
- Module startup can involve lifecycle callbacks.
- Module lifecycle events can interact with the Spring application context.
- `ModuleActivator` provides lifecycle callbacks for module initialization.
- The simplified `ModuleLoader` makes the lifecycle explicit without requiring the complete OpenMRS runtime.
- The real OpenMRS infrastructure adds additional concerns such as dependencies, descriptors, class loading, and module management.

The most important concept is:

```text
Module
  │
  ▼
Load
  │
  ▼
Loaded
  │
  ▼
Start
  │
  ▼
Lifecycle Callbacks
  │
  ▼
Running Module
```

---

## What's Next?

The next example explores **XML to Java Configuration**.

It will demonstrate how Spring configuration traditionally expressed in XML can be represented using Java configuration with `@Configuration` and `@Bean`, while also showing how XML and Java configuration can coexist during a migration.