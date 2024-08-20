#!/bin/bash
echo "Starting application..."
sudo systemctl start sololiving.service

# Start 후에 상태를 확인하여 문제가 없는지 확인
sudo systemctl status sololiving.service || {
  echo "Failed to start sololiving.service"
  exit 1
}