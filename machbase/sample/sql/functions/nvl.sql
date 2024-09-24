drop table sqltest;
create table sqltest (id varchar(10));
insert into sqltest values('sqltest');
insert into sqltest values(NULL);
select NVL(id, 'machbase') from sqltest;
