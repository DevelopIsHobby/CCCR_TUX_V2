#!/bin/sh

# 1. create table to store raw data
machsql -s 127.0.0.1 -u sys -p manager -f create_table_for_raw_data.sql

# 2. create table to store the filtered data with stream query
machsql -s 127.0.0.1 -u sys -p manager -f create_table_for_stream_result_srcip.sql
machsql -s 127.0.0.1 -u sys -p manager -f create_table_for_stream_result_dstport.sql

# 3. create stream and start 
machsql -s 127.0.0.1 -u sys -p manager -f create_and_start_stream_srcip.sql
machsql -s 127.0.0.1 -u sys -p manager -f create_and_start_stream_dstport.sql

# 4. insert data into the table for raw data(sample_data)
tar -xvzf sample_data.tar.gz
csvimport -t sample_data -d sample_data.csv -F "event_time YYYY-MM-DD HH24:MI:SS"

# 5. select the result of stream and a virtual table for stream
sleep 1

machsql -s 127.0.0.1 -u sys -p manager -f select_the_result.sql
