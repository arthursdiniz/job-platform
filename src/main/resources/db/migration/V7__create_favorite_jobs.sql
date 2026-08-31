CREATE TABLE favorite_jobs (
    id BIGSERIAL PRIMARY KEY,
    candidate_id BIGINT NOT NULL REFERENCES candidate_profiles(id) ON DELETE CASCADE,
    job_id BIGINT NOT NULL REFERENCES jobs(id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uk_favorite_candidate_job UNIQUE (candidate_id, job_id)
);
CREATE INDEX idx_favorites_candidate_id ON favorite_jobs(candidate_id);
CREATE INDEX idx_favorites_job_id ON favorite_jobs(job_id);
