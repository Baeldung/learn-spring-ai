package com.baeldung.controllers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ChatControllerIntegrationTest {

    private Logger logger = LoggerFactory.getLogger(ChatControllerIntegrationTest.class);

    @MockitoBean
    VectorStore vectorStore;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenAskQuestion_thenReturnsStringResponse() throws Exception {
        String result = mockMvc.perform(get("/chat").param("message", "What does it mean when a task is on hold?"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        logger.info("AI Result: " + result);
        assertTrue(result.toLowerCase().contains("hold"));
    }
}
