# Contributing to Spring by Example

Thank you for your interest in contributing!

The goal of this repository is to provide high-quality, runnable Spring Framework examples that are simple, educational, and easy to understand.

---

## Quick Start

Clone the repository

```bash
git clone https://github.com/<your-username>/spring-by-example.git
```

Navigate to an example

```bash
cd fundamentals/hello-bean
```

Run

```bash
./mvnw spring-boot:run
```

Explore the code and README.

---

## Guiding Principles

Every example should be:

- Focused on a single concept
- Small and easy to run
- Well documented
- Production-quality
- Beginner friendly

Avoid combining multiple unrelated concepts into one example.

---

## Before You Start

Check the [ROADMAP.md](ROADMAP.md) to check the status and avoid working on the already finished steps.

Please check existing issues and pull requests before creating a new example.

If you plan to add a large feature or multiple examples, consider opening an issue first to discuss the idea.

---

## Example Structure

Each example should contain:

```
example-name/
├── README.md
├── pom.xml
└── src/
```

The README should include:

- Problem
- Solution
- Project Structure
- Code Explanation
- Output (if applicable)
- Common Mistakes
- Further Reading

---

## Coding Style

- Use Java 21 (or the project's configured Java version)
- Keep examples minimal
- Prefer constructor injection
- Use meaningful class names
- Remove unnecessary dependencies
- Write readable code

---


## Branch Naming Convention

To keep the repository organized, please use the following naming conventions when creating branches.

Branches should follow this format:

Use lowercase letters and separate words with hyphens (`-`).

---

### Feature branches

For adding new examples, features, or learning material:

Examples:
```
feature/hello-bean
feature/dependency-injection

```

---



### Documentation branches

For documentation changes:

Examples:
```
docs/improve-readme
docs/add-contributing-guide

```

---

---

### Bug fix branches

For fixing issues:

Examples:
```
fix/maven-buid
fix/broken-example-test

```
---

---

## Good Branch Name Examples

```
feature/add-bean-lifecycle-example
docs/update/learning-path
refactor/maven-parent-structure
fix/update-spring-version
```

---

## Commit Message Convention

Write commit messages that clearly describe what changed.

Use:

```
(feature): Added Hello Bean Example
(feature): Added dependency injection example
(fix): Fixed Maven parent configuration
etc.
```
---

## Reporting Issues

If you discover an error, please open an issue including:

- What you expected
- What happened
- Steps to reproduce
- Relevant screenshots or logs (if applicable)

---

## Pull Requests

Before submitting a pull request:

- Ensure the example builds successfully.
- Keep the scope focused.
- Update documentation if needed.
- Follow the repository structure.

---

## After

Update the [ROADMAP.md](ROADMAP.md) to update the status and to prevent other contributors from working on the already.
finished steps.

Thank you for helping make Spring by Example better!

---