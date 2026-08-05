package com.springbyexample.mixedconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

/**
 * @author Mujuzi Moses
 */
@ImportResource("classpath:applicationContext.xml")
public class AppConfig {

    @Bean
    TimeService timeService() {
        return new TimeService();
    }

}
