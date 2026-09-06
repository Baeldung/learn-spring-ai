package com.baeldung.controllers;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RagController {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public RagController(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder
            .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).build())
            .build();
        this.vectorStore = vectorStore;
    }

    @GetMapping("/rag-chat")
    public String ragChat(@RequestParam String message) {
        return chatClient.prompt()
            .user(message)
            .call()
            .content();
    }

    @PostMapping("/ingest")
    public String ingest(@RequestBody String text) {
        Document document = new Document(text);
        vectorStore.add(List.of(document));
        return "Ingested successfully";
    }
}
