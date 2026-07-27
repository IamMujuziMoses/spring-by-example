package com.springbyexample.singletonscope;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class Main {

    public static void main(String[] args) {

        var context = new AnnotationConfigApplicationContext(AppConfig.class);
         CounterService service1 = context.getBean(CounterService.class);
         CounterService service2 = context.getBean(CounterService.class);

         Counter counter1 = context.getBean(Counter.class);
         Counter counter2 = context.getBean(Counter.class);

         service1.incrementCounter();

         System.out.println("Service 1 count: " + service1.getCount());
         System.out.println("Service 2 count: " + service2.getCount());
         System.out.println();
         System.out.println("Same CounterService instance? " + (service1 == service2));
         System.out.println("Same Counter instance? " + (counter1 == counter2));
    }
}
