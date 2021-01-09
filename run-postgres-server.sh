#!/bin/sh
docker run --rm -p 5432:5432 -e POSTGRES_DB=plantdiary -e POSTGRES_PASSWORD=User123 -e POSTGRES_USER=user postgres:13.1-alpine
