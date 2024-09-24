EXEC STREAM_CREATE(event_srcip, 'insert into sample_srcip select * from sample_data where src_ip > ''252.248.178.60'' ');
EXEC STREAM_SEEK(event_srcip, SEEK_BEGIN);
EXEC STREAM_START(event_srcip);
