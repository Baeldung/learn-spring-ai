package com.baeldung.seeding;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class DocumentSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DocumentSeeder.class);

    private final VectorStore vectorStore;
    private final Resource source;
    private final TokenTextSplitter splitter = new TokenTextSplitter();

    public DocumentSeeder(VectorStore vectorStore,
        @Value("classpath:documents/playbook.pdf") Resource source) {
        this.vectorStore = vectorStore;
        this.source = source;
    }

    @Override
    public void run(String... args) {
        PagePdfDocumentReader reader = new PagePdfDocumentReader(source);
        List<Document> documents = reader.get();
        List<Document> chunks = splitter.split(documents);
        vectorStore.accept(chunks);
        log.info("Ingested {} chunk(s) from {}", chunks.size(), source.getFilename());
    }
}
