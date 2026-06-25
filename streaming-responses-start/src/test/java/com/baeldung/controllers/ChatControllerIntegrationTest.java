package com.baeldung.controllers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ChatControllerIntegrationTest {

    private Logger logger = LoggerFactory.getLogger(ChatControllerIntegrationTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenAskQuestion_thenReturnsStringResponse() throws Exception {
        String result = mockMvc.perform(get("/chat").param("message", "What does JDK stand for in Java?"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        logger.info("AI Result: " + result);
        assertTrue(result.contains("Java"));
    }
}