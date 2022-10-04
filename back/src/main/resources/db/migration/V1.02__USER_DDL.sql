CREATE TABLE `users`
(
    id       VARCHAR(255) PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    email    VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    status   VARCHAR(20)  NOT NULL,
    roles    VARCHAR(20)  NOT NULL
);

INSERT INTO `users` VALUES(uuid(), 'Admin', 'admin@test.com', 'ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f', 'ACTIVE', 'ADMIN');