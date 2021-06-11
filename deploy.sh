#!/bin/bash

PID=`pidof java`
kill -9 ${PID}

./gradlew build
nohup java -jar -Dspring.profiles.active=prod ./build/libs/atdd-subway-fare-0.0.1-SNAPSHOT.jar 1> ./logs/error.log 2>&1 &

