drop table sqltest;
create table sqltest (id1 short);
insert into sqltest values(1);

select DECODE(id1, 0, 2, 99) from sqltest;
select DECODE(id1, 1, 2, 99) from sqltest;

select DECODE(id1, 0, 2) from sqltest;
select DECODE(id1, 1, 2) from sqltest;

drop table sqltest;
create table sqltest (id integer);

insert into sqltest values(1);
insert into sqltest values(2);
insert into sqltest values(3);
insert into sqltest values(4);
insert into sqltest values(5);
insert into sqltest values(6);
insert into sqltest values(7);
insert into sqltest values(8);
insert into sqltest values(9);

select decode(id, 1, 'aa', 2, 'bb', 3, 'cc', 4, 'dd', 5, 'ee', 6, 'ff', 7, 'gg', 8, 'hh', 'others') from sqltest;


drop table sqltest;
create table sqltest (id1 long);

insert into sqltest values(1);

select DECODE(id1, 0, 2, 99) from sqltest;
select DECODE(id1, 1, 2, 99) from sqltest;

select DECODE(id1, 0, 2) from sqltest;
select DECODE(id1, 1, 2) from sqltest;

drop table sqltest;
create table sqltest (id1 varchar(11));

insert into sqltest values('sqltest');
insert into sqltest values('sqltest2');

select id1, DECODE(id1, 'sqltest', 'SSSSSSSSSSSSSSSS', 'sqltest2', 'AAAAAAAAAAAAAAAAAA', 'DEFAULT') from sqltest;
select id1, DECODE(id1, 'sqltest', '192.168A.0.1', 'sqltest2', '@@@@192.168B.0.2', 'DEFAULT') from sqltest;
select id1, DECODE(id1, 'machbase', 2, 99) from sqltest;

select DECODE(id1, 'sqltest', 2) from sqltest;
select DECODE(id1, 'machbase', 2) from sqltest;


drop table sqltest;
create table sqltest (id1 ipv4);

insert into sqltest values('192.168.0.1');

select id1, DECODE(id1, '192.168.0.1', 'SSSSSSSSSSSSSSSS', '192.168.0.3', 'AAAAAAAAAAAAAAAAAA', 'DEFAULT') from sqltest;
select id1, DECODE(id1, '192.168.0.3', 2, 99) from sqltest;

select DECODE(id1, '192.168.0.1', 2) from sqltest;
select DECODE(id1, '192.168.0.3', 2) from sqltest;


drop table sqltest;
create table sqltest (id1 float);

insert into sqltest values(1);

select DECODE(id1, 0, 2, 99) from sqltest;
select DECODE(id1, 1, 2, 99) from sqltest;

select DECODE(id1, 0, 2) from sqltest;
select DECODE(id1, 1, 2) from sqltest;

drop table sqltest;
create table sqltest (id1 double);

insert into sqltest values(1);

select DECODE(id1, 0, 2, 99) from sqltest;
select DECODE(id1, 1, 2, 99) from sqltest;

select DECODE(id1, 0, 2) from sqltest;
select DECODE(id1, 1, 2) from sqltest;

drop table sqltest;
create table sqltest (id1 datetime);

insert into sqltest values(TO_DATE('1999-11-11 1:2:3 4:5:6'));

select DECODE(id1, TO_DATE('1999-11-11 1:2:3 4:5:6'), 2, 99) from sqltest;
select DECODE(id1, TO_DATE('1999-11-11 1:2:3 4:5:7'), 2, 99) from sqltest;

select DECODE(id1, TO_DATE('1999-11-11 1:2:3 4:5:6'), 2) from sqltest;
select DECODE(id1, TO_DATE('1999-11-11 1:2:3 4:5:7'), 2) from sqltest;
