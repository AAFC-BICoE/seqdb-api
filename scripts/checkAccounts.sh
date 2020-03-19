#!/bin/bash

export USERNAME=$(echo '$spring.datasource.username' | awk  -f envSubstitution.awk)

export PGPASSWORD="$(echo '$spring.datasource.password' | awk  -f envSubstitution.awk)"

TABLENAME=$(echo "SELECT to_regclass('seqdb.accounts');"  | psql -qtAX -U $USERNAME -h $POSTGRES_HOST -d $POSTGRES_DB)
echo "TABLENAME='$TABLENAME'"
if [ "$TABLENAME" != "accounts" ]; then
   echo "seqdb.accounts tables does not exist"
   exit -1
fi

USERCOUNT=$(echo "SELECT  count(*) from seqdb.accounts;"  | psql -qtAX -U $USERNAME -h $POSTGRES_HOST -d $POSTGRES_DB)
echo "USERCOUNT = '$USERCOUNT'"
if [ "$USERCOUNT" = "0" ]; then
   echo "No users in seqdb.accounts table"
   exit -2
fi

exit 0
