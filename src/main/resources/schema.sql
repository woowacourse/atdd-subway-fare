drop table if exists SECTION;
drop table if exists LINE;
drop table if exists MEMBER;
drop table if exists STATION;

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
    color varchar(20) not null unique,
    extra_fare int default 0,
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
    FOREIGN KEY (up_station_id) REFERENCES STATION(id),
    FOREIGN KEY (down_station_id) REFERENCES STATION(id),
    FOREIGN KEY (line_id) REFERENCES LINE(id) ON DELETE CASCADE
);

create table if not exists MEMBER
(
    id bigint auto_increment not null,
    email varchar(255) not null unique,
    password varchar(255) not null,
    age int not null,
    primary key(id)
);
