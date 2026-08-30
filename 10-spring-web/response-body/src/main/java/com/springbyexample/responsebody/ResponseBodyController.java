package com.springbyexample.responsebody;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mujuzi Moses
 */
@RestController
public class ResponseBodyController {

    @GetMapping("/api/message")
    public String getMessage() {
        return "Hello from Spring MVC";
    }
}
