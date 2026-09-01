package com.springbyexample.applicationscope;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Mujuzi Moses
 */
@WebMvcTest(ApplicationScopeController.class)
@Import(ApplicationScopedData.class)
public class ApplicationScopeTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReuseBeanAcrossRequests() throws Exception {

        String firstRequest = mockMvc.perform(get("/api/application")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String secondRequest = mockMvc.perform(get("/api/application")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(firstRequest, secondRequest);
    }

    @Test
    void shouldReuseBeanAcrossDifferentSessions() throws Exception {

        MockHttpSession firstSession = new MockHttpSession();
        MockHttpSession secondSession = new MockHttpSession();

        String firstRequest = mockMvc.perform(get("/api/application").session(firstSession))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String secondRequest = mockMvc.perform(get("/api/application").session(secondSession))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertEquals(firstRequest, secondRequest);
    }
}
