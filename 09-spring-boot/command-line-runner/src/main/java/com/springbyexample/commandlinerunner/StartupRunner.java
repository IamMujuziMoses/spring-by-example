package com.springbyexample.commandlinerunner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author Mujuzi Moses
 */
@Component
public class StartupRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("CommandLineRunner executed.");

        for (String arg : args) {
            System.out.println("Argument: " + arg);
        }
    }
}
