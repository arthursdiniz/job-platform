CREATE TABLE skills (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(80) NOT NULL,
    CONSTRAINT uk_skills_name UNIQUE (name)
);
CREATE TABLE candidate_skills (
    candidate_id BIGINT NOT NULL REFERENCES candidate_profiles(id) ON DELETE CASCADE,
    skill_id BIGINT NOT NULL REFERENCES skills(id) ON DELETE CASCADE,
    PRIMARY KEY (candidate_id, skill_id)
);
