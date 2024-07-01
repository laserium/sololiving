#!/bin/bash
echo "Stopping existing application..."
sudo systemctl stop sololiving.service || true
