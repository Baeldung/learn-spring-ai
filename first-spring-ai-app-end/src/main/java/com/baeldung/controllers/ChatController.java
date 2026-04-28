package com.baeldung.controllers;

import java.util.Optional;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.model.Usage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    @GetMapping("/chat/metadata")
    public String chatWithMetadata(@RequestParam String message) {
        ChatResponse response = chatClient.prompt()
                .user(message)
                .call()
                .chatResponse();
        Usage usage = response.getMetadata().getUsage();
        int tokenCount = Optional.ofNullable(usage).map(Usage::getTotalTokens).orElse(0);
        return response.getResult().getOutput().getText() + " (tokens: " + tokenCount + ")";
    }
}
