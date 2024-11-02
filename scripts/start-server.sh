echo "--------------- 서버 배포 시작 -----------------"
docker stop for-work-server || true
docker rm for-work-server || true
docker pull 730335533510.dkr.ecr.ap-northeast-2.amazonaws.com/for-work-server:latest
docker run -d --name for-work-server -p 8080:8080 730335533510.dkr.ecr.ap-northeast-2.amazonaws.com/for-work-server:latest
echo "--------------- 서버 배포 끝 -----------------"