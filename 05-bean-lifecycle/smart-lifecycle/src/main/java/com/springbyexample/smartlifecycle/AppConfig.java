package com.springbyexample.smartlifecycle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mujuzi Moses
 */
@Configuration
public class AppConfig {

    @Bean
    public ReportScheduler reportScheduler() {

        return new ReportScheduler();
    }

    @Bean
    public ReportSchedulerLifecycle reportSchedulerLifecycle(ReportScheduler scheduler) {

        return new ReportSchedulerLifecycle(scheduler);
    }
}
