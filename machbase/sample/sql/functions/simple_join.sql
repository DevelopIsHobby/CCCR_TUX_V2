-- drop table t1;
create table t1(c1 integer, c2 integer);

insert into t1 values (0, 1);
insert into t1 values (1, 1);
insert into t1 values (1, 1);
insert into t1 values (2, 1);

select t1.c1 from t1;

select t1.c1 from t1, v$sys_users where v$sys_users.name='SYS'  and V$sys_users.user_id = t1.c1;

select v$sys_columns.name from v$sys_columns, v$sys_tables where v$sys_tables.name = 'T1' and v$sys_tables.id = v$sys_columns.table_id;


select V$sys_tables.name from v$sys_tables, v$sys_users where v$sys_tables.user_id = v$sys_users.user_id and v$sys_users.name = 'SYS';

select v$sys_columns.name from v$sys_columns, v$sys_tables where  v$sys_tables.id = v$sys_columns.table_id and v$sys_tables.name = 'T1';
