package com.springbyexample.importselector;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Mujuzi Moses
 */
@Configuration
@Import(GreetingImportSelector.class)
public class ApplicationConfig {
}
