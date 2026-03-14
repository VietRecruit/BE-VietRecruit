package com.vietrecruit.common.storage;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.vietrecruit.common.enums.ApiErrorCode;
import com.vietrecruit.common.exception.ApiException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class R2StorageServiceImpl implements StorageService {

    private final S3Client s3Client;

    @Value("${cloudflare.r2.bucket}")
    private String bucket;

    @Value("${cloudflare.r2.public-url}")
    private String publicUrl;

    @Override
    @CircuitBreaker(name = "r2Storage", fallbackMethod = "uploadFallback")
    @Retry(name = "r2Storage")
    public String upload(String objectKey, InputStream data, String contentType, long sizeBytes) {
        PutObjectRequest request =
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(objectKey)
                        .contentType(contentType)
                        .build();

        s3Client.putObject(request, RequestBody.fromInputStream(data, sizeBytes));

        log.debug("Uploaded object: bucket={}, key={}", bucket, objectKey);
        return publicUrl + "/" + objectKey;
    }

    @Override
    @CircuitBreaker(name = "r2Storage", fallbackMethod = "deleteFallback")
    public void delete(String objectKey) {
        DeleteObjectRequest request =
                DeleteObjectRequest.builder().bucket(bucket).key(objectKey).build();

        s3Client.deleteObject(request);
        log.debug("Deleted object: bucket={}, key={}", bucket, objectKey);
    }

    @SuppressWarnings("unused")
    private String uploadFallback(
            String objectKey, InputStream data, String contentType, long sizeBytes, Throwable t) {
        log.error(
                "R2 upload circuit breaker triggered: bucket={}, key={}, error={}",
                bucket,
                objectKey,
                t.getMessage());
        throw new ApiException(
                ApiErrorCode.STORAGE_UNAVAILABLE,
                "File storage is temporarily unavailable. Please try again later.");
    }

    @SuppressWarnings("unused")
    private void deleteFallback(String objectKey, Throwable t) {
        log.warn(
                "R2 delete circuit breaker triggered: bucket={}, key={}, error={}. "
                        + "Orphaned object may remain.",
                bucket,
                objectKey,
                t.getMessage());
    }
}
