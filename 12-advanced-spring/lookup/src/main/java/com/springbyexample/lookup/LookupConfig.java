package com.springbyexample.lookup;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author Mujuzi Moses
 */
@Configuration
@ComponentScan(basePackageClasses = CommandManager.class)
public class LookupConfig {

    @Bean
    @Scope("prototype")
    public Command command() {
        return new Command("Hello from @Lookup!");
    }
}
