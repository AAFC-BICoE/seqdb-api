#!/bin/bash

export PGPASSWORD="$POSTGRES_PASSWORD"
CREATED_USER_COUNT=$(envsubst <getValidUserCount.sql | psql -qtAX -h $POSTGRES_HOST -U $POSTGRES_USER)

./executeSQL.sh "CREATE TABLE IF NOT EXISTS public.DatabaseReadiness (id SERIAL PRIMARY KEY, status INTEGER, process VARCHAR, application VARCHAR, lastModified TIMESTAMP);"
./executeSQL.sh "INSERT INTO public.DatabaseReadiness (status, process, application, lastModified) VALUES ($CREATED_USER_COUNT, 'database ready', '$APPLICATION', NOW());"
