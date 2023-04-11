CREATE SEQUENCE PARTICIPANT_SEQ AS INTEGER START WITH 1 INCREMENT BY 1;

CREATE TABLE participant (
    id integer,
    name varchar(128),
    date_of_birth date,
    phone_number varchar(128),
    address varchar(128),
    reference_number varchar(128),
    published boolean,
    PRIMARY KEY (id)
);