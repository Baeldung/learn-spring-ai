package com.baeldung.controllers;

import static org.junit.jupiter.api.Assertions.assertFalse;
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

    @Test
    void whenGenerateTask_thenReturnsTaskJson() throws Exception {
        String result = mockMvc.perform(get("/task/generate").param("description", "Buy groceries"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        logger.info("Generated Task: " + result);
        assertFalse(result.isEmpty());
    }

    @Test
    void whenGenerateChecklist_thenReturnsListOfStrings() throws Exception {
        String result = mockMvc.perform(get("/campaign/checklist").param("campaignName", "Summer Sale"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        logger.info("Checklist: " + result);
        assertFalse(result.isEmpty());
    }

    @Test
    void whenGenerateBatch_thenReturnsListOfTasks() throws Exception {
        String result = mockMvc.perform(get("/task/generate-batch").param("project", "Website Redesign"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        logger.info("Batch Tasks: " + result);
        assertFalse(result.isEmpty());
    }

    @Test
    void whenPrioritizeTasks_thenReturnsMap() throws Exception {
        String result = mockMvc.perform(get("/campaign/prioritize").param("tasks", "Fix server crash, Email the team, Buy coffee"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        logger.info("Prioritized Tasks: " + result);
        assertFalse(result.isEmpty());
    }
}
