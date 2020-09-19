# Saves the current hphelper database to databaseBackup.sql
mysqldump -ufc -h172.17.0.2 -p hphelper > databaseBackup.sql
# Cull the breaking utf8mb4 qualifier that's breaking prod because it's old
sed --in-place -e's/COLLATE=utf8mb4_0900_ai_ci//' databaseBackup.sql
