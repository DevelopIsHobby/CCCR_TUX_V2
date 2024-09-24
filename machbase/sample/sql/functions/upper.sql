drop table sqltest;
create table sqltest(i1 integer, i2 double, i3 varchar(10), i4 text);
insert into sqltest values(1, 1.0, '', '');
insert into sqltest values(2, 2.0, 'sqltest', 'sqltest');
insert into sqltest values(3, 3.3, 'machbase', 'machbase');
insert into sqltest values(4, 4.4, 'machbase1', 'machbase1');
insert into sqltest values(5, 5.5, 'machbase12', 'machbase12');
insert into sqltest values(-1, -1.0, '','');
insert into sqltest values(-2, -2.0, 'TSDB','TSDB');
insert into sqltest values(-3, -3.3, 'MACHBASE','MACHBASE');
insert into sqltest values(-4, -4.4, 'MACHBASE1','MACHBASE1');
insert into sqltest values(-5, -5.5, 'MACHBASE12','MACHBASE12');

select UPPER(i3),UPPER(i4) from sqltest;
