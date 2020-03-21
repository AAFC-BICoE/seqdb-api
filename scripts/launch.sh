#!/bin/bash
cd /app

./waitForDatabase.sh

echo "checking for users"
./checkUsers.sh

./checkAccounts.sh
if [ 0 != $? ]; then
   echo "creating sample accounts"
   export ARGS="-Dimport-sample-accounts=true"
fi

# Need to build the spring.datasource.url as database and schema name may be set differently in the Env Var settings.
SCHEMA=$(echo '$spring.liquibase.defaultSchema' | awk -f envSubstitution.awk)
URL="jdbc:postgresql://$POSTGRES_HOST/$POSTGRES_DB?currentSchema=$SCHEMA"

VERSION=$(cat ./pom.xml | grep -m 1 '<version>' | awk -F"[><]" '{print $3}')
echo "Version: '$VERSION'"

echo "executing java"
echo "java -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap $ARGS -Dspring.datasource.url=$URL -jar $1-$VERSION.jar"
exec java -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap $ARGS -Dspring.datasource.url=$URL -jar $1-$VERSION.jar
