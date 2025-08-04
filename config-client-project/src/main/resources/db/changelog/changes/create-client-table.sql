CREATE TABLE client (
                        id SERIAL PRIMARY KEY,
                        username VARCHAR(255) NOT NULL UNIQUE,
                        password VARCHAR(255) NOT NULL
);

INSERT INTO client (username, password)
VALUES ('client1', '$2a$10$JqW5y2hZi1OYUG1YJcGrG.aUE9kkuH5udWLM/DcKJT.iWP61.Zrn2');