# Hello Bean

## Goal

Learn the basic concept of a Spring Bean and how Spring manages objects.

---

## What is a Bean?

A Spring Bean is simply an object that is created, managed, and stored inside the Spring ApplicationContext.

Instead of creating objects manually:

```java
HelloService service = new HelloService();
```

Spring creates and manages the object for us.

---

## How It Works

```
@Configuration
     |
@Bean method
     |
BeanDefinition
     |
ApplicationContext
     |
HelloService Bean
```

---

## Creating the Bean

The `@Bean` annotation tells Spring:

"Create an object using this method and manage it as a bean."

```java
@Bean
public HelloService helloService() {
    return new HelloService();
}
```

---

## Getting the Bean

The ApplicationContext stores the bean.

We can retrieve it:

```java
HelloService service = context.getBean(HelloService.class);
```

---

## Why Use Spring Beans?

Spring manages:

- Object creation
- Dependencies
- Lifecycle
- Configuration
- Proxies
- Transactions
- Events

---

## Run

```bash
mvn clean compile

java com.springbyexample.hellobean.Main
```

---

## Next

Continue with:

- Dependency Injection
- Constructor Injection
- Setter Injection
- Field Injection