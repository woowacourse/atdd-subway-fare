#!/bin/bash

rm -rf atdd-subway-fare/
git clone -b step1 --single-branch https://github.com/pjy1368/atdd-subway-fare.git
cd atdd-subway-fare/
./gradlew clean build
cd build/libs/
kill $(lsof -t -i:8080)
nohup java -jar -Dspring.profiles.active=remote atdd-subway-fare-0.0.1-SNAPSHOT.jar &