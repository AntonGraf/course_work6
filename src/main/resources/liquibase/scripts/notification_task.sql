--liquibase formatted sql

--changeset agraf:1
CREATE TABLE public.notification_task
(
    id serial,
    "chat_id" integer,
    text text,
    date_time timestamp,
    PRIMARY KEY (id)
);