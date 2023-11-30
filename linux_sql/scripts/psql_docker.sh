#!/bin/bash

# Capture CLI arguments
cmd=$1
db_username=$2
db_password=$3

# Start docker if not already started (indicated by the status command)
sudo systemctl status docker || sudo systemctl start docker

# Pull the alpine 9.6 version of postgres just in case
docker pull postgres:9.6-alpine

# Check if the container has already been created
docker container inspect jrvs-psql
container_status=$? # 0 if container exists, 1 if it does not

# User switch case to handle create|stop|start options
case $cmd in
	create)
		#If the container already exists, this is invalid
		if [ $container_status -eq 0 ]; then
			echo "Container already exists"
			exit 1
		fi

		#Ensure there are 3 CLI arguments
		if [ $# -ne 3 ]; then
			echo "Requires username and password"
			exit 1
		fi

		#Create and run docker container
		docker volume create pgdata
		export PGPASSWORD=$db_password
		docker run --name jrvs-psql -e POSTGRES_PASSWORD="$PGPASSWORD" -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres:9.6-alpine
		
		#Exit with the return status of if the docker container ran or not
		exit $?
	;;

	start|stop)
		#If the container does not exist, this is invalid
		if [ $container_status -ne 0 ]; then
			echo "Container does not exist"
			exit 1
		fi

		#Start or stop the container
		docker container "$cmd" jrvs-psql

		#Exit with the return status of if the docker container ran or not
		exit $?
	;;

	*)
		echo "Illegal command"
		echo "Valid commands are create|start|stop"
		exit 1
esac