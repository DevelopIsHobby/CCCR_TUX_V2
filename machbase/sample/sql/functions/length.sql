drop table sqltest;
create table sqltest (id integer, id2 double, name varchar(10));
insert into sqltest values(1, 2, 'sqltest');
insert into sqltest values(NULL, 3, 'machbase');
insert into sqltest values(4, NULL, 'mach');
insert into sqltest values(5, 6, NULL);

select * from sqltest;
select id * 10 from sqltest;
select * from sqltest where id > 1 and id2 < 10;
select name || ' with null concat' from sqltest;
select LENGTH(name) from sqltest;

drop table sqltest;
create table sqltest (text text);
insert into sqltest values('write length sql');
insert into sqltest values('make manual');
select LENGTH(text) from sqltest;
