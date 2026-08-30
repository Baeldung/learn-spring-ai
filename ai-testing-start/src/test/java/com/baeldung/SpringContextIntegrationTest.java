package com.baeldung;

import org.junit.jupiter.api.Test;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class SpringContextIntegrationTest {

    @MockitoBean
    VectorStore vectorStore;

    @Test
    void contextLoads() {
    }

}
