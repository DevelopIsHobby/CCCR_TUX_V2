-- drop table realdual;
create table realdual (id1 integer, id2 varchar(40), id3 varchar(40));

create keyword index idx1 on realdual (id2);
create keyword index idx2 on realdual (id3);

insert into realdual values(1, 'aaa bbb1', 'abc, bcd1');
insert into realdual values(2, 'bbb ccc1', 'bcd/cdf1ad');
insert into realdual values(3, 'ccc ddd1', 'cdf def1');
insert into realdual values(4, 'ファ ッションアドバイザー、', 'errors');
insert into realdual values(5, 'ファ ッションアドバイザーa99', 'err404');
insert into realdual values(6, 'ddd, deee1',  'Unixsocket923');

select id2 from realdual where id2 esearch 'bbb';
select id2 from realdual where id2 esearch 'bbb%';

select id3 from realdual where id3 esearch '%cd%';

select id2 from realdual where id2 esearch '%dbc%' ;

select id3 from realdual where id3 esearch '%socket%' ;
select id3 from realdual where id3 esearch 'err%' ;

select id3 from realdual where id3 esearch '%rr%04';
