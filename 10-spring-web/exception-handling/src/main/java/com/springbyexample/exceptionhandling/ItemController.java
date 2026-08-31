package com.springbyexample.exceptionhandling;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mujuzi Moses
 */
@RestController
public class ItemController {

    @GetMapping("/api/items/{id}")
    public String getItem(@PathVariable Long id) {

        if (id != 1) {
            throw new ItemNotFoundException(id);
        }

        return "Item: " + id;
    }
}
