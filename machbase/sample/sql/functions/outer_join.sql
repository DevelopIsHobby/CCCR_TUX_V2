--drop table t1;
--drop table t2;
--drop table t3;

create table t1 (i1 integer, i2 varchar(20));
create table t2 (i3 integer, i4 varchar(20));
create table t3 (i5 integer, i6 varchar(20));

insert into t1 values (1, null);
insert into t1 values (2, 'aaaa');
insert into t1 values (3, 'bbbb');
insert inot t1 values (4, 'cccc');

insert into t2 values (2, 'ccccc');
insert into t2 values (4, 'dddcc');
insert into t2 values (6, 'eeeecc');

select i1, i3 from t1, t2 where i1 = 1 and t1.i1 = t2.i3;
select i1, i3 from t1 inner join on (t1.i1 = t2.i3) where t1.i1 = 1;

select i1, i2, i3, i4 from t1 left outer join t2 on (t1.i1 = t2.i3);
select i1, i2, i3, i4 from t1 right outer join t2 on (t1.i1 = t2.i3);

select i1, i2, i3, i4 from t1 left outer join t2 on (t1.i1 = t2.i3) where t1.i1 = 1;
select i1, i2, i3, i4 from t1 right outer join t2 on (t1.i1 = t2.i3) where t1.i1 = 1;

explain select i1, i2, i3, i4 from t1 left outer join t2 on (t1.i1 = t2.i3) where t1.i1 = 1;
explain select i1, i2, i3, i4 from t1 right outer join t2 on (t1.i1 = t2.i3) where t1.i1 = 1;

create bitmap index t2_i3_idx on t2(i3);

alter table t2 flush index all;

select i1, i2, i3, i4 from t1 left outer join t2 on (t1.i1 = t2.i3) where t1.i1 = 1;
select i1, i2, i3, i4 from t1 right outer join t2 on (t1.i1 = t2.i3) where t1.i1 = 1;

*+explain select i1, i2, i3, i4 from t1 left outer join t2 on (t1.i1 = t2.i3) where t1.i1 = 1;
*+explain select i1, i2, i3, i4 from t1 right outer join t2 on (t1.i1 = t2.i3) where t1.i1 = 1;

create bitmap index t1_i1_idx on t1(i1);
alter table t1 flush index all;
select i1, i2, i3, i4 from t1 left outer join t2 on (t1.i1 = t2.i3) where t1.i1 = 1;
select i1, i2, i3, i4 from t1 right outer join t2 on (t1.i1 = t2.i3) where t1.i1 = 1;

*+explain select i1, i2, i3, i4 from t1 left outer join t2 on (t1.i1 = t2.i3) where t1.i1 = 1;
*+explain select i1, i2, i3, i4 from t1 right outer join t2 on (t1.i1 = t2.i3) where t1.i1 = 1;

select i1, i3, i5 from t1 left outer join t2 on (t1.i1 = t2.i3) right outer join t3 on (t2.i3 = t3.i5);