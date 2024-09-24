# _RID column test
-- drop table t1;
-- drop table t2;

# Disk Column Table
++ create table t1 (c1 integer);

insert into t1 values(1);
insert into t1 values(2);
insert into t1 values(3);
insert into t1 values(4);
insert into t1 values(5);
insert into t1 values(6);

select * from t1;
select _rid from t1;
select _rid, c1 from t1;
select _rid, c1 from t1 where _rid = 1;

++ create bitmap index t1_idx1 on t1 (c1);

alter table t1 flush index all;

select _rid, c1 from t1;
select _rid, c1 from t1 where _rid = 1;

# Memory Column Table
++ create lookup table t2 (c1 integer);
insert into t2 values(1);
insert into t2 values(2);
insert into t2 values(3);
insert into t2 values(4);
insert into t2 values(5);
insert into t2 values(6);

select * from t2;
select _rid, c1 from t2;
select _rid, c1 from t2 where _rid = 1;

++ create redblack index t2_idx2 on t2 (c1);

select * from t2;
select _rid, c1 from t2;
select _rid, c1 from t2 where _rid = 1;

# Performance Table
*-select * from V$DUMP_DISK_TABLES where _rid = 1;

