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

echo "url=$(echo '$spring.datasource.url' | awk  -f envSubstitution.awk)"
VERSION=$(cat ./pom.xml | grep -m 1 '<version>' | awk -F"[><]" '{print $3}')
export VERSION
echo "Version: '$VERSION'"
echo "executing java"
echo "java -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap $ARGS -jar $1-$VERSION.jar"
exec java -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap $ARGS -jar $1-$VERSION.jar
