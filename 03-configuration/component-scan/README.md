# `@ComponentScan`

The `@ComponentScan` annotation tells Spring where to search for classes annotated with stereotype annotations such as `@Component`, `@Service`, `@Repository`, and `@Controller`.

Rather than registering each bean manually using `@Bean`, component scanning allows Spring to automatically discover, instantiate, and register eligible classes as managed beans.

In this example, you'll learn how Spring scans packages and automatically wires together the application's components.

---

## Learning Objectives

By the end of this module, you will be able to:

- Understand the purpose of `@ComponentScan`.
- Learn how Spring discovers beans automatically.
- Understand which annotations are detected during component scanning.
- Learn how Spring builds the application context.
- Know when to use `@ComponentScan`.

---

## What Is `@ComponentScan`?

`@ComponentScan` instructs Spring to search one or more packages for classes annotated with stereotype annotations.

```java
@Configuration
@ComponentScan("com.springbyexample.componentscan")
public class AppConfig {

}
```

When the application starts, Spring scans the specified package, discovers eligible components, creates bean definitions, instantiates the beans, and registers them in the `ApplicationContext`.

---

## What Does Spring Discover?

`@ComponentScan` automatically detects classes annotated with:

- `@Component`
- `@Service`
- `@Repository`
- `@Controller`

Because these annotations are all stereotype annotations, Spring treats them as candidates for automatic bean registration.

---

## Example

The application consists of three Spring-managed components.

```text
GreetingController
        │
GreetingService
        │
GreetingRepository
```

Each component depends on the next, and Spring automatically resolves these dependencies during bean creation.

---

## Running the Application

After creating the application context, the controller can be retrieved like any other Spring bean.

```java
GreetingController controller = context.getBean(GreetingController.class);

System.out.println(controller.greet());
```

Expected output:

```text
Hello from GreetingRepository!
```

Although only the controller is retrieved, Spring has already discovered and created the service and repository beans as part of the application context.

---

## How Component Scanning Works

When Spring starts, the following process occurs:

```text
@ComponentScan
        │
Search package
        │
@Component
@Service
@Repository
@Controller
        │
Create BeanDefinitions
        │
Instantiate Beans
        │
Register in ApplicationContext
```

This entire process happens automatically without explicitly defining each bean using `@Bean`.

---

## Package Scanning

In this example, Spring scans a single package.

```java
@ComponentScan("com.springbyexample.componentscan")
```

Every eligible class inside this package (and its subpackages) is automatically registered as a Spring bean.

---

## Why Use `@ComponentScan`?

Without component scanning, every bean would need to be declared manually.

```java
@Bean
GreetingService greetingService() {
    return new GreetingService(greetingRepository());
}
```

As applications grow, manually registering every bean becomes difficult to maintain.

`@ComponentScan` greatly reduces configuration by automatically discovering components.

---

## Best Practices

- Place configuration classes near the root package.
- Scan only the packages your application needs.
- Keep related components organized within the same package hierarchy.
- Use stereotype annotations consistently to communicate architectural responsibilities.
- Prefer `basePackageClasses` over string-based package names in larger applications for type safety.

---

## How It Works Internally

When the application starts, Spring performs the following steps:

```text
Application starts
        │
@Configuration found
        │
@ComponentScan processed
        │
Classpath scanned
        │
Annotated classes discovered
        │
BeanDefinitions created
        │
Dependencies resolved
        │
Beans instantiated
        │
ApplicationContext ready
```

---

## Key Takeaways

- `@ComponentScan` enables automatic bean discovery.
- Spring scans packages for stereotype annotations.
- `@Component`, `@Service`, `@Repository`, and `@Controller` are automatically detected.
- Component scanning reduces configuration and improves maintainability.
- Spring automatically resolves dependencies between discovered beans.

---

## What's Next?

So far, you've learned two ways to register Spring beans:

- Explicit registration using `@Bean`
- Automatic registration using `@ComponentScan`

The next example explores `@Import`, which allows one configuration class to import another, making Spring configuration more modular and easier to organize.