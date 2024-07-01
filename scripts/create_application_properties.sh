#!/bin/bash

# Get parameters from AWS Systems Manager Parameter Store
DB_TYPE=$(aws ssm get-parameter --name "/secrets/rds-type" --query "Parameter.Value" --output text --with-decryption)
DB_URL=$(aws ssm get-parameter --name "/secrets/db-url" --query "Parameter.Value" --output text --with-decryption)
DB_USERNAME=$(aws ssm get-parameter --name "/secrets/db-username" --query "Parameter.Value" --output text --with-decryption)
DB_PASSWORD=$(aws ssm get-parameter --name "/secrets/db-password" --query "Parameter.Value" --output text --with-decryption)
MYBATIS_MAPPER_LOCATION=$(aws ssm get-parameter --name "/mybatis/mapper-location" --query "Parameter.Value" --output text --with-decryption)

# Create application.properties file
cat << EOF > application.properties
spring.datasource.driver-class-name=$DB_TYPE
spring.datasource.url=$DB_URL
spring.datasource.username=$DB_USERNAME
spring.datasource.password=$DB_PASSWORD
mybatis.mapper-locations=$MYBATIS_MAPPER_LOCATION
EOF
