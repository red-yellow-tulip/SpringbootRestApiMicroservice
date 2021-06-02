--testchange1--------------------------------------------------------------------------
CREATE SEQUENCE university_schemas.testchange1_seq START 1;
CREATE TABLE university_schemas.testchange1 (
       id bigint DEFAULT nextval('university_schemas.testchange1_seq') NOT NULL PRIMARY KEY,
       data bigint NOT NULL
);
create unique index testchange1_id_idx  on university_schemas.testchange1(id);
create unique index testchange1_data_idx  on university_schemas.testchange1(data);


