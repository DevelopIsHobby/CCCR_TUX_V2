#!/bin/sh

if [ $# -ne 2 ]; then
  echo "Usage: $0 DBS_PATH OUTPUTFILE"
  exit -1
fi

machhome=$1
tagmetaid=`sqlite3 $machhome/meta.dbs-1 "SELECT ID FROM SYS_TABLES WHERE NAME='_TAG_META'"`

if [ -z "$tagmetaid" ]; then
    echo "No _TAG_META table in META"
    exit -1
fi

echo "Exporting shadow table for lookup table $tagmetaid to file $2"
tblname=__SHDT$tagmetaid
sqlite3 $machhome/meta.dbs-3 -cmd ".mode csv" ".output $2" "SELECT * FROM $tblname"

