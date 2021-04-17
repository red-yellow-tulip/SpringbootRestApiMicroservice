-- test
alter table testchange1  rename column data to name;
alter table testchange1  alter column name type varchar(64);
insert into testchange1(name) values ('testname1');


