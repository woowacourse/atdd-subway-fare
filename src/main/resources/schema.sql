CREATE TABLE IF NOT EXISTS station
(
    id
    BIGINT
    auto_increment
    NOT
    NULL,
    name
    VARCHAR
(
    255
) NOT NULL UNIQUE,
    PRIMARY KEY
(
    id
)
    );

CREATE TABLE IF NOT EXISTS line
(
    id
    BIGINT
    auto_increment
    NOT
    NULL,
    name
    VARCHAR
(
    255
) NOT NULL UNIQUE,
    color VARCHAR
(
    20
) NOT NULL UNIQUE,
    extra_fare INT NOT NULL DEFAULT 0,
    PRIMARY KEY
(
    id
)
    );

CREATE TABLE IF NOT EXISTS section
(
    id
    BIGINT
    auto_increment
    NOT
    NULL,
    line_id
    BIGINT
    NOT
    NULL,
    up_station_id
    BIGINT
    NOT
    NULL,
    down_station_id
    BIGINT
    NOT
    NULL,
    distance
    INT
    NOT
    NULL,
    PRIMARY
    KEY
(
    id
),
    FOREIGN KEY
(
    up_station_id
) REFERENCES station
(
    id
),
    FOREIGN KEY
(
    down_station_id
) REFERENCES station
(
    id
)
    );

CREATE TABLE IF NOT EXISTS member
(
    id
    BIGINT
    auto_increment
    NOT
    NULL,
    email
    VARCHAR
(
    255
) NOT NULL UNIQUE,
    password VARCHAR
(
    255
) NOT NULL,
    age INT NOT NULL,
    PRIMARY KEY
(
    id
)
    );