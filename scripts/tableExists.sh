#!/bin/bash

export PGPASSWORD="$POSTGRES_PASSWORD"
RESULT=$(echo "SELECT COUNT(to_regclass('$1.$2'));" | psql -qtAX -h $POSTGRES_HOST -d $3 -U $POSTGRES_USER)
if [ $RESULT != "1" ]; then
   echo "$1.$2 does not exist"
   exit -1
fi
exit 0

