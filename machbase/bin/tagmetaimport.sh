#!/bin/sh

if [ $# -ne 2 ]
then
  if [ $# -ne 3 ]
  then
    echo "Usage: $0 [--force] DBS_PATH INPUTFILE"
    exit -1
  elif [ "$1" == "--force" ]
  then
    machadmin -i -e &> /dev/null
    if [ $? -eq 0 ]; then
      echo "Warning: Machbase server need to be restarted."
    fi
    machhome=$2
    infile=$3
  else
    echo "Usage: $0 [--force] DBS_PATH INPUTFILE"
    exit -1
  fi
else
  machadmin -i -e &> /dev/null
  if [ $? -eq 0 ]; then
    echo "Machbase server is running, cannot import."
    exit -1
  fi

  machhome=$1
  infile=$2
fi

tagmetaid=`sqlite3 $machhome/meta.dbs-1 "SELECT ID FROM SYS_TABLES WHERE NAME='_TAG_META'"`

if [ -z "$tagmetaid" ]; then
  echo "No _TAG_META table in META"
  exit -1
fi

echo "Importing shadow table for lookup table $tagmetaid from file $infile"
tblname=__SHDT$tagmetaid
sqlite3 $machhome/meta.dbs-3 -cmd ".mode csv" ".separator \",\"" ".import $infile $tblname"

