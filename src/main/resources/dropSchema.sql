DELETE FROM roles WHERE name = 'user';

DELETE FROM roles WHERE name = 'admin';

DROP TABLE IF EXISTS users_roles;

DROP TABLE IF EXISTS roles;

DROP TABLE IF EXISTS users;

DROP SEQUENCE roles_seq;

DROP SEQUENCE users_seq;

