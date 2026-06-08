package com.baeldung.controllers;

import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final QuestionAnswerAdvisor questionAnswerAdvisor;

    public ChatController(ChatClient.Builder builder, VectorStore vectorStore, QuestionAnswerAdvisor questionAnswerAdvisor) {
        this.chatClient = builder.build();
        this.vectorStore = vectorStore;
        this.questionAnswerAdvisor = questionAnswerAdvisor;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return chatClient.prompt()
            .user(message)
            .call()
            .content();
    }

    @GetMapping("/search")
    public List<Document> search(@RequestParam String q) {
        return vectorStore.similaritySearch(
            SearchRequest.builder()
                .similarityThreshold(0.6)
                .query(q)
                .build());
    }

    @GetMapping("/qa")
    public String qa(@RequestParam String q) {
        return chatClient.prompt()
            .advisors(questionAnswerAdvisor)
            .user(q)
            .call()
            .content();
    }
}
