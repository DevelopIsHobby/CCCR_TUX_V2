--mount database
MOUNT DATABASE '/home/machbase/backup' TO MountDB1;

--select mounted table
SELECT * FROM MountDB1.sys.backuptable;

--unmount database
UNMOUNT DATABASE MountDB1;
