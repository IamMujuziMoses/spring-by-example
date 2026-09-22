package com.springbyexample.customspringprofiles;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class CustomSpringProfilesApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
//        context.getEnvironment().setActiveProfiles("development");
        context.getEnvironment().setActiveProfiles("production");
        context.register(OpenmrsProfileConfig.class);
        context.refresh();

        GreetingService service = context.getBean(GreetingService.class);

        System.out.println("Active profiles: " + String.join(", ", context.getEnvironment().getActiveProfiles()));
        System.out.println(service.greet());

        context.close();
    }
}
