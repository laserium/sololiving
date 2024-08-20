#!/bin/bash
echo "Stopping existing application..."
sudo systemctl stop sololiving.service || {
  echo "Service is not running or already stopped."
}