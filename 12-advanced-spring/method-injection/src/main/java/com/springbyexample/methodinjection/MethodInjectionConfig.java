package com.springbyexample.methodinjection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mujuzi Moses
 */
@Configuration
public class MethodInjectionConfig {

    @Bean
    public CommandManager commandManager() {
        return new CommandManager() {

            @Override
            protected Command createCommand() {
                throw new UnsupportedOperationException("Spring should replace this method");
            }
        };
    }

    @Bean
    public CommandReplacer commandReplacer() {
        return new CommandReplacer();
    }
}
