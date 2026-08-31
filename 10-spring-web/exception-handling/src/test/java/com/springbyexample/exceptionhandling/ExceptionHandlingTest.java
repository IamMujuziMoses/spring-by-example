package com.springbyexample.exceptionhandling;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Mujuzi Moses
 */
@WebMvcTest(ItemController.class)
public class ExceptionHandlingTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnItemWhenItemExists() throws Exception {
        mockMvc.perform(get("/api/items/1")).andExpect(status().isOk())
                .andExpect(content().string("Item: 1"));
    }

    @Test
    void shouldReturnNotFoundWhenItemDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/items/42")).andExpect(status().isNotFound())
                .andExpect(content().string("Item not found: 42"));
    }
}