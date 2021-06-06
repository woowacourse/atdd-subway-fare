CREATE TABLE IF NOT EXISTS station
(
    id BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS line
(
    id BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    color VARCHAR(20) NOT NULL,
    extra_fare int NOT NULL DEFAULT 0,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS section
(
    id BIGINT AUTO_INCREMENT NOT NULL,
    line_id BIGINT NOT NULL,
    up_station_id BIGINT NOT NULL,
    down_station_id BIGINT NOT NULL,
    distance INT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (up_station_id) references station(id),
    FOREIGN KEY (down_station_id) references station(id),
    FOREIGN KEY (line_id) references line(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS member
(
    id BIGINT AUTO_INCREMENT NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    PRIMARY KEY (id)
);
