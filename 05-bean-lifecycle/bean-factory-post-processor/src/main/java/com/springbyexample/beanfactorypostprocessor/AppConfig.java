package com.springbyexample.beanfactorypostprocessor;

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

    @Bean
    public ReportBeanFactoryPostProcessor reportBeanFactoryPostProcessor() {
        return new ReportBeanFactoryPostProcessor();
    }

}
