#!/bin/bash

# Check if CodeDeploy Agent is installed
if dpkg -l | grep codedeploy-agent > /dev/null 2>&1; then
    echo "CodeDeploy Agent is already installed."
else
    echo "Installing CodeDeploy Agent..."
    sudo apt-get update
    sudo apt-get install -y ruby wget

    cd /home/ubuntu
    wget https://aws-codedeploy-ap-northeast-2.s3.ap-northeast-2.amazonaws.com/latest/install
    chmod +x ./install
    sudo ./install auto

    sudo systemctl start codedeploy-agent
fi

# Confirm that the CodeDeploy agent is running
if sudo systemctl is-active --quiet codedeploy-agent; then
    echo "CodeDeploy Agent is running."
else
    echo "CodeDeploy Agent failed to start. Please check the logs."
    exit 1
fi
