#!/bin/sh

#-----------------------------------------------------------------
# retrieves ip address data 
#
# Usage: sh ip_address.sh
#-----------------------------------------------------------------
set -x

machsql -s 127.0.0.1 -u sys -p manager -f ip_address.sql


