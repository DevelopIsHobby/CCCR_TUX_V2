
--drop table t8;
create table t8(i1 float, i2 double);
insert into t8 values(1.2, 1000);
insert into t8 values(2.3, NULL);
insert into t8 values(NULL, 3);

select * from t8 where i1 < 0;
select * from t8 where i1 > 0;
select * from t8 where i1 is NULL;
select i1 from t8 where i1 is NOT NULL;


--drop table t8;
create table t8(i1 short, i2 double);
insert into t8 values(1, 1000);
insert into t8 values(2, NULL);
insert into t8 values(NULL, 3);

select * from t8 where i1 < 0;
select * from t8 where i1 > 0;
select * from t8 where i1 is NULL;
select i1 from t8 where i1 is NOT NULL;

--drop table t8;
create table t8(i1 integer, i2 double);
insert into t8 values(1, 1000);
insert into t8 values(2, NULL);
insert into t8 values(NULL, 3);

select * from t8 where i1 < 0;
select * from t8 where i1 > 0;
select * from t8 where i1 is NULL;
select i1 from t8 where i1 is NOT NULL;

--drop table t8;
create table t8(i1 long, i2 double);
insert into t8 values(1, 1000);
insert into t8 values(2, NULL);
insert into t8 values(NULL, 3);

select * from t8 where i1 < 0;
select * from t8 where i1 > 0;
select * from t8 where i1 is NULL;
select i1 from t8 where i1 is NOT NULL;


--drop table t8;
create table t8(i1 float, i2 double);
insert into t8 values(1.2, 1000);
insert into t8 values(2.3, NULL);
insert into t8 values(NULL, 3);

select * from t8 where i1 < 0;
select * from t8 where i1 > 0;
select * from t8 where i1 is NULL;
select i1 from t8 where i1 is NOT NULL;


--drop table t8;
create table t8(i1 double, i2 double);
insert into t8 values(1.1, 1000);
insert into t8 values(2.3, NULL);
insert into t8 values(NULL, 3);

select * from t8 where i1 < 0;
select * from t8 where i1 > 0;
select * from t8 where i1 is NULL;
select i1 from t8 where i1 is NOT NULL;


--drop table t8;
create table t8(i1 datetime, i2 double);
insert into t8 values(TO_DATE('1999-1-1 12:01:01 0:0:0'), 1000);
insert into t8 values(TO_DATE('1999-1-1 12:01:02 0:0:0'), NULL);
insert into t8 values(NULL, 3);

select * from t8 where i1 < TO_DATE('1999-1-1 12:00:00 0:0:0', 'YYYY-MM-DD HH24:MI:SS');
select * from t8 where i1 > TO_DATE('1999-1-1 12:00:00 0:0:0', 'YYYY-MM-DD HH24:MI:SS');
select * from t8 where i1 is NULL;
select i1 from t8 where i1 is NOT NULL;

--drop table t8;
create table t8(i1 varchar(10), i2 double);
insert into t8 values('tsdb1', 1);
insert into t8 values('tsdb2', NULL);
insert into t8 values(NULL, 3);

select * from t8 where i1 < '0';
select * from t8 where i1 > '0';
select * from t8 where i1 is NULL;
select i1 from t8 where i1 is NOT NULL;

--drop table nulltable;
create table nulltable (id integer, id2 double, name varchar(10));
insert into nulltable values(1, 2, 'tsdb');
insert into nulltable values(NULL, 3, 'machbase');
insert into nulltable values(4, NULL, 'mach');
insert into nulltable values(5, 6, NULL);

select * from nulltable;

select id * 10 from nulltable;

select * from nulltable where id > 1 and id2 < 10;

select name || ' with null concat' from nulltable;

select LENGTH(name) from nulltable;

select LENGTH(name) from nulltable where name is not null;

select * from nulltable where name is null;

select * from nulltable where id is null;

select * from nulltable where name is null and id is not null;

select * from nulltable where name is not null and id is null;

select * from nulltable where name is not null and id is not null;

select * from nulltable where name = 'tsdb'  and id = 1;
