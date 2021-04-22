-- test
drop index university_schemas.testchange1_data_idx;
alter table university_schemas.testchange1  rename column data to name;
alter table university_schemas.testchange1  alter column name type varchar(64);
create unique index testchange1_data_idx  on university_schemas.testchange1(name);
insert into university_schemas.testchange1 (name) values ('1.2_update_test_changes1.sql - ok');


