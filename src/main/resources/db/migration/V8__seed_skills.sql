INSERT INTO skills(name) VALUES
('Java'), ('Spring Boot'), ('PostgreSQL'), ('Docker'), ('AWS'),
('React'), ('Angular'), ('Python'), ('Git'), ('Linux')
ON CONFLICT (name) DO NOTHING;
