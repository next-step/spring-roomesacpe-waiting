CREATE TABLE RESERVATION
(
    id          bigint not null auto_increment,
    schedule_id bigint not null,
    member_id   bigint not null,
    deleted     bool   not null default false,
    status      varchar(20)  not null default 'CREATED',
    primary key (id)
);

CREATE TABLE RESERVATION_WAITING
(
    id          bigint not null auto_increment,
    schedule_id bigint not null,
    member_id   bigint not null,
    event_type  varchar(255) not null,
    created_at  timestamp not null,
    primary key (id)
);

CREATE TABLE theme
(
    id    bigint not null auto_increment,
    name  varchar(20),
    desc  varchar(255),
    price int,
    primary key (id)
);

CREATE TABLE schedule
(
    id       bigint not null auto_increment,
    theme_id bigint not null,
    date     date   not null,
    time     time   not null,
    primary key (id)
);

CREATE TABLE member
(
    id       bigint      not null auto_increment,
    username varchar(20) not null,
    password varchar(20) not null,
    name     varchar(20) not null,
    phone    varchar(20) not null,
    role     varchar(20) not null,
    primary key (id)
);

CREATE TABLE account
(
    id       bigint      not null auto_increment,
    member_id bigint not null,
    reservation_id bigint not null,
    amount     bigint not null,
    primary key (id)
);