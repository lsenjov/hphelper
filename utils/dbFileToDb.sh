# Pushes databaseBackup.sql to the mysql database
mysql -p hphelper < databaseBackup.sql
# Makes sure hpsaveload has the required tables
mysql -p hpsaveload < installSaveLoad.sql
