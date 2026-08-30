package com.springbyexample.requestparameters;

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
@WebMvcTest(RequestParamController.class)
public class RequestParametersTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldExtractRequestParameter() throws Exception {
        mockMvc.perform(get("/api/items").param("category", "books")).andExpect(status().isOk())
                .andExpect(content().string("Category: books"));
    }

    @Test
    void shouldUseDefaultValueWhenParameterIsMissing() throws Exception {
        mockMvc.perform(get("/api/search").param("query", "spring")).andExpect(status().isOk())
                .andExpect(content().string("Query: spring, Limit: 10"));
    }

    @Test
    void shouldExtractMultipleRequestParameters() throws Exception {
        mockMvc.perform(get("/api/search").param("query", "spring").param("limit", "20"))
                .andExpect(status().isOk()).andExpect(content().string("Query: spring, Limit: 20"));
    }
}
