REPOSITORY=/home/ubuntu/atdd-subway-fare
PROJECT_NAME=atdd-subway-fare

echo "> 프로젝트 폴더 경로로 이동"

echo "> cd $REPOSITORY"
cd $REPOSITORY

echo "> git fetch origin step1"
git fetch origin step1

echo "> git reset --hard FETCH_HEAD"
git reset --hard FETCH_HEAD

echo "> cd $REPOSITORY"
cd $REPOSITORY

echo "> scripts/deploy.sh 파일에 실행 권한 추가"
sudo chmod +x scripts/deploy.sh

echo "> scripts/deploy.sh 파일 실행"
sudo sh scripts/deploy.sh
