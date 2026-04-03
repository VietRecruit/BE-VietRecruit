package com.vietrecruit.feature.ai.knowledge.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.vietrecruit.feature.ai.knowledge.dto.KnowledgeDocumentResponse;
import com.vietrecruit.feature.ai.knowledge.dto.KnowledgeUploadResponse;

public interface KnowledgeIngestionService {

    /**
     * Uploads a knowledge document to storage and enqueues it for asynchronous chunking and
     * embedding ingestion.
     *
     * @param file the multipart document file (PDF supported)
     * @param title human-readable document title
     * @param category classification category for the document
     * @param uploadedBy UUID of the admin user uploading the document
     * @return upload result including the storage key and document ID
     */
    KnowledgeUploadResponse upload(
            MultipartFile file, String title, String category, UUID uploadedBy);

    /**
     * Returns a paginated list of knowledge documents, optionally filtered by category.
     *
     * @param category optional category filter; null to return all categories
     * @param pageable pagination and sort parameters
     * @return page of knowledge document responses
     */
    Page<KnowledgeDocumentResponse> list(String category, Pageable pageable);

    /**
     * Deletes a knowledge document and its associated vector embeddings from the store.
     *
     * @param documentId the target document's UUID
     */
    void delete(UUID documentId);

    /**
     * Performs the synchronous ingestion pipeline: fetches the file from storage, parses it with
     * Tika, chunks the text, generates embeddings, and stores them in the vector store.
     *
     * @param documentId the document entity UUID to update with ingestion status
     * @param fileKey the R2 storage object key for the raw file
     * @param category document category passed through to embedding metadata
     * @param title document title passed through to embedding metadata
     */
    void processIngestion(UUID documentId, String fileKey, String category, String title);
}
