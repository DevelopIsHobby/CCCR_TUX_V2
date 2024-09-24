
--drop table dmltable;
create table dmltable (id integer);

insert into dmltable values(1);
insert into dmltable values(2);
insert into dmltable values(3);
insert into dmltable values(4);
insert into dmltable values(5);
insert into dmltable values(6);
insert into dmltable values(7);
insert into dmltable values(8);
insert into dmltable values(9);
insert into dmltable values(10);

select * from dmltable;

delete from dmltable oldest 5 rows;

select * from dmltable;

delete from dmltable except 2 rows;

select * from dmltable;

delete from dmltable except 0 rows;

select * from dmltable;

insert into dmltable values(1);
insert into dmltable values(2);
insert into dmltable values(3);
insert into dmltable values(4);
insert into dmltable values(5);
insert into dmltable values(6);
insert into dmltable values(7);
insert into dmltable values(8);
insert into dmltable values(9);
insert into dmltable values(10);

delete from dmltable except 5 second;

select * from dmltable;


--drop table deltable;
create table deltable (id integer);

insert into deltable values(1);
insert into deltable values(2);
insert into deltable values(3);
insert into deltable values(4);
insert into deltable values(5);
insert into deltable values(6);
insert into deltable values(7);
insert into deltable values(8);
insert into deltable values(9);
insert into deltable values(10);

delete from deltable before ADD_TIME(sysdate, '0/0/0 0:0:-5');

select * from deltable;

delete from deltable before TO_DATE('2014-6-12 10:00:00', 'YYYY-MM-DD HH24:MI:SS');

select * from deltable;
