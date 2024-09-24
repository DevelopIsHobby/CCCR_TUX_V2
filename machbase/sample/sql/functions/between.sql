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
select id1 from intest where id1 BETWEEN ;
select id1 from intest where id1 BETWEEN 12 AND;

select id1 from intest where id1 NOT BETWEEN ;
select id1 from intest where id1 NOT BETWEEN 12 AND;
select * from intest where id1 BETWEEN 3 AND '6';

# short
select * from intest where id1 BETWEEN 3 AND 7;
select * from intest where id1 NOT BETWEEN 3 AND 7;

# integer
select * from intest where id2 BETWEEN 23 AND 27;
select * from intest where id2 NOT BETWEEN 23 AND 27;

# long
select * from intest where id3 BETWEEN 33 AND 37;
select * from intest where id3 NOT BETWEEN 33 AND 37;

# float
select * from intest where id4 BETWEEN 43 AND 47;
select * from intest where id4 NOT BETWEEN 43 AND 47;

# double
select * from intest where id5 BETWEEN 53 AND 57;
select * from intest where id5 NOT BETWEEN 53 AND 57;

# varchar
select * from intest where id6 BETWEEN '63' AND '67';
select * from intest where id6 NOT BETWEEN '63' AND '67';

# date
select * from intest where id7 BETWEEN TO_DATE('2073-7-7', 'YYYY-MM-DD') AND TO_DATE('2077-7-7', 'YYYY-MM-DD');
select * from intest where id7 NOT BETWEEN TO_DATE('2073-7-7', 'YYYY-MM-DD') AND TO_DATE('2077-7-7', 'YYYY-MM-DD');

# ipv4
select * from intest where id8 BETWEEN '127.0.0.83' AND '127.0.0.87';
select * from intest where id8 NOT BETWEEN '127.0.0.83' AND '127.0.0.87';

# none 
select * from intest where id1 BETWEEN 5 AND 3;
select * from intest where id1 NOT BETWEEN 5 AND 3;
