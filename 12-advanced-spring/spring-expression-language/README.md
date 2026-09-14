# Spring Expression Language (SpEL)

Spring Expression Language (SpEL) is a powerful expression language that provides support for querying and manipulating objects at runtime.

SpEL can evaluate expressions involving literals, arithmetic operations, object properties, variables, method calls, collections, operators, and more.

This example focuses on the core SpEL expression engine using `ExpressionParser`, `Expression`, and `StandardEvaluationContext`.

## Learning Objectives

- Understand what Spring Expression Language (SpEL) is
- Understand why Spring provides an expression language
- Learn how to create a SpEL expression parser
- Learn how to parse expressions
- Learn how to evaluate expressions
- Understand the `ExpressionParser` interface
- Understand `SpelExpressionParser`
- Understand the `Expression` interface
- Learn how to evaluate literal expressions
- Learn how to evaluate arithmetic expressions
- Learn how to evaluate expressions against a root object
- Learn how to use variables in expressions
- Understand the role of `EvaluationContext`
- Learn how SpEL separates expression parsing from expression evaluation

---

## What Is Spring Expression Language?

Spring Expression Language (SpEL) is an expression language provided by Spring for evaluating expressions at runtime.

An expression is a string that describes a value or operation that Spring can evaluate.

For example:

```java
'Hello from SpEL!'
```

is an expression that evaluates to:

```text
Hello from SpEL!
```

Similarly:

```java
10 + 20
```

is an expression that evaluates to:

```text
30
```

SpEL is not limited to literal values and arithmetic. It can also work with objects, properties, methods, variables, collections, operators, and other runtime values.

The basic flow is:

```text
Expression String
       │
       ▼
ExpressionParser
       │
       ▼
Expression
       │
       ▼
Evaluation
       │
       ▼
Result
```

---

## Why Does Spring Provide SpEL?

Applications frequently need to evaluate values dynamically rather than hard-coding every value in Java code.

For example, an application might need to:

- evaluate a mathematical expression
- access a property on an object
- reference a variable
- evaluate a conditional expression
- access collection elements
- invoke methods
- dynamically determine a value

Without an expression language, each of these operations would need to be implemented directly in Java.

SpEL provides a common expression syntax that Spring-based applications can use for these kinds of runtime operations.

---

## The SpEL Architecture

The core SpEL API can be understood through a few important abstractions:

```text
SpelExpressionParser
        │
        │ parseExpression(...)
        ▼
    Expression
        │
        │ getValue(...)
        ▼
   Evaluation
        │
        ▼
     Result
```

The main components are:

| Component | Responsibility |
|---|---|
| `ExpressionParser` | Parses expression strings |
| `SpelExpressionParser` | Standard SpEL parser implementation |
| `Expression` | Represents a parsed expression |
| `EvaluationContext` | Provides context during expression evaluation |
| `StandardEvaluationContext` | Standard evaluation context implementation |

---

## ExpressionParser

`ExpressionParser` is the abstraction used to parse expression strings.

```java
ExpressionParser parser = new SpelExpressionParser();
```

The parser converts an expression string into an `Expression`:

```java
Expression expression = parser.parseExpression("'Hello from SpEL!'");
```

The parser is responsible for parsing the expression, while the resulting `Expression` is responsible for evaluating it.

---

## SpelExpressionParser

`SpelExpressionParser` is the standard implementation of `ExpressionParser`.

```java
ExpressionParser parser = new SpelExpressionParser();
```

It can parse expressions such as:

```java
parser.parseExpression("'Hello from SpEL!'");
parser.parseExpression("10 + 20");
parser.parseExpression("name");
parser.parseExpression("#name");
```

The parser does not immediately return the final value.

Instead:

```text
Expression String
       │
       ▼
SpelExpressionParser
       │
       ▼
Expression
```

The expression is evaluated separately.

---

## Expression

`Expression` represents a parsed SpEL expression.

For example:

```java
Expression expression = parser.parseExpression("'Hello from SpEL!'");
```

The expression can then be evaluated:

```java
String result = expression.getValue(String.class);
```

This separation is useful because parsing and evaluation are distinct operations.

```text
parseExpression()
       │
       ▼
Expression
       │
       ▼
getValue()
       │
       ▼
Result
```

---

## Evaluating Literal Expressions

SpEL can evaluate literal values.

For example:

```java
String result = parser.parseExpression("'Hello from SpEL!'").getValue(String.class);
```

The expression:

```text
'Hello from SpEL!'
```

evaluates to:

```text
Hello from SpEL!
```

The expected type can be provided to `getValue()`:

```java
.getValue(String.class)
```

This tells SpEL that the expected result should be a `String`.

---

## Evaluating Arithmetic Expressions

SpEL supports arithmetic operations.

For example:

```java
Integer result = parser.parseExpression("10 + 20").getValue(Integer.class);
```

The expression:

```text
10 + 20
```

evaluates to:

```text
30
```

SpEL supports common arithmetic operators such as:

```text
+
-
*
/
%
```

For example:

```text
10 + 20
10 - 5
4 * 5
20 / 4
10 % 3
```

This allows expressions to describe calculations without requiring the calculation to be written directly as Java code.

---

## Evaluating Expressions Against a Root Object

SpEL can evaluate an expression against a root object.

Consider this class:

```java
public class User {

    private final String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
```

Create a `User`:

```java
User user = new User("Moses");
```

The user's `name` property can then be accessed from a SpEL expression:

```java
String result = parser.parseExpression("name").getValue(user, String.class);
```

The flow is:

```text
User
 │
 │ name = "Moses"
 ▼
SpEL Expression
 │
 │ "name"
 ▼
"Moses"
```

The object passed to `getValue()` becomes the root object used during evaluation.

---

## Root Objects

A root object is the primary object against which an expression is evaluated.

For example:

```java
User user = new User("Moses");

String result = parser.parseExpression("name").getValue(user, String.class);
```

Here:

```text
Root Object = user
Expression  = name
Result      = Moses
```

SpEL uses the root object's properties when evaluating the expression.

This makes it possible to write:

```text
name
```

instead of manually calling:

```java
user.getName()
```

The expression engine resolves the property against the root object.

---

## EvaluationContext

An `EvaluationContext` provides the context in which a SpEL expression is evaluated.

Spring provides `StandardEvaluationContext` as a general-purpose implementation.

```java
StandardEvaluationContext context = new StandardEvaluationContext();
```

An evaluation context can provide information used by SpEL during evaluation, including variables and other evaluation capabilities.

The basic flow becomes:

```text
Expression
    │
    │
    ▼
EvaluationContext
    │
    ▼
Evaluated Result
```

---

## Using Variables

SpEL supports variables using the `#` prefix.

For example:

```text
#name
```

The variable can be added to a `StandardEvaluationContext`:

```java
StandardEvaluationContext context = new StandardEvaluationContext();

context.setVariable("name","Moses");
```

The expression can then reference the variable:

```java
String result = parser.parseExpression("#name").getValue(context, String.class);
```

The result is:

```text
Moses
```

The flow is:

```text
context.setVariable("name", "Moses")
              │
              ▼
        #name expression
              │
              ▼
            "Moses"
```

---

## Root Object vs Variable

SpEL supports both root-object properties and variables, but they are accessed differently.

Root-object property:

```text
name
```

Variable:

```text
#name
```

For example:

```java
User user = new User("Moses");

String rootProperty = parser.parseExpression("name").getValue(user, String.class);
```

Whereas a variable uses an evaluation context:

```java
StandardEvaluationContext context = new StandardEvaluationContext();

context.setVariable("name", "Moses");

String variable = parser.parseExpression("#name").getValue(context, String.class);
```

The distinction is:

```text
Root object property
        │
        ▼
       name

Context variable
        │
        ▼
      #name
```

---

## SpEL Evaluation Flow

A typical SpEL evaluation can be visualized as:

```text
"10 + 20"
    │
    ▼
ExpressionParser
    │
    ▼
Expression
    │
    ▼
getValue(Integer.class)
    │
    ▼
   30
```

For a root object:

```text
User("Moses")
      │
      ▼
Expression: "name"
      │
      ▼
ExpressionParser
      │
      ▼
Expression
      │
      ▼
getValue(user, String.class)
      │
      ▼
   "Moses"
```

For a variable:

```text
"name" = "Moses"
      │
      ▼
StandardEvaluationContext
      │
      ▼
Expression: "#name"
      │
      ▼
Expression
      │
      ▼
getValue(context, String.class)
      │
      ▼
   "Moses"
```

---

## Complete Example

The example application creates a `SpelExpressionParser` and evaluates a literal and arithmetic expression.

### `SpelApplication.java`

```java
public class SpelApplication {

    public static void main(String[] args) {
        ExpressionParser parser = new SpelExpressionParser();

        Expression expression = parser.parseExpression("'Hello from SpEL!'");
        String message = expression.getValue(String.class);

        System.out.println(message);

        Integer result = parser.parseExpression("10 + 20").getValue(Integer.class);

        System.out.println("10 + 20 = " + result);
    }
}
```

Running the application produces:

```text
Hello from SpEL!
10 + 20 = 30
```

---

## User Example

The `User` class provides a simple root object for demonstrating property access.

### `User.java`

```java
public class User {

    private final String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
```

The expression:

```text
name
```

can then resolve the `name` property from a `User` object.

---

## Expression Parsing vs Evaluation

Parsing and evaluation are separate steps.

Parsing:

```java
Expression expression = parser.parseExpression("10 + 20");
```

Evaluation:

```java
Integer result = expression.getValue(Integer.class);
```

This distinction is important because an `Expression` represents the parsed expression, while `getValue()` evaluates it in a particular context.

Conceptually:

```text
"10 + 20"
    │
    │ parse
    ▼
Expression
    │
    │ evaluate
    ▼
30
```

---

## SpEL and Spring

SpEL is a Spring Framework feature, but it is useful to understand the expression engine independently from other Spring features.

The core expression module is:

```text
spring-expression
```

Other Spring features can build on SpEL for their own purposes, but this example intentionally uses the expression API directly.

This makes the underlying expression language easier to understand before introducing higher-level Spring features.

---

## SpEL vs Environment

The `Environment` and SpEL solve different problems.

`Environment` provides access to environment information such as:

- active profiles
- default profiles
- property sources
- configuration properties

SpEL evaluates expressions.

For example:

```text
Environment
    │
    ▼
configuration information
```

Whereas:

```text
SpEL
    │
    ▼
expression
    │
    ▼
evaluated value
```

They can be used together in Spring applications, but they represent different abstractions.

---

## SpEL vs ConversionService

`ConversionService` and SpEL also have different responsibilities.

`ConversionService` converts values between types:

```text
"42"
 │
 ▼
Integer
```

SpEL evaluates expressions:

```text
10 + 20
 │
 ▼
30
```

They can work together in the broader Spring ecosystem, but this example focuses only on expression evaluation.

---

## SpEL and Method Calls

SpEL can also interact with object methods.

For example, given an object with a method:

```java
public String greet() {
    return "Hello!";
}
```

an expression can invoke the method:

```text
greet()
```

This example does not include method invocation so that the core concepts of parsing, evaluation, root objects, and variables remain clear.

---

## SpEL and Operators

SpEL supports a variety of operators.

Examples include:

```text
10 + 20
10 > 5
true and false
```

These allow expressions to perform calculations and make decisions.

The same expression engine can therefore be used for more than simple property access.

---

## SpEL and Collections

SpEL can also work with collections and arrays.

For example, expressions can access collection elements and perform collection-related operations.

This makes SpEL useful when applications need to express dynamic operations over object graphs or collections.

These capabilities are beyond the scope of this introductory example.

---

## SpEL and Spring Bean References

SpEL can also be integrated with Spring's `ApplicationContext` to reference Spring-managed beans.

For example, Spring applications can use expressions that reference beans from the application context.

This is one reason SpEL is important within the broader Spring ecosystem.

However, this example intentionally avoids bean references and focuses on the underlying expression engine.

---

## Common SpEL Components

| Component | Purpose |
|---|---|
| `ExpressionParser` | Parses expression strings |
| `SpelExpressionParser` | Standard SpEL parser |
| `Expression` | Represents a parsed expression |
| `getValue()` | Evaluates an expression |
| `EvaluationContext` | Provides evaluation context |
| `StandardEvaluationContext` | Standard evaluation context implementation |
| Root object | Primary object used during evaluation |
| Variable | Named value referenced using `#` |

---

## Key Operations

| Operation | Purpose |
|---|---|
| `parseExpression()` | Parses a SpEL expression |
| `getValue()` | Evaluates an expression |
| `setVariable()` | Adds a variable to an evaluation context |
| `getValue(Class)` | Evaluates an expression with an expected result type |
| `getValue(Object, Class)` | Evaluates against a root object |
| `getValue(EvaluationContext, Class)` | Evaluates using an evaluation context |

---

## Dependencies

This example requires the Spring Expression module:

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-expression</artifactId>
</dependency>

<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
```

---

## Running the Example

From the `spel` directory:

```bash
mvn clean test
```

To build the module:

```bash
mvn clean install
```

To run the application:

```bash
mvn exec:java
```

Or run `SpelApplication` directly from your IDE.

Expected output:

```text
Hello from SpEL!
10 + 20 = 30
```

---

## Key Takeaways

- SpEL is Spring's expression language.
- `ExpressionParser` parses expression strings.
- `SpelExpressionParser` is the standard parser implementation.
- `Expression` represents a parsed expression.
- `getValue()` evaluates an expression.
- SpEL can evaluate literal values.
- SpEL supports arithmetic expressions.
- Expressions can be evaluated against root objects.
- Root-object properties can be referenced directly.
- `StandardEvaluationContext` provides an evaluation context.
- Variables can be added to an evaluation context.
- SpEL variables are referenced using the `#` prefix.
- Parsing and evaluation are separate operations.
- SpEL is broader than the basic examples shown here and can also work with methods, operators, collections, and Spring beans.

---

## What's Next?

The next example is **Method Injection**.

It will demonstrate how Spring can dynamically provide method implementations for beans, allowing a bean to obtain a different dependency or object each time a method is invoked.