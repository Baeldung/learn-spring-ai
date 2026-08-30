package com.baeldung;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class VectorStoreIntegrationTest {

    private static final String NOTE = "Campaign budgets require approval from the Finance team.";

    @TestBean
    EmbeddingModel embeddingModel;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VectorStore vectorStore;

    static EmbeddingModel embeddingModel() {
        return new FixedWidthEmbeddingModel();
    }

    @Test
    void givenIngestedDocument_whenSearchingTheStore_thenDocumentIsFound() throws Exception {
        mockMvc.perform(post("/ingest").contentType(MediaType.TEXT_PLAIN).content(NOTE))
            .andExpect(status().isOk());

        List<Document> matches = vectorStore.similaritySearch(SearchRequest.builder().query("campaign budget approval").build());

        assertEquals(1, matches.size());
        assertEquals(NOTE, matches.get(0).getText());
    }
}
