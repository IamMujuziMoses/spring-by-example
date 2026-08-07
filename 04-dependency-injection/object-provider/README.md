# ObjectProvider

Spring normally injects dependencies when creating a bean.

For example:

```java
@Component
public class ReportManager {

	private final Report report;

	public ReportManager(Report report) {
		this.report = report;
	}
}
```

In this case, Spring resolves and injects the `Report` bean during application startup.

However, there are situations where we need more control:

- The dependency should be created only when needed.
- The dependency may not exist.
- A new instance should be requested each time.
- We need access to multiple beans dynamically.

Spring provides `ObjectProvider` for these scenarios.

This example demonstrates how `ObjectProvider` allows lazy and programmatic access to beans from the Spring container.

---

## Learning Objectives

By the end of this module, you will be able to:

- Understand the purpose of `ObjectProvider`.
- Learn how `ObjectProvider` differs from direct injection.
- Retrieve beans lazily from the Spring container.
- Understand how `ObjectProvider` works with prototype scoped beans.
- Learn how to handle optional dependencies.
- Learn how to retrieve multiple beans using `ObjectProvider`.

---

# The Problem

Consider a reporting system.

A report object may be expensive to create, and the application only needs it when a user requests a report.

A normal dependency:

```java

private final Report report;

```

causes Spring to resolve the dependency immediately.

For prototype scoped beans, this can also cause unexpected behavior:

```text
ReportManager
      |
      |
Single Report Instance
```

The application receives one instance and continues using it.

---

# The Solution

`ObjectProvider` allows a bean to be requested from Spring when it is needed.

Instead of:

```java

private final Report report;

```

we inject:

```java

private final ObjectProvider<Report> reportProvider;

```

Then retrieve the bean:

```java

Report report = reportProvider.getObject();

```

Spring creates the object only when requested.

---

# Example

## Report Interface

```java
public interface Report {

	void generate();

}
```

---

## SalesReport

The report is configured as a prototype bean.

```java
@Component
@Scope("prototype")
public class SalesReport implements Report {

	@Override
	public void generate() {
		System.out.println("Generating sales report");
	}
}
```

Unlike singleton beans, prototype beans create a new instance every time they are requested.

---

# Using ObjectProvider

```java
@Component
public class ReportManager {

	private final ObjectProvider<Report> reportProvider;

	public ReportManager(ObjectProvider<Report> reportProvider) {

		this.reportProvider = reportProvider;
	}

	public void generateReports() {

		Report first = reportProvider.getObject();

		Report second = reportProvider.getObject();
	}
}
```

Each call to:

```java

reportProvider.getObject();

```

asks Spring for a new instance.

---

# Running the Example

```java

manager.generateReports();

```

Output:

```text
Creating SalesReport instance
Generating sales report

Creating SalesReport instance
Generating sales report
```

Because the bean is prototype scoped:

```java

first != second

```

---

# ObjectProvider Methods

## getObject()

Returns an instance from the Spring container.

```java

Report report = provider.getObject();

```

If the bean does not exist, Spring throws an exception.

---

## getIfAvailable()

Returns the bean if it exists.

```java

Report report = provider.getIfAvailable();

```

If no bean exists, it returns `null`.

Useful for optional dependencies.

---

## ifAvailable()

Runs code only when the bean exists.

```java

provider.ifAvailable(Report::generate);

```

---

## stream()

Retrieves all matching beans.

```java

provider.stream().forEach(Report::generate);

```

This works similarly to Collection Injection.

---

# ObjectProvider vs Direct Injection

| Direct Injection | ObjectProvider |
|---|---|
| Bean resolved immediately | Bean requested when needed |
| Simple dependency | Dynamic dependency access |
| Good for required beans | Good for optional or lazy beans |
| One injected instance | Can request multiple instances |

---

# ObjectProvider vs Collection Injection

Both can access multiple beans.

## Collection Injection

```java

List<Report> reports;

```

Spring provides all beans immediately.

---

## ObjectProvider

```java

ObjectProvider<Report> reports;

```

Beans are retrieved only when requested.

---

# ObjectProvider vs @Lazy

Both support delayed creation, but they solve different problems.

## @Lazy

Delays creation of a dependency until first use.

Example:

```java

@Lazy
private Report report;

```

---

## ObjectProvider

Provides programmatic control:

```java

provider.getObject();

```

The application decides when and how often to retrieve the bean.

---

# Real World Examples

## Prototype Objects

Objects that should not be shared:

- Reports
- Export tasks
- Documents
- Processing jobs

---

## Optional Integrations

A feature may only be available when a bean exists.

Example:

```java

ObjectProvider<PaymentGateway>

```

The application can run without a payment integration.

---

## Lazy Resources

Expensive resources can be created only when required.

Examples:

- External clients
- Large processors
- Cache loaders

---

# Testing ObjectProvider

The test verifies that prototype beans return different instances:

```java
@Test
void shouldReturnDifferentInstancesForPrototypeBean() {

	try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

		var provider = context.getBeanProvider(Report.class);

		Report first = provider.getObject();
		Report second = provider.getObject();

		assertNotSame(first, second);
	}
}
```

The assertion confirms:

- `ObjectProvider` retrieves beans from Spring.
- Prototype scope creates a new instance for each request.

---

# Best Practices

- Use direct injection for required dependencies.
- Use `ObjectProvider` when lazy access is needed.
- Use `getIfAvailable()` for optional dependencies.
- Prefer constructor injection over field injection.
- Avoid using `ObjectProvider` everywhere; it should solve a specific problem.

---

# Key Takeaways

- `ObjectProvider` provides access to beans from the Spring container.
- Beans are retrieved only when requested.
- It works well with prototype scoped beans.
- It supports optional dependencies.
- It can retrieve multiple beans dynamically.
- It gives more control than direct dependency injection.

---

# What's Next?

The next examples explore:

- Bean Aliases
- Optional Dependencies
- Circular Dependencies