CREATE TABLE jobs (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(180) NOT NULL,
    description TEXT NOT NULL,
    company_id BIGINT NOT NULL REFERENCES company_profiles(id) ON DELETE CASCADE,
    city VARCHAR(100),
    state VARCHAR(80),
    country VARCHAR(80),
    remote BOOLEAN NOT NULL DEFAULT FALSE,
    employment_type VARCHAR(20) NOT NULL CHECK (employment_type IN ('INTERNSHIP', 'FULL_TIME', 'PART_TIME', 'CONTRACT')),
    experience_level VARCHAR(20) NOT NULL CHECK (experience_level IN ('INTERN', 'JUNIOR', 'MID_LEVEL', 'SENIOR')),
    salary_min NUMERIC(14,2) CHECK (salary_min IS NULL OR salary_min >= 0),
    salary_max NUMERIC(14,2) CHECK (salary_max IS NULL OR salary_max >= 0),
    status VARCHAR(20) NOT NULL CHECK (status IN ('OPEN', 'CLOSED')),
    expires_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_jobs_salary_range CHECK (salary_min IS NULL OR salary_max IS NULL OR salary_max >= salary_min)
);
CREATE INDEX idx_jobs_title ON jobs(title);
CREATE INDEX idx_jobs_status ON jobs(status);
CREATE INDEX idx_jobs_company_id ON jobs(company_id);
CREATE INDEX idx_jobs_search ON jobs(status, experience_level, employment_type, remote);
CREATE TABLE job_skills (
    job_id BIGINT NOT NULL REFERENCES jobs(id) ON DELETE CASCADE,
    skill_id BIGINT NOT NULL REFERENCES skills(id) ON DELETE CASCADE,
    PRIMARY KEY (job_id, skill_id)
);
