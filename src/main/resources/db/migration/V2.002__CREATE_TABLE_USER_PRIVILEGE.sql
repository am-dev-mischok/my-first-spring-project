-- Create a table named 'user_privilege' to store the relationship between users and their privileges.
CREATE TABLE user_privilege
(
    -- Column 'privilege_id' of type BIGINT, not nullable. Part of the composite primary key.
    privilege_id BIGINT NOT NULL,

    -- Column 'user_id' of type BIGINT, not nullable. Part of the composite primary key.
    user_id      BIGINT NOT NULL,

    -- Defining a composite primary key constraint on 'privilege_id' and 'user_id'.
    CONSTRAINT pk_user_privilege PRIMARY KEY (privilege_id, user_id)
);

-- Add a foreign key constraint to 'user_privilege' on 'privilege_id',
-- referencing the 'id' column in the 'privilege' table.
ALTER TABLE user_privilege
    ADD CONSTRAINT fk_usepri_on_privilege FOREIGN KEY (privilege_id) REFERENCES privilege (id);

-- Add a foreign key constraint to 'user_privilege' on 'user_id',
-- referencing the 'id' column in the 'user' table.
ALTER TABLE user_privilege
    ADD CONSTRAINT fk_usepri_on_user FOREIGN KEY (user_id) REFERENCES web_user (id);