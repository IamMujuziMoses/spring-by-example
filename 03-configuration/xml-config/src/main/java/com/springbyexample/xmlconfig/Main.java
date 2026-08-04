package com.springbyexample.xmlconfig;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class Main {
    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        GreetingController controller = context.getBean(GreetingController.class);

        System.out.println(controller.greet());

        context.close();
    }
}
