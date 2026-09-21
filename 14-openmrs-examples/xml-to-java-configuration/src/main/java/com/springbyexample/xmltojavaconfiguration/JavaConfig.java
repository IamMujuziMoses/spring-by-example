package com.springbyexample.xmltojavaconfiguration;

import com.springbyexample.xmltojavaconfiguration.impl.GreetingServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author Mujuzi Moses
 */
@Configuration
@ImportResource("classpath:applicationContext.xml")
public class JavaConfig {

    @Bean
    public GreetingService javaGreetingService() {
        return new GreetingServiceImpl();
    }
}
