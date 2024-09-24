drop table sqltest;
create table sqltest (id varchar(100));
insert into sqltest values('::0.0.0.0');
insert into sqltest values('::127.0.0.1');
insert into sqltest values('::127.0' || '.0.2');
insert into sqltest values('::127.0.0.3');
insert into sqltest values('::127.0.0.4');
insert into sqltest values('::127.0.0.5');
insert into sqltest values('::127.0.0.6');
insert into sqltest values('   ::127.0.0.7');
insert into sqltest values('::127.0.0.8  ');
insert into sqltest values('   ::FFFF:255.255.255.255   ');
insert into sqltest values('21DA:D3:0:2F3B:2AA:FF:FE28:9C5A');
select TO_IPV6(id) from sqltest;

insert into sqltest values('128.33.22.221__invalidipv4');
select TO_IPV6(id) from sqltest limit 1;
select TO_IPV6_SAFE(id) from sqltest;
