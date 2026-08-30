package com.springbyexample.responsebody;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

/**
 * @author Mujuzi Moses
 */
@WebMvcTest(ResponseBodyController.class)
public class ResponseBodyTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldWriteResponseBody() throws Exception {
        mockMvc.perform(get("/api/message")).andExpect(status().isOk())
                .andExpect(content().string("Hello from Spring MVC"));
    }
}