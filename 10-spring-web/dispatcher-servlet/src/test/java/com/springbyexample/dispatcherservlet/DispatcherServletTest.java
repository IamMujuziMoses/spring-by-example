package com.springbyexample.dispatcherservlet;

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
@WebMvcTest(HelloController.class)
public class DispatcherServletTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldHandleRequestThroughDispatcherServlet() throws Exception {
        mockMvc.perform(get("/hello")).andExpect(status().isOk())
                .andExpect(content().string("Hello from DispatcherServlet!"));
    }
}