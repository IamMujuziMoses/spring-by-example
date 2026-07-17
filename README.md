# Spring by Example

> Learn the Spring Framework through small, focused, runnable examples.

Spring by Example is a curated collection of practical Spring Framework examples designed to help developers understand **how Spring works**, not just how to use it.

Each example focuses on a single concept, includes minimal production-quality code, and explains the reasoning behind the solution.

Whether you're learning Spring for the first time or exploring its internals, this repository aims to provide clear, runnable examples that answer real-world questions.

---

![Java](https://img.shields.io/badge/Java-21-red?style=for-the-badge&logo=paperlessngx)
![Spring](https://img.shields.io/badge/Spring-Framework-orange?style=for-the-badge&logo=spring&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge&logo=coveralls)
![Contributions Welcome](https://img.shields.io/badge/Contributions-Welcome-green?style=for-the-badge&logo=github)
![Coverage](https://img.shields.io/badge/coverage-10%25-indigo?style=for-the-badge&logo=codecov&logoColor=white)

---

## Goals

- ✅ One concept per example
- ✅ Small, runnable projects
- ✅ Clear explanations
- ✅ Real-world use cases
- ✅ Minimal boilerplate
- ✅ Easy to experiment with

---

## Why Spring by Example?

Spring has excellent documentation, but sometimes you don't need a complete guide—you just need a minimal, runnable example that demonstrates a single concept.

This repository focuses on:

- One concept at a time
- Minimal code
- Runnable examples
- Practical explanations
- Real-world scenarios

The goal isn't just to show *what* works, but *why* it works.

---

## Repository Structure

```
spring-by-example
├── fundamentals/
├── configuration/
├── injection/
├── lifecycle/
├── events/
├── aop/
├── transactions/
├── testing/
├── spring-boot/
├── advanced/
└── openmrs/
```

Each directory contains independent examples that can be run and explored individually.

---

## Learning Path

If you're new to Spring, follow this order:

1. Hello Bean
2. Dependency Injection
3. Constructor Injection
4. Setter Injection
5. Field Injection
6. Bean Scopes
7. Singleton vs Prototype
8. Bean Lifecycle
9. Lazy Initialization
10. @Component
11. @ComponentScan
12. @Import
13. AOP
14. Transactions
15. Testing
16. Spring Internals

---

## Planned Topics

### Fundamentals

- Hello Bean
- Bean Lifecycle
- Dependency Injection
- Bean Scopes
- Singleton vs Prototype

### Configuration

- `@Bean`
- `@Component`
- `@ComponentScan`
- `@Import`
- `@ImportResource`
- `@Profile`
- `@Conditional`

### Dependency Injection

- Constructor Injection
- Setter Injection
- Field Injection
- `@Qualifier`
- `@Primary`
- `@Lazy`

### Spring AOP

- JDK Dynamic Proxies
- CGLIB
- Custom Aspects
- Method Interceptors

### Transactions

- `@Transactional`
- Transaction Propagation
- Rollback Rules
- Programmatic Transactions

### Events

- Publishing Events
- Listening for Events
- Custom Events

### Testing

- Unit Testing
- Integration Testing
- TestContext Framework
- Mocking Beans

### Advanced

- BeanFactory
- ApplicationContext
- BeanPostProcessor
- FactoryBean
- ImportSelector
- BeanDefinitionRegistryPostProcessor
- Environment
- Property Sources

### OpenMRS

Examples demonstrating how Spring concepts are applied in OpenMRS.

---

## Contributing

Contributions are welcome!

Please read the [CONTRIBUTING.md](CONTRIBUTING.md) guide before opening a pull request.

---

## Roadmap

See our roadmap

Please read the [ROADMAP.md](ROADMAP.md) for the progress of the roadmap.

---

## License

This project is licensed under the MIT License.

See the [LICENSE](LICENSE) file for details.
