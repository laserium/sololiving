#!/bin/bash
echo "Starting application..."

# 로그 파일의 경로
LOG_FILE="/home/ubuntu/sololiving.jar/sololiving.log"

# 로그 파일을 작성할 수 있도록 디렉터리 권한 수정
sudo chmod 755 /home/ubuntu/sololiving.jar

# 애플리케이션 시작 및 로그 출력
nohup java -jar /home/ubuntu/sololiving.jar/sololiving-0.0.1-SNAPSHOT.jar > $LOG_FILE 2>&1 &

echo "Waiting for the application to start..."
sleep 60

# 애플리케이션이 성공적으로 시작되었는지 확인
if ! pgrep -f "sololiving-0.0.1-SNAPSHOT.jar"; then
    echo "Application failed to start within 60 seconds."
    echo "Check the log at $LOG_FILE for more information."
    exit 1
fi

echo "Application started successfully."
