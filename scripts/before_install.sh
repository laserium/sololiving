#!/bin/bash
echo "Stopping existing application..."
sudo systemctl stop your-app.service || true
