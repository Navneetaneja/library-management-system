CREATE TABLE public.users (
    id uuid not null primary key,
    name varchar not null,
    email varchar not null unique,
    created_at timestamp null
);

