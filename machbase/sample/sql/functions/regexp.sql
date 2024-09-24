-- drop table dual;
-- drop table realdual;
create table realdual (id1 integer, id2 varchar(20), id3 varchar(20));
create table dual (id integer);

insert into dual values(1);

insert into realdual values(1, 'tsdb1', 'machbase1 machbase21');
insert into realdual values(1, 'tsdb2', 'machbase2 machbase22');
insert into realdual values(1, 'tsdb3', 'machbase3 machbase32');

select * from realdual where id2 regexp 'tsdb' ;
select * from realdual where id2 regexp 'tsdb[12]' ;
select * from realdual where id2 regexp 'tsdb[13]' ;
select * from realdual where id2 regexp 'tsdb[13]' and id3 regexp 'machbase[12]';


select * from realdual where id2 not regexp 'tsdb' ;
select * from realdual where id2 not regexp 'tsdb[12]' ;
select * from realdual where id2 not regexp 'tsdb[13]' ;
select * from realdual where id2 not regexp 'tsdb[13]' and id3 regexp 'machbase[12]';

select * from realdual where id2 regexp 'tsdb[13]' or id2 regexp 'tsdb[13]' or id2 regexp 'tsdb[13]' or id2 regexp 'tsdb[13]' or id2 regexp 'tsdb[13]' or id2 regexp 'tsdb[13]' or id2 regexp 'tsdb[13]' or id2 regexp 'tsdb[13]' or id2 regexp 'tsdb[13]' or id2 regexp 'tsdb[13]' or id2 regexp 'tsdb[13]' or id2 regexp 'tsdb[13]' or id2 regexp 'tsdb[13]' or id2 regexp 'tsdb[13]' or id2 regexp 'tsdb[13]' or id2 regexp 'tsdb[13]';


SELECT 'Monty!' REGEXP '.*' from dual;
SELECT 'new*\n*line' REGEXP 'new\\*.\\*line' from dual;
SELECT 'a' REGEXP '^[a-d]' from dual;

SELECT 'fo\nfo' REGEXP '^fo$' from dual;
SELECT 'fofo' REGEXP '^fo' from dual;

#diff
SELECT 'fo\no' REGEXP '^fo\no$' from dual;

SELECT 'fo\no' REGEXP '^fo$' from dual;
SELECT 'fofo' REGEXP '^f.*$' from dual;
SELECT 'fo\r\nfo' REGEXP '^f.*$' from dual;
SELECT 'Ban' REGEXP '^Ba*n' from dual;
SELECT 'Baaan' REGEXP '^Ba*n' from dual;
SELECT 'Bn' REGEXP '^Ba*n' from dual;
SELECT 'Ban' REGEXP '^Ba+n' from dual;
SELECT 'Bn' REGEXP '^Ba+n' from dual;
SELECT 'Bn' REGEXP '^Ba?n' from dual;
SELECT 'Ban' REGEXP '^Ba?n' from dual;
SELECT 'Baan' REGEXP '^Ba?n' from dual;
SELECT 'pi' REGEXP 'pi|apa' from dual;
SELECT 'axe' REGEXP 'pi|apa' from dual;
SELECT 'apa' REGEXP 'pi|apa' from dual;
SELECT 'apa' REGEXP '^(pi|apa)$' from dual;
SELECT 'pi' REGEXP '^(pi|apa)$' from dual;
SELECT 'pix' REGEXP '^(pi|apa)$' from dual;
SELECT 'pi' REGEXP '^(pi)*$' from dual;
SELECT 'pip' REGEXP '^(pi)*$' from dual;
SELECT 'pipi' REGEXP '^(pi)*$' from dual;
SELECT 'abcde' REGEXP 'a[bcd]{2}e' from dual;
SELECT 'abcde' REGEXP 'a[bcd]{3}e' from dual;
SELECT 'abcde' REGEXP 'a[bcd]{1,10}e' from dual;
SELECT 'aXbc' REGEXP '[a-dXYZ]' from dual;
SELECT 'aXbc' REGEXP '^[a-dXYZ]$' from dual;
SELECT 'aXbc' REGEXP '^[a-dXYZ]+$' from dual;
SELECT 'aXbc' REGEXP '^[^a-dXYZ]+$' from dual;
SELECT 'gheis' REGEXP '^[^a-dXYZ]+$' from dual;
SELECT 'gheisa' REGEXP '^[^a-dXYZ]+$' from dual;
