-- drop table realdual;
create table realdual (id1 integer, id2 varchar(20), id3 varchar(20));

create keyword index idx1 on realdual (id2);
create keyword index idx2 on realdual (id3);

insert into realdual values(1, 'tsdb tsdb2', 'machbase machbase2');

select * from realdual;

# error
select * from realdual where id2 search 234;

# ok
select * from realdual where id2 search 'tsdb';

select * from realdual where id3 search 'machbase' ;

select * from realdual where id2 search 'tsdb' and id3 search 'machbase' ;

