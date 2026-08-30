package com.springbyexample.requestbody;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


/**
 * @author Mujuzi Moses
 */
@WebMvcTest(RequestBodyController.class)
public class RequestBodyTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReadRequestBody() throws Exception {
        mockMvc.perform(post("/api/items").contentType(MediaType.APPLICATION_JSON)
                        .content(""" 
                                {
                                    "name": "Laptop",
                                    "price": 999.99}
                                """))
                .andExpect(status().isOk())
                .andExpect(content().string("Created item: Laptop"));

    }
}
