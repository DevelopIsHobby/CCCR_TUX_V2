EXEC STREAM_CREATE(event_dstport, 'insert into sample_dstport select * from sample_data where dst_port > 12332');
EXEC STREAM_SEEK(event_dstport, SEEK_BEGIN);
EXEC STREAM_START(event_dstport);
