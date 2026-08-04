package com.springbyexample.importconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Mujuzi Moses
 */
@Configuration
@Import({ GreetingConfig.class, TimeConfig.class })
public class AppConfig {

}
