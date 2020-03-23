#!/bin/bash
export PGPASSWORD="$POSTGRES_PASSWORD"
echo "application = $APPLICATION, database = $POSTGRES_DB, schema = $spring_liquibase_defaultSchema"
./executeSQL.sh "SELECT id, process, status, lastModified FROM public.DatabaseReadiness WHERE application = '$APPLICATION' ORDER BY lastModified DESC LIMIT 1;"
STATUS=$(./executeSQL.sh "SELECT status FROM public.DatabaseReadiness WHERE application = '$APPLICATION' ORDER BY lastModified DESC LIMIT 1;")

if [ "$STATUS" = "3" ]; then
   exit
else
   echo "CREATE DATABASE $POSTGRES_DB;" | psql  -h $POSTGRES_HOST -d object_store -U $POSTGRES_USER
   envsubst <createUsers.sql | tee | psql  -h $POSTGRES_HOST -d $POSTGRES_DB -U $POSTGRES_USER
   
   export PGPASSWORD="$spring_liquibase_password"
   envsubst <createSchema.sql | tee | psql -h $POSTGRES_HOST -d $POSTGRES_DB -U $spring_liquibase_user
   
   export PGPASSWORD="$POSTGRES_PASSWORD"
   envsubst <setSearchPath.sql | tee | psql  -h $POSTGRES_HOST -d $POSTGRES_DB -U $POSTGRES_USER
   
   CREATED_USER_COUNT=$(envsubst <getValidUserCount.sql | psql -qtAX -h $POSTGRES_HOST -U $POSTGRES_USER)
   
   if [ "$STATUS" = "" ]; then	
      ./executeSQL.sh "CREATE TABLE IF NOT EXISTS public.DatabaseReadiness (id SERIAL PRIMARY KEY, status INTEGER, process VARCHAR, application VARCHAR, lastModified TIMESTAMP);" 
   fi
   ./executeSQL.sh "INSERT INTO public.DatabaseReadiness (status, process, application, lastModified) VALUES ($CREATED_USER_COUNT, 'database ready', '$APPLICATION', NOW());"
   ./executeSQL.sh "SELECT id, process, status, lastModified FROM public.DatabaseReadiness WHERE application = '$APPLICATION' ORDER BY lastModified DESC LIMIT 1;"

   STATUS=$(./executeSQL.sh "SELECT status FROM public.DatabaseReadiness WHERE application = '$APPLICATION' ORDER BY lastModified DESC LIMIT 1;")
   if [ "$STATUS" = "3" ]; then
   	exit
   fi
   exit -1
fi
