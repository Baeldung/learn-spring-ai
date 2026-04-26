package com.baeldung.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baeldung.domain.model.Task;

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

    @GetMapping("/task/generate")
    public Task generateTask(@RequestParam String description) {
        return chatClient.prompt()
            .user("Create a task based on: " + description)
            .call()
            .entity(Task.class);
    }

    @GetMapping("/campaign/checklist")
    public List<String> generateChecklist(@RequestParam String campaignName) {
        return chatClient.prompt()
            .user("List 5 key steps to launch a campaign for: " + campaignName)
            .call()
            .entity(new ParameterizedTypeReference<List<String>>() {});
    }

    @GetMapping("/task/generate-batch")
    public List<Task> generateBatch(@RequestParam String project) {
        return chatClient.prompt()
            .user("Generate 3 tasks for this project: " + project)
            .call()
            .entity(new ParameterizedTypeReference<List<Task>>() {});
    }

    @GetMapping("/campaign/prioritize")
    public Map<String, String> prioritizeTasks(@RequestParam String tasks) {
        return chatClient.prompt()
            .user("For each of these tasks, assign a priority (High, Medium, Low): " + tasks)
            .call()
            .entity(new ParameterizedTypeReference<Map<String, String>>() {});
    }
}
