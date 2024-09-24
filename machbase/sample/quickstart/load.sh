#!/bin/sh

#-----------------------------------------------------------------
# run csvimport command to load sample_data.csv into Machbase
#
# Usage: sh load.sh
#-----------------------------------------------------------------
set -x

tar -xvzf sample_data.tar.gz
csvimport -t sample_data -d sample_data.csv

 
