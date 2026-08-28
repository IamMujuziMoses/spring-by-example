package com.springbyexample.pathvariables;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mujuzi Moses
 */
@RestController
public class PathVariableController {

    @GetMapping("/api/items/{id}")
    public String getItem(@PathVariable Long id) {
        return "Item: " + id;
    }

    @GetMapping("/api/users/{userId}/items/{itemId}")
    public String getUserItem(@PathVariable Long userId, @PathVariable Long itemId) {
        return "User: " + userId + ", Item: " + itemId;
    }
}
