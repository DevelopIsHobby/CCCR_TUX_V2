
#-- KILL;
#-- DESTROYDB;
#-- CREATEDB;

# normal test
--drop table dmltable;
create table dmltable (id integer);

insert into dmltable values(100);
insert into dmltable values(-1234);
insert into dmltable values(+56);
insert into dmltable values(913);
select id from dmltable order by id;
select id from dmltable order by id desc;
select id from dmltable order by id asc;

--drop table dmltable2;
create table dmltable2 (id double);

insert into dmltable2 values(100);
insert into dmltable2 values(-1234);
insert into dmltable2 values(+58);
insert into dmltable2 values(0.1234);
select id from dmltable order by id desc;
select id from dmltable order by id asc;

--drop table dmltable3;
create table dmltable3 (id varchar(10));
insert into dmltable3 values('d');
insert into dmltable3 values('asd');
insert into dmltable3 values('def');
insert into dmltable3 values('dccd');
insert into dmltable3 values('zerefe');

select id from dmltable3 order by id desc;
select id from dmltable3 order by id asc;

--drop table ordtest;
create table ordtest (name varchar(10), i1 integer, i2 double);
insert into ordtest values('a', 1, 0.1);
insert into ordtest values('a', 2, 0.2);
insert into ordtest values('ab', 1, 0.1);
insert into ordtest values('ab', 2, 0.3);
insert into ordtest values('ab', 1, 0.2);
insert into ordtest values('ac', 1, 0.1);
insert into ordtest values('ac', 2, 100);
insert into ordtest values('ac', 2, 1001);
insert into ordtest values('d', 1, 0.1);
insert into ordtest values('z', 1, 0.1);
insert into ordtest values('d', 2, 0.1);
insert into ordtest values('z', 1, 0.1);
insert into ordtest values('daa', 1, 0.1);
insert into ordtest values('za', 1, 0.1);
insert into ordtest values('daa', 2, 0.1);
insert into ordtest values('za', 1, 0.1);
insert into ordtest values('daa', 3, 0.1);
insert into ordtest values('daa', 4, 0.1);
insert into ordtest values('ka', 1, 0.1);
insert into ordtest values('ka', 1, 0.1);

select sum(i1) from ordtest group by name order by sum(i1);
select sum(i1) from ordtest group by name order by avg(i2);
select name, i1, sum(i2) from ordtest group by name, i1, order by name asc , sum(i2) desc;
select name, i1 + 1, sum(i2) from ordtest group by i1, name order by name asc , sum(i2) desc;
