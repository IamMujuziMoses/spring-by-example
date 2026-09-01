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
- [x] Map Injection
- [x] ObjectProvider
- [x] Optional Dependencies
- [x] Circular Dependencies
- [x] Bean Aliases

---

# Module 5 — Bean Lifecycle

### Understand how Spring creates, initializes, and destroys beans.

- [x] InitializingBean
- [x] DisposableBean
- [x] @PostConstruct
- [x] @PreDestroy
- [x] BeanPostProcessor
- [x] BeanFactoryPostProcessor
- [x] SmartLifecycle

---

# Module 6 — Spring Events

### Learn event-driven programming with Spring.

- [x] Publishing Events
- [x] Listening for Events
- [x] Custom Events
- [x] Transactional Events
- [x] Async Events

---

# Module 7 — Spring AOP

### Understand Aspect-Oriented Programming.

- [x] What is AOP?
- [x] JDK Dynamic Proxies
- [x] CGLIB Proxies
- [x] Creating an Aspect
- [x] Before Advice
- [x] After Advice
- [x] After Returning Advice
- [x] Around Advice
- [x] Pointcuts
- [x] Advice Ordering

---

# Module 8 — Transactions

### Learn declarative and programmatic transaction management.

- [x] @Transactional
- [x] Transaction Propagation
- [x] Isolation Levels
- [x] Rollback Rules
- [x] Programmatic Transactions
- [x] TransactionProxyFactoryBean

---

# Module 9 — Spring Boot

### Learn the foundations of Spring Boot.

- [x] SpringApplication
- [x] Auto Configuration
- [x] Starter Dependencies
- [x] Configuration Properties
- [x] Profiles
- [x] CommandLineRunner
- [x] Actuator

---

# Module 10 — Spring Web (Spring MVC)

### Build web applications with the Spring MVC framework.

- [x] DispatcherServlet
- [x] Controllers
- [x] Request Mapping
- [x] Path Variables
- [x] Request Parameters
- [x] Request Body
- [x] Response Body
- [x] Model and View
- [x] View Resolvers
- [x] Exception Handling
- [x] Request Scope
- 🚧 Session Scope
- 🚧 Application Scope

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