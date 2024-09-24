#!/bin/sh

#-----------------------------------------------------------------
# backup databaase, mount database and retrieve data from mounted DB
#
# Usage: sh backup_mount.sh
#-----------------------------------------------------------------
set -x

machsql -s 127.0.0.1 -u sys -p manager -f backup.sql

ls -l $MACHBASE_HOME/dbs/backup_sample_table

machsql -s 127.0.0.1 -u sys -p manager -f mount.sql

machsql -s 127.0.0.1 -u sys -p manager -f get_data.sql

machsql -s 127.0.0.1 -u sys -p manager -f unmount.sql
