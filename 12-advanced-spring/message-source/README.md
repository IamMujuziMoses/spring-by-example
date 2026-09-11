# MessageSource

Spring provides the `MessageSource` abstraction for resolving messages from message bundles.

This is especially useful when an application needs to support multiple languages or locales. Instead of hard-coding user-facing text throughout the application, messages can be stored externally and resolved using a message key and a `Locale`.

This example demonstrates how to configure a `MessageSource`, define localized message bundles, resolve messages for different locales, and provide arguments to messages.

## Learning Objectives

By completing this example, you will understand:

- What Spring's `MessageSource` abstraction is
- Why Spring provides `MessageSource`
- How message bundles work
- How `ResourceBundleMessageSource` works
- How to configure a `MessageSource` bean
- How message keys are resolved
- How locales determine which message bundle is used
- How to pass arguments to messages
- How Spring falls back to the default message bundle
- How to test localized message resolution

## What Is MessageSource?

`MessageSource` is a Spring abstraction for resolving messages.

It is located in:

```java
org.springframework.context.MessageSource
```

Instead of putting user-facing text directly in application code:

```java
System.out.println("Hello, Moses!");
```

the application can use a message key:

```java
greeting
```

and let the `MessageSource` resolve the appropriate message.

For example:

```text
greeting
   │
   ├── Locale.ENGLISH
   │       └── Hello, Moses!
   │
   └── Locale.FRENCH
           └── Bonjour, Moses !
```

This allows the same application code to display different messages depending on the user's locale.

## Why Does MessageSource Exist?

Applications often need to support users from different countries and language environments.

Hard-coding every translated message directly into application code makes applications difficult to maintain.

For example:

```java
if (locale.equals(Locale.FRENCH)) {
    return "Bonjour";
}

return "Hello";
```

This approach becomes difficult to maintain as the number of messages and supported languages grows.

Spring's `MessageSource` provides a cleaner separation:

```text
Application Code
       │
       │ message key
       ▼
 MessageSource
       │
       │ locale
       ▼
 Message Bundle
       │
       ▼
Localized Message
```

The application code only needs to know the message key and requested locale.

## MessageSource Interface

The main method used in this example is:

```java
String getMessage(
        String code,
        Object[] args,
        Locale locale
);
```

For example:

```java
String message =
        messageSource.getMessage(
                "greeting",
                new Object[]{"Moses"},
                Locale.ENGLISH
        );
```

The parameters represent:

| Parameter | Purpose |
|---|---|
| `code` | Message key |
| `args` | Arguments inserted into the message |
| `locale` | Locale used to resolve the message |

For the example:

```text
code     → greeting
args     → Moses
locale   → English
```

The result is:

```text
Hello, Moses!
```

## Message Bundles

Messages are stored in resource bundle files.

The example contains:

```text
src/main/resources/
├── messages.properties
└── messages_fr.properties
```

The default message bundle is:

```properties
greeting=Hello, {0}!
welcome=Welcome to Spring by Example.
```

The French message bundle is:

```properties
greeting=Bonjour, {0} !
welcome=Bienvenue dans Spring by Example.
```

The same message key exists in both files:

```text
greeting
```

but the value differs depending on the locale.

## Default Message Bundle

The default bundle is:

```text
messages.properties
```

It contains messages that can be used as the application's default messages.

For example:

```properties
greeting=Hello, {0}!
```

When the application requests:

```java
Locale.ENGLISH
```

Spring can resolve the message to:

```text
Hello, Moses!
```

## Locale-Specific Message Bundles

A locale-specific bundle follows the naming convention:

```text
basename_language.properties
```

For French:

```text
messages_fr.properties
```

For German:

```text
messages_de.properties
```

For Spanish:

```text
messages_es.properties
```

For example:

```text
messages.properties
messages_fr.properties
messages_de.properties
messages_es.properties
```

The application can request a message using the corresponding `Locale`.

```java
messageSource.getMessage(
        "greeting",
        new Object[]{"Moses"},
        Locale.FRENCH
);
```

Spring then looks for an appropriate French message bundle.

## ResourceBundleMessageSource

Spring provides several `MessageSource` implementations.

This example uses:

```java
org.springframework.context.support.ResourceBundleMessageSource
```

`ResourceBundleMessageSource` resolves messages from Java-style resource bundles.

It can be configured with:

```java
ResourceBundleMessageSource messageSource =
        new ResourceBundleMessageSource();

messageSource.setBasenames("messages");
```

The basename:

```text
messages
```

corresponds to:

```text
messages.properties
messages_fr.properties
```

The locale determines which bundle is selected.

## Configuring MessageSource

The example defines a `MessageSource` bean using Java configuration:

```java
@Configuration
public class MessageSourceConfig {

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource =
                new ResourceBundleMessageSource();

        messageSource.setBasenames("messages");

        return messageSource;
    }
}
```

The configuration creates a `ResourceBundleMessageSource` and tells it to use the `messages` resource bundle.

```text
MessageSourceConfig
        │
        ▼
ResourceBundleMessageSource
        │
        │ basename = "messages"
        ▼
messages.properties
messages_fr.properties
```

## Resolving an English Message

A message can be resolved using `Locale.ENGLISH`:

```java
String message =
        messageSource.getMessage(
                "greeting",
                new Object[]{"Moses"},
                Locale.ENGLISH
        );
```

The result is:

```text
Hello, Moses!
```

The message key is:

```text
greeting
```

and `{0}` is replaced with:

```text
Moses
```

## Resolving a French Message

The same message key can be resolved using `Locale.FRENCH`:

```java
String message =
        messageSource.getMessage(
                "greeting",
                new Object[]{"Moses"},
                Locale.FRENCH
        );
```

Spring uses the French resource bundle:

```text
messages_fr.properties
```

and resolves the message to:

```text
Bonjour, Moses !
```

The application code does not need a separate `if` statement for French.

The locale determines which message should be used.

## Message Arguments

Messages can contain placeholders.

The English bundle contains:

```properties
greeting=Hello, {0}!
```

The `{0}` represents the first argument passed to `getMessage()`.

For example:

```java
messageSource.getMessage(
        "greeting",
        new Object[]{"Moses"},
        Locale.ENGLISH
);
```

produces:

```text
Hello, Moses!
```

Multiple arguments can also be supplied.

For example:

```properties
greeting=Hello, {0}! You have {1} new messages.
```

The application could provide:

```java
new Object[]{"Moses", 5}
```

resulting in:

```text
Hello, Moses! You have 5 new messages.
```

This allows message templates to remain in the message bundle while dynamic values are supplied by the application.

## Message Resolution Flow

The complete resolution process can be visualized as:

```text
Application
    │
    │ "greeting"
    │ arguments = ["Moses"]
    │ locale = Locale.FRENCH
    ▼
MessageSource
    │
    ▼
ResourceBundleMessageSource
    │
    │ searches for appropriate bundle
    ▼
messages_fr.properties
    │
    │ greeting=Bonjour, {0} !
    ▼
Message formatting
    │
    ▼
Bonjour, Moses !
```

For English:

```text
Application
    │
    │ "greeting"
    │ locale = Locale.ENGLISH
    ▼
MessageSource
    │
    ▼
messages.properties
    │
    │ greeting=Hello, {0}!
    ▼
Hello, Moses!
```

## Message Fallback

A default message bundle is useful when a locale-specific message is not available.

For example:

```text
messages.properties
messages_fr.properties
```

If the application requests French, Spring can use:

```text
messages_fr.properties
```

If a specific localized message is not available, message resolution can fall back according to the configured resource bundle behavior.

The default bundle therefore provides a useful baseline for the application.

## Complete Example

### MessageSourceConfig

```java
@Configuration
public class MessageSourceConfig {

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource =
                new ResourceBundleMessageSource();

        messageSource.setBasenames("messages");

        return messageSource;
    }
}
```

### MessageSourceApplication

```java
public class MessageSourceApplication {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(
                             MessageSourceConfig.class
                     )) {

            MessageSource messageSource =
                    applicationContext.getBean(MessageSource.class);

            String englishGreeting =
                    messageSource.getMessage(
                            "greeting",
                            new Object[]{"Moses"},
                            Locale.ENGLISH
                    );

            String frenchGreeting =
                    messageSource.getMessage(
                            "greeting",
                            new Object[]{"Moses"},
                            Locale.FRENCH
                    );

            System.out.println(englishGreeting);
            System.out.println(frenchGreeting);
        }
    }
}
```

### messages.properties

```properties
greeting=Hello, {0}!
welcome=Welcome to Spring by Example.
```

### messages_fr.properties

```properties
greeting=Bonjour, {0} !
welcome=Bienvenue dans Spring by Example.
```

Expected output:

```text
Hello, Moses!
Bonjour, Moses !
```

## MessageSource vs Resource Loading

The previous example in Module 12 demonstrated Spring's resource loading abstraction.

`ResourceLoader` is concerned with locating and accessing resources.

`MessageSource` builds on the idea of externally stored resources for a different purpose: resolving messages based on a message code and locale.

```text
ResourceLoader
    │
    └── Load resources

MessageSource
    │
    └── Resolve localized messages
```

The two concepts are related, but they have different responsibilities.

This example therefore focuses on message resolution rather than directly loading files with `ResourceLoader`.

## MessageSource vs Environment

`Environment` and `MessageSource` can both deal with externally defined information, but they solve different problems.

| Abstraction | Purpose |
|---|---|
| `Environment` | Access environment properties and profiles |
| `ResourceLoader` | Locate and access resources |
| `MessageSource` | Resolve messages for a locale |

For example:

```text
Environment
    └── application.name = Spring by Example

ResourceLoader
    └── greeting.txt

MessageSource
    └── greeting = Hello, Moses!
```

Keeping these responsibilities separate makes the Spring framework easier to understand and use.

## Internationalization

Internationalization, commonly abbreviated as **i18n**, is the process of designing an application so that it can support different languages and locales.

`MessageSource` is one of the Spring abstractions that supports this.

For example:

```text
Locale.ENGLISH
    └── Hello, Moses!

Locale.FRENCH
    └── Bonjour, Moses !

Locale.GERMAN
    └── Hallo, Moses!
```

The application can use the same message key:

```text
greeting
```

while allowing the message text to vary according to the locale.

## Common Message Bundle Naming

Message bundles generally follow a naming convention based on the base name and locale.

| File | Purpose |
|---|---|
| `messages.properties` | Default messages |
| `messages_fr.properties` | French messages |
| `messages_de.properties` | German messages |
| `messages_es.properties` | Spanish messages |
| `messages_fr_CA.properties` | Canadian French messages |

For this example:

```text
messages
    │
    ├── messages.properties
    └── messages_fr.properties
```

The basename configured in Spring is simply:

```java
messageSource.setBasenames("messages");
```

## Key MessageSource Operations

| Operation | Purpose |
|---|---|
| `getMessage()` | Resolves a message |
| `code` | Identifies the message |
| `args` | Provides dynamic message arguments |
| `locale` | Determines the requested locale |

A typical call looks like:

```java
messageSource.getMessage(
        "greeting",
        new Object[]{"Moses"},
        Locale.ENGLISH
);
```

The three important inputs are:

```text
Message code
     │
     ▼
"greeting"

Arguments
     │
     ▼
["Moses"]

Locale
     │
     ▼
Locale.ENGLISH
```

## Dependencies

This example uses `spring-context` because `MessageSource` and the configuration infrastructure are provided by Spring Context.

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
</dependency>
```

JUnit is included for testing:

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
```

## Running the Example

From the `message-source` directory, run:

```bash
mvn clean install
```

To run the application directly from an IDE, execute:

```text
MessageSourceApplication
```

Expected output:

```text
Hello, Moses!
Bonjour, Moses !
```

## Key Takeaways

- `MessageSource` is Spring's abstraction for resolving messages.
- Messages can be stored outside application code in resource bundles.
- `ResourceBundleMessageSource` resolves messages from resource bundles.
- Message keys identify messages independently of their translated text.
- `Locale` determines which localized message bundle should be used.
- `messages.properties` can provide default messages.
- Locale-specific bundles follow names such as `messages_fr.properties`.
- Message arguments can be supplied through the `args` parameter.
- `{0}`, `{1}`, and similar placeholders can be replaced with dynamic values.
- `MessageSource` helps applications support internationalization.
- `MessageSource` has a different responsibility from `Environment` and `ResourceLoader`.

## Next

The next example in Module 12 - Advanced Spring will cover:

**ConversionService**

This will demonstrate Spring's type-conversion abstraction and how values can be converted between different types.