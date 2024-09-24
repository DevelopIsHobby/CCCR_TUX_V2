drop table sqltest;
create table sqltest(i1 integer, i2 double);
insert into sqltest values (1, 1);
insert into sqltest values (2, 1);
insert into sqltest values (1, 2);
insert into sqltest values (2, 2);
select VARIANCE(i1) from sqltest;
select VAR_POP(i1) from sqltest;
