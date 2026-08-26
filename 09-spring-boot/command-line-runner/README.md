# CommandLineRunner Example

This example demonstrates how Spring Boot's `CommandLineRunner` can be used to execute code automatically after the application context has been initialized.

---

## What This Example Demonstrates

- What `CommandLineRunner` is
- How Spring Boot discovers a `CommandLineRunner` bean
- When the `run()` method is executed
- How command-line arguments are passed to the runner
- How startup logic can be separated from the main application class

---

## How It Works

The application entry point uses `SpringApplication.run()`:

```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

The `StartupRunner` is registered as a Spring bean using `@Component`:

```java
@Component
public class StartupRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("CommandLineRunner executed.");

        for (String arg : args) {
            System.out.println("Argument: " + arg);
        }
    }
}
```

Because `StartupRunner` implements `CommandLineRunner`, Spring Boot automatically calls its `run()` method after the application context has been initialized.

The `String... args` parameter contains the command-line arguments supplied when the application starts.

---

## Running the Example

From the `command-line-runner` directory:

```bash
mvn spring-boot:run
```

The application prints:

```text
CommandLineRunner executed.
```

You can also provide command-line arguments:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="hello spring boot"
```

The runner receives those arguments and prints them:

```text
CommandLineRunner executed.
Argument: hello
Argument: spring
Argument: boot
```

---

## Application Startup Flow

The simplified startup flow is:

```text
SpringApplication.run()
        │
        ▼
Create ApplicationContext
        │
        ▼
Discover and create Spring beans
        │
        ▼
ApplicationContext initialized
        │
        ▼
CommandLineRunner.run()
        │
        ▼
Application continues startup
```

This makes `CommandLineRunner` useful for small pieces of startup logic that should execute once the Spring application has been initialized.

---

## Why Use a Separate Runner?

The `CommandLineRunner` could be implemented directly on the `Application` class, but this example uses a separate component:

```java
@Component
public class StartupRunner implements CommandLineRunner {
    // ...
}
```

This keeps the application class focused on bootstrapping the application while the startup logic lives in its own Spring-managed component.

It also demonstrates that `CommandLineRunner` can be used as part of Spring's dependency injection model.

---

## Testing

The example includes a Spring Boot smoke test:

```java
@SpringBootTest
class ApplicationTest {

    @Test
    void shouldStartApplication() {
    }
}
```

Although the test method is empty, `@SpringBootTest` causes Spring Boot to create the application context before the test runs.

Therefore, the test verifies that the application can successfully start with its configured beans, including the `CommandLineRunner`.

If the application context fails to initialize, the test fails before reaching the empty test method.

---

## Key Takeaway

`CommandLineRunner` provides a simple way to execute code during Spring Boot application startup.

The important part is:

```java
@Override
public void run(String... args) {
    // startup logic
}
```

Spring Boot automatically invokes this method after the application context has been initialized.

This makes `CommandLineRunner` useful for tasks such as:

- Loading initial data
- Performing startup checks
- Printing application information
- Running one-time initialization logic
- Processing command-line arguments

For more complex startup behavior, Spring Boot also provides `ApplicationRunner`, which exposes command-line arguments through a more structured API.

---

## Technologies

- Java 21
- Spring Boot 4.1.0
- Maven
- JUnit 6

---

## Next

The next example will explore **Actuator**, demonstrating how Spring Boot provides production-ready features for monitoring and managing applications.