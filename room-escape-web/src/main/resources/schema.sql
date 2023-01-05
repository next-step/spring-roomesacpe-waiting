CREATE TABLE RESERVATION
(
    id          bigint      not null auto_increment,
    schedule_id bigint      not null,
    member_id   bigint      not null,
    status      varchar(20) not null,
    primary key (id)
);

CREATE TABLE WAITING
(
    id          bigint not null auto_increment,
    schedule_id bigint not null,
    member_id   bigint not null,
    waiting_number int not null,
    waiting_status varchar(255),
    reservation_waiting_time timestamp,
    primary key (id)
);

CREATE TABLE THEME
(
    id    bigint not null auto_increment,
    name  varchar(20),
    desc  varchar(255),
    price BIGINT,
    primary key (id)
);

CREATE TABLE SCHEDULE
(
    id       bigint not null auto_increment,
    theme_id bigint not null,
    date     date   not null,
    time     time   not null,
    primary key (id)
);

CREATE TABLE MEMBER
(
    id       bigint      not null auto_increment,
    username varchar(20) not null,
    password varchar(20) not null,
    name     varchar(20) not null,
    phone    varchar(20) not null,
    role     varchar(20) not null,
    primary key (id)
);

CREATE TABLE SALES
(
    id        bigint not null auto_increment,
    amount    bigint not null,
    status    varchar(20)   not null,
    reservation_id bigint not null,
    primary key (id)
);
