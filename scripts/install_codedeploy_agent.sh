#!/bin/bash
# Check if CodeDeploy Agent is installed
sudo apt-get update

if sudo systemctl status codedeploy-agent > /dev/null 2>&1; then
    echo "CodeDeploy Agent is already installed."
else
    echo "Installing CodeDeploy Agent..."
    sudo apt-get install -y ruby wget
    cd /home/ubuntu
    wget https://aws-codedeploy-ap-northeast-2.s3.ap-northeast-2.amazonaws.com/latest/install
    chmod +x ./install
    sudo ./install auto
    sudo service codedeploy-agent start
fi

sudo service codedeploy-agent status