#!/bin/bash

# Building: mvn
if ./mvnw clean -DskipTests package; then
  echo "Jar built successfully"
else
  echo "Jar build failed"
  exit 1
fi

# We use compose to spin up services
if [ "$1" = "--compose" ]; then
  docker-compose down && docker-compose up --build
fi