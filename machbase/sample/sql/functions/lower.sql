drop table sqltest;
create table sqltest(i1 integer, i2 double, i3 varchar(10));
insert into sqltest values(1, 1.0, '');
insert into sqltest values(2, 2.0, 'sqltest');
insert into sqltest values(3, 3.3, 'machbase');

select LOWER(i3) from sqltest;
