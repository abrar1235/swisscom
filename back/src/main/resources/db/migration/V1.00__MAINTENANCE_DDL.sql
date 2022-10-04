CREATE TABLE `maintenance`
(
    id          VARCHAR(255) PRIMARY KEY,
    description VARCHAR(100),
    start_time  TIMESTAMP    NOT NULL,
    end_time    TIMESTAMP    NOT NULL,
    time_zone   VARCHAR(50)  NOT NULL,
    user_id     VARCHAR(255) NOT NULL
);