-- Create a table named 'privilege' to store privilege-related information.
CREATE TABLE privilege
(
    -- Column 'id' of type BIGINT, not nullable. Acts as the primary key.
    id   BIGINT       NOT NULL,

    -- Column 'name' to store the username of the user. Must be unique and not null.
    name VARCHAR(255) NOT NULL UNIQUE,

    -- Defining the primary key constraint on the 'id' column.
    CONSTRAINT pk_privilege PRIMARY KEY (id)
);
CREATE SEQUENCE privilege_seq START WITH 1 INCREMENT BY 1;