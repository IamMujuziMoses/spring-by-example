package com.springbyexample.deferredimportselector;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Mujuzi Moses
 */
@Configuration
@Import(GreetingDeferredImportSelector.class)
public class ApplicationConfig {
}
