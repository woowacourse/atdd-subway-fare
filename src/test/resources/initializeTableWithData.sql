DROP TABLE MEMBER;

DROP TABLE SECTION;

DROP TABLE LINE;

DROP TABLE STATION;

create table if not exists STATION
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    primary key(id)
);

create table if not exists LINE
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    color varchar(20) not null,
    extraFare int not null default 0,
    primary key(id)
);

create table if not exists SECTION
(
    id bigint auto_increment not null,
    line_id bigint not null,
    up_station_id bigint not null,
    down_station_id bigint not null,
    distance int not null,
    primary key(id),
    foreign key (up_station_id) references station(id),
    foreign key (down_station_id) references station(id)
);

create table if not exists MEMBER
(
    id bigint auto_increment not null,
    email varchar(255) not null unique,
    password varchar(255) not null,
    age int not null,
    primary key(id)
);

INSERT INTO LINE(name, color, extraFare) VALUES('OneLine', 'BLUE', 0);
INSERT INTO STATION(name) VALUES('신설동역');
INSERT INTO STATION(name) VALUES('동묘앞역');
INSERT INTO STATION(name) VALUES('동대문역');
INSERT INTO SECTION(line_id, up_station_id, down_station_id, distance) VALUES(1, 1, 2, 10);
INSERT INTO SECTION(line_id, up_station_id, down_station_id, distance) VALUES(1, 2, 3, 10);