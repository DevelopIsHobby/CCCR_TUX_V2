-- drop table addrtable;
create table addrtable (addr ipv4);

insert into addrtable values(NULL);
insert into addrtable values('192.0.0.1');
insert into addrtable values('192.0.10.1');
insert into addrtable values('192.128.0.1');
insert into addrtable values('192.128.99.128');
insert into addrtable values('192.128.99.64');
insert into addrtable values('192.128.99.32');
insert into addrtable values('192.128.99.16');
insert into addrtable values('192.128.99.8');
insert into addrtable values('192.128.99.4');
insert into addrtable values('192.128.99.2');
insert into addrtable values('192.128.99.1');

select * from addrtable;


# error situation
select * from addrtable where '::192.0.0.0/8' contains addr;

select * from addrtable where '192.0.0.0/8' contains addr;
select * from addrtable where '192.0.0.0/16' contains addr;
select * from addrtable where '192.128.0.0/16' contains addr;
select * from addrtable where '192.0.10.0/24' contains addr;
select * from addrtable where '192.128.99.0/24' contains addr;
select * from addrtable where '192.128.99.0/25' contains addr;
select * from addrtable where '192.128.99.0/26' contains addr;
select * from addrtable where '192.128.99.0/27' contains addr;
select * from addrtable where '192.128.99.0/28' contains addr;
select * from addrtable where '192.128.99.0/29' contains addr;
select * from addrtable where '192.128.99.0/30' contains addr;
select * from addrtable where '192.128.99.0/31' contains addr;
select * from addrtable where '192.128.99.0/32' contains addr;
select * from addrtable where '192.128.99.0/32' not contains addr;

# error situation
select * from addrtable where  addr contained '::192.0.0.0/8';

select * from addrtable where addr contained '192.0.0.0/8';
select * from addrtable where addr contained '192.0.0.0/16';
select * from addrtable where addr contained '192.128.0.0/16';
select * from addrtable where addr contained '192.0.10.0/24';
select * from addrtable where addr contained '192.128.99.0/24';
select * from addrtable where addr contained '192.128.99.0/25';
select * from addrtable where addr contained '192.128.99.0/26';
select * from addrtable where addr contained '192.128.99.0/27';
select * from addrtable where addr contained '192.128.99.0/28';
select * from addrtable where addr contained '192.128.99.0/29';
select * from addrtable where addr contained '192.128.99.0/30';
select * from addrtable where addr contained '192.128.99.0/31';
select * from addrtable where addr contained '192.128.99.0/32';
select * from addrtable where addr not contained '192.128.99.0/32';

-- drop table addrtable6;
create table addrtable6 (addr ipv6);

insert into addrtable values(NULL);
insert into addrtable6 values('FFFF::192.0.0.1');
insert into addrtable6 values('FFFF::192.0.10.1');
insert into addrtable6 values('FFFF::192.128.0.1');
insert into addrtable6 values('FFFF::192.128.99.128');
insert into addrtable6 values('FFFF::192.128.99.64');
insert into addrtable6 values('FFFF::192.128.99.32');
insert into addrtable6 values('FFFF::192.128.99.16');
insert into addrtable6 values('FFFF::192.128.99.8');
insert into addrtable6 values('FFFF::192.128.99.4');
insert into addrtable6 values('FFFF::192.128.99.2');
insert into addrtable6 values('FFFF::192.128.99.1');

select * from addrtable6;

# error situation
select * from addrtable6 where '192.0.0.0/8' contains addr;

select * from addrtable6 where 'FFFF::192.0.0.0/104' contains addr;
select * from addrtable6 where 'FFFF::192.0.0.0/112' contains addr;
select * from addrtable6 where 'FFFF::192.128.0.0/112' contains addr;
select * from addrtable6 where 'FFFF::192.0.10.0/120' contains addr;
select * from addrtable6 where 'FFFF::192.128.99.0/120' contains addr;
select * from addrtable6 where 'FFFF::192.128.99.0/121' contains addr;
select * from addrtable6 where 'FFFF::192.128.99.0/122' contains addr;
select * from addrtable6 where 'FFFF::192.128.99.0/123' contains addr;
select * from addrtable6 where 'FFFF::192.128.99.0/124' contains addr;
select * from addrtable6 where 'FFFF::192.128.99.0/125' contains addr;
select * from addrtable6 where 'FFFF::192.128.99.0/126' contains addr;
select * from addrtable6 where 'FFFF::192.128.99.0/127' contains addr;
select * from addrtable6 where 'FFFF::192.128.99.0/128' contains addr;
select * from addrtable6 where 'FFFF::192.128.99.0/128' not contains addr;

# error situation
select * from addrtable6 where addr contained '192.0.0.0/8';

select * from addrtable6 where addr contained 'FFFF::192.0.0.0/104';
select * from addrtable6 where addr contained 'FFFF::192.0.0.0/112';
select * from addrtable6 where addr contained 'FFFF::192.128.0.0/112';
select * from addrtable6 where addr contained 'FFFF::192.0.10.0/120';
select * from addrtable6 where addr contained 'FFFF::192.128.99.0/120';
select * from addrtable6 where addr contained 'FFFF::192.128.99.0/121';
select * from addrtable6 where addr contained 'FFFF::192.128.99.0/122';
select * from addrtable6 where addr contained 'FFFF::192.128.99.0/123';
select * from addrtable6 where addr contained 'FFFF::192.128.99.0/124';
select * from addrtable6 where addr contained 'FFFF::192.128.99.0/125';
select * from addrtable6 where addr contained 'FFFF::192.128.99.0/126';
select * from addrtable6 where addr contained 'FFFF::192.128.99.0/127';
select * from addrtable6 where addr contained 'FFFF::192.128.99.0/128';
select * from addrtable6 where addr not contained 'FFFF::192.128.99.0/128';

-- drop table err_mask;
create table err_mask (short1 short, integer1 integer, long1 long, float1 float, double1 double, datetime1 datetime, varchar1 varchar(10), ip ipv4, ip2 ipv6, text1 text, bin1 binary);

insert into err_mask values(1, 2, 3, 4, 5, '1999-11-11', '11.22.33.44', '111.22.33.44', '::222.111.222.231', '111', '222');

select * from err_mask;

select * from err_mask where '192.0.0.0/8' contains short1;
select * from err_mask where '192.0.0.0/8' contains integer1;
select * from err_mask where '192.0.0.0/8' contains long1;
select * from err_mask where '192.0.0.0/8' contains float1;
select * from err_mask where '192.0.0.0/8' contains double1;
select * from err_mask where '192.0.0.0/8' contains datetime1;
select * from err_mask where '192.0.0.0/8' contains varchar1;
select * from err_mask where '192.0.0.0/8' contains ip;
select * from err_mask where '192.0.0.0/8' contains ip2;
select * from err_mask where '192.0.0.0/8' contains text1;
select * from err_mask where '192.0.0.0/8' contains bin1;

select * from err_mask where short1 contained '192.0.0.0/8';
select * from err_mask where integer1 contained '192.0.0.0/8';
select * from err_mask where long1 contained '192.0.0.0/8';
select * from err_mask where float1 contained '192.0.0.0/8';
select * from err_mask where double1 contained '192.0.0.0/8';
select * from err_mask where datetime1 contained '192.0.0.0/8';
select * from err_mask where varchar1 contained '192.0.0.0/8';
select * from err_mask where ip contained '192.0.0.0/8';
select * from err_mask where ip2 contained '192.0.0.0/8';
select * from err_mask where text1 contained '192.0.0.0/8';
select * from err_mask where bin1 contained '192.0.0.0/8';


#index test
-- drop table addrtable;
create table addrtable (addr ipv4);
create bitmap index addr_idx on addrtable(addr);
insert into addrtable values(NULL);
insert into addrtable values('192.0.0.1');
insert into addrtable values('192.0.10.1');
insert into addrtable values('192.128.0.1');
insert into addrtable values('192.128.99.128');
insert into addrtable values('192.128.99.64');
insert into addrtable values('192.128.99.32');
insert into addrtable values('192.128.99.16');
insert into addrtable values('192.128.99.8');
insert into addrtable values('192.128.99.4');
insert into addrtable values('192.128.99.2');
insert into addrtable values('192.128.99.1');
alter table addrtable flush index all;
select * from addrtable;


# error situation
select * from addrtable where '::192.0.0.0/8' contains addr;

select * from addrtable where '192.0.0.0/8' contains addr;
select * from addrtable where '192.0.0.0/16' contains addr;
select * from addrtable where '192.128.0.0/16' contains addr;
select * from addrtable where '192.0.10.0/24' contains addr;
select * from addrtable where '192.128.99.0/24' contains addr;
select * from addrtable where '192.128.99.0/25' contains addr;
select * from addrtable where '192.128.99.0/26' contains addr;
select * from addrtable where '192.128.99.0/27' contains addr;
select * from addrtable where '192.128.99.0/28' contains addr;
select * from addrtable where '192.128.99.0/29' contains addr;
select * from addrtable where '192.128.99.0/30' contains addr;
select * from addrtable where '192.128.99.0/31' contains addr;
select * from addrtable where '192.128.99.0/32' contains addr;
select * from addrtable where '192.128.99.0/32' not contains addr;

# error situation
select * from addrtable where  addr contained '::192.0.0.0/8';

select * from addrtable where addr contained '192.0.0.0/8';
select * from addrtable where addr contained '192.0.0.0/16';
select * from addrtable where addr contained '192.128.0.0/16';
select * from addrtable where addr contained '192.0.10.0/24';
select * from addrtable where addr contained '192.128.99.0/24';
select * from addrtable where addr contained '192.128.99.0/25';
select * from addrtable where addr contained '192.128.99.0/26';
select * from addrtable where addr contained '192.128.99.0/27';
select * from addrtable where addr contained '192.128.99.0/28';
select * from addrtable where addr contained '192.128.99.0/29';
select * from addrtable where addr contained '192.128.99.0/30';
select * from addrtable where addr contained '192.128.99.0/31';
select * from addrtable where addr contained '192.128.99.0/32';
select * from addrtable where addr not contained '192.128.99.0/32';

-- drop table addrtable6;
create table addrtable6 (addr ipv6);
create bitmap index addr6_idx on addrtable6(addr);

insert into addrtable values(NULL);
insert into addrtable6 values('FFFF::192.0.0.1');
insert into addrtable6 values('FFFF::192.0.10.1');
insert into addrtable6 values('FFFF::192.128.0.1');
insert into addrtable6 values('FFFF::192.128.99.128');
insert into addrtable6 values('FFFF::192.128.99.64');
insert into addrtable6 values('FFFF::192.128.99.32');
insert into addrtable6 values('FFFF::192.128.99.16');
insert into addrtable6 values('FFFF::192.128.99.8');
insert into addrtable6 values('FFFF::192.128.99.4');
insert into addrtable6 values('FFFF::192.128.99.2');
insert into addrtable6 values('FFFF::192.128.99.1');
alter table addrtable6 flush index all;

select * from addrtable6;

# error situation
select * from addrtable6 where '192.0.0.0/8' contains addr;

select * from addrtable6 where 'FFFF::192.0.0.0/104' contains addr;
select * from addrtable6 where 'FFFF::192.0.0.0/112' contains addr;
select * from addrtable6 where 'FFFF::192.128.0.0/112' contains addr;
select * from addrtable6 where 'FFFF::192.0.10.0/120' contains addr;
select * from addrtable6 where 'FFFF::192.128.99.0/120' contains addr;
select * from addrtable6 where 'FFFF::192.128.99.0/121' contains addr;
select * from addrtable6 where 'FFFF::192.128.99.0/122' contains addr;
select * from addrtable6 where 'FFFF::192.128.99.0/123' contains addr;
select * from addrtable6 where 'FFFF::192.128.99.0/124' contains addr;
select * from addrtable6 where 'FFFF::192.128.99.0/125' contains addr;
select * from addrtable6 where 'FFFF::192.128.99.0/126' contains addr;
select * from addrtable6 where 'FFFF::192.128.99.0/127' contains addr;
select * from addrtable6 where 'FFFF::192.128.99.0/128' contains addr;
select * from addrtable6 where 'FFFF::192.128.99.0/128' not contains addr;

# error situation
select * from addrtable6 where addr contained '192.0.0.0/8';

select * from addrtable6 where addr contained 'FFFF::192.0.0.0/104';
select * from addrtable6 where addr contained 'FFFF::192.0.0.0/112';
select * from addrtable6 where addr contained 'FFFF::192.128.0.0/112';
select * from addrtable6 where addr contained 'FFFF::192.0.10.0/120';
select * from addrtable6 where addr contained 'FFFF::192.128.99.0/120';
select * from addrtable6 where addr contained 'FFFF::192.128.99.0/121';
select * from addrtable6 where addr contained 'FFFF::192.128.99.0/122';
select * from addrtable6 where addr contained 'FFFF::192.128.99.0/123';
select * from addrtable6 where addr contained 'FFFF::192.128.99.0/124';
select * from addrtable6 where addr contained 'FFFF::192.128.99.0/125';
select * from addrtable6 where addr contained 'FFFF::192.128.99.0/126';
select * from addrtable6 where addr contained 'FFFF::192.128.99.0/127';
select * from addrtable6 where addr contained 'FFFF::192.128.99.0/128';
select * from addrtable6 where addr not contained 'FFFF::192.128.99.0/128';

-- drop table err_mask;
create table err_mask (short1 short, integer1 integer, long1 long, float1 float, double1 double, datetime1 datetime, varchar1 varchar(10), ip ipv4, ip2 ipv6, text1 text, bin1 binary);

insert into err_mask values(1, 2, 3, 4, 5, '1999-11-11', '11.22.33.44', '111.22.33.44', '::222.111.222.231', '111', '222');

select * from err_mask;

select * from err_mask where '192.0.0.0/8' contains short1;
select * from err_mask where '192.0.0.0/8' contains integer1;
select * from err_mask where '192.0.0.0/8' contains long1;
select * from err_mask where '192.0.0.0/8' contains float1;
select * from err_mask where '192.0.0.0/8' contains double1;
select * from err_mask where '192.0.0.0/8' contains datetime1;
select * from err_mask where '192.0.0.0/8' contains varchar1;
select * from err_mask where '192.0.0.0/8' contains ip;
select * from err_mask where '192.0.0.0/8' contains ip2;
select * from err_mask where '192.0.0.0/8' contains text1;
select * from err_mask where '192.0.0.0/8' contains bin1;

select * from err_mask where short1 contained '192.0.0.0/8';
select * from err_mask where integer1 contained '192.0.0.0/8';
select * from err_mask where long1 contained '192.0.0.0/8';
select * from err_mask where float1 contained '192.0.0.0/8';
select * from err_mask where double1 contained '192.0.0.0/8';
select * from err_mask where datetime1 contained '192.0.0.0/8';
select * from err_mask where varchar1 contained '192.0.0.0/8';
select * from err_mask where ip contained '192.0.0.0/8';
select * from err_mask where ip2 contained '192.0.0.0/8';
select * from err_mask where text1 contained '192.0.0.0/8';
select * from err_mask where bin1 contained '192.0.0.0/8';

