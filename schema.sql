CREATE TABLE public.users
(
    id         uuid    NOT NULL,
    "name"     varchar NOT NULL,
    email      varchar NOT NULL,
    created_at timestamp NULL,
    CONSTRAINT users_email_key UNIQUE (email),
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE public.book
(
    id                  uuid    NOT NULL,
    isbn                varchar NOT NULL,
    title               varchar NOT NULL,
    author              varchar NOT NULL,
    published_year      int4 NULL,
    availability_status varchar NULL,
    deleted             bool DEFAULT false NULL,
    created_at          timestamp NULL,
    CONSTRAINT book_isbn_key UNIQUE (isbn),
    CONSTRAINT book_pkey PRIMARY KEY (id)
);