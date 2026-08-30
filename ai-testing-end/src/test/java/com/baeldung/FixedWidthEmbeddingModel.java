package com.baeldung;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;

public class FixedWidthEmbeddingModel implements EmbeddingModel {

    private static final float[] VECTOR = { 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f };

    @Override
    public float[] embed(Document document) {
        return VECTOR.clone();
    }

    @Override
    public EmbeddingResponse call(EmbeddingRequest request) {
        List<Embedding> results = new ArrayList<>();
        for (int i = 0; i < request.getInstructions().size(); i++) {
            results.add(new Embedding(VECTOR.clone(), i));
        }
        return new EmbeddingResponse(results);
    }
}
