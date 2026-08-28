package com.springbyexample.pathvariables;

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
@WebMvcTest(PathVariableController.class)
class PathVariablesTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldExtractPathVariable() throws Exception {
        mockMvc.perform(get("/api/items/42")).andExpect(status().isOk())
                .andExpect(content().string("Item: 42"));
    }

    @Test
    void shouldExtractMultiplePathVariables() throws Exception {
        mockMvc.perform(get("/api/users/10/items/42")).andExpect(status().isOk())
                .andExpect(content().string("User: 10, Item: 42"));
    }
}
