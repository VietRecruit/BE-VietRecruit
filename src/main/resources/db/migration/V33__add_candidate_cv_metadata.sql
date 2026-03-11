ALTER TABLE candidates ADD COLUMN cv_file_size_bytes BIGINT;
ALTER TABLE candidates ADD COLUMN cv_original_filename VARCHAR(255);
ALTER TABLE candidates ADD COLUMN cv_content_type VARCHAR(100);
ALTER TABLE candidates ADD COLUMN cv_uploaded_at TIMESTAMP WITH TIME ZONE;
ALTER TABLE candidates ADD CONSTRAINT uq_candidates_user_id UNIQUE (user_id);
