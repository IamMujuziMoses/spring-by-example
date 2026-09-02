# Spring by Example

> Learn the Spring Framework through small, focused, runnable examples.

**Spring by Example** is a curated collection of practical Spring Framework examples designed to help developers understand **how Spring works**, not just how to use it.

Rather than presenting a large sample application, this repository breaks Spring down into small, independent modules. Each example focuses on a single concept, contains minimal production-quality code, and includes detailed explanations to help you understand both the *how* and the *why*.

Whether you're learning Spring for the first time, preparing for interviews, or exploring the framework's internals, this repository provides a structured learning path from the fundamentals to advanced topics.

---

![Java](https://img.shields.io/badge/Java-21-red?style=for-the-badge&logo=openjdk)
![Spring](https://img.shields.io/badge/Spring-Framework-orange?style=for-the-badge&logo=spring&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge&logo=coveralls)
![Contributions Welcome](https://img.shields.io/badge/Contributions-Welcome-green?style=for-the-badge&logo=github)
![Coverage](https://img.shields.io/badge/coverage-71.43%25-indigo?style=for-the-badge&logo=codecov&logoColor=white)

---

## Goals

- ✅ One concept per example
- ✅ Small, runnable projects
- ✅ Clear explanations
- ✅ Production-quality code
- ✅ Comprehensive documentation
- ✅ Unit tests for every module
- ✅ Progressive learning path

---

## Why Spring by Example?

Spring has outstanding documentation, but there are times when you don't need an entire reference guide—you just need a small, runnable example that demonstrates a single concept.

This repository is built around that philosophy.

Every module is designed to:

- Teach one concept at a time.
- Keep examples small and easy to understand.
- Explain *why* something works, not just *how* to use it.
- Build naturally toward more advanced Spring concepts.
- Encourage experimentation by keeping each example independent.

---

## Repository Structure

```text
spring-by-example/
│
├── fundamentals/
│
├── bean-scopes/
│
├── configuration/
│
├── dependency-injection/
│
├── bean-lifecycle/
│
├── events/
│
├── aop/
│
├── transactions/
│
├── spring-boot/
│
├── spring-web-mvc/
│
├── testing/
│
├── advanced-spring/
│
├── spring-internals/
│
└── openmrs/
```

Each directory contains independent examples that can be built and run on their own.

---

## Learning Roadmap

The repository is organized into progressive modules.

### Module 1 — Fundamentals ✅

Learn the core concepts of the Spring IoC container.

- Hello Bean
- Dependency Injection
- Constructor Injection
- Setter Injection
- Field Injection
- Choosing an Injection Strategy

---

### Module 2 — Bean Scopes ✅

Learn how Spring manages bean instances.

- Overview
- Singleton Scope
- Prototype Scope

---

### Module 3 — Configuration ✅

Learn the different ways Spring registers beans.

- `@Configuration`
- `@Bean`
- `@Component`
- `@Service`
- `@Repository`
- `@Controller`
- `@ComponentScan`
- `@Import`
- XML Configuration
- Java Configuration
- `@ImportResource`
- Mixing XML and Java Configuration

---

### Module 4 — Dependency Injection ✅

Master advanced dependency injection techniques.

- `@Primary`
- `@Qualifier`
- Optional Dependencies
- Collection Injection
- Map Injection
- ObjectProvider
- Circular Dependencies
- Bean Aliases

---

### Module 5 — Bean Lifecycle ✅

Understand how Spring manages beans from creation to destruction.

- InitializingBean
- DisposableBean
- `@PostConstruct`
- `@PreDestroy`
- BeanPostProcessor
- BeanFactoryPostProcessor
- SmartLifecycle

---

### Module 6 — Events ✅

Explore Spring's event system.

- Publishing Events
- Listening for Events
- Custom Events
- Transactional Events
- Async Events

---

### Module 7 — Spring AOP ✅

Learn Aspect-Oriented Programming in Spring.

- What is AOP?
- JDK Dynamic Proxies
- CGLIB Proxies
- Creating an Aspect
- Before Advice
- After Advice
- After Returning Advice
- Around Advice
- Pointcuts
- Advice Ordering

---

### Module 8 — Transactions ✅

Understand transaction management.

- `@Transactional`
- Transaction Propagation
- Isolation Levels
- Rollback Rules
- Programmatic Transactions
- TransactionProxyFactoryBean

---

### Module 9 — Spring Boot ✅

Build modern Spring applications.

- SpringApplication
- Auto Configuration
- Starter Dependencies
- Configuration Properties
- Profiles
- CommandLineRunner
- Actuator

---

### Module 10 — Spring Web (Spring MVC) ✅

Build web applications with the Spring MVC framework.

- DispatcherServlet
- Controllers
- Request Mapping
- Path Variables
- Request Parameters
- Request Body
- Response Body
- Model and View
- View Resolvers
- Exception Handling
- Request Scope
- Session Scope
- Application Scope

---

### Module 11 — Testing 🚧

Write reliable Spring applications.

- Unit Testing
- Spring TestContext
- `@SpringBootTest`
- MockBean
- TestConfiguration
- Integration Testing

---

### Module 12 — Advanced Spring

Dive deeper into the framework.

- BeanFactory
- ApplicationContext
- FactoryBean
- BeanDefinition
- BeanDefinitionRegistry
- ImportSelector
- DeferredImportSelector
- ImportBeanDefinitionRegistrar
- Environment
- Property Sources
- Resource Loading
- MessageSource
- ConversionService
- Validation
- Spring Expression Language (SpEL)
- Method Injection
- `@Lookup`

---

### Module 13 — Spring Internals

Understand what happens behind the scenes.

- How Beans Are Registered
- How Dependency Injection Works
- How Component Scanning Works
- How `@Autowired` Works
- How `@Transactional` Works
- How AOP Proxies Are Created
- How Bean Post Processors Work
- Understanding DefaultListableBeanFactory
- Understanding ConfigurationClassPostProcessor

---

### Module 14 — OpenMRS Examples

See how Spring concepts are applied in a real-world project.

Examples include:

- Service Registration
- Module Loading
- XML to Java Configuration
- OpenmrsBeanRegistrar
- ServiceContext
- AOP in OpenMRS
- Transaction Management
- Custom Spring Profiles

---

## Getting Started

Clone the repository:

```bash
git clone https://github.com/<your-username>/spring-by-example.git
```

Choose any module and run it independently using Maven.

For example:

```bash
cd fundamentals/hello-bean
mvn clean test
```

Each module contains:

- A focused example
- Source code
- Unit tests
- Detailed documentation
- Suggested next steps

---

## Contributing

Contributions are welcome!

If you'd like to improve an example, fix an issue, or add a new learning module, please read the [CONTRIBUTING.md](CONTRIBUTING.md) guide before opening a pull request.

---

## Roadmap

The project's progress is tracked in [ROADMAP.md](ROADMAP.md), where you can see completed modules, upcoming topics, and future plans.

---

## License

This project is licensed under the MIT License.

See the [LICENSE](LICENSE) file for details.
