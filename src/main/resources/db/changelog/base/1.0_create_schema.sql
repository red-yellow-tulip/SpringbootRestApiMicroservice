--student--------------------------------------------------------------------------
CREATE SEQUENCE university_schemas.student_seq START 1;
CREATE TABLE university_schemas.student (
       id bigint DEFAULT nextval('university_schemas.student_seq') NOT NULL PRIMARY KEY,
       student_id bigint NOT NULL,
       name varchar(64) NOT NULL,
       surname varchar(64) NOT NULL,
       dateBirth date
);
create unique index student_id_idx  on university_schemas.student(id);
create unique index name_surname_id_idx  on university_schemas.student(name,surname);
--student--------------------------------------------------------------------------
CREATE SEQUENCE university_schemas.student_group_seq START 1;
CREATE TABLE university_schemas.student_group (
        id bigint DEFAULT nextval('university_schemas.student_group_seq') NOT NULL PRIMARY KEY,
        group_id bigint NOT NULL,
        name varchar(64) NOT NULL,
        university_id bigint NOT NULL,
        dateBirth date
);

create unique index student_group_id_idx  on university_schemas.student_group(id);

--university--------------------------------------------------------------------------
CREATE SEQUENCE university_schemas.university_seq START 1;
CREATE TABLE university_schemas.university (
      id bigint DEFAULT nextval('university_schemas.university_seq') NOT NULL PRIMARY KEY,
      university_id bigint NOT NULL,
      name varchar(64) NOT NULL
);

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

