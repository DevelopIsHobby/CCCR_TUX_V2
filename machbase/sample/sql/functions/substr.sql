drop table sqltest;
create table sqltest(i1 varchar(10));

insert into sqltest values('12345');
select substr(i1, 1, 1) from sqltest;
select substr(i1, 3, 3) from sqltest;
select substr(i1, 2) from sqltest;