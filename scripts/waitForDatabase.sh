#!/bin/bash

set +e

export PGPASSWORD="$POSTGRES_PASSWORD"
for i in {1..30}
   do
      ./executeSQL.sh "SELECT 0"
      if [ 0 == $? ]; then
         exit 0
      fi
      echo "Retry database connection. Attempt $i of 30"
      sleep 1s
   done

exit -1
