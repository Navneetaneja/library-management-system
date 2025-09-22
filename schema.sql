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
    borrowed_by_user    uuid NULL,
    deleted             bool DEFAULT false NULL,
    created_at          timestamp NULL,
    CONSTRAINT book_isbn_key UNIQUE (isbn),
    CONSTRAINT book_pkey PRIMARY KEY (id),
    CONSTRAINT book_borrowed_user_f_key FOREIGN KEY (borrowed_by_user) REFERENCES public.users(id)
);

CREATE TABLE public.wishlist
(
    user_id uuid NOT NULL,
    book_id uuid NOT NULL,
    CONSTRAINT wishlist_pkey PRIMARY KEY (user_id, book_id),
    CONSTRAINT wishlist_user_f_key FOREIGN KEY (user_id) references public.users (id),
    CONSTRAINT wishlist_book_f_key FOREIGN KEY (book_id) references public.book (id)
);