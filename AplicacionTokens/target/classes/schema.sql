CREATE SEQUENCE clients_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE clients (
    id BIGINT NOT NULL DEFAULT nextval('clients_id_seq'),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);
