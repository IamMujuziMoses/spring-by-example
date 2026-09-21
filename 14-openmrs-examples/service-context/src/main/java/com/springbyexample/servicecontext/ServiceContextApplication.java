package com.springbyexample.servicecontext;

import com.springbyexample.servicecontext.impl.GreetingServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Demonstrates OpenMRS-style service access through ServiceContext.
 *
 * @author Mujuzi Moses
 */
public class ServiceContextApplication {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {

            ServiceContext serviceContext = ServiceContext.getInstance();
            serviceContext.setApplicationContext(applicationContext);

            GreetingService greetingService = applicationContext.getBean(GreetingService.class);
            serviceContext.setService(GreetingService.class, greetingService);

            GreetingService service = serviceContext.getService(GreetingService.class);

            System.out.println(service.greet());

            GreetingService registeredComponent = serviceContext
                    .getRegisteredComponent("greetingService", GreetingService.class);

            System.out.println(registeredComponent.greet());
        } finally {
            ServiceContext.destroyInstance();
        }
    }

    @Configuration
    static class AppConfig {

        @Bean
        public GreetingService greetingService() {
            return new GreetingServiceImpl();
        }
    }
}
