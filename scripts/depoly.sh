#!/usr/bin/env bash

REPOSITORY=/home/ubuntu/project/
PROJECT_NAME=atdd-subway-fare

CURRENT_PID=$(pgrep -fl atdd-subway-fare | grep java | awk '{print $1}')

cd $REPOSITORY/$PROJECT_NAME/

echo "> git reset --hard"

git reset --hard # 깃허브 초기화

BRANCH="step1"

git pull origin $BRANCH

echo "> gradle clean build 실행"

./gradlew clean build

echo "현재 구동중인 어플리케이션 pid: $CURRENT_PID"

cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/$PROJECT_NAME

if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "> 새 어플리케이션 배포"

JAR_NAME=$(ls -tr /*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 실행"

nohup java -jar -Dspring.profiles.active=prod $JAR_NAME 1> log-prod.md 2>&1  &
