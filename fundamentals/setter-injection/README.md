# Setter Injection

## Goal

Learn how Spring uses **setter injection** to provide dependencies to objects and understand when it is appropriate to use compared to constructor injection.

By the end of this example, you will understand:

- What setter injection is
- How Spring injects dependencies using setter methods
- When setter injection is useful
- The advantages and disadvantages of setter injection
- How setter injection compares to constructor injection

---

## Prerequisites

Before continuing, complete:

- [Hello Bean](../hello-bean)
- [Dependency Injection](../dependency-injection)
- [Constructor Injection](../constructor-injection)

These examples introduced:

- Spring Beans
- `ApplicationContext`
- Dependency Injection
- Constructor Injection

---

## The Problem

Some dependencies are optional.

For example, a `ReportService` can generate reports without printing them. Printing is simply an additional feature.

Instead of requiring a `PrinterService` during object creation, it can be provided later.

---

## What is Setter Injection?

Setter injection is a dependency injection technique where Spring provides an object's dependencies by calling its setter methods after the object has been created.

Instead of supplying the dependency through the constructor, Spring creates the object first and then injects the dependency.

```text
ApplicationContext
        |
        +----------------+
        |                |
 PrinterService    ReportService
                           |
                setPrinterService(...)
```

---

## Example

`PrinterService` is responsible for printing reports.

```java
public class PrinterService {

    public void print(String report) {
        System.out.println("Printing: " + report);
    }
}
```

`ReportService` depends on `PrinterService`.

```java
public class ReportService {

    private PrinterService printerService;

    public void setPrinterService(PrinterService printerService) {
        this.printerService = printerService;
    }

    public void generateReport() {

        if (printerService != null) {
            printerService.print("Monthly Sales Report");
        }
    }
}
```

Notice that `ReportService` does not require a constructor parameter.

Instead, Spring injects the dependency by calling the setter method.

---

## Java Configuration

Spring creates both beans and injects the dependency using the setter.

```java
@Configuration
public class AppConfig {

    @Bean
    public PrinterService printerService() {
        return new PrinterService();
    }

    @Bean
    public ReportService reportService(PrinterService printerService) {

        ReportService reportService = new ReportService();

        reportService.setPrinterService(printerService);

        return reportService;
    }
}
```

Unlike constructor injection, the object is created first, then configured with its dependency.

---

## Running the Example

Run the `Main` class.

Expected output:

```text
Printing: Monthly Sales Report
```

---

## Advantages of Setter Injection

### Optional dependencies

Setter injection works well when a dependency is optional.

An object can still be created even if the dependency has not been provided.

---

### Flexible configuration

Dependencies can be replaced after the object has been created.

This can be useful in some specialized scenarios.

---

### Clear separation

Object creation and dependency configuration happen in separate steps.

---

## Constructor Injection vs Setter Injection

| Feature | Constructor Injection | Setter Injection |
|----------|----------------------|------------------|
| Required dependencies | ✅ Yes | ❌ No |
| Supports immutable fields | ✅ Yes | ❌ No |
| Dependencies are explicit | ✅ Yes | ⚠️ Less explicit |
| Allows dependency changes | ❌ No | ✅ Yes |
| Recommended for most applications | ✅ Yes | ⚠️ Mainly for optional dependencies |

---

## Behind the Scenes

When the `ApplicationContext` starts, Spring first creates the object.

```text
new ReportService()
```

At this point:

```text
printerService == null
```

Spring then finds the setter method.

```java
setPrinterService(PrinterService printerService)
```

It locates the `PrinterService` bean in the container and calls the setter automatically.

Internally, the process looks like this:

```text
Application starts
        |
Create ApplicationContext
        |
Read AppConfig
        |
Create PrinterService bean
        |
Create ReportService bean
        |
Call setPrinterService(...)
        |
Store ReportService in the ApplicationContext
```

After the setter has been called, the bean is fully initialized.

---

## Common Mistakes

### Forgetting to inject the dependency

If the setter is never called, the dependency remains `null`.

This can result in a `NullPointerException`.

---

### Using setter injection for required dependencies

If an object cannot function without a dependency, constructor injection is usually the better choice.

Constructor injection guarantees that all required dependencies are available when the object is created.

---

### Modifying dependencies unexpectedly

Since setter injection allows dependencies to be replaced, changing them after initialization may lead to unexpected behavior.

---

## Try It Yourself

As an exercise:

1. Create an `EmailService`.
2. Add a setter for `EmailService` in `ReportService`.
3. Generate a report.
4. Print the report.
5. Email the report.

---

## Key Takeaways

- Setter injection provides dependencies after object creation.
- It is useful for optional dependencies.
- Objects can exist before all dependencies have been configured.
- Constructor injection is generally preferred for required dependencies.
- Spring supports both approaches depending on the application's needs.

---

## What's Next?

Continue with:

- Field Injection
- Choosing an Injection Strategy
- Bean Scopes
- Singleton vs Prototype