#!/bin/sh

#-----------------------------------------------------------------
# retrieves data with using DURATION syntax
#
# Usage: sh time_series.sh
#-----------------------------------------------------------------
set -x

machsql -s 127.0.0.1 -u sys -p manager -f minmax.sql

machsql -s 127.0.0.1 -u sys -p manager -f count_by_minute.sql
 
machsql -s 127.0.0.1 -u sys -p manager -f duration.sql

