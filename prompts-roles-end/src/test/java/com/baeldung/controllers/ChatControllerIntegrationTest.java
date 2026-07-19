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
    void whenInScopeMarketingPmRequest_thenReturnsOkAndNonBlankResponse() throws Exception {
        String result = mockMvc.perform(get("/marketing-pm")
            .param("message", "We are launching a new product in Q4. How should I structure the timeline?"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        logger.info("Marketing PM in-scope result: " + result);
        assertFalse(result.isBlank());
    }

    @Test
    void whenOutOfScopeMarketingPmRequest_thenReturnsOkAndNonBlankResponse() throws Exception {
        String result = mockMvc.perform(get("/marketing-pm")
            .param("message", "Write a catchy slogan for our new coffee brand"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        logger.info("Marketing PM out-of-scope result: " + result);
        assertFalse(result.isBlank());
    }

    @Test
    void whenCampaignPlanRequest_thenReturnsOkAndNonBlankResponse() throws Exception {
        String result = mockMvc.perform(get("/campaign-plan")
            .param("campaign", "Customer Referral")
            .param("channel", "Email"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        logger.info("Campaign plan result: " + result);
        assertFalse(result.isBlank());
    }

    @Test
    void whenMarketingPmFollowUpRequest_thenReturnsOkAndNonBlankResponse() throws Exception {
        String result = mockMvc.perform(get("/marketing-pm-followup")
            .param("message", "Who owns the creative concepting step?"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        logger.info("Marketing PM follow-up result: " + result);
        assertFalse(result.isBlank());
    }
}