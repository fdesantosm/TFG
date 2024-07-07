CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 1;
CREATE TYPE user_role AS ENUM ('ADMIN', 'USER');

CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL DEFAULT nextval('users_seq'),
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role user_role NOT NULL DEFAULT 'USER',
    PRIMARY KEY (id)
);



INSERT INTO users (username, email, password, role) VALUES ('tester', 'tfg@example.com', '$2a$10$xpdiB47q5sWsM3krCTMu9.RUmKcY5KGGa1LpWjwy48O/uOwnacP96', 'ADMIN');--1234556
