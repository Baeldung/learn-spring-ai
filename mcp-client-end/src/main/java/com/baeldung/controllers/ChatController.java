package com.baeldung.controllers;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatClient chatClient;
    private final ChatClient mcpChatClient;

    public ChatController(ChatClient.Builder builder, ChatClient mcpChatClient) {
        this.chatClient = builder.build();
        this.mcpChatClient = mcpChatClient;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return chatClient.prompt()
            .user(message)
            .call()
            .content();
    }

    @GetMapping("/mcp-chat")
    public String mcpChat(@RequestParam String message) {
        return mcpChatClient.prompt()
            .user(message)
            .call()
            .content();
    }
}
