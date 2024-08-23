#!/bin/bash

sudo apt-get update

if [-f "$/home/ubuntu/install"]; then
    echo "CodeDeploy Agent is already installed."
else
    sudo apt-get install -y ruby wget
    cd /home/ubuntu
    wget https://aws-codedeploy-ap-northeast-2.s3.ap-northeast-2.amazonaws.com/latest/install
    chmod +x ./install
    sudo ./install auto
    sudo systemctl start codedeploy-agent
fi