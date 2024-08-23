#!/bin/bash

sudo apt-get update
echo "CodeDeploy Agent is already installed."
sudo apt-get install -y ruby wget
cd /home/ubuntu
wget https://aws-codedeploy-ap-northeast-2.s3.ap-northeast-2.amazonaws.com/latest/install
chmod +x ./install
sudo ./install auto
sudo systemctl start codedeploy-agent