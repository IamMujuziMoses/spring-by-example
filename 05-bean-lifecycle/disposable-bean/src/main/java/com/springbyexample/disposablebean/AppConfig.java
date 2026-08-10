package com.springbyexample.disposablebean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mujuzi Moses
 */
@Configuration
public class AppConfig {

    @Bean
    public ReportService reportService() {
        return new ReportService();
    }
}
