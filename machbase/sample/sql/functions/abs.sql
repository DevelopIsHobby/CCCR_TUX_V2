drop table sqltest;
create table sqltest(i1 integer, i2 double, i3 varchar(10));
insert into sqltest values(1, 1.0, '');
insert into sqltest values(2, 2.0, 'sqltest');

select ABS(i1), abs(i2) from sqltest;
