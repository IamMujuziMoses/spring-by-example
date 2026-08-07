# Roadmap

This roadmap outlines the planned examples for **Spring by Example**.

The goal is to provide small, focused, runnable examples that explain how the Spring Framework works—from the basics to advanced internals.

> **Legend**
>
> - [ ] Planned
> - [x] Completed
> - 🚧 In Progress

---

# Module 1 — Fundamentals

### Learn the core concepts behind the Spring IoC container.

- [x] Hello Bean
- [x] Dependency Injection
- [x] Constructor Injection
- [x] Setter Injection
- [x] Field Injection
- [x] Choosing an Injection Strategy

---

# Module 2 — Bean Scopes

### Learn how Spring manages bean instances within the Spring IoC container.

- [x] Overview
- [x] Singleton Scope
- [x] Prototype Scope

> **Note:** Web-specific scopes (`Request`, `Session`, and `Application`) are covered later in the Spring Web section after introducing the web application context.

---

# Module 3 — Configuration

### Learn the different ways Spring registers beans.

- [x] @Configuration
- [x] @Bean
- [x] @Component
- [x] @Service
- [x] @Repository
- [x] @Controller
- [x] @ComponentScan
- [x] @Import
- [x] @ImportResource
- [x] XML Configuration
- [x] Java Configuration
- [x] Mixing XML and Java Config

---

# Module 4 — Dependency Injection

### Master advanced dependency injection techniques.

- [x] @Primary
- [x] @Qualifier
- [x] Collection Injection
- [x] @Order
- 🚧 Map Injection
- 🚧 Bean Aliases
- [ ] Optional Dependencies
- [ ] ObjectProvider
- [ ] Circular Dependencies

---

# Module 5 — Bean Lifecycle

### Understand how Spring creates, initializes, and destroys beans.

- [ ] InitializingBean
- [ ] DisposableBean
- [ ] @PostConstruct
- [ ] @PreDestroy
- [ ] BeanPostProcessor
- [ ] BeanFactoryPostProcessor
- [ ] SmartLifecycle

---

# Module 6 — Spring Events

### Learn event-driven programming with Spring.

- [ ] Publishing Events
- [ ] Listening for Events
- [ ] Custom Events
- [ ] Transactional Events
- [ ] Async Events

---

# Module 7 — Spring AOP

### Understand Aspect-Oriented Programming.

- [ ] What is AOP?
- [ ] JDK Dynamic Proxies
- [ ] CGLIB Proxies
- [ ] Creating an Aspect
- [ ] Before Advice
- [ ] After Advice
- [ ] Around Advice
- [ ] Pointcuts
- [ ] Advice Ordering

---

# Module 8 — Transactions

### Learn declarative and programmatic transaction management.

- [ ] @Transactional
- [ ] Transaction Propagation
- [ ] Isolation Levels
- [ ] Rollback Rules
- [ ] Programmatic Transactions
- [ ] TransactionProxyFactoryBean

---

# Module 9 — Spring Boot

### Learn the foundations of Spring Boot.

- [ ] SpringApplication
- [ ] Auto Configuration
- [ ] Starter Dependencies
- [ ] Configuration Properties
- [ ] Profiles
- [ ] CommandLineRunner
- [ ] Actuator

---

# Module 10 — Spring Web (Spring MVC)

### Build web applications with the Spring MVC framework.

- [ ] DispatcherServlet
- [ ] Controllers
- [ ] Request Mapping
- [ ] Path Variables
- [ ] Request Parameters
- [ ] Request Body
- [ ] Response Body
- [ ] Model and View
- [ ] View Resolvers
- [ ] Exception Handling
- [ ] Request Scope
- [ ] Session Scope
- [ ] Application Scope

---

# Module 11 — Testing

### Learn how to test Spring applications.

- [ ] Unit Testing
- [ ] Spring TestContext
- [ ] @SpringBootTest
- [ ] MockBean
- [ ] TestConfiguration
- [ ] Integration Testing

---

# Module 12 — Advanced Spring

### Explore advanced Spring container features.

- [ ] BeanFactory
- [ ] ApplicationContext
- [ ] FactoryBean
- [ ] BeanDefinition
- [ ] BeanDefinitionRegistry
- [ ] ImportSelector
- [ ] DeferredImportSelector
- [ ] ImportBeanDefinitionRegistrar
- [ ] Environment
- [ ] Property Sources
- [ ] Resource Loading
- [ ] MessageSource
- [ ] ConversionService
- [ ] Type Conversion
- [ ] Validation
- [ ] SpEL
- [ ] @Lookup
- [ ] Method Injection

---

# Module 13 — Spring Internals

### Discover what happens behind the scenes.

- [ ] How Beans Are Registered
- [ ] How Dependency Injection Works
- [ ] How Component Scanning Works
- [ ] How @Autowired Works
- [ ] How @Transactional Works
- [ ] How AOP Proxies Are Created
- [ ] How Bean Post Processors Work
- [ ] Understanding DefaultListableBeanFactory
- [ ] Understanding ConfigurationClassPostProcessor

---

# Module 14 — OpenMRS Examples

### See how Spring concepts are applied in a real-world OpenMRS application.

Examples showing how Spring concepts are applied in OpenMRS.

- [ ] Service Registration
- [ ] Module Loading
- [ ] XML to Java Configuration
- [ ] OpenmrsBeanRegistrar
- [ ] ServiceContext
- [ ] AOP in OpenMRS
- [ ] Transaction Management
- [ ] Custom Spring Profiles

---

# Future Ideas

### Potential additions after v1.0.

- [ ] Kotlin Examples
- [ ] GraalVM Native Images
- [ ] Reactive Spring
- [ ] Spring AI
- [ ] Spring Modulith
- [ ] Spring Security
- [ ] Spring Data
- [ ] Spring Batch
- [ ] Spring Integration
- [ ] Spring Cloud

---

## Want to Contribute?

Contributions are welcome!

Feel free to pick any unchecked example, open an issue to discuss it, or submit a pull request.

See [CONTRIBUTING.md](CONTRIBUTING.md) for more details.