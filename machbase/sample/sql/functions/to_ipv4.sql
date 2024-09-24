drop table sqltest;
create table sqltest (id varchar(100));
insert into sqltest values('123.123.22.11');
insert into sqltest values('     127.0.0.1    ');
insert into sqltest values(NULL);

select id from sqltest;
select TO_IPV4(id) from sqltest;

insert into sqltest values('128.33.22.221__invalidipv4');
select TO_IPV4(id) from sqltest limit 1;
select TO_IPV4_SAFE(id) from sqltest;
