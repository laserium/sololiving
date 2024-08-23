#!/bin/bash
# Install AWS CodeDeploy Agent
echo "Installing CodeDeploy Agent..."
sudo su
apt-get update
apt-get install -y ruby wget

cd /home/ubuntu
wget https://bucket-name.s3.amazonaws.com/latest/install
chmod +x ./install
./install auto

# Start the CodeDeploy Agent
systemctl codedeploy-agent start