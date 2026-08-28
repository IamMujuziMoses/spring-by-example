package com.springbyexample.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mujuzi Moses
 */
@RestController
public class HelloRestController {

    @GetMapping("/rest-hello")
    public String hello() {
        return "Hello from RestController!";
    }
}
