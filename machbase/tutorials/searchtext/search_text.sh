#!/bin/sh

#-----------------------------------------------------------------
# create keyword index and retrieve data with SEARCH syntax
#
# Usage: sh search_text.sh
#-----------------------------------------------------------------
set -x

machsql -s 127.0.0.1 -u sys -p manager -f create_index.sql

machsql -s 127.0.0.1 -u sys -p manager -f search_text.sql

