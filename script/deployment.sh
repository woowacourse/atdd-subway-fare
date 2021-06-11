#!/bin/bash

cd /home/ubuntu/atdd-subway-fare

PID=`pidof java`
kill -9 ${PID}

ORIGIN="https://github.com/da-nyee/atdd-subway-fare"
BRANCH="step1"
git pull ${ORIGIN} ${BRANCH}

./gradlew build
nohup java -jar -Dspring.profiles.active=prod ./build/libs/atdd-subway-fare-0.0.1-SNAPSHOT.jar 1> ./logs/subway.log 2>&1 &