-- drop table test;
create table test(c1 double);
insert into test values(1);
insert into test values(3);
insert into test values(4);
insert into test values(2);
select sum(c1) from test;

-- drop table test;
create table test(c1 float);
insert into test values(1);
insert into test values(3);
insert into test values(4);
insert into test values(2);
select sum(c1) from test;

-- drop table test;
create table test(c1 varchar(100));
insert into test values('111');
insert into test values('333');
insert into test values('444');
insert into test values('222');
insert into test values('4444');
select sum(c1) from test;

-- drop  table test;
create table test(c1 short);
insert into test values(1);
insert into test values(3);
insert into test values(4);
insert into test values(2);
select sum(c1) from test;


-- drop  table test;
create table test(c1 integer);
insert into test values(1);
insert into test values(3);
insert into test values(4);
insert into test values(2);
select sum(c1) from test;


-- drop table test;
create table test(c1 long);
insert into test values(1);
insert into test values(3);
insert into test values(4);
insert into test values(2);
select sum(c1) from test;


-- drop  table test;
create table test(c1 ipv4);
insert into test values('192.168.0.1');
insert into test values('255.168.0.1');
insert into test values('193.168.0.1');
insert into test values('194.168.0.1');
insert into test values('192.168.0.1');
select sum(c1) from test;


-- drop table test;
create table test(c1 datetime);
insert into test values(TO_DATE('2014-6-12 10:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into test values(TO_DATE('2014-12-11 10:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into test values(TO_DATE('2014-6-12 10:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into test values(TO_DATE('2014-6-19 10:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into test values(TO_DATE('2014-6-12 10:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into test values(TO_DATE('2014-5-12 10:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into test values(TO_DATE('2014-4-12 10:00:00', 'YYYY-MM-DD HH24:MI:SS'));
select sum(c1) from test;

