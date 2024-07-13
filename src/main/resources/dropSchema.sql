-- DELETE FROM users;

DROP TABLE file_tokens;
DROP SEQUENCE file_tokens_seq;

DROP TABLE files;
DROP SEQUENCE files_seq;

DELETE FROM users WHERE id = 1;

DROP TABLE users;
DROP TYPE user_role;
DROP SEQUENCE users_seq;

