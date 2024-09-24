CREATE TABLE recommendation (
    id BIGSERIAL PRIMARY KEY,
    url TEXT,
    name VARCHAR(255),
    rating INT,
    description TEXT,
    submitted TIMESTAMP WITHOUT TIME ZONE,
    target_group VARCHAR(50),
    person_id BIGINT,
    CONSTRAINT fk_recommendation_on_person FOREIGN KEY (person_id) REFERENCES person (id)
);
