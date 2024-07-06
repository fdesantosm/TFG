CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE roles_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE users (
    id BIGINT NOT NULL DEFAULT nextval('users_seq'),
    username VARCHAR(30) NOT NULL,
    email VARCHAR(200) NOT NULL,
    password VARCHAR(30) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE roles (
    id BIGINT NOT NULL DEFAULT nextval('roles_seq'),
    name VARCHAR (30) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE users_roles (
    user_id BIGINT NOT NULL,
    rol_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, rol_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);

INSERT INTO roles (name) VALUES ('user');
INSERT INTO roles (name) VALUES ('admin');