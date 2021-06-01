echo "> subway pid 확인"
 CURRENT_PID=$(ps -ef | grep java | grep subway-fare | awk '{print $2}')
echo "$CURRENT_PID"
 if [ -z $CURRENT_PID ]; then
echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
echo "> kill -9 $CURRENT_PID"
kill -9 $CURRENT_PID
sleep 10
fi
 echo "> subwaybot 배포"
nohup java -jar -Dspring.profiles.active=prod /home/ubuntu/subway-fare/deploy/*.jar >> /home/ubuntu/subway-fare/logs/subway.log &