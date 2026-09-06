package com.baeldung;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class RagEvaluatorIntegrationTest {

    private static final String QUESTION = "Who approves campaign budgets?";
    private static final String CONTEXT = "Campaign budgets require approval from the Finance team.";

    @MockitoBean
    VectorStore vectorStore;

    @Autowired
    ChatModel chatModel;

    @BeforeEach
    void setUpContext() {
        when(vectorStore.similaritySearch(any(SearchRequest.class))).thenReturn(List.of(new Document(CONTEXT)));
    }

    @Test
    void givenRagAnswer_whenEvaluatingRelevancy_thenPasses() {
        EvaluationRequest request = createEvaluationRequest();

        RelevancyEvaluator evaluator = new RelevancyEvaluator(ChatClient.builder(chatModel));
        EvaluationResponse response = evaluator.evaluate(request);

        assertTrue(response.isPass());
    }

    @Test
    void givenSupportedAndUnsupportedClaims_whenFactChecking_thenDistinguishesThem() {
        FactCheckingEvaluator evaluator = FactCheckingEvaluator.builder(ChatClient.builder(chatModel)).build();

        EvaluationRequest supportedRequest = createEvaluationRequest();
        EvaluationResponse supportedResponse = evaluator.evaluate(supportedRequest);

        assertTrue(supportedResponse.isPass());

        EvaluationRequest unsupportedRequest = new EvaluationRequest(QUESTION, List.of(new Document(CONTEXT)), "The Finance team approves campaign budgets within 24 hours.");
        EvaluationResponse unsupportedResponse = evaluator.evaluate(unsupportedRequest);

        assertFalse(unsupportedResponse.isPass());
    }

    private EvaluationRequest createEvaluationRequest() {
        QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor.builder(vectorStore).build();
        ChatResponse chatResponse = ChatClient.builder(chatModel).build().prompt().user(QUESTION).advisors(advisor).call().chatResponse();

        return new EvaluationRequest(QUESTION, chatResponse.getMetadata().get(QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS), chatResponse.getResult().getOutput().getText());
    }
}
