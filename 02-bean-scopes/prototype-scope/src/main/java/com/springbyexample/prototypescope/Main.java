package com.springbyexample.prototypescope;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class Main {

    public static void main(String[] args) {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            Counter counter1 = context.getBean(Counter.class);
            Counter counter2 = context.getBean(Counter.class);

            counter1.increment();

            System.out.println("Counter 1: " + counter1.getCount());
            System.out.println("Counter 2: " + counter2.getCount());
            System.out.println("Same instance? " + (counter1 == counter2));
        }
    }
}
