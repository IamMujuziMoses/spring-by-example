package com.springbyexample.requestscope;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author Mujuzi Moses
 */
@WebMvcTest(RequestScopeController.class)
@Import(RequestScopedData.class)
public class RequestScopeTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateDifferentBeanForEachRequest() throws Exception {

        String firstRequest = mockMvc.perform(get("/api/request")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String secondRequest = mockMvc.perform(get("/api/request")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertNotEquals(firstRequest, secondRequest);
    }
}
