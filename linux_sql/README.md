# Linux Cluster Monitoring Agent

# Introduction
This set of bash scripts is designed to run on a cluster of Linux machines. This will survey the specs of every machine in the network
and store them in a central machine. It will then record the performance specs of every machine into the database, every minute.

The database used for this is a Postgres SQL database, hosted on a Docker container running on one of the machines in the cluster.

# Quick Start
1. Install `host_info.sh` and `host_usage.sh` onto every machine. Install `psql_docker.sh` onto the machine where you wish to 
store the database of machine information. For the rest of this tutorial, I am assuming that the folder where these scripts are
is `/yourpath/scripts`

2. Create a PSQL docker container by running:

`bash psql_docker.sh create [USERNAME] [PASSWORD]`

3. Create a database named `[DATABASE NAME]` in the PSQL container and then create the tables `host_info` and `host_usage` by taking the 
`ddl.sql` file and running:

`psql -h localhost -p 5432 -U postgres -c "CREATE DATABASE [DATABASE NAME];"`

`psql -h [PSQL HOST] -U [USERNAME] -d host_agent -f sql/ddl.sql`

4. Run `host_info.sh` on every machine once to survey the basic specs of your machine:

`bash host_info.sh [PSQL HOST] [PORT #] [DATABASE NAME] [PSQL USERNAME] [PSQL PASSWORD]`

5. Create a new cron job on the system with `psql_docker.sh` by running `crontab -e` and then pasting in

`* * * * * bash /yourpath/scripts/host_usage.sh [PSQL HOST] [PORT #] [DATABASE NAME] [PSQL USERNAME] [PSQL PASSWORD] &> /tmp/host_usage.log` 

# Implementation
## Architecture
![Architecture Diagram](./assets/ArchitectureDiagram.svg)

## Scripts
### psql_docker.sh
This script will either create, start or stop a docker container with an instance of Postgres SQL inside that holds our database.



## Database Modeling

# Test

# Deployment

# Improvement