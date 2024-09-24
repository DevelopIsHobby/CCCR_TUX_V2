--create volatile table
create volatile table t1 (c1 int, c2 varchar(1024));

--create redblack index
create index idx1 on t1 (c2) index_type redblack;

--create volatile table with primary key
create volatile table t2 (c1 int primary key, c2 varchar(1024), c3 int);

--insert into on duplicate key update
-- if there is already a record that c1 = 1, 
-- then its existing record will be updated to c2 = 'MACHBASE' and c3 = 0.
-- otherwise, new record will be inserted.
insert into t2 values (1,'MACHBASE',0) on duplicate key update;

--insert into on duplicate key update set
-- if there is already a record that c1 = 1, 
-- then its existing record will be updated to c3 = 1.
-- otherwise, new record will be inserted.
insert into t2 values (1,'mach',0) on duplicate key update set c3 = 1;

--delete using primary key value comparison
-- if there is already a record that c1 = 1, then its existing record will be deleted.
delete from t2 where c1 = 1;
