package com.springbyexample.requestmapping;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Mujuzi Moses
 */
@WebMvcTest(RequestMappingController.class)
class RequestMappingTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldHandleGetRequest() throws Exception {
        mockMvc.perform(get("/api/items")).andExpect(status().isOk())
                .andExpect(content().string("GET request"));
    }

    @Test
    void shouldHandlePostRequest() throws Exception {
        mockMvc.perform(post("/api/items")).andExpect(status().isOk())
                .andExpect(content().string("POST request"));
    }

    @Test
    void shouldHandlePutRequest() throws Exception {
        mockMvc.perform(put("/api/items")).andExpect(status().isOk())
                .andExpect(content().string("PUT request"));
    }

    @Test
    void shouldHandlePatchRequest() throws Exception {
        mockMvc.perform(patch("/api/items")).andExpect(status().isOk())
                .andExpect(content().string("PATCH request"));
    }

    @Test
    void shouldHandleDeleteRequest() throws Exception {
        mockMvc.perform(delete("/api/items")).andExpect(status().isOk())
                .andExpect(content().string("DELETE request"));
    }
}
