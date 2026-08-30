package com.springbyexample.requestbody;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mujuzi Moses
 */
@RestController
public class RequestBodyController {

    @PostMapping("/api/items")
    public String createItem(@RequestBody Item item) {
        return "Created item: " + item.name();
    }
}
