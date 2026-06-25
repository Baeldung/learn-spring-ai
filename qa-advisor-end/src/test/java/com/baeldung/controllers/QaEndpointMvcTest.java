package com.baeldung.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChatController.class)
@Import(QaEndpointMvcTest.ChatClientStubConfig.class)
class QaEndpointMvcTest {

    private static final String STUB_ANSWER = "A Worker is the person who carries out Tasks within a Campaign.";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VectorStore vectorStore;

    @MockitoBean
    private QuestionAnswerAdvisor questionAnswerAdvisor;

    @Test
    void whenCallingQaEndpoint_thenReturnsGroundedAnswer() throws Exception {
        mockMvc.perform(get("/qa").param("q", "what is a Worker"))
            .andExpect(status().isOk())
            .andExpect(content().string(STUB_ANSWER));
    }

    @TestConfiguration
    static class ChatClientStubConfig {

        @Bean
        ChatClient.Builder chatClientBuilder() {
            ChatClient.Builder builder = mock(ChatClient.Builder.class);
            ChatClient chatClient = mock(ChatClient.class);
            ChatClient.ChatClientRequestSpec requestSpec = mock(ChatClient.ChatClientRequestSpec.class);
            ChatClient.CallResponseSpec callResponseSpec = mock(ChatClient.CallResponseSpec.class);

            when(builder.build()).thenReturn(chatClient);
            when(chatClient.prompt()).thenReturn(requestSpec);
            when(requestSpec.advisors(any(QuestionAnswerAdvisor.class))).thenReturn(requestSpec);
            when(requestSpec.user(any(String.class))).thenReturn(requestSpec);
            when(requestSpec.call()).thenReturn(callResponseSpec);
            when(callResponseSpec.content()).thenReturn(STUB_ANSWER);

            return builder;
        }
    }
}
