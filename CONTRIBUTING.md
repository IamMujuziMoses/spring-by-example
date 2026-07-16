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

## Commit Messages

Please use descriptive commit messages.

Examples:

```
Add @Import example

Improve Bean Lifecycle documentation

Fix typo in Constructor Injection example
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

Thank you for helping make Spring by Example better!