package com.springbyexample.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Mujuzi Moses
 */
@WebMvcTest
public class ControllersTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldHandleRequestWithController() throws Exception {
        mockMvc.perform(get("/hello")).andExpect(status().isOk())
                .andExpect(content().string("Hello from Controller!"));
    }

    @Test
    void shouldHandleRequestWithRestController() throws Exception {
        mockMvc.perform(get("/rest-hello")).andExpect(status().isOk())
                .andExpect(content().string("Hello from RestController!"));
    }
}
