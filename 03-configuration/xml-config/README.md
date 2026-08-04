# XML Configuration

Before Java-based configuration and annotations became the preferred approach, Spring applications were commonly configured using XML.

XML configuration allows you to define beans, configure dependencies, and wire an entire application together without adding any Spring annotations to your Java classes.

In this example, you'll build a complete Spring application using **only XML configuration**.

---

## Learning Objectives

By the end of this module, you will be able to:

- Understand how XML configuration works.
- Learn how beans are declared in XML.
- Configure constructor injection using XML.
- Build an application without Spring annotations.
- Understand why many enterprise applications still use XML configuration.

---

## What Is XML Configuration?

XML configuration is the original way of configuring Spring applications.

Instead of using annotations such as `@Component`, `@Service`, or `@Configuration`, bean definitions are declared inside an XML file.

```xml
<bean id="greetingRepository" class="com.springbyexample.xmlconfiguration.GreetingRepository"/>
```

Spring reads the XML file, creates the bean instances, resolves their dependencies, and registers them in the `ApplicationContext`.

---

## Configuring Dependencies

Constructor injection can also be configured entirely in XML.

```xml
<bean id="greetingService" class="com.springbyexample.xmlconfiguration.GreetingService">
    <constructor-arg ref="greetingRepository"/>
</bean>
```

Spring automatically resolves the referenced bean and passes it to the constructor when creating the object.

---

## Complete Application Configuration

The entire application is wired together in a single XML file.

```text
applicationContext.xml
        │
        ├── GreetingRepository
        ├── GreetingService
        └── GreetingController
```

No Spring annotations are required in the Java classes.

---

## Running the Application

The application context is created directly from the XML configuration.

```java
try (var context = new ClassPathXmlApplicationContext("applicationContext.xml")) {

    GreetingController controller = context.getBean(GreetingController.class);

    System.out.println(controller.greet());
}
```

Expected output:

```text
Hello from XML configuration!
```

Although the application uses XML instead of annotations, the resulting beans behave exactly like any other Spring-managed beans.

---

## How XML Configuration Works

When the application starts, Spring performs the following steps:

```text
applicationContext.xml
        │
XML parsed
        │
BeanDefinitions created
        │
Constructor dependencies resolved
        │
Beans instantiated
        │
ApplicationContext ready
```

The IoC container manages XML-configured beans in exactly the same way it manages annotation-based beans.

---

## Why Use XML Configuration?

While Java configuration is now the recommended approach for new applications, XML configuration remains relevant for several reasons.

- Many mature Spring applications were originally built using XML.
- XML keeps configuration separate from application code.
- Legacy systems often continue using XML for stability and gradual migration.
- Understanding XML configuration makes it easier to maintain existing Spring applications.

---

## XML vs Annotations

| XML Configuration | Annotation-Based Configuration |
|-------------------|--------------------------------|
| Bean definitions stored in XML | Bean definitions stored in Java code |
| No Spring annotations required | Uses annotations such as `@Component` and `@Configuration` |
| Configuration separate from source code | Configuration lives alongside the code |
| Common in legacy applications | Preferred for modern applications |

Both approaches ultimately produce the same result: Spring-managed beans inside the `ApplicationContext`.

---

## Best Practices

- Prefer constructor injection over setter injection, even in XML.
- Keep XML configuration organized and readable.
- Group related bean definitions together.
- Prefer Java configuration for new projects.
- Understand XML configuration when maintaining existing applications.

---

## In Practice

Many enterprise applications still contain XML configuration.

Projects such as **OpenMRS** continue to use XML alongside Java configuration, allowing applications to evolve gradually while maintaining compatibility with existing configurations.

Understanding XML configuration is valuable when working with mature Spring applications.

---

## Key Takeaways

- XML configuration is the original Spring configuration mechanism.
- Beans and dependencies can be configured entirely through XML.
- Constructor injection works without annotations.
- XML-configured beans are managed exactly like annotation-based beans.
- Understanding XML configuration is essential for maintaining many existing Spring applications.

---

## What's Next?

Now that you've configured an application entirely with XML, the next example rebuilds the same application using **Java Configuration**.

Comparing the two approaches will help you understand their similarities, differences, and when each is most appropriate.