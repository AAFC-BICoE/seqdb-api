#!/bin/bash

./checkDatabaseReadiness.sh
if [ $? != 0 ]; then
   export PGPASSWORD="$POSTGRES_PASSWORD"
   echo "CREATE DATABASE $POSTGRES_DB;" | psql  -h $POSTGRES_HOST -d postgres -U $POSTGRES_USER
   envsubst <createUsers.sql | psql  -h $POSTGRES_HOST -d $POSTGRES_DB -U $POSTGRES_USER
   
   export PGPASSWORD="$spring_liquibase_password"
   envsubst <createSchema.sql | psql -h $POSTGRES_HOST -d $POSTGRES_DB -U $spring_liquibase_user
   
   export PGPASSWORD="$POSTGRES_PASSWORD"
   envsubst <setSearchPath.sql | psql  -h $POSTGRES_HOST -d $POSTGRES_DB -U $POSTGRES_USER
   
   ./updateDataReadiness.sh 
fi
