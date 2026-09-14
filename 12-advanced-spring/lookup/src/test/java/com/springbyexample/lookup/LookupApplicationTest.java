package com.springbyexample.lookup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class LookupApplicationTest {

    @Test
    void shouldCreateCommandManager() {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(LookupConfig.class)) {

            CommandManager commandManager = applicationContext.getBean(CommandManager.class);

            assertNotNull(commandManager);
        }
    }

    @Test
    void shouldLookupCommand() {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(LookupConfig.class)) {

            CommandManager commandManager = applicationContext.getBean(CommandManager.class);

            Command command = commandManager.process();

            assertEquals("Hello from @Lookup!", command.message());
        }
    }

    @Test
    void shouldCreateNewCommandForEachLookup() {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(LookupConfig.class)) {

            CommandManager commandManager = applicationContext.getBean(CommandManager.class);

            Command first = commandManager.process();
            Command second = commandManager.process();

            assertNotSame(first, second);
        }
    }
}
