#!/bin/bash
echo "Starting application..."
nohup java -jar /home/ubuntu/sololiving.jar/sololiving-0.0.1-SNAPSHOT.jar > /home/ubuntu/nohup.out 2>&1 &

# 애플리케이션이 시작될 때까지 대기
echo "Waiting for the application to start..."

# 최대 60초 동안 8080 포트에서 애플리케이션이 실행 중인지 확인
timeout=60
elapsed=0
while ! nc -z localhost 8080; do
  sleep 2
  elapsed=$((elapsed + 2))
  if [ $elapsed -ge $timeout ]; then
    echo "Application failed to start within $timeout seconds."
    echo "Check the logs for more information."
    exit 1
  fi
done

echo "Application started successfully."