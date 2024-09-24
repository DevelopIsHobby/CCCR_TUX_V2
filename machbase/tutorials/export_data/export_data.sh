#!/bin/sh

#-----------------------------------------------------------------
# export data with csvexport, machloader and SAVE syntax
#
# Usage: sh export_data.sh
#-----------------------------------------------------------------
set -x

if [ -f sample.exp ];
then
	rm -f sample.exp
fi

csvexport -t sample_table -d sample.exp

if [ -f sample.out ];
then
	rm -f sample.out
fi

machloader -o -t sample_table -d sample.out -r raw -D ','

if [ -f sample.dat ];
then
	rm -f sample.dat
fi

machsql -s localhost -u sys -p manager -f save_data.sql
