--user--------------------------------------------------------------------------
CREATE SEQUENCE university_schemas.user_seq START 1;
CREATE TABLE university_schemas.users (
        id bigint DEFAULT nextval('university_schemas.user_seq') NOT NULL PRIMARY KEY,
        login varchar(64) NOT NULL,
        psw varchar(64) NOT NULL,
        name varchar(64) NOT NULL,
        role varchar(64) NOT NULL
);
create unique index users_id_idx    on university_schemas.users(id);
create unique index users_name_idx  on university_schemas.users(login);

