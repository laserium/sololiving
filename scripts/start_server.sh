#!/bin/bash
echo "Starting application..."
nohup java -jar /home/ubuntu/sololiving.jar/sololiving-0.0.1-SNAPSHOT.jar 2>&1 &
echo "Waiting for the application to start..."
sleep 60