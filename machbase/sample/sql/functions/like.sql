
--drop table tb;
create table tb(i1 varchar(10));
insert into tb values('01234');
insert into tb values('56789');
insert into tb values('0123456789');
insert into tb values('ABCDE');
insert into tb values('FGHIJ');
insert into tb values('ABCDEFGHIJ');
insert into tb values(NULL);

select * from tb;

select * from tb where i1 like '0%';
select * from tb where i1 like '5%';
select * from tb where i1 like '%4';
select * from tb where i1 like '%9';
select * from tb where i1 like '%2%';
select * from tb where i1 like '%8%';
select * from tb where i1 like '%C%';
select * from tb where i1 like '%I%';
select * from tb where i1 like '%23%';
select * from tb where i1 like '%78%';
select * from tb where i1 like '%BC%';
select * from tb where i1 like '%GH%';
select * from tb where i1 like '%2%4%';
select * from tb where i1 like '%6%8%';
select * from tb where i1 like '%B%D%';
select * from tb where i1 like '%G%I%';
select * from tb where i1 like '_12%';
select * from tb where i1 like '__2345%';
select * from tb where i1 like '_BC%';
select * from tb where i1 like '__CDEF%';
select * from tb where i1 like '_1_3%';
select * from tb where i1 like '__2__5%';
select * from tb where i1 like '_BC%';
select * from tb where i1 like '__CD_F%';

select * from tb where i1 not like '0%';
select * from tb where i1 not like '5%';
select * from tb where i1 not like '%4';
select * from tb where i1 not like '%9';
select * from tb where i1 not like '%2%';
select * from tb where i1 not like '%8%';
select * from tb where i1 not like '%C%';
select * from tb where i1 not like '%I%';
select * from tb where i1 not like '%23%';
select * from tb where i1 not like '%78%';
select * from tb where i1 not like '%BC%';
select * from tb where i1 not like '%GH%';
select * from tb where i1 not like '%2%4%';
select * from tb where i1 not like '%6%8%';
select * from tb where i1 not like '%B%D%';
select * from tb where i1 not like '%G%I%';
select * from tb where i1 not like '_12%';
select * from tb where i1 not like '__2345%';
select * from tb where i1 not like '_BC%';
select * from tb where i1 not like '__CDEF%';
select * from tb where i1 not like '_1_3%';
select * from tb where i1 not like '__2__5%';
select * from tb where i1 not like '_BC%';
select * from tb where i1 not like '__CD_F%';



--drop table tb2;
create table tb2(i1 long);
insert into tb2 values(123);
insert into tb2 values(6789);

select * from tb2 where i1 like '012%';
