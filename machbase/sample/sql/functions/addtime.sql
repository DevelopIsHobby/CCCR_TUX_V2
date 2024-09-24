drop table sqltest;
create table sqltest (id integer, dt datetime);
insert into  sqltest values(1, TO_DATE('1999-11-11 1:2:3 4:5:6'));
insert into  sqltest values(2, TO_DATE('2000-11-11 1:2:3 4:5:6'));
insert into  sqltest values(3, TO_DATE('2012-11-11 1:2:3 4:5:6'));
insert into  sqltest values(4, TO_DATE('2013-11-11 1:2:3 4:5:6'));
insert into  sqltest values(5, TO_DATE('2014-12-30 11:22:33 444:555:666'));
insert into  sqltest values(5, TO_DATE('2014-12-30 23:22:33 444:555:666'));

select * from sqltest;

select ADD_TIME(dt, '1/0/0 0:0:0') from sqltest;
select ADD_TIME(dt, '0/0/0 1:1:1') from sqltest;
select ADD_TIME(dt, '1/1/1 0:0:0') from sqltest;

select ADD_TIME(dt, '-1/0/0 0:0:0') from sqltest;
select ADD_TIME(dt, '0/0/0 -1:-1:-1') from sqltest;
select ADD_TIME(dt, '-1/-1/-1 0:0:0') from sqltest;


select * from sqltest where dt > ADD_TIME(TO_DATE('2014-12-30 11:22:33 444:555:666'), '-1/-1/-1 0:0:0');

select * from sqltest where dt > ADD_TIME(TO_DATE('2014-12-30 11:22:33 444:555:666'), '-1/-2/-1 0:0:0');

select ADD_TIME(TO_DATE('2000-12-01 00:00:00 000:000:001'), '-1/0/0 0:0:-1') from sqltest;
