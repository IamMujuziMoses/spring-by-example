package com.springbyexample.requestmapping;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mujuzi Moses
 */
@RestController
@RequestMapping("/api")
public class RequestMappingController {

    @GetMapping("/items")
    public String getItems() {
        return "GET request";
    }

    @PostMapping("/items")
    public String createItem() {
        return "POST request";
    }

    @PutMapping("/items")
    public String updateItem() {
        return "PUT request";
    }

    @PatchMapping("/items")
    public String partiallyUpdateItem() {
        return "PATCH request";
    }

    @DeleteMapping("/items")
    public String deleteItem() {
        return "DELETE request";
    }
}
