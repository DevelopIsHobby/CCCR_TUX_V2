--backup database directory
backup database into disk = 'backuptest';

--backup database file
backup database into ibfile = 'backuptest';

--backup with duration - directory
backup database FROM to_date('2015-07-14 00:00:00','YYYY-MM-DD HH24:MI:SS')
                         TO to_date('2015-07-14 23:59:59','YYYY-MM-DD HH24:MI:SS')
                         into DISK = '/home/machbase/backup_2015_07_14';

--backup with duration - file
backup database FROM to_date('2015-07-14 00:00:00','YYYY-MM-DD HH24:MI:SS')
                         TO to_date('2015-07-14 23:59:59','YYYY-MM-DD HH24:MI:SS')
                         into IBFILE = '/home/machbase/backupFile_2015_07_14';

--table backup
backup table = 'tempTable' into IBFILE= '/home/machbase/backupFile/SyslogBackup';
