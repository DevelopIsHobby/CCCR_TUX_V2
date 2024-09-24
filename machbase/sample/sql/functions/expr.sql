-- drop table realdual;
create table realdual (id double);
insert into realdual values(2.22507385850720138309e-308);

select to_hex(2.22507385850720138309e-308) from realdual;
select to_hex(id) from realdual;
select 2.22507385850720138309e-308 from realdual;

-- drop table mydual;
create table mydual (id long);
insert into mydual values(1000000 * 1000000);
insert into mydual values(1);
insert into mydual values(1000 * 1000 * 1000);
insert into mydual values(1000 * 1000 * 1000 * 10);
insert into mydual values(1000 * 1000 * 1000 * 10 * 10);
insert into mydual values(1000.1 * 1000.1 * 1000.1 * 10.1 * 10.1 * 10.1);
select * from mydual;

-- drop table mydual2;
create table mydual2 (id double);
insert into mydual2 values(1);
insert into mydual2 values(1000 * 1000 * 1000);
insert into mydual2 values(1000 * 1000 * 1000 * 10);
insert into mydual2 values(1000 * 1000 * 1000 * 10 * 10);
insert into mydual2 values(1000.1 * 1000.1 * 1000.1 * 10.1 * 10.1 * 10.1);
select * from mydual2;

--drop table tb1;
create table tb1(i1 short);

insert into tb1 values(1);
insert into tb1 values(1 + 2);
insert into tb1 values(1.5 + 2.5);
insert into tb1 values(3 * 4 + 2);
insert into tb1 values( 3 * (4 + 2));
insert into tb1 values(1 / 0);
insert into tb1 values(32766);
insert into tb1 values(32767);
insert into tb1 values(32767 + 2);
insert into tb1 values(32768);
insert into tb1 values(-32766);
insert into tb1 values(-32766 - 1);
insert into tb1 values(-32766 - 2);
insert into tb1 values(-32766 - 3);
select * from tb1;





--drop table tb2;
create table tb2(i1 integer);

insert into tb2 values(1);
insert into tb2 values(1 + 2);
insert into tb2 values(1.5 + 2.5);
insert into tb2 values(3 * 4 + 2);
insert into tb2 values(3 * (4 + 2));
insert into tb2 values(1 / 0);
insert into tb2 values(NULL);
insert into tb2 values(2147483647);
insert into tb2 values(2147483647 + 1);
insert into tb2 values(2147483647 + 2);
insert into tb2 values(2147483647 - 2);
insert into tb2 values(-2147483647);
insert into tb2 values(-2147483647 - 1);
insert into tb2 values(-2147483647 - 2);
select * from tb2 order by _arrival_time asc;



--drop table tb3;
create table tb3(i1 long);

insert into tb3 values(1);
insert into tb3 values(1 + 2);
insert into tb3 values(1.5 + 2.5);
insert into tb3 values(3 * 4 + 2);
insert into tb3 values(3 * (4 + 2));
insert into tb3 values(1 / 0);
insert into tb3 values(NULL);
insert into tb3 values(9223372036854775807);
insert into tb3 values(9223372036854775807 + 1);
insert into tb3 values(9223372036854775807 + 2);
insert into tb3 values(-9223372036854775807);
insert into tb3 values(-9223372036854775807 - 1);
insert into tb3 values(-9223372036854775807 - 2);
select * from tb3 order by _arrival_time asc;


--drop table tb4;
create table tb4(i1 float);

insert into tb4 values(1);
insert into tb4 values(1 + 2);
insert into tb4 values(1.5 + 2.5);
insert into tb4 values(3 * 4 + 2);
insert into tb4 values(3 * (4 + 2));
insert into tb4 values(1 / 0);
insert into tb4 values(NULL);
# 3.402823466e+38F is NULL
insert into tb4 values(3.40282346638528859812e+37F);
insert into tb4 values(1.17549435082228750797e-38F);
select * from tb4 order by _arrival_time asc;


--drop table tb5;
create table tb5(i1 double);

insert into tb5 values(1);
insert into tb5 values(1 + 2);
insert into tb5 values(1.5 + 2.5);
insert into tb5 values(3 * 4 + 2);
insert into tb5 values(3 * (4 + 2));
insert into tb5 values(1 / 0);
insert into tb5 values(NULL);
# 3.402823466e+38F is NULL
insert into tb5 values(1.79769313486231570815e+308);
insert into tb5 values(2.22507385850720138309e-308);
select * from tb5 order by _arrival_time asc;

--drop table tb6;
create table tb6(i1 datetime);

insert into tb6 values(TO_DATE('1999', 'YYYY'));
insert into tb6 values(TO_DATE('1999', 'YYYY') + 30 * 24 * 60 * 1000 * 1000 * 1000);
select * from tb6 order by _arrival_time asc;


