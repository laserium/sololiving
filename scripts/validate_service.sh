#!/bin/bash
echo "Validating service..."
if ! curl -s http://localhost:8080/actuator/health; then
  echo "Application is not running!"
  exit 1
fi