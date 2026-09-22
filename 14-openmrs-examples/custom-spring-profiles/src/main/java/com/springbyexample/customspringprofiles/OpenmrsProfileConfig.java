package com.springbyexample.customspringprofiles;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Mujuzi Moses
 */
@Configuration
@Import({ DevelopmentConfig.class, ProductionConfig.class })
public class OpenmrsProfileConfig {
}
