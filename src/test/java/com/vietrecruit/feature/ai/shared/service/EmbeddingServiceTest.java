package com.vietrecruit.feature.ai.shared.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.vietrecruit.feature.ai.shared.config.VectorStoreProperties;

/** Unit tests for EmbeddingService — covers document ID normalization and embedding cache logic. */
@ExtendWith(MockitoExtension.class)
class EmbeddingServiceTest {

    @Mock private VectorStore vectorStore;
    @Mock private EmbeddingModel embeddingModel;
    @Mock private StringRedisTemplate redisTemplate;
    @Mock private VectorStoreProperties vectorStoreProperties;

    private EmbeddingService embeddingService;

    @BeforeEach
    void setUp() {
        embeddingService =
                new EmbeddingService(
                        vectorStore, embeddingModel, redisTemplate, vectorStoreProperties);
    }

    @Test
    @DisplayName("embedAndStore: prefixed logical key is converted to a valid UUID before storing")
    void embedAndStore_prefixedId_storedAsValidUuid() {
        UUID candidateId = UUID.randomUUID();
        String logicalKey = "cv-" + candidateId;
        String content = "Java developer with 5 years experience";
        Map<String, Object> metadata = Map.of("type", "cv", "candidateId", candidateId.toString());

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Document>> captor = ArgumentCaptor.forClass(List.class);

        embeddingService.embedAndStore(logicalKey, content, metadata);

        verify(vectorStore).add(captor.capture());
        List<Document> stored = captor.getValue();
        assertEquals(1, stored.size());

        String storedId = stored.get(0).getId();
        // The stored ID must be a valid UUID — not the raw prefixed string
        assertDoesNotThrow(
                () -> UUID.fromString(storedId), "Stored document ID must be a valid UUID");
        assertNotEquals(logicalKey, storedId, "Stored ID must not be the raw prefixed logical key");

        // Deterministic: same input always yields the same UUID
        String expected =
                UUID.nameUUIDFromBytes(logicalKey.getBytes(StandardCharsets.UTF_8)).toString();
        assertEquals(expected, storedId);
    }

    @Test
    @DisplayName(
            "embedAndStore: two calls with the same logical key produce the same document UUID")
    void embedAndStore_sameLogicalKey_deterministicUuid() {
        UUID candidateId = UUID.randomUUID();
        String logicalKey = "cv-" + candidateId;
        Map<String, Object> metadata = Map.of("type", "cv");

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Document>> captor = ArgumentCaptor.forClass(List.class);

        embeddingService.embedAndStore(logicalKey, "first content", metadata);
        embeddingService.embedAndStore(logicalKey, "second content", metadata);

        verify(vectorStore, times(2)).add(captor.capture());
        List<List<Document>> allInvocations = captor.getAllValues();

        String firstId = allInvocations.get(0).get(0).getId();
        String secondId = allInvocations.get(1).get(0).getId();
        assertEquals(firstId, secondId, "Same logical key must always map to the same UUID");
    }

    @Test
    @DisplayName("embedAndStore: job-prefixed logical key is also converted to a valid UUID")
    void embedAndStore_jobPrefixedId_storedAsValidUuid() {
        UUID jobId = UUID.randomUUID();
        String logicalKey = "job-" + jobId;

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Document>> captor = ArgumentCaptor.forClass(List.class);

        embeddingService.embedAndStore(logicalKey, "Software engineer job", Map.of("type", "job"));

        verify(vectorStore).add(captor.capture());
        String storedId = captor.getValue().get(0).getId();
        assertDoesNotThrow(() -> UUID.fromString(storedId));
        assertNotEquals(logicalKey, storedId);
    }
}
