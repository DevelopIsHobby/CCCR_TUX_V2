library("RODBC")

channel <- odbcConnect("MACHBASE_DSN", believeNRows=FALSE, case="toupper")

sqlTables(channel)

sqlColumns(channel, "sample_table")

res <- sqlQuery(channel, "select * from sample_table limit 10")
res

odbcClose(channel)

odbcCloseAll()
