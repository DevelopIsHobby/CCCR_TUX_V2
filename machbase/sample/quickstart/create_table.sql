CREATE TABLE sample_data
( srcip     IPV4, 
  srcport   INTEGER, 
  dstip     IPV4, 
  dstport   INTEGER, 
  protocol  SHORT, 
  eventlog  VARCHAR(1024), 
  eventcode SHORT, 
  eventsize LONG
);
