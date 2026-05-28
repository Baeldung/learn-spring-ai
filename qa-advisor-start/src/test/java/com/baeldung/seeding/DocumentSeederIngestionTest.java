package com.baeldung.seeding;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class DocumentSeederIngestionTest {

    @MockitoBean
    private VectorStore vectorStore;

    @Autowired
    private DocumentSeeder seeder;

    @Test
    @SuppressWarnings("unchecked")
    void whenSeederRuns_thenChunksWithSourceMetadataIngestedIntoVectorStore() throws Exception {
        seeder.run();

        ArgumentCaptor<List<Document>> captor = ArgumentCaptor.forClass(List.class);
        verify(vectorStore, atLeastOnce()).accept(captor.capture());

        List<Document> ingested = captor.getValue();
        assertThat(ingested).isNotEmpty();
        assertThat(ingested.get(0)
            .getMetadata()).containsKey("file_name");
        assertThat(ingested.get(0)
            .getMetadata()).containsEntry("file_name", "playbook.pdf");
    }
}
