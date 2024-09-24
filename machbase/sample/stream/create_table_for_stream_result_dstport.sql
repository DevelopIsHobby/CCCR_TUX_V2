CREATE TABLE sample_dstport ( 
   event_time   DATETIME,
   src_ip       IPV4,
   src_port     INTEGER,
   dst_ip       IPV4,
   dst_port     INTEGER,
   protocol     SHORT,
   event_log    VARCHAR(1024),
   event_code   SHORT,
   event_size   LONG
);
