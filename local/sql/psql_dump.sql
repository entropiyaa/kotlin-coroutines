create schema dogs;


CREATE TABLE IF NOT EXISTS dogs.breed
(
    id               bigserial      not null constraint breed_pkey primary key,
    breed_name       varchar(60)    not null,
    image_url        varchar
);

CREATE TABLE IF NOT EXISTS dogs.breed_types
(
    id               bigserial      not null constraint breed_types_pkey primary key,
    type             varchar(60)    not null,
    breed_id         int8           not null
);

ALTER TABLE dogs.breed_types
    ADD CONSTRAINT breed_id_fkey
        FOREIGN KEY (breed_id) REFERENCES dogs.breed(id) ON DELETE CASCADE;