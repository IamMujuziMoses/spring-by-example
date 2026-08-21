CREATE TABLE accounts (id BIGINT PRIMARY KEY,
    owner VARCHAR(255) NOT NULL,
    balance DECIMAL(10, 2) NOT NULL
);

INSERT INTO accounts (id, owner, balance)
VALUES (1, 'Spring', 1000.00);