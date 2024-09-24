#!/bin/sh

machsql -s 127.0.0.1 -u sys -p manager -f stop_and_drop_stream_table.sql
