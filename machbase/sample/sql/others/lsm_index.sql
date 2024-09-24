--create simple tablespace
create tablespace tbs1 datadisk disk1 (disk_path="lsm_disk");

--create table
create table t1(c1 integer, c2 varchar(1024));

--create lsm index with tablespace;
create index idx1 on t1 (c1) index_type lsm tablespace tbs1 max_level = 2, part_value_count = 1000;
