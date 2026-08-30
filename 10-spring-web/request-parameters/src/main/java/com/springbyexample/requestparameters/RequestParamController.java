package com.springbyexample.requestparameters;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mujuzi Moses
 */
@RestController
public class RequestParamController {

    @GetMapping("/api/items")
    public String getItems(@RequestParam String category) {
        return "Category: " + category;
    }

    @GetMapping("/api/search")
    public String search(@RequestParam String query, @RequestParam(defaultValue = "10") int limit) {
        return "Query: " + query + ", Limit: " + limit;
    }
}