package com.springbyexample.viewresolvers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Mujuzi Moses
 */
@WebMvcTest(ViewResolverController.class)
public class ViewResolversTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldResolveLogicalViewName() throws Exception {
        mockMvc.perform(get("/items")).andExpect(status().isOk())
                .andExpect(view().name("item"))
                .andExpect(model().attribute("name", "Laptop"))
                .andExpect(model().attribute("price", 999.99));
    }
}
