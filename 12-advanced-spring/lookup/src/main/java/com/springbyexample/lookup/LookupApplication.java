package com.springbyexample.lookup;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class LookupApplication {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(LookupConfig.class)) {

            CommandManager commandManager = applicationContext.getBean(CommandManager.class);
            Command command = commandManager.process();

            System.out.println(command.message());
        }
    }
}
