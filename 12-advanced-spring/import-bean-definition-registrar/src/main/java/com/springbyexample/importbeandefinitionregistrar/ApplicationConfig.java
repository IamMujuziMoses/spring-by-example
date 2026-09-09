package com.springbyexample.importbeandefinitionregistrar;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Mujuzi Moses
 */
@Configuration
@Import(GreetingRegistrar.class)
public class ApplicationConfig {
}
