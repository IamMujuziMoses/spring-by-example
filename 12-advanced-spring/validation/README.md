# Validation

Spring provides a validation abstraction for validating objects and reporting validation errors in a consistent way.

The central interface is `Validator`, which separates validation rules from the object being validated. A validator examines a target object, applies validation rules, and records any violations in an `Errors` object.

This example demonstrates Spring's `Validator` abstraction using a simple `User` object.

## Learning Objectives

By completing this example, you will understand:

- What Spring's `Validator` interface is
- Why validation logic can be separated from domain objects
- How `supports()` determines which types a validator can validate
- How `validate()` performs validation
- What the `target` parameter represents
- How the `Errors` abstraction records validation failures
- How `rejectValue()` records field-specific errors
- How `ValidationUtils` performs common validation checks
- How `BeanPropertyBindingResult` stores validation results
- How to determine whether an object contains validation errors
- How to test valid and invalid objects

---

## What Is Validation?

Validation is the process of checking whether an object satisfies a set of rules.

For example, a user might be required to have:

- A non-empty name
- A non-empty email address
- A valid email format

A validation process can therefore be represented as:

```text
Object
  │
  ▼
Validator
  │
  ├── valid
  │
  └── invalid
         │
         ▼
       Errors
```

Spring provides a common abstraction for this process through:

```java
org.springframework.validation.Validator
```

---

## Why Does Spring Provide a Validator Abstraction?

Without a validation abstraction, validation rules can become mixed into domain objects or duplicated throughout an application.

For example:

```java
if (user.getName() == null || user.getName().isBlank()) {
    // validation failure
}
```

Such logic could potentially appear in controllers, services, or other parts of an application.

Spring allows validation rules to be placed in a dedicated validator:

```text
User
 │
 │ contains data
 ▼
UserValidator
 │
 │ contains validation rules
 ▼
Errors
 │
 └── contains validation failures
```

This separates the object's data from the rules used to validate it.

---

## The Validator Interface

The Spring `Validator` interface provides two primary methods:

```java
boolean supports(Class<?> clazz);

void validate(Object target, Errors errors);
```

The methods have different responsibilities.

### supports()

`supports()` determines whether the validator can validate a particular type.

```java
@Override
public boolean supports(Class<?> clazz) {
    return User.class.equals(clazz);
}
```

This validator therefore supports:

```java
User.class
```

but does not support unrelated types such as:

```java
String.class
```

### validate()

`validate()` performs the actual validation.

```java
@Override
public void validate(Object target, Errors errors) {
    // validation rules
}
```

The method receives two important parameters:

```text
target
    → the object being validated

errors
    → where validation failures are recorded
```

For example:

```java
validator.validate(user, errors);
```

means that:

```text
user
  │
  ▼
target

errors
  │
  ▼
validation results
```

---

## What Is target?

The `target` parameter is the object that the validator has been asked to validate.

For example:

```java
User user = new User("Moses", "moses@example.com");

validator.validate(user, errors);
```

Inside `validate()`:

```java
Object target
```

refers to that same `User` instance.

When custom validation rules need to inspect the object's values, the target can be cast to its expected type:

```java
User user = (User) target;
```

The validator can then inspect its properties:

```java
if (!user.getEmail().contains("@")) {
    errors.rejectValue("email","email.invalid");
}
```

The `target` parameter therefore gives the validator access to the object being validated.

---

## What Is Errors?

The `Errors` interface represents validation errors.

The validator does not normally return a boolean such as:

```java
true
```

or:

```java
false
```

Instead, it records validation failures in the supplied `Errors` object.

For example:

```java
errors.rejectValue("email","email.invalid");
```

This records an error against the `email` field.

After validation, the caller can inspect the errors:

```java
if (errors.hasErrors()) {
    // validation failed
}
```

The overall model is:

```text
Validator
    │
    │ validate(target, errors)
    ▼
Errors
    │
    ├── no errors
    │
    └── field errors
```

---

## BeanPropertyBindingResult

`BeanPropertyBindingResult` is an implementation of Spring's `Errors` abstraction.

It can be created for the object being validated:

```java
Errors errors = new BeanPropertyBindingResult(user, "user");
```

The first argument is the target object:

```text
user
```

The second argument is the object name:

```text
user
```

This creates the connection between the object and the errors being collected.

Conceptually:

```text
BeanPropertyBindingResult
        │
        ├── target → User
        │
        └── errors → validation failures
```

---

## How ValidationUtils Works

Spring provides `ValidationUtils` for common validation operations.

For example:

```java
ValidationUtils.rejectIfEmptyOrWhitespace(errors,"name","name.required");
```

This checks the `name` property and registers an error when the value is empty or contains only whitespace.

The validator does not need to manually write the equivalent empty-value check.

Instead of:

```java
if (user.getName() == null || user.getName().isBlank()) {

    errors.rejectValue("name","name.required");
}
```

we can use:

```java
ValidationUtils.rejectIfEmptyOrWhitespace(errors,"name","name.required");
```

---

## Why Doesn't ValidationUtils Receive target?

The `Errors` object is created for the object being validated:

```java
Errors errors = new BeanPropertyBindingResult(user, "user");
```

The `Errors` implementation therefore knows which object it represents.

When we call:

```java
ValidationUtils.rejectIfEmptyOrWhitespace(errors,"name","name.required");
```

Spring can use the `Errors` object's associated target and property information to perform the field check and record the error.

Conceptually:

```text
User
 │
 │ name = ""
 ▼
BeanPropertyBindingResult
 │
 │ associated with User
 ▼
ValidationUtils
 │
 │ checks "name"
 ▼
Errors
 │
 └── name.required
```

---

## rejectValue()

`rejectValue()` records an error for a specific field.

For example:

```java
errors.rejectValue("email","email.invalid");
```

The first argument is the field:

```text
email
```

The second argument is the error code:

```text
email.invalid
```

This allows the caller or a higher-level framework to identify which field failed validation and why.

---

## Example Domain Object

The example uses a simple `User` class:

```java
public class User {

    private final String name;

    private final String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
```

The class contains data but does not contain validation rules.

---

## UserValidator

The validation rules are defined separately:

```java
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.required");

        if (user.getEmail() != null && !user.getEmail().contains("@")) {
            errors.rejectValue("email", "email.invalid");
        }
    }
}
```

There are three validation rules:

```text
1. name must not be empty
2. email must not be empty
3. email must contain "@"
```

The first two use `ValidationUtils`.

The third rule demonstrates how `target` can be inspected directly.

---

## Validation Flow

The complete validation process is:

```text
Create User
    │
    ▼
Create Errors
    │
    ▼
Create Validator
    │
    ▼
validator.validate(user, errors)
    │
    ├── target
    │     └── User
    │
    └── errors
          └── validation results
                  │
                  ▼
            errors.hasErrors()
```

For example:

```java
User user = new User("", "moses.example.com");

Validator validator = new UserValidator();

Errors errors = new BeanPropertyBindingResult(user, "user");

validator.validate(user, errors);
```

The validator evaluates:

```text
name = ""
email = "moses.example.com"
```

The result is:

```text
Errors
 ├── name.required
 └── email.invalid
```

Therefore:

```java
errors.hasErrors()
```

returns:

```text
true
```

---

## Complete Example

### User.java

```java
public class User {

    private final String name;

    private final String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
```

### UserValidator.java

```java
public class UserValidator implements Validator {

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {

        User user = (User) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.required");

        if (user.getEmail() != null && !user.getEmail().contains("@")) {
            errors.rejectValue("email", "email.invalid");
        }
    }
}
```

### ValidationApplication.java

```java
public class ValidationApplication {

    public static void main(String[] args) {
        User user = new User("Moses", "moses@example.com");

        Validator validator = new UserValidator();

        Errors errors = new BeanPropertyBindingResult(user, "user");

        validator.validate(user, errors);

        System.out.println("Validation errors: " + errors.getErrorCount());
    }
}
```

For the valid user above, the output is:

```text
Validation errors: 0
```

---

## Why Separate Validation From the Domain Object?

The `User` class contains user data:

```text
User
 ├── name
 └── email
```

The `UserValidator` contains validation rules:

```text
UserValidator
 ├── name.required
 ├── email.required
 └── email.invalid
```

This separation allows the same validation logic to be reused wherever a `User` needs to be validated.

It also keeps the domain object focused on representing its data rather than knowing how every application context should validate it.

---

## Validation vs ConversionService

The previous example introduced `ConversionService`.

The two abstractions solve different problems.

### ConversionService

Converts one type into another:

```text
"42"
 │
 ▼
Integer
```

### Validator

Determines whether an object satisfies a set of rules:

```text
User
 │
 ▼
Validator
 │
 ├── valid
 │
 └── invalid
```

In short:

```text
ConversionService
    → "Can this value be converted?"

Validator
    → "Does this object satisfy these rules?"
```

---

## Validation and Environment

The previous examples also covered `Environment` and property sources.

These abstractions have different responsibilities:

```text
Environment
    → exposes environment information and properties

PropertySource
    → provides property values

ConversionService
    → converts values between types

Validator
    → checks objects against validation rules
```

Together they demonstrate how Spring separates common infrastructure responsibilities into focused abstractions.

---

## Spring Validation and Web Applications

Spring's validation abstraction is particularly useful in applications that receive user input.

A simplified web request flow might look like:

```text
HTTP Request
     │
     ▼
Controller
     │
     ▼
User object
     │
     ▼
Validator
     │
     ▼
Errors
```

If validation fails, the application can use the collected errors to report problems back to the user.

This example intentionally does not introduce Spring MVC so that the `Validator` abstraction remains the primary focus.

---

## Validation vs Jakarta Bean Validation

Spring's `Validator` abstraction should not be confused with Jakarta Bean Validation.

Spring provides:

```java
org.springframework.validation.Validator
```

Jakarta Bean Validation provides annotations such as:

```java
@NotNull
@Size
@Email
```

This example focuses specifically on Spring's validation abstraction.

The two mechanisms can be integrated in Spring applications, but they represent different validation APIs.

---

## Key Operations

| Operation | Purpose |
|---|---|
| `supports()` | Determines whether the validator supports a type |
| `validate()` | Performs validation |
| `rejectValue()` | Registers an error for a specific field |
| `hasErrors()` | Checks whether any validation errors exist |
| `hasFieldErrors()` | Checks whether a specific field has errors |
| `getErrorCount()` | Returns the number of validation errors |
| `rejectIfEmptyOrWhitespace()` | Rejects empty or whitespace-only values |


---

## Dependencies

The Spring validation abstraction is provided by `spring-context`.

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
</dependency>

<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
```

No Jakarta Bean Validation provider is required for this example.

---

## Running the Example

From the `validation` directory:

```bash
mvn clean test
```

To build the complete project:

```bash
mvn clean install
```

The application can also be run directly from the IDE by running:

```text
ValidationApplication
```

---

## Key Takeaways

- Spring provides the `Validator` abstraction for object validation.
- `supports()` determines which types a validator can validate.
- `validate()` contains the validation rules.
- `target` represents the object being validated.
- `Errors` is used to record validation failures.
- `BeanPropertyBindingResult` is an implementation of `Errors`.
- `ValidationUtils` provides convenient common validation operations.
- `rejectValue()` associates an error with a particular field.
- A validator can use `target` directly when validation rules need to inspect object values.
- Validation logic can be separated from the object being validated.
- Spring's `Validator` abstraction is distinct from Jakarta Bean Validation.

---

## What's Next?

The next example covers **Spring Expression Language (SpEL)**.

It will introduce Spring's expression language and demonstrate how expressions can access properties, invoke methods, and perform operations against objects.