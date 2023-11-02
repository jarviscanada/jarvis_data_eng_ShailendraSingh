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

#Store the hostname
hostname=$(hostname -f)

#Get the number of CPUs, CPU architecture, CPU model, CPU speed in MHz, L2 cache size in KB
lscpu_out=$(lscpu)
cpu_number=$(echo "$lscpu_out" | egrep "^CPU\(s\):" | awk '{print $2}' | xargs) #egrep retrieves line starting with CPU(s), awk retrieves 2nd column, xargs trims whitespace
cpu_architecture=$(echo "$lscpu_out" | egrep "^Architecture:" | awk '{print $2}' | xargs) #egrep retrieves line starting with Architecture, awk retrieves 2nd column, xargs trims whitespace
cpu_model=$(echo "$lscpu_out" | egrep "^Model name:" | awk '{$1=$2=""; print $0}' | xargs) #egrep retrieves line starting with Model name, awk retrieves all columns after the first and second (Model name), xargs trims whitespace
cpu_mhz=$(echo "$lscpu_out" | egrep "^CPU MHz:" | awk '{print $3}' | xargs) #egrep retrieves line starting with CPU MHz, awk retrieves 3rd column, xargs trims whitespace
l2_cache=$(echo "$lscpu_out" | egrep "^L2 cache:" | awk '{print $3}' | sed 's/K//g' | xargs) #egrep retrieves line starting with L2 cache, awk retrieves 3rd column, sed removes K, xargs trims whitespace

#Get the total memory in MB
total_mem=$(grep MemTotal /proc/meminfo | awk '{print $2}' | xargs) #grep retrieves line starting with MemTotal, awk retrieves 2nd column, xargs trims whitespace

#Get the timestamp in 'YYYY-MM-DD HH:MM:SS' UTC Format
timestamp=$(date -u '+%Y-%m-%d %H:%M:%S')

#Construct insert statement to insert system stats into host_info table
insert_statement="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, \"timestamp\", total_mem) VALUES ('$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', $cpu_mhz, $l2_cache, '$timestamp', $total_mem);"

#Insert system stats into host_info table
export PGPASSWORD=$psql_password
psql -h "$psql_host" -p "$psql_port" -U "$psql_user" -d "$db_name" -c "$insert_statement"
exit $?