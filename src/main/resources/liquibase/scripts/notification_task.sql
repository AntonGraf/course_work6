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

--changeset agraf:2
CREATE TABLE public.notification_task_status
(
    id serial,
    status text,
    PRIMARY KEY (id)
);
ALTER TABLE public.notification_task
ADD COLUMN status_id integer;

--changeset agraf:3
INSERT INTO public.notification_task_status (status) VALUES ('добавлено'), ('отправлено'), ('доставлено'), ('ошибка');