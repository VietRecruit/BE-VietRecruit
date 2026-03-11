package com.vietrecruit.common.storage;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    public void delete(String objectKey) {
        try {
            DeleteObjectRequest request =
                    DeleteObjectRequest.builder().bucket(bucket).key(objectKey).build();

            s3Client.deleteObject(request);
            log.debug("Deleted object: bucket={}, key={}", bucket, objectKey);
        } catch (Exception e) {
            log.warn(
                    "Failed to delete object from R2: bucket={}, key={}, error={}",
                    bucket,
                    objectKey,
                    e.getMessage());
        }
    }
}
