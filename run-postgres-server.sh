#!/bin/sh
docker run --rm -p 5432:5432 -e POSTGRES_DB=plantdiary -e POSTGRES_PASSWORD=User123 -e POSTGRES_USER=user -v postgre_plantdiary_data:/var/lib/postgresql/data -v $(pwd)/src/main/resources/db/migration/:/docker-entrypoint-initdb.d:ro postgres
