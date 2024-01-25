#!/bin/bash

psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

if [ "$#" -ne 5 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

vmstat_mb=$(vmstat --unit M)
hostname=$(hostname -f)
memory_free=$(echo "$vmstat_mb" | awk '{print $4}'| tail -n1 | xargs)
cpu_idle=$(echo "$vmstat_mb" | tail -1 | awk -v col="15" '{print $col}') #TODO:done
cpu_kernel=$(echo "$vmstat_mb" | tail -1 | awk -v col="14" '{print $col}') #TODO:done
disk_io=$(vmstat -d | awk '{print $10}' | tail -1 | awk -v col="10" '{print $col}') #TODO
disk_available=$(df -BM / | tail -1 | awk -v col="4" '{print substr( $col, 1, length($col)-1 ) }') #TODO:done
timestamp=$(vmstat -t | tail -1 | awk '{print $18,$19}') #TODO:done

host_id="(SELECT id FROM host_info WHERE hostname='$hostname')";
insert_stmt="INSERT INTO host_usage("timestamp", host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available)
VALUES('$timestamp', '$host_id', '$memory_free', '$cpu_idle', '$cpu_kernel', '$disk_io', '$disk_available')"

#set up env var for pql cmd
export PGPASSWORD=$psql_password
#Insert date into a database
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?
