package com.baeldung.seeding;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DocumentSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DocumentSeeder.class);

    private final VectorStore vectorStore;

    public DocumentSeeder(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void run(String... args) {
        List<Document> documents = List.of(
            new Document("A Worker can be assigned a Task to carry out within a Campaign."),
            new Document("Each Task tracks a status that moves from PENDING to IN_PROGRESS to DONE."),
            new Document("Campaigns group related Tasks under a shared deadline and budget."),
            new Document("A Worker who finishes a Task can pick up the next pending one in the same Campaign."),
            new Document("A Campaign is uniquely identified by its code."),
            new Document("Campaign budgets require approval from the Finance team."));
        vectorStore.add(documents);
    }
}
