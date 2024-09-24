#! /bin/bash

#******************************************************************************
# Copyright of this product 2013-2023,
# MACHBASE Corporation(or Inc.) or its subsidiaries.
# All Rights reserved.
#******************************************************************************
# Program     : machbase_get_mra_linux.sh
# Description : get machbase related analytics data.
#******************************************************************************


N_CLR="\e[40m\e[92m"
I_CLR="\e[40m\e[94m"
E_CLR="\e[40m\e[91m"
CLR_R="\e[0m"

#######################################################
# Setting work / Pre work
#######################################################

if [ -z $MACHBASE_HOME ]
then
    echo -e "$E_CLR [ERR] .... machbase home not found $CLR_R"
    exit;
fi

echo "MACHBASE_HOME : $MACHBASE_HOME"


NOW=`date +'%Y%m%d%H%M%S'`
LOG_HOME=machlog_${NOW}

if [ ! -d ${LOG_HOME} ]
then
    mkdir ${LOG_HOME}
    if [ $? -ne 0 ]; then
        echo -e "$E_CLR [ERR] .... Directory creation failed [${LOG_HOME}] $CLR_R"
        exit
    fi
else
    mv ${LOG_HOME} ${LOG_HOME}_old
fi

TRC_DIR=${LOG_HOME}/trclog
CONF_DIR=${LOG_HOME}/conflog
META_DIR=${LOG_HOME}/metalog
PSTK_DIR=${LOG_HOME}/pstacklog
HWRC_DIR=${LOG_HOME}/hwlog
SYSLOG_DIR=${LOG_HOME}/syslog

if [ ! -d ${TRC_DIR} ]
then
    mkdir ${TRC_DIR}
    if [ $? -ne 0 ]; then
        echo -e "$E_CLR [ERR] .... Directory creation failed [${TRC_DIR}] $CLR_R"
        exit
    fi
fi

if [ ! -d ${CONF_DIR} ]
then
    mkdir ${CONF_DIR}
    if [ $? -ne 0 ]; then
        echo -e "$E_CLR [ERR] .... Directory creation failed [${CONF_DIR}] $CLR_R"
        exit
    fi
fi

if [ ! -d ${META_DIR} ]
then
    mkdir ${META_DIR}
    if [ $? -ne 0 ]; then
        echo -e "$E_CLR [ERR] .... Directory creation failed [${META_DIR}] $CLR_R"
        exit
    fi
fi

if [ ! -d ${PSTK_DIR} ]
then
    mkdir ${PSTK_DIR}
    if [ $? -ne 0 ]; then
        echo -e "$E_CLR [ERR] .... Directory creation failed [${PSTK_DIR}] $CLR_R"
        exit
    fi
fi

if [ ! -d ${HWRC_DIR} ]
then
    mkdir ${HWRC_DIR}
    if [ $? -ne 0 ]; then
        echo -e "$E_CLR [ERR] .... Directory creation failed [${HWRC_DIR}] $CLR_R"
        exit
    fi
fi

if [ ! -d ${SYSLOG_DIR} ]
then
    mkdir ${SYSLOG_DIR}
    if [ $? -ne 0 ]; then
        echo -e "$E_CLR [ERR] .... Directory creation failed [${SYSLOG_DIR}] $CLR_R"
        exit
    fi
fi


MYIP=`hostname -I | cut -d' ' -f1`
CONF_DBPORT=`grep "^PORT_NO" $MACHBASE_HOME/conf/machbase.conf | awk -F '=' '{print $2}' | sed -e 's/^ *//' -e 's/ *$//'`
ENV_DBPORT=`env | grep MACHBASE_PORT_NO | awk -F '=' '{print $2}'`
echo config_dbport=$CONF_DBPORT
echo env_dbport=$ENV_DBPORT

if [ ! -z $ENV_DBPORT ]; then
    DBPORT=$ENV_DBPORT
else
    DBPORT=$CONF_DBPORT
fi

echo set_dbport=$DBPORT


###################################################################################################
# get machbase information and log files
###################################################################################################

echo -e "$N_CLR [START] .. Get machbase information and log files $CLR_R"

#cp -rf $MACHBASE_HOME/trc ${TRC_DIR}

cp -rf $MACHBASE_HOME/trc/machbase* ${TRC_DIR}
cp -rf $MACHBASE_HOME/trc/machsql* ${TRC_DIR}
cp -rf $MACHBASE_HOME/trc/MWA* ${TRC_DIR}

cp -f $MACHBASE_HOME/conf/machbase.conf ${CONF_DIR}

DBS_PATH=`grep "^DBS_PATH" $MACHBASE_HOME/conf/machbase.conf | awk -F '=' '{print $2}' | sed -e 's/^ *//' -e 's/ *$//'`

if [ "$DBS_PATH" == "?/dbs" ]; then
        echo "DBS_PATH=${MACHBASE_HOME}/dbs"
        cp -f $MACHBASE_HOME/dbs/meta.dbs-* ${META_DIR}
else
        echo "DBS_PATH=${DBS_PATH}"
        cp -f $DBS_PATH/meta.dbs-* ${META_DIR}
fi


PID=`cat ${MACHBASE_HOME}/conf/machbase*.pid`

which gdb &> /dev/null
if [ $? -eq 1 ]; then
	echo -e "$I_CLR [WARN] ... You need to install 'gdb' to collect the process stack trace. $CLR_R"
	echo -e "$I_CLR [WARN] ... Skip process stack trace. $CLR_R"
else
	echo "thread apply all bt" > ${PSTK_DIR}/gdb.cmd
	gdb -p ${PID} --batch -x ${PSTK_DIR}/gdb.cmd &> ${PSTK_DIR}/gdb_strace1.txt
	sleep 3
	gdb -p ${PID} --batch -x ${PSTK_DIR}/gdb.cmd &> ${PSTK_DIR}/gdb_strace2.txt
fi
##pstack ${PID} > ${PSTK_DIR}/pstack1.txt
##sleep 3
##pstack ${PID} > ${PSTK_DIR}/pstack2.txt

echo -e "$N_CLR [DONE] ... Get machbase information and log files $CLR_R"


###################################################################################################
# get all meta and virtual tables information
###################################################################################################
echo -e "$N_CLR [START] .. Get all meta and virtual tables information $CLR_R"

META_SQL_FILE=meta_tables.sql
VIRT_SQL_FILE=virtual_tables.sql

META_RES_FILE=meta.txt
VIRT_RES_FILE=virtual.txt

TEMP_SQL_FILE=tmp_query.sql

META_RES_FILE=${LOG_HOME}/${META_RES_FILE}
VIRT_RES_FILE=${LOG_HOME}/${VIRT_RES_FILE}

echo 'select name from m$tables' > ${META_SQL_FILE}
echo 'select name from v$tables' > ${VIRT_SQL_FILE}

MTABLES=`machsql -i -s ${MYIP} -u sys -p manager -P ${DBPORT} -f ${META_SQL_FILE} | grep 'M\\$'`
VTABLES=`machsql -i -s ${MYIP} -u sys -p manager -P ${DBPORT} -f ${VIRT_SQL_FILE} | grep 'V\\$'`

rm -f ${META_RES_FILE}
rm -f ${VIRT_RES_FILE}


# get meta tables

for t in $MTABLES
do
    echo "select * from $t" > ${TEMP_SQL_FILE}

    machsql -i -s ${MYIP} -u sys -p manager -P ${DBPORT} -f ${TEMP_SQL_FILE} >> ${META_RES_FILE}

done
 
# get virtual tables

for t in $VTABLES
do
    echo "select * from $t" > ${TEMP_SQL_FILE}

    machsql -i -s ${MYIP} -u sys -p manager -P ${DBPORT} -f ${TEMP_SQL_FILE} >> ${VIRT_RES_FILE}

done


# clear

rm -f ${TEMP_SQL_FILE}
rm -f ${META_SQL_FILE} 
rm -f ${VIRT_SQL_FILE} 

echo -e "$N_CLR [DONE] ... Get all meta and virtual tables information $CLR_R"


###################################################################################################
# get all user tables and index
###################################################################################################

echo -e "$N_CLR [START] .. Get all user tables and index $CLR_R"

USER_SQL_FILE=msys_tables.sql
USER_RES_FILE=schema.txt
TEMP_SQL_FILE=tmp_query.sql

USER_RES_FILE=${LOG_HOME}/${USER_RES_FILE}

echo 'select name from m$sys_tables' > ${USER_SQL_FILE}

UTABLES=`machsql -i -s ${MYIP} -u sys -p manager -P ${DBPORT} -f ${USER_SQL_FILE} | tail -n +4 | head -n -2 | grep -v ^_`

rm -f ${USER_RES_FILE}

# get list of  tables

echo 'set linesize 200;' > ${TEMP_SQL_FILE}
echo "show tables;" >> ${TEMP_SQL_FILE}
machsql -i -s ${MYIP} -u sys -p manager -P ${DBPORT} -f ${TEMP_SQL_FILE} >> ${USER_RES_FILE}

# get description of tables

for t in $UTABLES
do
    printf "\n\n" >> ${USER_RES_FILE}

    echo 'set linesize 200;' > ${TEMP_SQL_FILE}
    echo "desc $t;" >> ${TEMP_SQL_FILE}

    machsql -i -s ${MYIP} -u sys -p manager -P ${DBPORT} -f ${TEMP_SQL_FILE} >> ${USER_RES_FILE}

    echo 'set linesize 200;' > ${TEMP_SQL_FILE}
    echo "select count(*) from $t;" >> ${TEMP_SQL_FILE}

    machsql -i -s ${MYIP} -u sys -p manager -P ${DBPORT} -f ${TEMP_SQL_FILE} >> ${USER_RES_FILE}

done


# get show result

echo 'set linesize 200;' > ${TEMP_SQL_FILE}
echo "show indexgap;" >> ${TEMP_SQL_FILE}
machsql -i -s ${MYIP} -u sys -p manager -P ${DBPORT} -f ${TEMP_SQL_FILE} >> ${USER_RES_FILE}

echo 'set linesize 200;' > ${TEMP_SQL_FILE}
echo "show lsm;" >> ${TEMP_SQL_FILE}
machsql -i -s ${MYIP} -u sys -p manager -P ${DBPORT} -f ${TEMP_SQL_FILE} >> ${USER_RES_FILE}

echo 'set linesize 200;' > ${TEMP_SQL_FILE}
echo "show tagindexgap;" >> ${TEMP_SQL_FILE}
machsql -i -s ${MYIP} -u sys -p manager -P ${DBPORT} -f ${TEMP_SQL_FILE} >> ${USER_RES_FILE}

echo 'set linesize 200;' > ${TEMP_SQL_FILE}
echo "show rollupgap;" >> ${TEMP_SQL_FILE}
machsql -i -s ${MYIP} -u sys -p manager -P ${DBPORT} -f ${TEMP_SQL_FILE} >> ${USER_RES_FILE}
 
rm -f ${TEMP_SQL_FILE}
rm -f ${USER_SQL_FILE} 
echo -e "$N_CLR [DONE] ... Get all user tables and index $CLR_R"


###################################################################################################
# get server spec and usage
###################################################################################################

echo -e "$N_CLR [START] .. Get server spec and usage $CLR_R"

HW_RES_LOG=hw_spec.txt
HW_USE_LOG=hw_dstat.txt
HW_NET_LOG=hw_netstat.txt

COUNT=30   # dstat loop count


HW_RES_FILE=${HWRC_DIR}/${HW_RES_LOG}
HW_USE_FILE=${HWRC_DIR}/${HW_USE_LOG}
HW_NET_FILE=${HWRC_DIR}/${HW_NET_LOG}

echo "==========================================================================================" >> ${HW_RES_FILE}
date >> ${HW_RES_FILE}
echo "==========================================================================================" >> ${HW_RES_FILE}
cat /proc/cpuinfo | grep 'model name' | uniq >> ${HW_RES_FILE}
cat /proc/cpuinfo | grep 'cpu cores' | uniq >> ${HW_RES_FILE}
lscpu | grep '^CPU(s)' >> ${HW_RES_FILE}
echo "==========================================================================================" >> ${HW_RES_FILE}
cat /proc/meminfo |grep MemTotal >> ${HW_RES_FILE}
echo "==========================================================================================" >> ${HW_RES_FILE}
cat /etc/*release* >> ${HW_RES_FILE}
echo "==========================================================================================" >> ${HW_RES_FILE}
uname -r >> ${HW_RES_FILE}
echo "==========================================================================================" >> ${HW_RES_FILE}
lsblk -d -o name,model,rota >> ${HW_RES_FILE}
echo "==========================================================================================" >> ${HW_RES_FILE}
df -Th >> ${HW_RES_FILE}
echo "==========================================================================================" >> ${HW_RES_FILE}
ulimit -a >> ${HW_RES_FILE}
echo "==========================================================================================" >> ${HW_RES_FILE}
#/usr/sbin/sysctl -a | egrep 'mem_(max|default)|tcp_.*mem' >> ${HW_RES_FILE}
echo "==========================================================================================" >> ${HW_RES_FILE}

netstat -anp 2> /dev/null >> ${HW_NET_FILE}

which dstat &> /dev/null
if [ $? -eq 1 ]; then
	echo -e "$I_CLR [WARN] ... You need to install 'dstat' to collect system resources. $CLR_R"
	echo -e "$I_CLR [WARN] ... Skip collecting system resources. $CLR_R"
else
	ETHERNET_NAME=(`/sbin/ip addr | grep LOWER_UP | awk -F ':' '{print $2}' | sed "s/@.*//g"`)
	midx=0
	ELIST=
	size=${#ETHERNET_NAME[@]}
	for line in ${ETHERNET_NAME[@]}; do
		if [ $(($size-1)) -eq $midx ]; then
			ELIST="${ELIST}${line}"
		else
			ELIST="${ELIST}${line},"
		fi
		midx=$(($midx + 1))
	done
	
	PARTITION_NAME=(`/bin/lsblk -l | grep part | grep -v "/boot" | grep -v SWAP | awk '$1 ~ "[0-9]" {print $1}'`)
	midx=0
	DLIST=
	size=${#PARTITION_NAME[@]}
	for line in ${PARTITION_NAME[@]}; do
		if [ $(($size-1)) -eq $midx ]; then
			DLIST="${DLIST}${line}"
		else
			DLIST="${DLIST}${line},"
		fi
		midx=$(($midx + 1))
	done
	
	echo -e "$I_CLR [INFO] ... Collect system resource usage ${COUNT} seconds. Please wait. $CLR_R"
	#dstat -tclmsndr -C total -D $DLIST -N $ELIST --disk-util 1 $((${COUNT} - 1)) >> ${HW_USE_FILE}
	dstat -tclmsndr -C total -D $DLIST -N $ELIST --disk-util 1 ${COUNT} >> ${HW_USE_FILE}
fi
echo -e "$N_CLR [DONE] ... Get server spec and usage $CLR_R"

###################################################################################################
# get messages syslog file
###################################################################################################

echo -e "$N_CLR [START] .. Get messages syslog file $CLR_R"
echo -n " Requires root privileges. are you sure you want to continue it (yes/no)?:"
read YN
if [[ "$YN" == "Y" ]] || [[ "$YN" == "y" ]] || [[ "$YN" == "yes" ]] || [[ "$YN" == "" ]] ; then

    SYS_MSG_DIR=varlogmessages


        if [ ! -d ${SYSLOG_DIR}/${SYS_MSG_DIR} ]
        then
             mkdir -p ${SYSLOG_DIR}/${SYS_MSG_DIR}
            if [ $? -ne 0 ]; then
                echo -e "$E_CLR [ERR] .... Directory creation failed [${SYSLOG_DIR}/${SYS_MSG_DIR}] $CLR_R"
                exit
            fi
        fi


        SYS_MSG_FILE=${SYSLOG_DIR}/${SYS_MSG_DIR}

        echo "=========================================================================================="
        log_list=`find /var/log/messages*`
        for list in ${log_list[@]}; do
            mlist=`basename $list`
            set -x
            ##su root -c "cp -f ${list} ${SYS_MSG_FILE}/${mlist}"
            su root -c "cat ${list}" >> ${SYS_MSG_FILE}/${mlist}
            set +x
        done
        echo "=========================================================================================="
fi
echo -e "$N_CLR [DONE] ... Get messages syslog file $CLR_R"

###################################################################################################
# Compress the collected logs
###################################################################################################

echo -e "$N_CLR [START] .. Compress the collected logs $CLR_R"
set -x
tar zcf ${LOG_HOME}.tgz ${LOG_HOME}
set +x
echo -e "$N_CLR [DONE] ... Compress the collected logs $CLR_R"

###################################################################################################
# Send compress file to Machbase server
###################################################################################################

echo -e "$N_CLR [START] .. Send compressed file to Machbase server $CLR_R"

echo -n " Are you sure you want to continue it (yes/no)?:"

read YN
if [[ "$YN" == "Y" ]] || [[ "$YN" == "y" ]] || [[ "$YN" == "yes" ]] || [[ "$YN" == "" ]] ; then

	which curl &> /dev/null
	if [ $? -eq 1 ]; then
		echo -e "$I_CLR [WARN] ... You need 'curl' installed to upload the compressed file. $CLR_R"
		echo -e "$I_CLR [WARN] ... Skip uploading compressed files. $CLR_R"
	else
		echo "curl --form file=@${LOG_HOME}.tgz http://trc.machbase.com:8888/upload -vvv"
		curl --form file=@${LOG_HOME}.tgz http://trc.machbase.com:8888/upload -vvv
	fi

else
	echo -e "$I_CLR [INFO] ... Skip this task. $CLR_R"
fi

echo -e "$N_CLR [DONE] ... Send compress file to Machbase server $CLR_R"