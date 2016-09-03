# Saves the current hphelper database to databaseBackup.sql
mysqldump --extended-insert=FALSE -p hphelper > databaseBackup.sql
