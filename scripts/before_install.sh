#!/bin/bash
echo "Stopping any existing Java applications..."
pkill -f 'java -jar /home/ubuntu/sololiving.jar' || true