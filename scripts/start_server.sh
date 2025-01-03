#!/bin/bash
echo "Starting application..."
nohup java -jar /home/ubuntu/sololiving.jar/sololiving-0.0.1-SNAPSHOT.jar > /home/ubuntu/app.log 2>&1 &
echo "Waiting for the application to start..."

# 60초 동안 애플리케이션이 시작되었는지 확인
for i in {1..12}; do
    if curl -s http://localhost:8080/actuator/health | grep -q 'UP'; then
        echo "Application started successfully."
        exit 0
    fi
    sleep 5
done

echo "Application started successfully."