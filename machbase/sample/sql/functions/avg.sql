drop table sqltest;
create table sqltest (id integer, id2 integer);
insert into sqltest values(1, 1);
insert into sqltest values(1, 2);
insert into sqltest values(1, 3);
insert into sqltest values(2, 1);
insert into sqltest values(2, 2);
insert into sqltest values(2, 3);
insert into sqltest values(null, 4);

select id, AVG(id2) from sqltest group by id;
