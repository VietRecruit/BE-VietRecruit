package com.vietrecruit.common.storage;

import java.io.InputStream;

public interface StorageService {

    /**
     * Uploads a file to object storage.
     *
     * @param objectKey the storage key (path) for the object
     * @param data the file content stream
     * @param contentType MIME type of the file
     * @param sizeBytes file size in bytes
     * @return the public URL of the uploaded object
     */
    String upload(String objectKey, InputStream data, String contentType, long sizeBytes);

    /**
     * Deletes an object from storage by its key.
     *
     * @param objectKey the storage key to delete
     */
    void delete(String objectKey);
}
