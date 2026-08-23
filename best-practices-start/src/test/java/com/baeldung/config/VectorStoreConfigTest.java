package com.baeldung.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;

class VectorStoreConfigTest {

    @Test
    void givenInMemoryVectorStore_whenAddingDocument_thenSearchFindsIt() {
        EmbeddingModel embeddingModel = mock(EmbeddingModel.class);
        Document campaign = new Document("The Website Refresh campaign has four tasks.");
        when(embeddingModel.dimensions()).thenReturn(2);
        when(embeddingModel.embed(campaign)).thenReturn(new float[] { 1.0f, 0.0f });
        when(embeddingModel.embed("Website Refresh")).thenReturn(new float[] { 1.0f, 0.0f });

        VectorStore vectorStore = new VectorStoreConfig().vectorStore(embeddingModel);
        vectorStore.add(List.of(campaign));
        List<Document> matches = vectorStore.similaritySearch(SearchRequest.builder()
            .query("Website Refresh")
            .topK(1)
            .similarityThreshold(0.0)
            .build());

        assertInstanceOf(SimpleVectorStore.class, vectorStore);
        assertEquals(1, matches.size());
        assertEquals(campaign.getText(), matches.getFirst().getText());
    }
}
