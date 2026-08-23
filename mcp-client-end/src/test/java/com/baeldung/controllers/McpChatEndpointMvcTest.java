package com.baeldung.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChatController.class)
@Import(McpChatEndpointMvcTest.ChatClientStubConfig.class)
class McpChatEndpointMvcTest {

    private static final String STUB_ANSWER = "It's 17:55 in Tokyo, which would be 04:55 in New York.";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenCallingMcpChatEndpoint_thenReturnsToolDerivedAnswer() throws Exception {
        mockMvc.perform(get("/mcp-chat").param("message", "What time is it in Tokyo right now, and what would that be in New York?"))
            .andExpect(status().isOk())
            .andExpect(content().string(STUB_ANSWER));
    }

    @TestConfiguration
    static class ChatClientStubConfig {

        @Bean
        ChatClient.Builder chatClientBuilder() {
            ChatClient.Builder builder = mock(ChatClient.Builder.class);
            when(builder.build()).thenReturn(mock(ChatClient.class));

            return builder;
        }

        @Bean
        ChatClient mcpChatClient() {
            ChatClient mcpChatClient = mock(ChatClient.class);
            ChatClient.ChatClientRequestSpec requestSpec = mock(ChatClient.ChatClientRequestSpec.class);
            ChatClient.CallResponseSpec callResponseSpec = mock(ChatClient.CallResponseSpec.class);

            when(mcpChatClient.prompt()).thenReturn(requestSpec);
            when(requestSpec.user(any(String.class))).thenReturn(requestSpec);
            when(requestSpec.call()).thenReturn(callResponseSpec);
            when(callResponseSpec.content()).thenReturn(STUB_ANSWER);

            return mcpChatClient;
        }
    }
}
