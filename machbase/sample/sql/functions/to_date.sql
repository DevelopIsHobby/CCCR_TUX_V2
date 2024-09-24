drop table sqltest;
create table sqltest (id integer, dt datetime);
insert into  sqltest values(1, TO_DATE('1999-11-11 1:2:3 4:5:6'));
insert into  sqltest values(2, TO_DATE('2000-11-11 1:2:3 4:5:6'));
insert into  sqltest values(3, TO_DATE('2012-11-11 1:2:3 4:5:6'));
insert into  sqltest values(4, TO_DATE('2013-11-11 1:2:3 4:5:6'));
insert into  sqltest values(5, TO_DATE('2014-12-30 11:22:33 444:555:666'));
insert into  sqltest values(5, TO_DATE('2014-12-30 23:22:33 444:555:666'));
insert into  sqltest values(6, TO_DATE('2014-DEC-30 23:22:34 777:888:999', 'YYYY-MON-DD HH24:MI:SS mmm:uuu:nnn'));

select id, dt from sqltest where dt > TO_DATE('1999-11-11 1:2:3 4:5:0');
select id, dt from sqltest where dt > TO_DATE('2000-11-11 1:2:3 4:5:0');
select id, dt from sqltest where dt > TO_DATE('2013-11-11 1:2:3','YYYY-MM-DD HH24:MI:SS') and dt < TO_DATE('2015-11-11 1:2:3','YYYY-MM-DD HH24:MI:SS');

# to date
select id, TO_DATE('1999', 'YYYY') from sqltest limit 1;
select id, TO_DATE('1999-12', 'YYYY-MM') from sqltest limit 1;
select id, TO_DATE('1999-12-31', 'YYYY-MM-DD') from sqltest limit 1;
select id, TO_DATE('1999-12-31 13', 'YYYY-MM-DD HH24') from sqltest limit 1;
select id, TO_DATE('1999-12-31 13:12', 'YYYY-MM-DD HH24:MI') from sqltest limit 1;
select id, TO_DATE('1999-12-31 13:12:32', 'YYYY-MM-DD HH24:MI:SS') from sqltest limit 1;
select id, TO_DATE('1999-12-31 13:12:32 123', 'YYYY-MM-DD HH24:MI:SS mmm') from sqltest limit 1;
select id, TO_DATE('1999-12-31 13:12:32 123:456', 'YYYY-MM-DD HH24:MI:SS mmm:uuu') from sqltest limit 1;
select id, TO_DATE('1999-12-31 13:12:32 123:456:789', 'YYYY-MM-DD HH24:MI:SS mmm:uuu:nnn') from sqltest limit 1;

select id, TO_CHAR(TO_DATE('1999', 'YYYY')) from sqltest limit 1;
select id, TO_CHAR(TO_DATE('1999-12', 'YYYY-MM')) from sqltest limit 1;
select id, TO_CHAR(TO_DATE('1999-12-31', 'YYYY-MM-DD')) from sqltest limit 1;
select id, TO_CHAR(TO_DATE('1999-12-31 13', 'YYYY-MM-DD HH24')) from sqltest limit 1;
select id, TO_CHAR(TO_DATE('1999-12-31 13:12', 'YYYY-MM-DD HH24:MI')) from sqltest limit 1;
select id, TO_CHAR(TO_DATE('1999-12-31 13:12:32', 'YYYY-MM-DD HH24:MI:SS')) from sqltest limit 1;
select id, TO_CHAR(TO_DATE('1999-12-31 13:12:32 123', 'YYYY-MM-DD HH24:MI:SS mmm')) from sqltest limit 1;
select id, TO_CHAR(TO_DATE('1999-12-31 13:12:32 123:456', 'YYYY-MM-DD HH24:MI:SS mmm:uuu')) from sqltest limit 1;
select id, TO_CHAR(TO_DATE('1999-12-31 13:12:32 123:456:789', 'YYYY-MM-DD HH24:MI:SS mmm:uuu:nnn')) from sqltest limit 1;

# to char
select id, TO_CHAR(dt) from sqltest;
select id, TO_CHAR(dt, 'YYYY') from sqltest;
select id, TO_CHAR(dt, 'YYYY-MM') from sqltest;
select id, TO_CHAR(dt, 'YYYY-MM-DD') from sqltest;
select id, TO_CHAR(dt, 'YYYY-MM-DD yaho!!') from sqltest;
select id, TO_CHAR(dt, 'YYYY-MM-DD yaho!! AM') from sqltest;
select id, TO_CHAR(dt, 'YYYY-MM-DD yaho!! PM') from sqltest;
select id, TO_CHAR(dt, 'YYYY-MM-DD HH24:MI:SS') from sqltest;
select id, TO_CHAR(dt, 'YYYY-MM-DD HH24:MI:SS mmm-uuu-nnn') from sqltest;
select id, TO_CHAR(dt, 'YYYY-MON-DD HH24:MI:SS mmm-uuu-nnn') from sqltest;
