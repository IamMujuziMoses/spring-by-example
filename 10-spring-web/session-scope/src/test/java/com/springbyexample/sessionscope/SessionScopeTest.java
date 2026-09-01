package com.springbyexample.sessionscope;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
@WebMvcTest(SessionScopeController.class)
@Import(SessionScopedData.class)
public class SessionScopeTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReuseBeanWithinSameSession() throws Exception {

        MockHttpSession session = new MockHttpSession();

        String firstRequest = mockMvc.perform(get("/api/session").session(session))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String secondRequest = mockMvc.perform(get("/api/session").session(session))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertEquals(firstRequest, secondRequest);
    }

    @Test
    void shouldCreateDifferentBeanForDifferentSessions() throws Exception {

        MockHttpSession firstSession = new MockHttpSession();
        MockHttpSession secondSession = new MockHttpSession();

        String firstRequest = mockMvc.perform(get("/api/session").session(firstSession))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String secondRequest = mockMvc.perform(get("/api/session").session(secondSession))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertNotEquals(firstRequest, secondRequest);
    }
}
