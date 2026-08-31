CREATE TABLE candidate_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    headline VARCHAR(160),
    bio TEXT,
    city VARCHAR(100),
    state VARCHAR(80),
    country VARCHAR(80),
    experience_level VARCHAR(20) CHECK (experience_level IN ('INTERN', 'JUNIOR', 'MID_LEVEL', 'SENIOR')),
    linkedin_url VARCHAR(300),
    github_url VARCHAR(300),
    portfolio_url VARCHAR(300),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
