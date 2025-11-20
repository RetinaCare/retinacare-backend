#!/bin/bash

echo "Warn: this will NUKE all data in the database, this is irreversible."
read -p "Proceed? (y/n): " answer

answer=$(echo "$answer" | tr '[:upper:]' '[:lower:]')

if [[ "$answer" != "y" ]]; then
  echo "Aborted."
  exit 1
fi

echo "Proceeding with nuke."

./scripts/load-env.sh

# Internally docker maps :5435 to :5432
export FLYWAY_URL=jdbc:postgresql://localhost:5435/retinacaredb
export FLYWAY_USER=${DB_USERNAME}
export FLYWAY_PASSWORD=${DB_PASSWORD}
export FLYWAY_CLEAN_DISABLED=false

./mvnw flyway:clean

./mvnw flyway:migrate