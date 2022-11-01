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
    seq         int    not null,
    primary key (id),
    constraint uq_schedule_id_seq
        unique (schedule_id, seq)
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

CREATE TABLE daily_revenue
(
    id        bigint not null auto_increment,
    member_id bigint not null,
    profit    bigint not null,
    daily_at  date   not null,
    primary key (id)
);

CREATE TABLE revenue_history
(
    id               bigint not null auto_increment,
    daily_revenue_id bigint not null,
    original_profit  bigint not null,
    target_profit    bigint not null,
    remark           varchar(200),
    created_at       datetime(6) default CURRENT_TIMESTAMP (6) not null,
    primary key (id)
);