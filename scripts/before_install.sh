#!/bin/bash
echo "Stopping any existing Java applications..."
pkill -f 'java -jar /home/ubuntu/sololiving.jar/sololiving-0.0.1-SNAPSHOT.jar' || true

# Java 설치
if ! java -version &>/dev/null; then
    echo "Java is not installed. Installing OpenJDK 17..."
    sudo apt-get update
    sudo apt-get install -y openjdk-17-jdk
else
    echo "Java is already installed."
fi