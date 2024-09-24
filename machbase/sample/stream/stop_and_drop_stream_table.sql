
EXEC STREAM_STOP(event_srcip);
EXEC STREAM_STOP(event_dstport);

EXEC STREAM_DROP(event_srcip);
EXEC STREAM_DROP(event_dstport);

DROP TABLE sample_srcip;
DROP TABLE sample_dstport;
DROP TABLE sample_data;
