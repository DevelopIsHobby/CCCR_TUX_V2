drop table sqltest;
create table sqltest (id1 short, id2 integer, id3 varchar(10), id4 float, id5 double, id6 long, id7 ipv4, id8 ipv6, id9 text, id10 binary, id11 datetime);
insert into sqltest values(256, 65535, '0123456789', 3.141592, 1024 * 1024 * 1024 * 3.14, 13513135446, '192.168.0.1', '::192.168.0.1', 'textext', 'binary', TO_DATE('1999', 'YYYY'));
select TO_HEX(id1), TO_HEX(id2), TO_HEX(id3), TO_HEX(id4), TO_HEX(id5), TO_HEX(id6), TO_HEX(id7), TO_HEX(id8), TO_HEX(id9), TO_HEX(id10), TO_HEX(id11) from sqltest;
