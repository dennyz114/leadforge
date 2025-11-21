#!/usr/bin/env bash
set -euo pipefail
MYSQL_HOST=${MYSQL_HOST:-localhost}
MYSQL_PORT=${MYSQL_PORT:-3306}
MYSQL_PWD=${MYSQL_ROOT_PASSWORD:-root}

dbs=( "${AUTH_DB:-leadflow_auth}" "${COMPANY_DB:-leadflow_company}" "${LEAD_DB:-leadflow_lead}" "${NOTIF_DB:-leadflow_notification}" )

for db in "${dbs[@]}"; do
  docker exec -i $(docker ps -qf "ancestor=mysql:8") \
    bash -lc "mysql -uroot -p$MYSQL_PWD -e 'CREATE DATABASE IF NOT EXISTS ${db} CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;'"
  echo "DB ensured: ${db}"
done