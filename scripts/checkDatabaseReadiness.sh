#!/bin/bash

./tableExists.sh public DatabaseReadiness postgres
if [ $? == 0 ]; then
   export PGPASSWORD="$POSTGRES_PASSWORD"
   RECORD=$(envsubst <latestDbUpdate.sql | psql -qtAX  -h $POSTGRES_HOST -d postgres -U $POSTGRES_USER)
   echo "status|process|id|lastModified = $RECORD"
   STATUS=$(echo $RECORD | awk -F"|" '{print $1}')
   if [ $STATUS == "3" ]; then
      exit 0
   fi
fi

exit -1

