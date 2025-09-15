-- Create a table named 'user' to store user-related information.
CREATE TABLE web_user
(
    -- Column 'id' of type BIGINT, not nullable. Acts as the primary key.
    id       BIGINT       NOT NULL,

    -- Column 'username' to store the username of the user. Must be unique and not null.
    username VARCHAR(255) NOT NULL UNIQUE,

    -- Column 'password' to store the user's password. Not null.
    password VARCHAR(255) NOT NULL,

    -- Defining the primary key constraint on the 'id' column.
    CONSTRAINT pk_user PRIMARY KEY (id)
);
CREATE SEQUENCE WEB_USER_SEQ START WITH 1 INCREMENT BY 1;