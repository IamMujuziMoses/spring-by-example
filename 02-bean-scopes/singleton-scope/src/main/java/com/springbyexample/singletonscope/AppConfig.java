package com.springbyexample.singletonscope;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mujuzi Moses
 */
@Configuration
public class AppConfig {

    @Bean
    public Counter counter() {
        return new Counter();
    }

    @Bean
    public CounterService counterService(Counter counter) {
        return new CounterService(counter);
    }

}
