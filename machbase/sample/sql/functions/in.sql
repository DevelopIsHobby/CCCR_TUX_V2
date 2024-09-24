-- drop table intest;

create table intest (id1 short, id2 integer, id3 long, id4 float, id5 double, id6 varchar(128), id7 datetime, id8 ipv4);

insert into intest values(1, 21, 31, 41, 51, '61', TO_DATE('2071-7-7', 'YYYY-MM-DD'), '127.0.0.81');
insert into intest values(2, 22, 32, 42, 52, '62', TO_DATE('2072-7-7', 'YYYY-MM-DD'), '127.0.0.82');
insert into intest values(3, 23, 33, 43, 53, '63', TO_DATE('2073-7-7', 'YYYY-MM-DD'), '127.0.0.83');
insert into intest values(4, 24, 34, 44, 54, '64', TO_DATE('2074-7-7', 'YYYY-MM-DD'), '127.0.0.84');
insert into intest values(5, 25, 35, 45, 55, '65', TO_DATE('2075-7-7', 'YYYY-MM-DD'), '127.0.0.85');
insert into intest values(6, 26, 36, 46, 56, '66', TO_DATE('2076-7-7', 'YYYY-MM-DD'), '127.0.0.86');
insert into intest values(7, 27, 37, 47, 57, '67', TO_DATE('2077-7-7', 'YYYY-MM-DD'), '127.0.0.87');
insert into intest values(8, 28, 38, 48, 58, '68', TO_DATE('2078-7-7', 'YYYY-MM-DD'), '127.0.0.88');
insert into intest values(9, 29, 39, 49, 59, '69', TO_DATE('2079-7-7', 'YYYY-MM-DD'), '127.0.0.89');

select * from intest;


## ERROR ###
select id1 from intest where id1 IN (1, 2, 3, 'varchar');
select id1 from intest where id1 IN id2;

select id1 from intest where id1 NOT IN (1, 2, 3, 'varchar');
select id1 from intest where id1 NOT IN id2;

# short
select * from intest where id1 IN (1, 3, 5, 7, 9999);
select * from intest where id1 NOT IN (1, 3, 5, 7, 9999);

# integer
select * from intest where id2 IN (21, 23, 25, 27, 9999);

# long
select * from intest where id3 IN (31, 33, 35, 37, 9999);
select * from intest where id3 NOT IN (31, 33, 35, 37, 9999);

# float
select * from intest where id4 IN (41, 43, 45, 47, 9999);
select * from intest where id4 NOT IN (41, 43, 45, 47, 9999);

# double
select * from intest where id5 IN (51, 53, 55, 57, 9999);
select * from intest where id5 NOT IN (51, 53, 55, 57, 9999);

# varchar
select * from intest where id6 IN ('61', '63', '65', '67');
select * from intest where id6 NOT IN ('61', '63', '65', '67');

# date
select * from intest where id7 IN (TO_DATE('2071-7-7', 'YYYY-MM-DD'),TO_DATE('2073-7-7', 'YYYY-MM-DD'),TO_DATE('2075-7-7', 'YYYY-MM-DD'),TO_DATE('2077-7-7', 'YYYY-MM-DD'));
select * from intest where id7 NOT IN (TO_DATE('2071-7-7', 'YYYY-MM-DD'),TO_DATE('2073-7-7', 'YYYY-MM-DD'),TO_DATE('2075-7-7', 'YYYY-MM-DD'),TO_DATE('2077-7-7', 'YYYY-MM-DD'));

# ipv4
select * from intest where id8 IN ('127.0.0.81', '127.0.0.83', '127.0.0.85', '127.0.0.87');
select * from intest where id8 NOT IN ('127.0.0.81', '127.0.0.83', '127.0.0.85', '127.0.0.87');

explain select id1 from intest where id1 in (2, 3, 5);
select id1 from intest where id1 in (2, 3, 5);

create bitmap index intest_idx1 on intest(id1);

explain select id1 from intest where id1 in (2, 3, 5);
select id1 from intest where id1 in (2, 3, 5);

