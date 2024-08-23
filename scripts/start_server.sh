#!/bin/bash
echo "Starting application..."
nohup java -jar /home/ubuntu/sololiving.jar/sololiving-0.0.1-SNAPSHOT.jar > /home/ubuntu/sololiving.jar/sololiving.log 2>&1 &

echo "Waiting for the application to start..."
sleep 60

# Check if the application started successfully
if ! pgrep -f "sololiving-0.0.1-SNAPSHOT.jar"; then
    echo "Application failed to start within 60 seconds."
    echo "Check the log at /home/ubuntu/sololiving.jar/sololiving.log for more information."
    exit 1
fi

echo "Application started successfully."
