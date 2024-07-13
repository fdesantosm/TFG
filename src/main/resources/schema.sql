CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 1;
CREATE TYPE user_role AS ENUM ('ADMIN', 'USER');

CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL DEFAULT nextval('users_seq'),
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role user_role NOT NULL DEFAULT 'USER',
    PRIMARY KEY (id),
    UNIQUE (username),
    UNIQUE (email)
);

INSERT INTO users (username, email, password, role) VALUES ('tester', 'tfg@example.com', '$2a$10$xpdiB47q5sWsM3krCTMu9.RUmKcY5KGGa1LpWjwy48O/uOwnacP96', 'ADMIN');--1234556

CREATE SEQUENCE files_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE IF NOT EXISTS files (
    id BIGINT NOT NULL DEFAULT nextval('files_seq'),
    title VARCHAR(50),
    file_name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL,
    public_token VARCHAR(255),
    visible BOOLEAN,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT name_length CHECK (LENGTH(title) >= 5 AND LENGTH(title) <= 50),
    UNIQUE (title)
);

CREATE SEQUENCE file_tokens_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE IF NOT EXISTS file_tokens (
    id BIGINT NOT NULL DEFAULT nextval('file_tokens_seq'),
    token VARCHAR(255) NOT NULL UNIQUE,
    file_identifier VARCHAR(255) NOT NULL,
    expiration_time TIMESTAMP NOT NULL,
    file_id BIGINT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (file_id) REFERENCES files (id)

);