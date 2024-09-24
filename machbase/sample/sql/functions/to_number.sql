drop table sqltest;
create table sqltest (id varchar(100));
insert into sqltest values('1');
insert into sqltest values('10');
insert into sqltest values('30');
select TO_NUMBER(id) from sqltest;

drop table sqltest;
create table sqltest (id varchar(100));
insert into sqltest values('10invalidnumber');
select TO_NUMBER(id) from sqltest;
select TO_NUMBER_SAFE(id) from sqltest;
