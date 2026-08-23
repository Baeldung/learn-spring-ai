package com.baeldung.controllers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class RagControllerIntegrationTest {

    private Logger logger = LoggerFactory.getLogger(RagControllerIntegrationTest.class);

    @MockitoBean
    VectorStore vectorStore;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenAskQuestionWithRag_thenReturnsStringResponse() throws Exception {
        Mockito.when(vectorStore.similaritySearch(Mockito.any(SearchRequest.class)))
            .thenReturn(List.of(new Document("Spring AI is a framework for building AI-powered applications.")));

        String result = mockMvc.perform(get("/rag-chat").param("message", "What is Spring AI?"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        logger.info("RAG AI Result: " + result);
        assertNotNull(result);
    }
}
