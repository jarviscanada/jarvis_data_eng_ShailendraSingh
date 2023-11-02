#!/bin/bash

#Setup script arguments
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

#Ensure there are correct number of arguments
if [ "$#" -ne 5 ]; then
  echo "Illegal number of parameters"
  exit 1
fi

#Store all system stats (in MB) and hostname
hostname=$(hostname -f)
vmstat_mb=$(vmstat --unit M)

#Parse vmstat output to get all system info
memory_free=$(echo "$vmstat_mb" | awk '{print $4}'| tail -n1 | xargs) #awk retrieves 4th column, tail retrieves last line, xargs trims whitespace
cpu_idle=$(echo "$vmstat_mb" | awk '{print $15}'| tail -n1 | xargs) #awk retrieves 15th column, tail retrieves last line, xargs trims whitespace
cpu_kernel=$(echo "$vmstat_mb" | awk '{print $14}'| tail -n1 | xargs) #awk retrieves 14th column, tail retrieves last line, xargs trims whitespace
disk_io=$(vmstat -d | awk '{print $10}'| tail -n1 | xargs) #awk retrieves 10th column (THIS IS ONLY DISK OUTPUT Blocks/s BUT, TICKET DEMANDS IT CALLED IO), tail retrieves last line, xargs trims whitespace
disk_available=$(df -BM / | awk '{print $4}' | tail -n1 | sed 's/M//g' | xargs) #df -BM / gets volume at / info in MB, awk retrieves 4th column, tail retrieves last line, sed removes M, xargs trims whitespace

#Get timestamp in 'YYYY-MM-DD HH:MM:SS' UTC Format
timestamp=$(vmstat -t | tail -n1 | awk '{print $18 " " $19}' | xargs) #tail retrieves last line, awk retrieves 18th and 19th column (date and time), xargs trims whitespace

#Construct query to find matching id in host_info table
host_id_query="SELECT id FROM host_info WHERE hostname='$hostname';"

#Retrieve host_id from database
export PGPASSWORD=$psql_password
host_id=$(psql -h "$psql_host" -p "$psql_port" -U "$psql_user" -d "$db_name" -t -c "$host_id_query" | xargs) #psql runs query (-t removes column and row headers), xargs trims whitespace

#Construct insert statement to insert system stats into host_usage table
insert_statement="INSERT INTO host_usage (\"timestamp\", host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available) VALUES ('$timestamp', $host_id, $memory_free, $cpu_idle, $cpu_kernel, $disk_io, $disk_available);"

#Insert system stats into host_usage table
psql -h "$psql_host" -p "$psql_port" -U "$psql_user" -d "$db_name" -c "$insert_statement"
exit $?