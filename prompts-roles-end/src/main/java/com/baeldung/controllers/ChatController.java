package com.baeldung.controllers;

import org.springframework.ai.chat.client.ChatClient;
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

    @GetMapping("/marketing-pm")
    public String marketingPmChat(@RequestParam String message) {
        return chatClient.prompt()
                .system("""
                        You are a Marketing Project Manager. \
                        You focus on campaign timelines, budgets, \
                        and coordinating the creative team. \
                        You do NOT write ad copy or design visuals yourself. \
                        If a user asks you to write a slogan or design a logo, \
                        politely tell them that is a task for the Creative Team \
                        and suggest they brief the Copywriter or Art Director.""")
                .user(message)
                .call()
                .content();
    }

    @GetMapping("/campaign-plan")
    public String campaignPlan(
            @RequestParam String campaign,
            @RequestParam String channel) {
        String templateText = """
                Draft a high-level project plan \
                for a {campaign} campaign launching on {channel}. \
                List 3 key milestones.""";

        return chatClient.prompt()
                .user(u -> u.text(templateText)
                        .param("campaign", campaign)
                        .param("channel", channel))
                .call()
                .content();
    }
}
